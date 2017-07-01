 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;

    public class Piace extends Panel implements MouseListener, MouseMotionListener, KeyListener { 
  
   static Graph3DI graph;                                 //������
   final static ArrayList points3D = new ArrayList(),     //������ ���������� �����(3D).
						  figures = new ArrayList();      //������ ���� �����.

   static Figure figure;                                  //������� ������.
   static Point offset;                                   //������ �������.
   static Element select;                                 //������� �������.
   static Side copy;                                      //������������ ��� ���������� ������� �������(Side).
   final static Matrix3D matrix = new Matrix3D();         //��� �������� ���� �����3D   
   static int  tab, x0, y0, step, zero;             //tab-��� ��������� �� ��������� ����;step-����������� �������� �� ����.(x0,y0)-���� ��������� ���������� �����.
   static float turn, turnSum, rotate;                    //turn-���������� ����� ��������,turnSum-���� �������� � "reconstruct()"
   static int tab_add = 1;                                //��� ��������� �� ���������.
   static floatPanel FP;                                  //������ ������������.         
   static boolean full,                                   //"�������������",
				  free,
		          flag_backface,                          //"���� ������ �������" 
  		          regimeMove = true,                      //� "�����" �������������� ���������.
		          net = true;                             //"����" ������ ������,


   static Color color = Color.black;                      //�������� ���� ���������.
   final static Rectangle rect = new Rectangle(0,0,8,8);  //������� �� �������� ���� � ���������� ������� ������ 3D�����.						  
   static Image offScreenImage;                           //��� ������� �����������.
   static Graphics offScreen;                             //��� ������� �����������.

   public Piace(Graph3DI graph)
	{
	  this.graph = graph; 
	  addMouseMotionListener(this);
	  addMouseListener(this);
	  addKeyListener(this);
	  figures.add(figure = new Figure(this, net, flag_backface));
	  figure.elements.add(select = new Side(this, color, free, full, false));
	  setBackground(Color.white);
	  rotate = step  = 3;                        
	  FP = new floatPanel(this, "view Right :Shell Figure and full selected Side");  
	}

   public void mousePressed(MouseEvent e) 
	{
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)      //����� ���������
	    Pencil.mouseDown(this, x, y) ;
	  else
	    Move.mouseDown(this,x,y,e);  //����� �����������.  
	}

	public void mouseDragged(MouseEvent e)               
	{ 
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)                 //����� ���������
	    Pencil.mouseDrag(this, x, y);
	  else
	    Move.mouseDrag(this,x,y,e);   //����� �����������.
	}

   public void mouseReleased(MouseEvent e) 
	{ 
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)    //����� ���������.
	    Pencil.mouseUp(this, x, y);
	  else
	    Move.mouseUp(this,x,y,e);        //����� �����������.
	}

   public void mouseMoved(MouseEvent e) {}                     
   public void mouseEntered(MouseEvent e) {}                     
   public void mouseExited(MouseEvent e) {}                     
   public void mouseClicked(MouseEvent e) {
//	   System.out.println("mouseClicked ");
  int x = e.getX(), y = e.getY();
  Move.mouseClicked(this, x, y);
}                     



   int numberPoint(int x, int y)  //���������� ����� ����� ������� � ������������(x,y)
	{                                   
	  rect.translate(x - rect.x - 4, y - rect.y - 4);            
	  if(select instanceof Side)
		for (int i = points3D.size()-1; i >= 0 ; i--)
		 {
		  x = Math.round(((Point3D)points3D.get(i)).x) + offset.x;
		  y = Math.round(((Point3D)points3D.get(i)).y) + offset.y; 
		  if (rect.contains(x, y))                                 
			return i;                                                     
		 }
	  return points3D.size();                    
	}

   private void message()
	 {System.out.println("3Dpoints:"+points3D.size()+ "; figures:"+figures.size() + "; sides in figure:"+figure.elements.size()
						 +";Points in figure:"+figure.field.size() + ";select:" + select.field.size()+ "; elements in select:"+((Side)select).elements.size());}
/*   private void message()// ����������� �����
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
		 { matrix.identity(); matrix.rotate(rotate,"X", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.get(i));}
	  else if (key == 'Y')  
		 { matrix.identity(); matrix.rotate(rotate,"Y", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.get(i));}
	  else if (key == 'Z') 
		 { matrix.identity(); matrix.rotate(rotate,"Z", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.get(i));}

		  else if (key == 'r') 
			 select.rotate(matrix, select.axis, rotate);  //new
		  else if (key == 'R') 
			 figure.rotate(matrix, figure.axis, rotate);  //new

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
		Shade.mouseUp(this);             //��������� ��������� �������� ����.
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
		 Shade.mouseUp(this);      //��������� ��������� �������� ����.
	   repaint();     
	 }

   public void keyReleased(KeyEvent e) {}                     


   private void deletePoint3D(int t)          //�������� 3D����� � ������� "t"
	 {
	   points3D.remove(t);
	   for (int i=0; i<figures.size(); i++)
		 trim3D(t, (Figure)figures.get(i));
	 }  
    
   private boolean reference(int t)          //��������: ��������� �� ���� �� ������ �� ����� ����� "t"
	 {
	  ListIterator litr = figures.listIterator();
	  while (litr.hasNext()) {
		 Figure figure = (Figure)litr.next();
		 ListIterator litr1 = figure.elements.listIterator();
		 while (litr1.hasNext()) {
		   Side side = (Side)litr1.next();
		   ListIterator litr2 = side.field.listIterator();
		   while (litr2.hasNext()) {
			 int n = ((Integer)litr2.next()).intValue();   //"n"- ����� "m"-�� 3D�����
			 if(n==t)                                          
			   return true;    //���� ������, �� return true;		   	
		   }
		 }
	  } 
	  return false;   //���� �� ������, �� return false
	 }


  
///////////////////////////////////////////////////////���� �������� ���������:
   void newElement() //�������� ������ �������� �������-select
	 {
	   if(select instanceof Side)  //���������� �������-select � "copy"
		 copy = (Side)select;
	   if (graph.SP.kind.getSelectedIndex() == 0)	 
	     select = new Poly(this, color, free, full);
	   else if(graph.SP.kind.getSelectedIndex() == 2)
	     select = new Line(this, color, free, full); 
	   copy.elements.add(select);
//	   message();
	 }
   void newSide()   //�������� ����� ������� ������� ������(figure)
	 {
	   figure.elements.add(select = new Side(this, color, free, full, false));
//	   message();
	 }
   void newFigure()  //�������� ����� ������(figure)
	 {
	   figures.add(figure = new Figure(this, net, flag_backface));
	   figure.elements.add(select = new Side(this, color, free, full, false));
//	   message();
	 }
//////////////////////////////////////////////////���� ��������� �� ���������:
   void next_offset()  ////����� ��� �������� 
	 {
	   next(select.field);    //����� �������� "tab"
	   zero = ((Integer)select.field.get(tab)).intValue(); 
	   Point3D p = (Point3D)points3D.get(zero);
	   offset.translate(Math.round(p.x), Math.round(p.y));   //��������� ��� �������� � ����� "p"
	   matrix.identity();
	   matrix.translate(-p.x, -p.y, -p.z);                   //��������� ��������� ����(points3D)
	   for (int i=0; i<points3D.size(); i++)
		 matrix.transform((Point3D)points3D.get(i)); 
	 }
   void nextSide()      ////��������� ������� ������
	 {
	   next(figure.elements);  //����� �������� "tab"
	   select = (Side)figure.elements.get(tab);
	   graph.SP.setControl();  
	 }
   void nextElement()   ////��������� ������� �������-select
	 {
	   if(select instanceof Side) 
		 copy = (Side)select;  //���������� �������(select-Side) � "copy"      
	   if(copy.elements.size() == 0)
		 return;
	   next(copy.elements);   //����� �������� "tab"
	   select = (Element)copy.elements.get(tab);
	   graph.SP.setControl();  
	 }
   void nextFigure()   ////��������� ������.
	 {
	   next(figures);              //����� �������� "tab"
	   figure = (Figure)figures.get(tab);
	   if(!(figure.elements.contains(select)))
		 select = (Side)figure.elements.get(0);
	   graph.SP.setControl();  
	 }

//////////////////////////////////////////////////////���� ������������� ���������:
   void reconstruct(Element elem, String axis)   //��������� ������ ��������, axis-��� ��������.
	 { 
	   Point3D p;
	   int k = select.field.size();  //���-�� ����� ��������
	   matrix.identity();
	   matrix.rotate(turn, axis, true);
	   for (int i=0, t=0; i<k; i++) 
		 {        
		   t = ((Integer)select.field.get(i)).intValue();
		   select.add(points3D.size());
		   figure.add(points3D.size());
		   points3D.add(p = matrix.transform(Point3D.copy((Point3D)points3D.get(t))));  
		 }
/*	   ListIterator litr = select.field.listIterator();
	   while (litr.hasNext()) {
	   	 int t = ((Integer)litr.next()).intValue();
		 select.add(points3D.size());
		 figure.add(points3D.size());
		 points3D.add(p = matrix.transform(Point3D.copy((Point3D)points3D.get(t)))); 	   	
	   } */
	   turnSum += turn;        //turnSum-�������� ������ �� 360 ��������.          
	   turn *= 2.0f;           //turn-���� �������� ������.
	 }   

 


    
 
 
///////////////////////////////////////////////////////���� "face" ������ ������.
   void face(Side side, boolean alone) //�������� ������� �� ����������� ���������.
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
		   ((Element)side.elements.get(k)).transform(matrix);
		  } 
		 }
	   else
		  figure.transform(matrix);
	 } 
    
///////////////////////////////////////////////////////���� ��� ��������� ���������:
   public void update(Graphics g)      //� �������������� ������� �����������                    
	 {
	   if(offScreen == null)  
		{
		 offScreenImage = createImage(getSize().width, getSize().height);  
		 offScreen = offScreenImage.getGraphics();
		}                    
	   offScreen.setColor(getBackground());
	   offScreen.fillRect(0, 0, getSize().width, getSize().height);
	   paint(offScreen);              //�������� ������������ �����������.
	   g.drawImage(offScreenImage, 0, 0, this); //� ����� ��� �� �����.
	   FP.repaint();
	   if(graph.anim == null)         //� �������� ����� ������ ����������� �������                                   
	     requestFocus(); 
	 }                                                  
   public void paint(Graphics g)
	 { 
	  sort(figures);              //����������  �����.
	  ListIterator litr = figures.listIterator();
	  while (litr.hasNext()) {
		Figure figure = (Figure)litr.next();
		sort(figure.elements);                //���������� ������ ������
		figure.draw(g, offset, "view Face");  //��������� ������(figure)
		if(figure.net) {            //���������� ������� ������� ����� ������ ������
	   	   g.setColor(Color.black);
	   	   ListIterator litr1 = figure.elements.listIterator();
	       while (litr1.hasNext()) {
	       ((Side)litr1.next()).net(g);
	    }     
	  }	 	
	 } 
	 if (offset != null)                 //��������� ����������� ������ ������ �������(offset)
	   {g.setColor(Color.black); g.fillOval(offset.x - 3, offset.y - 3, 6, 6);} 
	 if (select instanceof Side)         //��������� �������-select
	   {g.setColor(Color.red); select.net(g);}
	 else if(select instanceof Element)  //��������� ��������-select
	   {g.setColor(Color.green); copy.net(g);}
	 if (select.field.size() != 0)       //�������� �� ��������� ����� "select"
	   {
		g.setColor(Color.red);
		int t = ((Integer)select.field.get(select.field.size()-1)).intValue();
		Point3D p = (Point3D)points3D.get(t); 
		g.drawOval(offset.x + Math.round(p.x) - 3, offset.y + Math.round(p.y) - 3, 6, 6);
		x0 = Math.round(p.x)+offset.x; y0 = Math.round(p.y)+offset.y;       //��������� x0, y0.
	   } 
	 }

  
///////////////////////////////////////////////////////���� ����������� ���������:
   protected void savePoint3D(int i, int x, int y)  //������ ����� ����� ��������, "i" - ����� �����
	{
	  if(select.field.contains(new Integer(i)))  //������ �������� � ������� ��������� �����: 
	   {                                         //����� ��������� �������� ������ �������� ������.
		select.add(points3D.size());
		figure.add(points3D.size()); 
		points3D.add(Point3D.copy((Point3D)points3D.get(i)));
	   }
	  else
		select.add(i);
	  if(i == points3D.size())   //���� ����� �����, ��:
	   {
		points3D.add(new Point3D(x - offset.x, y - offset.y, 0));     
		figure.add(i); 
	   }
	   x0 = x; y0 = y;           //���������� ��������� ��������� ���������� 3D�����.
	}
/////////////////////////////////////////////////////���� ������ ������� "new ..." 
   void copy(Swarm e) //����������� �������� "�"
	 {
	   HashMap hash = new HashMap(); 
	   copyElement(e, hash);         
	 }   private void copyElement(Swarm e, HashMap hash)  //����������� ����������� �������� "�"
	 {
	  if(e instanceof Figure) //����������� ������ 
	   { 
		figures.add(figure = new Figure(this, figure.net, figure.flag_backface));
		for (int i=0; i<((Figure)e).elements.size(); i++) 
		  copyElement((Side)((Figure)e).elements.get(i), hash);//����������� ������ ������(figure)
	   }       	     
	  else if(e instanceof Side) //����������� �������
	   { 
		figure.elements.add(select = new Side(this, ((Side)e).color, ((Side)e).free, ((Side)e).full, ((Side)e).normal_Left));
		copyPoint3D(e.field, hash);
		copy = (Side)select;    //���������� "select" � "copy"
		for (int i=0; i<((Side)e).elements.size(); i++) 
		  copyElement((Element)((Side)e).elements.get(i), hash);//����������� ��������� ������� select
	   }       	     
	  else if(e instanceof Element) //����������� Element.
	   {
		if (select.getClass() == Poly.class)
		  select = new Poly(this, ((Element)e).color, ((Element)e).free, ((Element)e).full);
		if (select.getClass() == Line.class)
		  select = new Line(this, ((Element)e).color, ((Element)e).free, ((Element)e).full); 		  
		copy.elements.add(select);
		copyPoint3D(e.field, hash);
	   }         
	 }   private void copyPoint3D(ArrayList vector, HashMap hash) //����������� 3D����� "vector"
	 {
	   ListIterator litr = vector.listIterator();
	   while (litr.hasNext()) {
		int t = ((Integer)litr.next()).intValue();
		if((select instanceof Side)&&(hash.containsKey(new Integer(t)))) //���� ������������ ����� ������� ������ ��-�� ,��:
		  select.add(((Integer)hash.get(new Integer(t))).intValue());   //����� ����� �����, ��������������� ������(t).
		else   //���� 3D����� �� ���������� � ����� Element-e:
		 {
		  int n = points3D.size();
		  select.add(n);
		  figure.add(n);
		  hash.put(new Integer(t), new Integer(n));   //����� ����� �����-n, ������-t
		  points3D.add(Point3D.copy((Point3D)points3D.get(t)));
		 }
	   }
	 }/////////////////////////////////////////////////////////���� �������� ���������:
   void delete(Swarm e)   //�������� �������� "e"
	 {
	   while(e.field.size() != 0)
		 deletelast3D(e);
	   if(e instanceof Figure)        //�������� ������"e"
		{
		 while(((Figure)e).elements.size() != 0)
		   delete((Side)((Figure)e).elements.get(((Figure)e).elements.size()-1));  //�������� ������ ������"e"
		} 
	   if(e instanceof Side)          //�������� �������"e"
		{
		 copy = (Side)e;    //���������� "e" � "copy"
		 while(((Side)e).elements.size() != 0)
		   delete((Element)((Side)e).elements.get(((Side)e).elements.size()-1));  //�������� ��������� �������"e"
		 figure.elements.remove(e); 
		 if(figure.elements.size() == 0)               //������� � ���������� ������
		  {
		   figures.remove(figure);
		   if(figures.size() != 0)                       
			 figure = (Figure)figures.get(figures.size()-1);
		   else 
			 newFigure();
		  }
		 select = (Side)figure.elements.get(figure.elements.size()-1); //��������� �������-select
		} 
		else if(e instanceof Element)                  //�������� ��������"�"
		{                                             
		 copy.elements.remove(e);               //��������� �������-select
		 select = copy;
		} 
	 }	void deletelast3D(Swarm e)   //�������� ��������� 3D����� �������� "e"
	 {
	   if(e.field.size() != 0)
		{
		 int t = ((Integer)e.field.get(e.field.size()-1)).intValue();
		 e.field.remove(e.field.size()-1);     
		 if(!reference(t))       //���� ��� ������ ������ �� 3D����� � ������� "t" , �� ��������;
		   deletePoint3D(t);
		}
	 }   private void next(ArrayList vector) //��������� "tab" - ������ �������� "vector"
	 {                             //� �������� ��� �������.
	   tab = Math.min(Math.max(-1, tab + tab_add), vector.size());
	   if(tab == vector.size())
		 tab = 0;  
	   else if(tab == -1)
		 tab = vector.size()-1; 
	 }///////////////////////////////////////////////////////���� �������� ���������:
   synchronized void rotate(String axis, Swarm swarm, float rotate) //�������� Element-a "e" ������ ��� ���������"axis"
	 {                                                      //�� ���� "rotate"
	   swarm.rotate(matrix, axis, rotate);
	   if(swarm.getClass() == Side.class)  {     // ���� 3D����� ��������� "elem" ������� "Side"		   
	    ListIterator litr = ((Side)swarm).elements.listIterator();
	    while (litr.hasNext()) {
		   Element elem =(Element)litr.next();
		   elem.rotate(matrix, axis, rotate);	    	
	    }
	   } 
	 }///////////////////////////////////////////////////////���� ��������������� ���������:
   synchronized void scale( Swarm swarm, float x, float y, float z) //��������������� "�" �� ���� X, Y, Z
	 {
	   swarm.scale(matrix, x, y, z);
	   if(swarm instanceof Side)   {     //��������������� ��������� ������� "�" �� ���� X, Y, Z
	    ListIterator litr = ((Side)swarm).elements.listIterator();
	    while (litr.hasNext()) {
		   Element elem =(Element)litr.next();
		   elem.scale(matrix, x, y, z);	    	
	    }
	   } 
	 }   private synchronized void sort(ArrayList v)  //���������� "vector"-a �� ����������� ��� ��������� 
	 {
	   int j = v.size();
	   while(--j > 0)
		{ 
		 for (int i=0; i<j; i++)
		  {
		   if (((Swarm)v.get(i)).weight() < ((Swarm)v.get(i+1)).weight())
			{     
			 Swarm temp = (Swarm)v.get(i);
			 v.set(i, (Swarm)v.get(i+1)); 
			 v.set(i+1, temp);
			} 
		  }
		}
	 }///////////////////////////////////////////////////////���� ����������� ���������:
   synchronized void translate(Swarm swarm, int dx, int dy, int dz)  //����������� "e" �� ���������� "dx", "dy", "dz"
	 {
	   swarm.translate(matrix, dx, dy, dz);
	   if(swarm instanceof Side) { //����������� ��������� ������� "e" �� ���������� "dx", "dy", "dz"
	    ListIterator litr = ((Side)swarm).elements.listIterator();
	    while (litr.hasNext()) {
		   Element elem =(Element)litr.next();
		   elem.translate(matrix, dx, dy, dz);	    	
	    }
	   }
	 }   private void trim3D(int t, Swarm elem)   //������������ �������� "elem" ����� �������� "t"-�� 3D�����
	 {
	   elem.field.remove(new Integer(t));
	   for (int k=0, n=0; k<elem.field.size(); k++)
		{
		  n = ((Integer)elem.field.get(k)).intValue();
		  if(n>t)
			elem.field.set(k, new Integer(n-1)); 
		}
	   if(elem instanceof Side) 	
	    for (int i=0; i<((Side)elem).elements.size(); i++)   //������������ ������������ �������� "elem"
		 trim3D(t, (Element)((Side)elem).elements.get(i));
	   else if(elem instanceof Figure) 	
	    for (int i=0; i<((Figure)elem).elements.size(); i++)   //������������ ������������ �������� "elem"
		 trim3D(t, (Side)((Figure)elem).elements.get(i));
		 
	 }}