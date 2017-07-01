 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;

 public class Piace extends Panel implements MouseListener, MouseMotionListener, KeyListener {   
   Graph3DI graph;                               //апплет
   Vector points3D = new Vector();               //Вектор физических точек(3D).
   Vector figures = new Vector();                //Вектор всех фигур.
   Figure figure;                                //Текущая фигура.
   Point offset;                                 //Начало отсчета.
   Element select;                               //Текущий элемент.
   Side copy;                                    //Используется для сохранения текущей стороны(Side).
   Matrix3D matrix = new Matrix3D();             //Для динамики мира точек3D
   int  tab, x0, y0, kind, step;                 //tab-для навигации по элементам мира;kind-вид элемента;step-перемещение элемента по осям.(x0,y0)-последняя записанная точка
   float turn, turnSum, rotate;                  //turn-приращение угола поворота,turnSum-угол поворота в reconstruct_A.
   int tab_add = 1;                              //для навигации по элементам.
   floatPanel FP;                                //red? панель инструментов.
   boolean full, flag_backface;                  //"заполненность" и "флаг задней стороны" Элементов,
   boolean net = true;                           //"сеть" сторон фигуры,
   Color color = Color.black;                    //цвет Элементов.
   Rectangle rect = new Rectangle(0, 0, 10, 10); //следует за нажатием мыши.
   Image offScreenImage;                         //для двойной буферизации.
   Graphics offScreen;                           //для двойной буферизации.

   public Piace(Graph3DI graph)
    {
      this.graph = graph; 
      addMouseMotionListener(this);
      addMouseListener(this);
      addKeyListener(this);
      figures.addElement(figure = new Figure(this, kind, color, full, net, flag_backface));
      figure.elements.addElement(select = new Side(this, kind, color, full));
      setBackground(Color.white);
      rotate = step  = 3;                        
      FP = new floatPanel(this, "view Right :Shell Figure and full selected Side");  //red
    }

   public void mousePressed(MouseEvent e) 
    {
      int x = e.getX(), y = e.getY();
      rect.translate(x - rect.x - 5, y - rect.y - 5); //new
      Shade.mouseDown(this, x, y);              
      if (offset == null)
         offset = new Point(x, y);  
      if(select.field.size() == 0)
         {SavePoint3D(numberPoint(x, y) , x, y); x0 = x; y0 = y;}                  
      repaint(x-5, y-5, 10, 10);                
    }

    public void mouseDragged(MouseEvent e)       //new show Shade.java
    { 
      int x = e.getX(), y = e.getY();
      rect.translate(x - rect.x - 5, y - rect.y - 5);            
      if (((select.kind == Element.freeLine)||(select.kind == Element.freePoly))&&(!(rect.contains(x0, y0))))
         {SavePoint3D(numberPoint(x, y) , x, y); x0 = x; y0 = y;}                  
      Shade.mouseDrag(this, x, y);             
    }

   public void mouseReleased(MouseEvent e) 
    { 
      int x = e.getX(), y = e.getY();
      rect.translate(x - rect.x - 5, y - rect.y - 5); //new
      if(!rect.contains(x0, y0))                //new
         SavePoint3D(numberPoint(x, y) , x, y);                            
      Rectangle rec = select.getShade(offset, "view Face").getBounds();
      repaint(rec.x-5, rec.y-5, rec.width+10, rec.height+10);
      Shade.mouseUp(this);       
    }

   public void mouseMoved(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) {}

   public void SavePoint3D(int i, int x, int y)
    {
      select.add(i);
      if(i == points3D.size())//если точка новая, то:
       {
        points3D.addElement(new Point3D(x - offset.x, y - offset.y, 0));//current?
        figure.add(i); 
       }
    }

   public int numberPoint(int x, int y)
    {
      rect.translate(x - rect.x - 5, y - rect.y - 5);            
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

   public void message()
     {System.out.println("3Dpoints:"+points3D.size()+ "; figures:"+figures.size() + "; sides in figure:"+figure.elements.size()
                         +";Points in figure:"+figure.field.size() + ";select:" + select.field.size()+ "; elements in select:"+select.elements.size());}
                                                                                  
   public void keyTyped(KeyEvent e)
     {
      char key = e.getKeyChar();

      if ((key == 'e')&&(select.field.size() != 0))
         {newElement();}
      if ((key == 's')&&(select.field.size() != 0))
         {newSide(); }
      if ((key == 'f')&&(select.field.size() != 0))                                                  	
         {newFigure(); }

      if ((key == 'n')&&(select.field.size() != 0))
         {next_offset();}      

      if (key == '+')
         {tab_add = 1; rotate = Math.abs(rotate); step = Math.abs(step);}         
      if (key == '-')
         {tab_add = -1; rotate = - Math.abs(rotate); step = -Math.abs(step);}
      if (key == '*')
         {tab_add = - tab_add; rotate = - rotate; step = - step;}        

      if ((key == 'c')&&(select.field.size() > 1)) 
         {copy(select);}
      if ((key == 'C') && (select.field.size() > 1)) 
         {copy(figure);}

      if ((key == 'A')&&(turnSum < 360)&&(select.field.size() != 0))
         {reconstruct(select, "Z");}
      if ((key == 'B')&&(turnSum < 360)&&(select.field.size() != 0))
         {reconstruct(select, "Y");}
      if ((key == 'D')&&(turnSum < 360)&&(select.field.size() != 0))
         {reconstruct(select, "X");}

      if ((key == 'L')&&(select instanceof Side)&&(select.field.size() != 0))
         {face((Side)select, false);}
      if ((key == 'l')&&(select instanceof Side)&&(select.field.size() != 0))
         {face((Side)select, true);}

      if (key == 'x')
         rotate("X", figure);
      if (key == 'y') 
         rotate("Y", figure);
      if (key == 'z')
         rotate("Z", figure);  

      if (key == 'X')
         { matrix.identity(); matrix.rotate(rotate,"X", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
      if (key == 'Y')  
         { matrix.identity(); matrix.rotate(rotate,"Y", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
      if (key == 'Z') 
         { matrix.identity(); matrix.rotate(rotate,"Z", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}

      if (key == '1') 
         rotate("X", select);
      if (key == '2') 
         rotate("Y", select);
      if (key == '3') 
         rotate("Z", select); 

      if ((key == '4') && (step > 0))
         {scale(select, 1.1f, 1f, 1f);} 
      if ((key == '4') && (step < 0))
         {scale(select, 1f/1.1f, 1f, 1f);} 
      if ((key == '5') && (step > 0))
         {scale(select, 1f, 1.1f, 1f);}
      if ((key == '5') && (step < 0))
         {scale(select, 1f, 1f/1.1f, 1f);}
      if ((key == '6') && (step > 0))
         {scale(select, 1f, 1f, 1.1f);}
      if ((key == '6') && (step < 0))
         {scale(select, 1f, 1f, 1f/1.1f);}

      if ((key == '7') && (step > 0))
         {scale(figure, 1.1f, 1f, 1f);} 
      if ((key == '7') && (step < 0))
         {scale(figure, 1f/1.1f, 1f, 1f);} 
      if ((key == '8') && (step > 0))
         {scale(figure, 1f, 1.1f, 1f);}
      if ((key == '8') && (step < 0))
         {scale(figure, 1f, 1f/1.1f, 1f);}
      if ((key == '9') && (step > 0))
         {scale(figure, 1f, 1f, 1.1f);}
      if ((key == '9') && (step < 0))
         {scale(figure, 1f, 1f, 1f/1.1f);}

      if ((key == '0')&&(select instanceof Side))
         {copy = (Side)select; copy.normal_Left = !(copy.normal_Left);} 

      if (key == 'm')
        message();             
      
      if(select.field.size() != 0) 
        Shade.mouseUp(this); 
      repaint(); // else  return;   
     }

   public void keyPressed(KeyEvent e) 
     {
       int key = e.getKeyCode();

       if ((key == e.VK_UP) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x, offset.y - Math.abs(step));}
       if ((key == e.VK_DOWN) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x, offset.y + Math.abs(step));}
       if ((key == e.VK_LEFT) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x - Math.abs(step), offset.y);}
       if ((key == e.VK_RIGHT) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x + Math.abs(step), offset.y);}
       
       if ((key == e.VK_UP)&& e.isControlDown())
           translate(select, 0, -Math.abs(step), 0);
       if ((key == e.VK_DOWN)&& e.isControlDown())
           translate(select, 0, Math.abs(step), 0);
       if ((key == e.VK_RIGHT)&& e.isControlDown())
           translate(select, Math.abs(step), 0, 0);
       if ((key == e.VK_LEFT)&& e.isControlDown())
           translate(select, -Math.abs(step), 0, 0); 
       if ((key == e.VK_END) && e.isControlDown())
           translate(select, 0, 0, Math.abs(step));           //new
       if ((key == e.VK_HOME) && e.isControlDown())
           translate(select, 0, 0, -Math.abs(step));          //new

       if ((key == e.VK_UP)&& e.isShiftDown())
           translate(figure, 0, -Math.abs(step), 0);
       if ((key == e.VK_DOWN)&& e.isShiftDown())
           translate(figure, 0, Math.abs(step), 0); 
       if ((key == e.VK_RIGHT)&& e.isShiftDown())
           translate(figure, Math.abs(step), 0, 0); 
       if ((key == e.VK_LEFT)&& e.isShiftDown())
           translate(figure, -Math.abs(step), 0, 0); 
       if ((key == e.VK_END) && e.isShiftDown())
           translate(figure, 0, 0, Math.abs(step));           //new
       if ((key == e.VK_HOME) && e.isShiftDown())
           translate(figure, 0, 0, -Math.abs(step));          //new

       if ((key == e.VK_TAB) && !e.isControlDown() && !e.isShiftDown() && (select.field.size() != 0))  
          {nextSide();}
       if ((key == e.VK_TAB) && e.isControlDown() && (select.field.size() != 0))  
          {nextElement();}
       if ((key == e.VK_TAB) && e.isShiftDown() && (select.field.size() != 0))  
          {nextFigure();}

       if (key == e.VK_PAGE_UP)
         {scale(figure, 1.1f, 1.1f, 1.1f);}
       if (key == e.VK_PAGE_DOWN)
         {scale(figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);}

       if ((key == e.VK_DELETE) && !e.isControlDown() && !e.isShiftDown() && (points3D.size() != 0))         
         {deletelast3D(select); graph.SP.setControl();}                         
       if ((key == e.VK_DELETE) && e.isControlDown() && (points3D.size() != 0))         
         {delete(select); escape(); graph.SP.setControl();}                         
       if ((key == e.VK_DELETE) && e.isShiftDown() && (points3D.size() != 0))         
         {delete(figure); escapeFigure(); graph.SP.setControl();}                         

       if ((key == e.VK_ESCAPE)&&(points3D.size() != 0)) 
         {escape();graph.SP.setControl();}  

       if(select.field.size() != 0) 
         Shade.mouseUp(this); 

       repaint(); // else  return;     
     }

   public void keyReleased(KeyEvent e) {}
/////////////////////////////////////////////////////////Блок удаления элементов:
   public void delete(Element e)   //удаление элемента "e"
     {
       while(e.field.size() != 0)
        deletelast3D(e);
       while(e.elements.size() != 0)
        {
          delete((Element)e.elements.lastElement());  //удаление подЭлементов элемента "elem"
          if(points3D.size() == 0)   //сохранение последней оставшейся select-стороны в последней оставшейся фигуре 
            break;
          e.elements.removeElement((Element)e.elements.lastElement()); 
        } 
     }
   public void deletelast3D(Element e)  //удаление последней 3Dточки элемента "e"
     {
       if(e.field.size() != 0)
        {
         int t = ((Integer)e.field.lastElement()).intValue();
         e.field.removeElement((Integer)e.field.lastElement());
         if(!reference(t))   //если нет других ссылок на 3Dточку с номером "t" , то удаление;
           deletePoint3D(t);
        }
     }
   public void deletePoint3D(int t)//удаление 3Dточки с номером "t"
     {
       points3D.removeElementAt(t);
       for (int i=0; i<figures.size(); i++)
         trim3D(t, (Figure)figures.elementAt(i));
     }  
   public void trim3D(int t, Element elem)   //упорядочение элемента "elem" после удаления "t"-ой 3Dточки
     {
       elem.field.removeElement(new Integer(t));
       for (int k=0, n=0; k<elem.field.size(); k++)
        {
          n = ((Integer)elem.field.elementAt(k)).intValue();
          if(n>t)
            elem.field.setElementAt(new Integer(n-1), k); 
        }
       for (int i=0; i<elem.elements.size(); i++) //упорядочение подЭлементов элемента "elem"
         trim3D(t, (Element)elem.elements.elementAt(i));
     }    
   public boolean reference(int t)          //проверка: ссылается ли одна из сторон на точку номер "t"
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
/////////////////////////////////////////////////////Блок отмены методов "new ..." 
   public void escape()   //отмена создания нового select-Элемента или фигуры.
     {
      if((select.field.size() == 0)&&(select.elements.size() == 0))                                 
       {
        if(select instanceof Side) 
         {
          if((figures.size() != 1)||(figure.elements.size()!=1))  //сохранение оставшейся стороны оставшейся фигуры 
           {
             figure.elements.removeElement(select);
             escapeFigure();
           }
         } 
        else if(select instanceof Element)
         {
           copy.elements.removeElement(select);
           select = copy;
         } 
       }
     } 
   public void escapeFigure()   //отмена создания нового select-Элемента или фигуры.
     {
      if(select.field.size() == 0)
       {
        if((figure.elements.size() == 0)&&(figures.size() > 1)) //переход к предыдущей фигуре
         {
           figures.removeElement(figure);
           figure = (Figure)figures.lastElement();
         }
        select = (Side)figure.elements.lastElement();
       }
     } 
///////////////////////////////////////////////////////Блок копирования элементов:
   public void copy(Element e) //копирование элемента "е"
     {
       Hashtable hash = new Hashtable(); 
       copyElement(e, hash);         
     }
   public void copyElement(Element e, Hashtable hash)  //рекурсивное копирование элемента "е"
     {
      if(e instanceof Figure) //копирование фигуры 
       { 
        figures.addElement(figure = new Figure(this, e.kind, e.color, e.full, figure.net, figure.flag_backface));
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Side)e.elements.elementAt(i), hash);//копирование сторон фигуры(figure)
       }       	     
      else if(e instanceof Side) //копирование стороны
       { 
        figure.elements.addElement(select = new Side(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, hash);
        copy = (Side)select;    //сохранение "select" в "copy"
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Element)e.elements.elementAt(i), hash);//копирование элементов стороны select
       }       	     
      else if(e instanceof Element) //копирование Element.
       { 
        copy.elements.addElement(select = new Element(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, hash);
       }         
     }
   public void copyPoint3D(Vector vector, Hashtable hash) //копирование 3Dточек "vector"
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
   public void newElement() //создание нового элемента стороны-select
     {
       if(select instanceof Side)  //сохранение стороны-select в "copy"
         copy = (Side)select;        
       copy.elements.addElement(select = new Element(this, kind, color, full));
       message();
     }
   public void newSide()   //создание новой стороны текущей фигуры(figure)
     {
       figure.elements.addElement(select = new Side(this, kind, color, full));
       message();
     }
   public void newFigure()  //создание новой фигуры(figure)
     {
       figures.addElement(figure = new Figure(this, kind, Color.black, full, net, flag_backface));
       figure.elements.addElement(select = new Side(this, kind, color, full));
       message();
     }
//////////////////////////////////////////////////Блок навигации по элементам:
   public void next_offset()  ////смена оси вращения 
     {
       next(select.field);    //смена значения "tab"
       int t = ((Integer)select.field.elementAt(tab)).intValue(); 
       Point3D p = (Point3D)points3D.elementAt(t);
       offset.translate(Math.round(p.x), Math.round(p.y));   //переводит ось вращения в точку "p"
       matrix.identity();
       matrix.translate(-p.x, -p.y, -p.z);                   //изменение координат мира(points3D)
       for (int i=0; i<points3D.size(); i++)
         matrix.transform((Point3D)points3D.elementAt(i)); 
     }
   public void nextSide()      ////следующая сторона фигуры
     {
       next(figure.elements);  //смена значения "tab"
       select = (Side)figure.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   public void nextElement()   ////следующий элемент стороны-select
     {
       if(select instanceof Side) 
         copy = (Side)select;  //сохранение стороны(select-Side) в "copy"      
       if(copy.elements.size() == 0)
         return;
       next(copy.elements);   //смена значения "tab"
       select = (Element)copy.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   public void nextFigure()
     {
       next(figures);         ////смена значения "tab"
       figure = (Figure)figures.elementAt(tab);
       if(!(figure.elements.contains(select)))
         select = (Side)figure.elements.elementAt(0);
       graph.SP.setControl();  
     }
   public void next(Vector vector) //изменение "tab" - номера элемента "vector"
     {                             //в пределах его размера.
       tab = Math.min(Math.max(-1, tab + tab_add), vector.size());
       if(tab == vector.size())
         tab = 0;  
       else if(tab == -1)
         tab = vector.size()-1; 
     }
//////////////////////////////////////////////////////Блок реконструкции элементов:
   public void reconstruct(Element elem, String axis)
     {                                      
       float xc, yc, zc;
       int k = select.field.size();
       matrix.identity();
       matrix.rotate(turn, axis, false);
       for (int i=0,t=0; i<k; i++) 
         {        
           t = ((Integer)select.field.elementAt(i)).intValue();
           select.add(points3D.size());
           figure.add(points3D.size());
           points3D.addElement(matrix.transform(Point3D.copy((Point3D)points3D.elementAt(t))));  
         }
       turnSum += turn;          
       turn *= 2.0f;       
     }   
///////////////////////////////////////////////////////Блок вращения элементов:
   public void rotate(String axis, Element e)
     {
       matrix.identity();
       matrix.rotate(rotate, axis, false);
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //else if???
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           rotate(axis, elem);
         }  
     } 
///////////////////////////////////////////////////////Блок перемещения элементов:
   public void translate(Element e, int dx, int dy, int dz)
     {
       matrix.identity();
       matrix.translate(dx, dy, dz);  
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //else if???
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           translate(elem, dx, dy, dz);  
         }  
     } 
///////////////////////////////////////////////////////Блок масштабирования элементов:
   public void scale( Element e, float x, float y, float z)
     {
       matrix.identity();
       matrix.scale(x, y, z);
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //else if ???
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           scale(elem, x, y, z);
         }  
     } 
///////////////////////////////////////////////////////Блок "face" сторон фигуры.
   public void face(Side side, boolean alone)//положить сторону на поверхность рисования.
     {
       Vector3D nV = side.normal();
       matrix.identity();
       matrix.rotate(Math.atan2(nV.z, nV.x),"Y", true);
       matrix.rotate(-Math.atan2(nV.y, Math.sqrt(nV.x*nV.x + nV.z*nV.z)),"Z", true);
       matrix.rotate(Math.PI/2, "Y", true);
       if(alone)
         {
          transform(matrix, side.field);
          for (int k=0; k<side.elements.size(); k++)
           transform(matrix, ((Element)side.elements.elementAt(k)).field);
         }
       else
          transform(matrix, figure.field);
     } 
///////////////////////////////////////////////////////общий Блок:
   public void transform(Matrix3D m, Vector v) //метод, вызываемый после изменения СК
     {
       for (int i=0, t=0; i<v.size(); i++)
        {
          t = ((Integer)v.elementAt(i)).intValue();
          m.transform((Point3D)points3D.elementAt(t)); 
        }
     }
///////////////////////////////////////////////////////Блок для рисования элементов:
   public void update(Graphics g)                         
     {
       if(offScreen == null)  //start new text:
        {
         offScreenImage = createImage(getSize().width, getSize().height);  
         offScreen = offScreenImage.getGraphics();
        }                    //end new metod;
       offScreen.setColor(getBackground());
       offScreen.fillRect(0, 0, getSize().width, getSize().height);
       paint(offScreen);
       g.drawImage(offScreenImage, 0, 0, this);
       FP.repaint();
       requestFocus();                                     
     }                                                  
   public void paint(Graphics g)
     { 
      for (int i=0; i<figures.size(); i++)
       {
        Figure figure = (Figure)figures.elementAt(i);
        sort(figure.elements);
        figure.draw(g, offset, "view Face");
        if(figure.net)
          {
            g.setColor(Color.black); 
            Side side;
            for(int k=0; k<figure.elements.size(); k++)
              net(g, ((Side)figure.elements.elementAt(k)).field);
          } 
       }       
     if (offset != null)
       { g.setColor(Color.black); g.fillOval(offset.x - 3, offset.y - 3, 6, 6);} 
     if (select instanceof Side)
       { g.setColor(Color.red); net(g, select.field);}
     else if(select instanceof Element)
       { g.setColor(Color.green); net(g, copy.field);}
     if (select.field.size() != 0)
       {
        g.setColor(Color.red);
        int t = ((Integer)select.field.lastElement()).intValue();
        Point3D p = (Point3D)points3D.elementAt(t);
        g.drawOval(offset.x + Math.round(p.x) - 3, offset.y + Math.round(p.y) - 3, 6, 6);
       } 
     }
   public void net(Graphics g, Vector vector)
     { 
       for (int i=0, t=0; i<vector.size(); i++) 
        {
          t = ((Integer)vector.elementAt(i)).intValue();
          Point3D p = (Point3D)points3D.elementAt(t);
          g.drawOval(offset.x + Math.round(p.x) - 5, offset.y + Math.round(p.y) - 5, 10, 10); 
        } 
    }
   public void sort(Vector v)
     {
       int j = v.size();
       while(--j > 0)
        { 
         for (int i=0; i<j; i++)
          {
           if (((Side)v.elementAt(i)).weight() < ((Side)v.elementAt(i+1)).weight())
            {     
             Side temp = (Side)v.elementAt(i);
             v.setElementAt((Side)v.elementAt(i+1), i); 
             v.setElementAt(temp, i+1);
            } 
          }
        }
     }  
 }    