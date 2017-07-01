 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;

    public class Piace extends Panel implements MouseListener, MouseMotionListener, KeyListener { 
  
   Graph3DI graph;                                 //апплет
   Vector points3D = new Vector();                 //Вектор физических точек(3D).
   Vector figures = new Vector();                  //Вектор всех фигур.
   Figure figure;                                  //Текущая фигура.
   Point offset;                                   //Начало отсчета.
   Element select;                                 //Текущий элемент.
   Side copy;                                      //Используется для сохранения текущей стороны(Side).
   final static Matrix3D matrix = new Matrix3D();     //Для динамики мира точек3D
   int  tab, x0, y0, kind, step, zero;             //tab-для навигации по элементам мира;kind-вид элемента;step-перемещение элемента по осям.(x0,y0)-тень последней записанная точка.
   float turn, turnSum, rotate;                    //turn-приращение угола поворота,turnSum-угол поворота в "reconstruct()"
   int tab_add = 1;                                //для навигации по элементам.
   floatPanel FP;                                  //red? панель инструментов.      
   boolean full, flag_backface;                    //"заполненность", "флаг задней стороны" 
   boolean regimeMove = true;                      //и "режим" редактирования Элементов.
   boolean net = true;                             //"сеть" сторон фигуры,
   Color color = Color.black;                      //исходный цвет Элементов.
   Rectangle rect = new Rectangle(0, 0, 8, 8);     //следует за нажатием мыши и определяет частоту записи 3Dточек.
   Image offScreenImage;                           //для двойной буферизации.
   Graphics offScreen;                             //для двойной буферизации.

   public Piace(Graph3DI graph)
	{
	  this.graph = graph; 
	  addMouseMotionListener(this);
	  addMouseListener(this);
	  addKeyListener(this);
	  figures.addElement(figure = new Figure(this, net, flag_backface));
	  figure.elements.addElement(select = new Side(this, kind, color, full, false));
	  setBackground(Color.white);
	  rotate = step  = 3;                        
	  FP = new floatPanel(this, "view Right :Shell Figure and full selected Side");  
	}

   public void mousePressed(MouseEvent e) 
	{
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)      //режим рисования
	   {
		rect.translate(x - rect.x - 4, y - rect.y - 4);  //"rect" следует за нажатием мыши
		Shade.mouseDown(this, x, y);   //начало создания тени            
		if (offset == null)            //создание отображения центра системы координат(СК)
		   offset = new Point(x, y);  
		if(select.field.size() == 0)   //запись только первой точки элемента-select
		  savePoint3D(numberPoint(x, y) , x, y);                
		repaint(x-5, y-5, 10, 10); 
	   }               
	  else  Move.mouseDown(this,x,y,e);  //режим перемещения.  
	}

	public void mouseDragged(MouseEvent e)               
	{ 
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)    //режим рисования
	   {
		rect.translate(x - rect.x - 4, y - rect.y - 4);  //"rect" следует за нажатием мыши          
		if (((select.kind == Element.freeLine)||(select.kind == Element.freePoly))&&(!(rect.contains(x0, y0))))
		  savePoint3D(numberPoint(x, y) , x, y);                  
		Shade.mouseDrag(this, x, y);  //перемещение созданной тени
	   }            
	  else Move.mouseDrag(this,x,y,e);  //режим перемещения.
	}

   public void mouseReleased(MouseEvent e) 
	{ 
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)    //режим рисования.
	   {
		rect.translate(x - rect.x - 4, y - rect.y - 4);     //"rect" следует за нажатием мыши.                         //
		if(!rect.contains(x0, y0))                          //контроль расстояния между точками.
		  savePoint3D(numberPoint(x, y) , x, y);                             
		repaint();
		Shade.mouseUp(this);          //окончание создания тени.
	   }       
	  else Move.mouseUp(this,x,y,e);        //режим перемещения.
	}

   public void mouseMoved(MouseEvent e) {}                     
   public void mouseEntered(MouseEvent e) {}                     
   public void mouseExited(MouseEvent e) {}                     
   public void mouseClicked(MouseEvent e) {}                     



   int numberPoint(int x, int y)  //возвращает номер точки стороны с координатами(x,y)
	{                                   
	  rect.translate(x - rect.x - 4, y - rect.y - 4);            
	  if(select instanceof Side)
		for (int i = points3D.size()-1; i >= 0 ; i--)
		 {
		  x = Math.round(((Point3D)points3D.elementAt(i)).x) + offset.x;
		  y = Math.round(((Point3D)points3D.elementAt(i)).y) + offset.y; 
		  if (rect.contains(x, y))                                 
			return i;                                                     
		 }
	  return points3D.size();                    
	}

   private void message()
	 {System.out.println("3Dpoints:"+points3D.size()+ "; figures:"+figures.size() + "; sides in figure:"+figure.elements.size()
						 +";Points in figure:"+figure.field.size() + ";select:" + select.field.size()+ "; elements in select:"+((Side)select).elements.size());}
/*   private void message()// расширенный метод
	 {

	  for(int i=0; i<points3D.size(); i++)
	   {
		 Point3D thePoint3D = (Point3D)points3D.elementAt(i); 
		 System.out.println("Point3D N "+ i + " : " + thePoint3D);  
	   }

	  System.out.println("3Dpoints:"+points3D.size()+ "; figures:"+figures.size() + "; sides in figure:"+figure.elements.size()
						 +";Points in figure:"+figure.field.size() + ";select:" + select.field.size()+ "; elements in select:"+select.elements.size()+figure);
	 } 
*/                                                                                  
 
	 
																				  
   public void keyTyped(KeyEvent e)
	 {
	  char key = e.getKeyChar();

	  if ((key == 'e')&&(select.field.size() != 0))
		 {newElement();}
	  else if ((key == 's')&&(select.field.size() != 0))
		 {newSide(); }
	  else if ((key == 'f')&&(select.field.size() != 0))                                                  	
		 {newFigure(); }

	  else if ((key == 'n')&&(select.field.size() != 0))
		 {next_offset();}      

	  else if (key == '+')
		 {tab_add = 1; rotate = Math.abs(rotate); step = Math.abs(step);}         
	  else if (key == '-')
		 {tab_add = -1; rotate = - Math.abs(rotate); step = -Math.abs(step);}
	  else if (key == '*')
		 {tab_add = - tab_add; rotate = - rotate; step = - step;}        

	  else if ((key == 'c')&&(select.field.size() > 1)) 
		 {copy(select);}
	  else if ((key == 'C') && (select.field.size() > 1)) 
		 {copy(figure);}

	  else if ((key == 'A')&&(turnSum < (float)(1.5*Math.PI))&&(select.field.size() != 0))
		 {reconstruct(select, "Z");}
	  else if ((key == 'B')&&(turnSum < (float)(1.5*Math.PI))&&(select.field.size() != 0))
		 {reconstruct(select, "Y");}
	  else if ((key == 'D')&&(turnSum < (float)(1.5*Math.PI))&&(select.field.size() != 0))
		 {reconstruct(select, "X");}

	  else if ((key == 'L')&&(select instanceof Side)&&(select.field.size() >= 3))
		 {face((Side)select, false);}
	  else if ((key == 'l')&&(select instanceof Side)&&(select.field.size() >= 3))
		 {face((Side)select, true);}

	  else if (key == 'x')
		 rotate("X", figure, rotate);
	  else if (key == 'y') 
		 rotate("Y", figure, rotate);
	  else if (key == 'z')
		 rotate("Z", figure, rotate);  

	  else if (key == 'X')
		 { matrix.identity(); matrix.rotate(rotate,"X", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
	  else if (key == 'Y')  
		 { matrix.identity(); matrix.rotate(rotate,"Y", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
	  else if (key == 'Z') 
		 { matrix.identity(); matrix.rotate(rotate,"Z", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}

		  else if (key == 'r') 
			 select.rotate(matrix, select.a, select.b, rotate);  //new
		  else if (key == 'R') 
			 figure.rotate(matrix, figure.a, figure.b, rotate);  //new

	  else if (key == '1') 
		 rotate("X", select, rotate);
	  else if (key == '2') 
		 rotate("Y", select, rotate);
	  else if (key == '3') 
		 rotate("Z", select, rotate); 

	  else if ((key == '4') && (step > 0))
		 {scale(select, 1.1f, 1f, 1f);} 
	  else if ((key == '4') && (step < 0))
		 {scale(select, 1f/1.1f, 1f, 1f);} 
	  else if ((key == '5') && (step > 0))
		 {scale(select, 1f, 1.1f, 1f);}
	  else if ((key == '5') && (step < 0))
		 {scale(select, 1f, 1f/1.1f, 1f);}
	  else if ((key == '6') && (step > 0))
		 {scale(select, 1f, 1f, 1.1f);}
	  else if ((key == '6') && (step < 0))
		 {scale(select, 1f, 1f, 1f/1.1f);}

	  else if ((key == '7') && (step > 0))
		 {scale(figure, 1.1f, 1f, 1f);} 
	  else if ((key == '7') && (step < 0))
		 {scale(figure, 1f/1.1f, 1f, 1f);} 
	  else if ((key == '8') && (step > 0))
		 {scale(figure, 1f, 1.1f, 1f);}
	  else if ((key == '8') && (step < 0))
		 {scale(figure, 1f, 1f/1.1f, 1f);}
	  else if ((key == '9') && (step > 0))
		 {scale(figure, 1f, 1f, 1.1f);}
	  else if ((key == '9') && (step < 0))
		 {scale(figure, 1f, 1f, 1f/1.1f);}

	  else if ((key == '0')&&(select instanceof Side))
		 {copy = (Side)select; copy.normal_Left = !(copy.normal_Left);} 

	  else if (key == 'm')
		message();
			 
	  else return;  

	  if(select.field.size() != 0) 
		Shade.mouseUp(this);             //коррекция окончания создания тени.
	  repaint();   
	 }

   public void keyPressed(KeyEvent e) 
	 {
	   int key = e.getKeyCode();

	   if ((key == e.VK_UP) && !e.isControlDown() && !e.isShiftDown())
		   {offset = new Point(offset.x, offset.y - Math.abs(step));}
	   else if ((key == e.VK_DOWN) && !e.isControlDown() && !e.isShiftDown())
		   {offset = new Point(offset.x, offset.y + Math.abs(step));}
	   else if ((key == e.VK_LEFT) && !e.isControlDown() && !e.isShiftDown())
		   {offset = new Point(offset.x - Math.abs(step), offset.y);}
	   else if ((key == e.VK_RIGHT) && !e.isControlDown() && !e.isShiftDown())
		   {offset = new Point(offset.x + Math.abs(step), offset.y);}
	   
	   else if ((key == e.VK_UP)&& e.isControlDown())
		   translate(select, 0, -Math.abs(step), 0);
	   else if ((key == e.VK_DOWN)&& e.isControlDown())
		   translate(select, 0, Math.abs(step), 0);
	   else if ((key == e.VK_RIGHT)&& e.isControlDown())
		   translate(select, Math.abs(step), 0, 0);
	   else if ((key == e.VK_LEFT)&& e.isControlDown())
		   translate(select, -Math.abs(step), 0, 0); 
	   else if ((key == e.VK_END) && e.isControlDown())
		   translate(select, 0, 0, Math.abs(step));          
	   else if ((key == e.VK_HOME) && e.isControlDown())
		   translate(select, 0, 0, -Math.abs(step));       

	   else if ((key == e.VK_UP)&& e.isShiftDown())
		   translate(figure, 0, -Math.abs(step), 0);
	   else if ((key == e.VK_DOWN)&& e.isShiftDown())
		   translate(figure, 0, Math.abs(step), 0); 
	   else if ((key == e.VK_RIGHT)&& e.isShiftDown())
		   translate(figure, Math.abs(step), 0, 0); 
	   else if ((key == e.VK_LEFT)&& e.isShiftDown())
		   translate(figure, -Math.abs(step), 0, 0); 
	   else if ((key == e.VK_END) && e.isShiftDown())
		   translate(figure, 0, 0, Math.abs(step));           
	   else if ((key == e.VK_HOME) && e.isShiftDown())
		   translate(figure, 0, 0, -Math.abs(step));          

	   else if ((key == e.VK_TAB) && !e.isControlDown() && !e.isShiftDown() && (select.field.size() != 0))  
		  {nextSide();}
	   else if ((key == e.VK_TAB) && e.isControlDown() && (select.field.size() != 0))  
		  {nextElement();}
	   else if ((key == e.VK_TAB) && e.isShiftDown() && (select.field.size() != 0))  
		  {nextFigure();}

	   else if (key == e.VK_PAGE_UP)
		 {scale(figure, 1.1f, 1.1f, 1.1f);}
	   else if (key == e.VK_PAGE_DOWN)
		 {scale(figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);}

	   else if ((key == e.VK_DELETE) && !e.isControlDown() && !e.isShiftDown() && (points3D.size() != 0))         
		 {deletelast3D(select); graph.SP.setControl();}                         
	   else if ((key == e.VK_DELETE) && e.isControlDown() && (points3D.size() != 0))         
		 {delete(select); graph.SP.setControl();}                         
	   else if ((key == e.VK_DELETE) && e.isShiftDown() && (points3D.size() != 0))         
		 {delete(figure); graph.SP.setControl();}                        

	   else if ((key == e.VK_ESCAPE)&&(points3D.size() != 0)) 
		 {delete(select); graph.SP.setControl();}  

	   else return;
		
	   if(select.field.size() != 0) 
		 Shade.mouseUp(this);      //коррекция окончания создания тени.
	   repaint();     
	 }

   public void keyReleased(KeyEvent e) {}                     


   private void deletePoint3D(int t)          //удаление 3Dточки с номером "t"
	 {
	   points3D.removeElementAt(t);
	   for (int i=0; i<figures.size(); i++)
		 trim3D(t, (Figure)figures.elementAt(i));
	 }  
    
   private boolean reference(int t)          //проверка: ссылается ли одна из сторон на точку номер "t"
	 {
	  for (int i=0; i<figures.size(); i++)  //"i" - текущий номер фигуры
	   {
		 Figure figure = (Figure)figures.elementAt(i);
		 for (int k=0; k<figure.elements.size(); k++)    //"k"- текущая сторона в "i"-ой фигуре 
		  {
		   Side side = (Side)figure.elements.elementAt(k);
		   for (int m=0, n=0; m<side.field.size(); m++)  //"m"- текущая 3Dточка в "k"-ой стороне
			{
			  n = ((Integer)side.field.elementAt(m)).intValue();//"n"- номер "m"-ой 3Dточки
			  if(n==t)                                          
				return true;    //если совпал, то return true;
			}
		  }
	   }                                                        
	  return false;   //если не совпал, то return false
	 }


   private void copyPoint3D(Vector vector, Hashtable hash) //копирование 3Dточек "vector"
	 {
	  for (int i=0, t=0, n=0; i<vector.size(); i++)
	   {
		t = ((Integer)vector.elementAt(i)).intValue();
		if((select instanceof Side)&&(hash.containsKey(new Integer(t)))) //если существующая точка стороны нового эл-та ,то:
		  select.add(((Integer)hash.get(new Integer(t))).intValue());   //номер новой точки, соответствующей старой(t).
		else   //если 3Dточка не существует в новом Element-e:
		 {
		  n = points3D.size();
		  select.add(n);
		  figure.add(n);
		  hash.put(new Integer(t), new Integer(n));   //номер новой точки-n, старой-t
		  points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(t)));
		 }
	   } 
	 }  
///////////////////////////////////////////////////////Блок создания элементов:
   void newElement() //создание нового элемента стороны-select
	 {
	   if(select instanceof Side)  //сохранение стороны-select в "copy"
		 copy = (Side)select;        
	   copy.elements.addElement(select = new Element(this, kind, color, full));
//	   message();
	 }
   void newSide()   //создание новой стороны текущей фигуры(figure)
	 {
	   figure.elements.addElement(select = new Side(this, kind, color, full, false));
//	   message();
	 }
   void newFigure()  //создание новой фигуры(figure)
	 {
	   figures.addElement(figure = new Figure(this, net, flag_backface));
	   figure.elements.addElement(select = new Side(this, kind, color, full, false));
//	   message();
	 }
//////////////////////////////////////////////////Блок навигации по элементам:
   void next_offset()  ////смена оси вращения 
	 {
	   next(select.field);    //смена значения "tab"
	   zero = ((Integer)select.field.elementAt(tab)).intValue(); 
	   Point3D p = (Point3D)points3D.elementAt(zero);
	   offset.translate(Math.round(p.x), Math.round(p.y));   //переводит ось вращения в точку "p"
	   matrix.identity();
	   matrix.translate(-p.x, -p.y, -p.z);                   //изменение координат мира(points3D)
	   for (int i=0; i<points3D.size(); i++)
		 matrix.transform((Point3D)points3D.elementAt(i)); 
	 }
   void nextSide()      ////следующая сторона фигуры
	 {
	   next(figure.elements);  //смена значения "tab"
	   select = (Side)figure.elements.elementAt(tab);
	   graph.SP.setControl();  
	 }
   void nextElement()   ////следующий элемент стороны-select
	 {
	   if(select instanceof Side) 
		 copy = (Side)select;  //сохранение стороны(select-Side) в "copy"      
	   if(copy.elements.size() == 0)
		 return;
	   next(copy.elements);   //смена значения "tab"
	   select = (Element)copy.elements.elementAt(tab);
	   graph.SP.setControl();  
	 }
   void nextFigure()   ////следующая фигура.
	 {
	   next(figures);              //смена значения "tab"
	   figure = (Figure)figures.elementAt(tab);
	   if(!(figure.elements.contains(select)))
		 select = (Side)figure.elements.elementAt(0);
	   graph.SP.setControl();  
	 }
   private void next(Vector vector) //изменение "tab" - номера элемента "vector"
	 {                             //в пределах его размера.
	   tab = Math.min(Math.max(-1, tab + tab_add), vector.size());
	   if(tab == vector.size())
		 tab = 0;  
	   else if(tab == -1)
		 tab = vector.size()-1; 
	 }
//////////////////////////////////////////////////////Блок реконструкции элементов:
   void reconstruct(Element elem, String axis)   //формирует фигуры вращения, axis-ось вращения.
	 { 
	   Point3D p;
	   int k = select.field.size();  //кол-во точек элемента
	   matrix.identity();
	   matrix.rotate(turn, axis, true);
	   for (int i=0, t=0; i<k; i++) 
		 {        
		   t = ((Integer)select.field.elementAt(i)).intValue();
		   select.add(points3D.size());
		   figure.add(points3D.size());
		   points3D.addElement(p = matrix.transform(Point3D.copy((Point3D)points3D.elementAt(t))));  
		 }
	   turnSum += turn;        //turnSum-контроль выхода за 360 градусов.          
	   turn *= 2.0f;           //turn-угол поворота фигуры.
	 }   

 


    
 
 
///////////////////////////////////////////////////////Блок "face" сторон фигуры.
   void face(Side side, boolean alone) //положить сторону на поверхность рисования.
	 {
	   next_offset();  //new
	   Vector3D nV = side.normal();
	   matrix.identity();
	   matrix.rotate(Math.atan2(nV.z, nV.x),"Y", true);
	   matrix.rotate(-Math.atan2(nV.y, Math.sqrt(nV.x*nV.x + nV.z*nV.z)),"Z", true);
	   matrix.rotate(Math.PI/2, "Y", true);
	   if(alone)
		 {
		  side.transform(matrix);
		  for (int k=0; k<side.elements.size(); k++) {
		   ((Element)side.elements.elementAt(k)).transform(matrix);
		  } 
		 }
	   else
		  figure.transform(matrix);
	 } 
    
///////////////////////////////////////////////////////Блок для рисования элементов:
   public void update(Graphics g)      //с использованием двойной буферизации                    
	 {
	   if(offScreen == null)  
		{
		 offScreenImage = createImage(getSize().width, getSize().height);  
		 offScreen = offScreenImage.getGraphics();
		}                    
	   offScreen.setColor(getBackground());
	   offScreen.fillRect(0, 0, getSize().width, getSize().height);
	   paint(offScreen); //создание внеэкранного изображения.
	   g.drawImage(offScreenImage, 0, 0, this); //и вывод его на экран.
	   FP.repaint();
		   if(graph.anim == null)   //в анимации будет мешать приоритетам потоков                                   
	     requestFocus(); 
	 }                                                  
   public void paint(Graphics g)
	 { 
	  sort(figures);              //сортировка  фигур.
	  for (int i=0; i<figures.size(); i++)
	   {
		Figure figure = (Figure)figures.elementAt(i);
		sort(figure.elements);                //сортировка сторон фигуры
		figure.draw(g, offset, "view Face");  //рисование фигуры(figure)
		if(figure.net) {            //прорисовка пустыми овалами точек сторон фигуры
	   	   g.setColor(Color.black); 
		   for(int k=0; k<figure.elements.size(); k++)
			 ((Side)figure.elements.elementAt(k)).net(g);
		}
	   }       
	 if (offset != null)                 //выделение заполненным овалом начала отсчета(offset)
	   {g.setColor(Color.black); g.fillOval(offset.x - 3, offset.y - 3, 6, 6);} 
	 if (select instanceof Side)         //выделение стороны-select
	   {g.setColor(Color.red); select.net(g);}
	 else if(select instanceof Element)  //выделение элемента-select
	   {g.setColor(Color.green); copy.net(g);}
	 if (select.field.size() != 0)       //указание на последнюю точку "select"
	   {
		g.setColor(Color.red);
		int t = ((Integer)select.field.lastElement()).intValue();
		Point3D p = (Point3D)points3D.elementAt(t); 
		g.drawOval(offset.x + Math.round(p.x) - 3, offset.y + Math.round(p.y) - 3, 6, 6);
		x0 = Math.round(p.x)+offset.x; y0 = Math.round(p.y)+offset.y;       //коррекция x0, y0.
	   } 
	 }

   private void sort(Vector v)                  //сортировка "vector"-a по удаленности его элементов 
	 {
	   int j = v.size();
	   while(--j > 0)
		{ 
		 for (int i=0; i<j; i++)
		  {
		   if (((Swarm)v.elementAt(i)).weight() < ((Swarm)v.elementAt(i+1)).weight())
			{     
			 Swarm temp = (Swarm)v.elementAt(i);
			 v.setElementAt((Swarm)v.elementAt(i+1), i); 
			 v.setElementAt(temp, i+1);
			} 
		  }
		}
	 }  
///////////////////////////////////////////////////////Блок перемещения элементов:
   protected void savePoint3D(int i, int x, int y)  //запись новой точки Элемента, "i" - номер точки
	{
	  if(select.field.contains(new Integer(i)))  //против создания в стороне повторных точек: 
	   {                                         //чтобы корректно работали методы динамики сторон.
		select.add(points3D.size());
		figure.add(points3D.size()); 
		points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(i)));
	   }
	  else
		select.add(i);
	  if(i == points3D.size())   //если точка новая, то:
	   {
		points3D.addElement(new Point3D(x - offset.x, y - offset.y, 0));     
		figure.add(i); 
	   }
	   x0 = x; y0 = y;           //сохранение координат последней записанной 3Dточки.
	}
/////////////////////////////////////////////////////Блок отмены методов "new ..." 
   void copy(Swarm e) //копирование элемента "е"
	 {
	   Hashtable hash = new Hashtable(); 
	   copyElement(e, hash);         
	 }   private void copyElement(Swarm e, Hashtable hash)  //рекурсивное копирование элемента "е"
	 {
	  if(e instanceof Figure) //копирование фигуры 
	   { 
		figures.addElement(figure = new Figure(this, figure.net, figure.flag_backface));
		for (int i=0; i<((Figure)e).elements.size(); i++) 
		  copyElement((Side)((Figure)e).elements.elementAt(i), hash);//копирование сторон фигуры(figure)
	   }       	     
	  else if(e instanceof Side) //копирование стороны
	   { 
		figure.elements.addElement(select = new Side(this, ((Side)e).kind, ((Side)e).color, ((Side)e).full, ((Side)e).normal_Left));
		copyPoint3D(e.field, hash);
		copy = (Side)select;    //сохранение "select" в "copy"
		for (int i=0; i<((Side)e).elements.size(); i++) 
		  copyElement((Element)((Side)e).elements.elementAt(i), hash);//копирование элементов стороны select
	   }       	     
	  else if(e instanceof Element) //копирование Element.
	   { 
		copy.elements.addElement(select = new Element(this, ((Element)e).kind, ((Element)e).color, ((Element)e).full));
		copyPoint3D(e.field, hash);
	   }         
	 }/////////////////////////////////////////////////////////Блок удаления элементов:
   void delete(Swarm e)   //удаление элемента "e"
	 {
	   while(e.field.size() != 0)
		 deletelast3D(e);
	   if(e instanceof Figure)        //удаление фигуры"e"
		{
		 while(((Figure)e).elements.size() != 0)
		   delete((Side)((Figure)e).elements.lastElement());  //удаление сторон фигуры"e"
		} 
	   if(e instanceof Side)          //удаление стороны"e"
		{
		 copy = (Side)e;    //сохранение "e" в "copy"
		 while(((Side)e).elements.size() != 0)
		   delete((Element)((Side)e).elements.lastElement());  //удаление элементов стороны"e"
		 figure.elements.removeElement(e); 
		 if(figure.elements.size() == 0)               //переход к предыдущей фигуре
		  {
		   figures.removeElement(figure);
		   if(figures.size() != 0)                       
			 figure = (Figure)figures.lastElement();
		   else 
			 newFigure();
		  }
		 select = (Side)figure.elements.lastElement(); //установка стороны-select
		} 
		else if(e instanceof Element)                  //удаление элемента"е"
		{                                             
		 copy.elements.removeElement(e);               //установка стороны-select
		 select = copy;
		} 
	 }   void deletelast3D(Swarm e)  //удаление последней 3Dточки элемента "e"
	 {
	   if(e.field.size() != 0)
		{
		 int t = ((Integer)e.field.lastElement()).intValue();
		 e.field.removeElementAt(e.field.size()-1);      //new //old : e.field.removeElement((Integer)e.field.lastElement());
		 if(!reference(t))   //если нет других ссылок на 3Dточку с номером "t" , то удаление;
		   deletePoint3D(t);
		}
	 }///////////////////////////////////////////////////////Блок вращения элементов:
   synchronized void rotate(String axis, Swarm e, float rotate) //вращение Element-a "e" вокруг оси координат"axis"
	 {                                                      //на угол "rotate"
	   e.rotate(matrix, axis, rotate);
	   if((e instanceof Side)&&(!(e instanceof Figure))) // учет 3Dточек элементов "elem" стороны "Side"
		for (int k=0; k<((Side)e).elements.size(); k++)
		 {
		   Element elem =(Element)((Side)e).elements.elementAt(k);
		   elem.rotate(matrix, axis, rotate);
		 }  
	 }///////////////////////////////////////////////////////Блок масштабирования элементов:
   synchronized void scale( Swarm e, float x, float y, float z) //масштабирование "е" по осям X, Y, Z
	 {
	   e.scale(matrix, x, y, z);
	   if(e instanceof Side)        //масштабирование элементов стороны "е" по осям X, Y, Z
		for (int k=0; k<((Side)e).elements.size(); k++)
		 {
		   Element elem =(Element)((Side)e).elements.elementAt(k);
		   elem.scale(matrix, x, y, z);
		 }  
	 }///////////////////////////////////////////////////////Блок перемещения элементов:
   synchronized void translate(Swarm e, int dx, int dy, int dz)  //перемещение "e" на расстояние "dx", "dy", "dz"
	 {
	   e.translate(matrix, dx, dy, dz);
	   if(e instanceof Side) //перемещение элементов стороны "e" на расстояние "dx", "dy", "dz"
		for (int k=0; k<((Side)e).elements.size(); k++)
		 {
		   Element elem =(Element)((Side)e).elements.elementAt(k);
		   elem.translate(matrix, dx, dy, dz);
		 }  
	 }   private void trim3D(int t, Swarm elem)   //упорядочение элемента "elem" после удаления "t"-ой 3Dточки
	 {
	   elem.field.removeElement(new Integer(t));
	   for (int k=0, n=0; k<elem.field.size(); k++)
		{
		  n = ((Integer)elem.field.elementAt(k)).intValue();
		  if(n>t)
			elem.field.setElementAt(new Integer(n-1), k); 
		}
	   if(elem instanceof Side) 	
	    for (int i=0; i<((Side)elem).elements.size(); i++)   //упорядочение подЭлементов элемента "elem"
		 trim3D(t, (Element)((Side)elem).elements.elementAt(i));
	   else if(elem instanceof Figure) 	
	    for (int i=0; i<((Figure)elem).elements.size(); i++)   //упорядочение подЭлементов элемента "elem"
		 trim3D(t, (Side)((Figure)elem).elements.elementAt(i));
		 
	 }}