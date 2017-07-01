 import java.awt.event.*;  
 import java.util.Vector;
 import java.applet.*;
 import java.awt.*;

 public class Piace extends Applet implements MouseListener, MouseMotionListener, KeyListener {   
 
   Vector points3D = new Vector();
   Vector figures = new Vector();             //new
   Figure figure;                             //new
   Point offset;  
   Point3D current;   //нужна ли current?
   Element select;
   Side copy;
   Matrix3D matrix = new Matrix3D();
   Color color = Color.black;
   int  tab, x0, y0, kind, step;   //??
   float turn, turnSum, rotate;  //?
   int tab_add = -1;   //?
   floatPanel FP;   //red
   boolean full;
   Rectangle rect = new Rectangle(0, 0, 10, 10); //new
    
   public void init()
    {
      FP = new floatPanel(this, "Float Panel");  //red
      addMouseMotionListener(this);
      addMouseListener(this);
      addKeyListener(this);
      figures.addElement(figure = new Figure(this, kind, color, full));
      figure.elements.addElement(select = new Side(this, kind, color, full));
      setBackground(Color.white);
      rotate = step  = 3;   //?
    }

   public void mousePressed(MouseEvent e) 
    {
      int x = e.getX(), y = e.getY();           
      Shade.mouseDown(this, x, y);              
      if (offset == null)
         offset = new Point(x, y);  
      if(select.field.size() == 0)
         {SavePoint3D(numberPoint(x, y) , x, y); x0 = x; y0 = y;}                  
      repaint(x-5, y-5, 10, 10);                
    }

    public void mouseDragged(MouseEvent e) //new show Shade.java
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
        points3D.addElement(current = new Point3D(x - offset.x, y - offset.y, 0));//current?
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
          figures.addElement(figure = new Figure(this, kind, color, full));
          figure.elements.addElement(select = new Side(this, kind, color, full));
          message();
        }

      if ((key == e.VK_TAB) && !e.isControlDown() && !e.isShiftDown() && (select.field.size() != 0))  
        {
          tab = Math.min(Math.max(0, tab + tab_add), figure.elements.size()-1); 
          select = (Side)figure.elements.elementAt(tab);
          FP.setControl(); Shade.mouseUp(this); 
        }

      if ((key == e.VK_TAB) && e.isControlDown() && (select.field.size() != 0))  
        {
          if(select instanceof Side) 
            copy = (Side)select;        
          if(copy.elements.size() == 0)
            return;
          tab = Math.min(Math.max(0, tab + tab_add), copy.elements.size()-1); 
          select = (Element)copy.elements.elementAt(tab);
          FP.setControl(); Shade.mouseUp(this); 
        }

      if ((key == e.VK_TAB) && e.isShiftDown() && (select.field.size() != 0))  
        {
          tab = Math.min(Math.max(0, tab + tab_add), figures.size()-1); 
          figure = (Figure)figures.elementAt(tab);
          select = (Side)figure.elements.elementAt(0);
          FP.setControl(); Shade.mouseUp(this); 
        }

      if (key == '+')
         {tab_add = 1; rotate = Math.abs(rotate); step = Math.abs(step);}         
      if (key == '-')
         {tab_add = -1; rotate = - Math.abs(rotate); step = -Math.abs(step);}
      if (key == '*')
         {tab_add = - tab_add; rotate = - rotate; step = - step;}        

      if ((key == 'c')&&(select.field.size() > 1)) 
        {
          copy(select, points3D.size());
        }
      if ((key == 'C') && (select.field.size() > 1)) 
        {                                           
          copy(figure, points3D.size());
        }

      if ((key == 'd')&&(points3D.size() != 0)) 
        {
          delete(select);
        }
      if ((key == 'D')&&(points3D.size() != 0)) 
        {                                           
          delete(figure);
        }

/*      if ((key == e.VK_DELETE)&&(points3D.size() != 0)) 
        {
          if (select.field.size() != 0)
            {
              if (((Boolean)del.lastElement()).booleanValue())
                points3D.removeElementAt(points3D.size() - 1);
              select.field.removeElementAt(select.field.size() - 1); 
              del.removeElementAt(del.size() - 1);
            }           
          if (select.field.size() == 0)
            {
              figure.sides.removeElement(select);
              tab = figure.sides.size();
              if (points3D.size() != 0)
                {select = (Side)figure.sides.lastElement();
                // FP.setControl();
                }
              else
                offset = null;              
            }                     
          repaint();      
        }   

      else if ((key == 'a')&&(turnSum < 360)) 
        {
          int t;
          float xc, yc, zc;
          int k = select.field.size();
          matrix.identity();
          matrix.rotateZ(turn);
          for (int i=0; i<k; i++) 
            {        
              t = ((Integer)select.field.elementAt(i)).intValue();
              select.add(points3D.size());
              del.addElement(new Boolean(true));
              xc = ((Point3D)points3D.elementAt(t)).x;
              yc = ((Point3D)points3D.elementAt(t)).y;
              zc = ((Point3D)points3D.elementAt(t)).z;
              current = matrix.transform(new Point3D(xc, yc, zc)); 
              points3D.addElement(current);  
            }
          turnSum += turn;          
          turn *= 2.0f;       
        }   */

      else if ((key == 'n')&&(select.field.size() != 0))
        {
          tab = Math.min(Math.max(0, tab + tab_add), select.field.size()-1); 
          int t = ((Integer)select.field.elementAt(tab)).intValue();
          float translateX, translateY, translateZ;      
          translateX = ((Point3D)points3D.elementAt(t)).x;
          translateY = ((Point3D)points3D.elementAt(t)).y;
          translateZ = ((Point3D)points3D.elementAt(t)).z;
          offset.translate(Math.round(translateX), Math.round(translateY));//new
          matrix.identity();
          matrix.translate(-translateX, -translateY, -translateZ);
          for (int i=0; i<points3D.size(); i++)
             matrix.transform((Point3D)points3D.elementAt(i)); 
        }      

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
 
       if (key == '8') 
          translate(figure, step, 0, 0);
       if (key == '9') 
          translate(figure, 0, step, 0);
       if (key == '0') 
          translate(figure, 0, 0, step);

/*       else if (key == '0') 
        {
         int t;
         matrix.identity();
         matrix.translate(0f, 0f, stepZ); 
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t));
            points3D.setElementAt(current, t);
          }
        }      
 
      else if (key == '7')
        {select.normal_Left = !(select.normal_Left);} */
      if ((key == 'm')&&(select.field.size() != 0))
        message();             
      else
        return;
      //Shade.mouseUp(this); 
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
        {
          scale(select, 1f, 1.1f, 1f);
        }
       if ((key == e.VK_DOWN)&& e.isControlDown())
        {
          scale(select, 1f, 1f/1.1f, 1f);
        }
       if ((key == e.VK_RIGHT)&& e.isControlDown())
        {
          scale(select, 1.1f, 1f, 1f);
        }
       if ((key == e.VK_LEFT)&& e.isControlDown())
        {
          scale(select, 1f/1.1f, 1f, 1f);
        } 

       if ((key == e.VK_UP)&& e.isShiftDown())
        {
         scale(figure, 1f, 1.1f, 1f);
        }
       if ((key == e.VK_DOWN)&& e.isShiftDown())
        {
         scale(figure, 1f, 1f/1.1f, 1f);
        }
       if ((key == e.VK_RIGHT)&& e.isShiftDown())
        { 
         scale(figure, 1.1f, 1f, 1f);
        }
       if ((key == e.VK_LEFT)&& e.isShiftDown())
        {
         scale(figure, 1f/1.1f, 1f, 1f);
        } 

       if (key == e.VK_PAGE_UP)
        {
         scale(figure, 1.1f, 1.1f, 1.1f);
        }
       if (key == e.VK_PAGE_DOWN)
        {
         scale(figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);
        }
       repaint();      
     }

   public void keyReleased(KeyEvent e) {}

   public void delete(Element e)
     {
      if(e instanceof Figure)
       { 
        for (int i=0; i<e.elements.size(); i++) 
          delete((Side)e.elements.elementAt(i));
      //  figures.removeElement(figure);
      //  figure =  (Figure)figures.lastElement(); //добавить переход к селекту
       }       	     
      else if(e instanceof Side)
       { 
        copy = (Side)e; //позволяет создать redo/undo и режим вхождения в Element!!!
        do {
             int t = ((Integer)e.field.elementAt(0)).intValue();
             e.field.removeElementAt(0);
             if(!reference(t)) //
                deletePoint3D(t);
           }   while(e.field.size() != 0);
        for (int i=0; i<e.elements.size(); i++) 
          delete((Element)e.elements.elementAt(i));
        figure.elements.removeElement(e);
        if(figure.elements.size()==0)
        {
          figures.removeElement(figure);
          figure =  (Figure)figures.lastElement(); //добавить переход к селекту
        }
        select = (Side)figure.elements.lastElement();
       }       	     
      else if(e instanceof Element)
       {
        do {
             int t = ((Integer)e.field.elementAt(0)).intValue();
             e.field.removeElementAt(0);
             deletePoint3D(t);
           }   while(e.field.size() != 0);
        copy.elements.removeElement(e);
        select = copy;   //???select = (Element)figure.elements.lastElement(); 
       }
     }
   public void delete(Element e) //new delete
     {
      if(e instanceof Figure)
       { 
        do { 
            delete((Side)e.elements.elementAt(0));
           }   while(e.elements.size() != 0);
       }       	     
      else if(e instanceof Side)
       { 
        copy = (Side)e; //позволяет создать redo/undo и режим вхождения в Element!!!
        do {
             int t = ((Integer)e.field.elementAt(0)).intValue();
             e.field.removeElementAt(0);
             if(!reference(t)) //
                deletePoint3D(t);
           }   while(e.field.size() != 0);
        do { 
             delete((Element)e.elements.elementAt(0));
           }   while(e.elements.size() != 0);
        if((figures.size()!=1)||(figure.elements.size()!=1))
          figure.elements.removeElement(e);
        if(figure.elements.size()==0)
        {
          figures.removeElement(figure);
          figure =  (Figure)figures.lastElement(); //добавить переход к селекту
        }
        select = (Side)figure.elements.lastElement();
       }       	     
      else if(e instanceof Element)
       {
        do {
             int t = ((Integer)e.field.elementAt(0)).intValue();
             e.field.removeElementAt(0);
             deletePoint3D(t);
           }   while(e.field.size() != 0);
        copy.elements.removeElement(e);
        select = copy;   //???select = (Element)figure.elements.lastElement(); 
       }
     }
   public void deletePoint3D(int t)
     {
       points3D.removeElementAt(t);
       figure.field.removeElement(new Integer(t));
       for (int i=0; i<figures.size(); i++)
        {
          Figure figure = (Figure)figures.elementAt(i);
          for (int k=0, n=0; k<figure.field.size(); k++)
           {
              n = ((Integer)figure.field.elementAt(k)).intValue();
              if(n>t)
                figure.field.setElementAt(new Integer(n-1), k); 
           }
          for (int m=0; m<figure.elements.size(); m++)
           {
              Side side = (Side)figure.elements.elementAt(m);
              for(int k=0, n=0; k<side.field.size(); k++)
              {
                n = ((Integer)side.field.elementAt(k)).intValue();
                if(n>t)
                  side.field.setElementAt(new Integer(n-1), k);
              }  
              for (int l=0; l<side.elements.size(); l++)
              {
                 Element elem = (Element)side.elements.elementAt(l);
                 for(int k=0, n=0; k<elem.field.size(); k++)
                 {
                   n = ((Integer)elem.field.elementAt(k)).intValue();
                   if(n>t)
                     elem.field.setElementAt(new Integer(n-1), k);
                 }  
              }
           }
        }       
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

   public void copy(Element e, int ferst)
     {
      if(e instanceof Figure)
       { 
        Figure f = figure;
        figures.addElement(figure = new Figure(this, e.kind, e.color, e.full));
        for (int i=0; i<f.elements.size(); i++) 
          copy((Side)f.elements.elementAt(i), ferst);
       }       	     
      else if(e instanceof Side)
       { 
        figure.elements.addElement(select = new Side(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, ferst);
        copy = (Side)select;
        for (int i=0; i<e.elements.size(); i++) 
          copy((Element)e.elements.elementAt(i), ferst);
       }       	     
      else if(e instanceof Element)
       { 
        copy.elements.addElement(select = new Element(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, points3D.size()+1);
       }         
     }
   public void copyPoint3D(Vector vector, int ferst)
     {
      for (int i=0, t=0, n=0; i<vector.size(); i++)
       {
        t = ((Integer)vector.elementAt(i)).intValue();
        n = numberPoint(Math.round(((Point3D)points3D.elementAt(t)).x)+offset.x, Math.round(((Point3D)points3D.elementAt(t)).y)+offset.y);
        if((n >= ferst)&&(select instanceof Side)) //если точка нового эл-та ,то:
          select.add(n);
        else
         {
          select.add(points3D.size());
          figure.add(points3D.size()); 
          points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(t)));
         }
       } 
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
       matrix.translate(dx, dy, dz);  //
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //else if???
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           translate(elem, dx, dy, dz);  //
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
       Image offScreenImage = createImage(getSize().width, getSize().height); 
       Graphics offScreen = offScreenImage.getGraphics();
       offScreen.setColor(getBackground());
       offScreen.fillRect(0, 0, getSize().width, getSize().height);
       paint(offScreen);
       g.drawImage(offScreenImage, 0, 0, this);
       //FP.repaint();
       requestFocus();                                   //end new metod;
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
         {g.setColor(Color.black);g.fillOval(offset.x - 5, offset.y - 5, 10, 10);} 
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