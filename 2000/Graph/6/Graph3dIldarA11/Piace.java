 import java.awt.event.*;  
 import java.util.*;
 import java.applet.*;
 import java.awt.*;

 public class Piace extends Applet implements MouseListener, MouseMotionListener, KeyListener {   
 
   Vector points3D = new Vector();               //Вектор физических точек(3D).
   Vector figures = new Vector();                //Вектор всех фигур апплета.
   Figure figure;                                //Текущая фигура.
   Point offset;                                 //Начало отсчета.
   Element select;                               //Текущий элемент.
   Side copy;                                    //Используется для сохранения текущей стороны(Side).
   Matrix3D matrix = new Matrix3D();             //Для динамики мира точек3D
   int  tab, x0, y0, kind, step;                 //tab-для навигации по элементам мира;kind-вид элемента;step-перемещение элемента по осям.(x0,y0)-последняя записанная точка
   float turn, turnSum, rotate;                  //turn-приращение угола поворота,turnSum-угол поворота в reconstruct_A.
   int tab_add = 1;                             //для навигации по элементам.
   floatPanel FP;                                //red?панель инструментов.
   boolean full;                                 //заполненность Элементов.
   Color color = Color.black;                    //цвет Элементов.
   Rectangle rect = new Rectangle(0, 0, 10, 10); //следует за нажатием мыши.
   Image offScreenImage;                         //для двойной буферизации.
   Graphics offScreen;                           //для двойной буферизации.

   public void init()
    {
      addMouseMotionListener(this);
      addMouseListener(this);
      addKeyListener(this);
      figures.addElement(figure = new Figure(this, kind, color, full));
      figure.elements.addElement(select = new Side(this, kind, color, full));
      setBackground(Color.white);
      rotate = step  = 3;                        //?
      FP = new floatPanel(this, "Float Panel");  //red
      offScreenImage = createImage(getSize().width, getSize().height);  
      offScreen = offScreenImage.getGraphics();
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
     {System.out.println("3Dpoints:"+points3D.size()+";sides:"+figure.elements.size()+";select:"+select.field.size()+";figure:"+figure.field.size());}

   public void keyTyped(KeyEvent e)
    {
      char key = e.getKeyChar();

      if ((key == 's')&&(select.field.size() != 0))
        {
          figure.elements.addElement(select = new Side(this, kind, color, full));
          message();
        }

      if ((key == 'e')&&(select.field.size() != 0))
        {
          if(select instanceof Side) 
            copy = (Side)select;        
          copy.elements.addElement(select = new Element(this, kind, color, full));
          message();
        }

      if ((key == 'f')&&(select.field.size() != 0))                                                  	
        {
          figures.addElement(figure = new Figure(this, kind, Color.black, full));
          figure.elements.addElement(select = new Side(this, kind, color, full));
          message();
        }

      if ((key == e.VK_TAB) && !e.isControlDown() && !e.isShiftDown() && (select.field.size() != 0))  
        {nextSide();}
      if ((key == e.VK_TAB) && e.isControlDown() && (select.field.size() != 0))  
        {nextElement();}
      if ((key == e.VK_TAB) && e.isShiftDown() && (select.field.size() != 0))  
        {nextFigure();}

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

      if ((key == 'd')&&(select.field.size() != 0)) 
         {delete(select);}
      if ((key == 'D')&&(select.field.size() != 0)&&(figures.size() > 1))
         {delete(figure);}

      if ((key == 'A')&&(turnSum < 360)&&(select.field.size() != 0))
         {reconstruct_A(select);}

      if ((key == 'n')&&(select.field.size() != 0))
        { next_offset();}      

       if (key == 'x')
          rotate("X", figure);
       if (key == 'y') 
          rotate("Y", figure);
       if (key == 'z')
          rotate("Z", figure);  

       if (key == 'X')
          { matrix.identity(); matrix.rotate(rotate,"X");for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
       if (key == 'Y')  
          { matrix.identity(); matrix.rotate(rotate,"Y");for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
       if (key == 'Z') 
          { matrix.identity(); matrix.rotate(rotate,"Z");for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}

       if (key == '1') 
          rotate("X", select);
       if (key == '2') 
          rotate("Y", select);
       if (key == '3') 
          rotate("Z", select); 
 
       if (key == '4') 
          translate(select, step, 0, 0);
       if (key == '5') 
          translate(select, 0, step, 0);
       if (key == '6') 
          translate(select, 0, 0, step);
 
       if ((key == '7')&&(select instanceof Side))
          {copy = (Side)select; copy.normal_Left = !(copy.normal_Left);} 
 
       if (key == '8') 
          translate(figure, step, 0, 0);
       if (key == '9') 
          translate(figure, 0, step, 0);
       if (key == '0') 
          translate(figure, 0, 0, step);

       if (key == 'm')
         message();             
      // else
      //   return; 
       if(select.field.size() != 0) 
         Shade.mouseUp(this); 
       repaint();    
     }

   public void keyPressed(KeyEvent e) 
     {
       int key = e.getKeyCode();

       if ((key == e.VK_UP) && !e.isControlDown() && !e.isShiftDown())
         {offset = new Point(offset.x, offset.y - step);}
       if ((key == e.VK_DOWN) && !e.isControlDown() && !e.isShiftDown())
         {offset = new Point(offset.x, offset.y + step);}
       if ((key == e.VK_LEFT) && !e.isControlDown() && !e.isShiftDown())
         {offset = new Point(offset.x - step, offset.y);}
       if ((key == e.VK_RIGHT) && !e.isControlDown() && !e.isShiftDown())
         {offset = new Point(offset.x + step, offset.y);}
       
       if ((key == e.VK_UP)&& e.isControlDown())
        {scale(select, 1f, 1.1f, 1f);}
       if ((key == e.VK_DOWN)&& e.isControlDown())
        {scale(select, 1f, 1f/1.1f, 1f);}
       if ((key == e.VK_RIGHT)&& e.isControlDown())
        {scale(select, 1.1f, 1f, 1f);}
       if ((key == e.VK_LEFT)&& e.isControlDown())
        {scale(select, 1f/1.1f, 1f, 1f);} 

       if ((key == e.VK_UP)&& e.isShiftDown())
        {scale(figure, 1f, 1.1f, 1f);}
       if ((key == e.VK_DOWN)&& e.isShiftDown())
        {scale(figure, 1f, 1f/1.1f, 1f);}
       if ((key == e.VK_RIGHT)&& e.isShiftDown())
        {scale(figure, 1.1f, 1f, 1f);}
       if ((key == e.VK_LEFT)&& e.isShiftDown())
        {scale(figure, 1f/1.1f, 1f, 1f);} 

       if (key == e.VK_PAGE_UP)
        {scale(figure, 1.1f, 1.1f, 1.1f);}
       if (key == e.VK_PAGE_DOWN)
        {scale(figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);}

       if ((key == e.VK_DELETE)&&(points3D.size() != 0)) 
         {deletelast3D(select);}                         //System.out.println(e);

       if ((key == e.VK_ESCAPE)&&(points3D.size() != 0)) 
         {escape();FP.setControl();}  

       if(select.field.size() != 0) 
         Shade.mouseUp(this); 

       repaint();      
     }

   public void keyReleased(KeyEvent e) {}

   public void escape()//new metod 3.1
     {
      if((select.field.size() == 0)&&(points3D.size() != 0)) 
       {
        if((select instanceof Side)&&((figures.size() != 1)||(figure.elements.size()!=1)))
         {
           figure.elements.removeElement(select);
         } 
        else if(select instanceof Element)
         {
           copy.elements.removeElement(select);
         } 
        if((figure.elements.size() == 0)&&(figures.size() > 1))
         {
           figures.removeElement(figure);
           figure = (Figure)figures.lastElement();
         }
        select = (Side)figure.elements.lastElement();
       }
     } 

   public void delete(Element e)//new metod 1.1
     {
       while(e.field.size() != 0)
        deletelast3D(e);
       while(e.elements.size() != 0)
        {
          delete((Element)e.elements.lastElement());
          e.elements.removeElement((Element)e.elements.lastElement()); 
        } 
     }
   public void deletelast3D(Element e)
     {
       if(e.field.size() != 0)
        {
         int t = ((Integer)e.field.lastElement()).intValue();
         e.field.removeElement((Integer)e.field.lastElement());
         if(!reference(t)) 
           deletePoint3D(t);
        }
     }
   public void deletePoint3D(int t)//new2.1
     {
       points3D.removeElementAt(t);
       for (int i=0; i<figures.size(); i++)
         trim3D(t, (Figure)figures.elementAt(i));
     }  
   public void trim3D(int t, Element elem)   //new2.2
     {
       elem.field.removeElement(new Integer(t));
       for (int k=0, n=0; k<elem.field.size(); k++)
        {
          n = ((Integer)elem.field.elementAt(k)).intValue();
          if(n>t)
            elem.field.setElementAt(new Integer(n-1), k); 
        }
       for (int i=0; i<elem.elements.size(); i++)
         trim3D(t, (Element)elem.elements.elementAt(i));
     }    
   public boolean reference(int t)
     {
      for (int i=0; i<figures.size(); i++)
       {
         Figure figure = (Figure)figures.elementAt(i);
         for (int k=0; k<figure.elements.size(); k++)
          {
           Side side = (Side)figure.elements.elementAt(k);
           for (int m=0, n=0; m<side.field.size(); m++)
            {
              n = ((Integer)side.field.elementAt(m)).intValue();
              if(n==t)
                return true;
            }
          }
       }       
      return false;
     }

   public void copy(Element e)
     {
       Hashtable hash = new Hashtable();
       copyElement(e, hash);         
     }
   public void copyElement(Element e, Hashtable hash)
     {
      if(e instanceof Figure)
       { 
        figures.addElement(figure = new Figure(this, e.kind, e.color, e.full));
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Side)e.elements.elementAt(i), hash);
       }       	     
      else if(e instanceof Side)
       { 
        figure.elements.addElement(select = new Side(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, hash);
        copy = (Side)select;
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Element)e.elements.elementAt(i), hash);
       }       	     
      else if(e instanceof Element)
       { 
        copy.elements.addElement(select = new Element(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, hash);
       }         
     }
   public void copyPoint3D(Vector vector, Hashtable hash)
     {
      for (int i=0, t=0, n=0; i<vector.size(); i++)
       {
        t = ((Integer)vector.elementAt(i)).intValue();
        if((select instanceof Side)&&(hash.containsKey(new Integer(t)))) //если существующая точка стороны нового эл-та ,то:
          select.add(((Integer)hash.get(new Integer(t))).intValue());
        else
         {
          n = points3D.size();
          select.add(n);
          figure.add(n);
          hash.put(new Integer(t), new Integer(n)); 
          points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(t)));
         }
       } 
     }  

   public void next_offset()
     {
       next(select.field); 
       int t = ((Integer)select.field.elementAt(tab)).intValue();
       Point3D p = (Point3D)points3D.elementAt(t);
       offset.translate((int)p.x, (int)p.y);//new
       matrix.identity();
       matrix.translate(-p.x, -p.y, -p.z);
       for (int i=0; i<points3D.size(); i++)
         matrix.transform((Point3D)points3D.elementAt(i)); 
     }
   public void nextSide()
     {
       next(figure.elements); 
       select = (Side)figure.elements.elementAt(tab);
       FP.setControl(); Shade.mouseUp(this); 
     }
   public void nextElement()
     {
       if(select instanceof Side) 
         copy = (Side)select;        
       if(copy.elements.size() == 0)
         return;
       next(copy.elements); 
       select = (Element)copy.elements.elementAt(tab);
       FP.setControl(); Shade.mouseUp(this); 
     }
   public void nextFigure()
     {
       next(figures); 
       figure = (Figure)figures.elementAt(tab);
       select = (Side)figure.elements.elementAt(0);
       FP.setControl(); Shade.mouseUp(this); 
     }
   public void next(Vector vector)
     {
       tab = Math.min(Math.max(0, tab + tab_add), vector.size()-1);
       if((tab == vector.size()-1) || (tab == 0))
         tab_add = -tab_add;
     }
     
   public void reconstruct_A(Element elem)
     {                                      
       float xc, yc, zc;
       int k = select.field.size();
       matrix.identity();
       matrix.rotate(turn, "Z");
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

   public void rotate(String axis, Element e)
     {
       matrix.identity();
       matrix.rotate(rotate, axis);
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //else if???
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           rotate(axis, elem);
         }  
     }     
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
   public void transform(Matrix3D m, Vector v)
     {
       for (int i=0, t=0; i<v.size(); i++)
        {
          t = ((Integer)v.elementAt(i)).intValue();
          m.transform((Point3D)points3D.elementAt(t)); 
        }
     }
   public void update(Graphics g)                         //start new text:
     {
       offScreen.setColor(getBackground());
       offScreen.fillRect(0, 0, getSize().width, getSize().height);
       paint(offScreen);
       g.drawImage(offScreenImage, 0, 0, this);
       FP.repaint();
       requestFocus();                                     //end new metod;
     }                                                  
   public void paint(Graphics g)
     { 
       for (int i=0; i<figures.size(); i++)
         {
           Figure figure = (Figure)figures.elementAt(i);
           sort(figure.elements);
           figure.draw(g, offset, "view Face");
           if(figure.net)
             {g.setColor(Color.black); net(g, figure.field);}
         }       

       if (offset != null)
         {g.setColor(Color.black);g.fillOval(offset.x - 3, offset.y - 3, 6, 6);} 
       g.setColor(Color.red); 
       net(g, select.field);
     } 

   public void net(Graphics g, Vector vector)
     {
       int t, xs, ys;
       for (int i=0; i<vector.size(); i++) 
        {
          t = ((Integer)vector.elementAt(i)).intValue();
          xs = Math.round(((Point3D)points3D.elementAt(t)).x);
          ys = Math.round(((Point3D)points3D.elementAt(t)).y);
          g.drawOval(offset.x + xs - 5, offset.y + ys - 5, 10, 10); 
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