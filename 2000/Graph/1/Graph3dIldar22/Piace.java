 import java.awt.event.*;  
 import java.util.Vector;
 import java.applet.*;
 import java.awt.*;

 class Element  {              //элемент стороны(Side) фигуры(figure).
 
  Piace piace;                 //ссылка на апплет.
  boolean full;                //показатель(full) заполненности элемента. 
  Color color;                 //цвет(color) элемента.
  int kind;                    //тип(kind) элемента:{Poly, freePoly, Line, freeLine}.
  Vector field = new Vector(); //набор номеров(number) трехмерных точек из которых состоит элемент.
  public static final int Poly = 0, freePoly = 1;//замкнутые:многоугольник(Poly),кривая(freePoly).
  public static final int Line = 2, freeLine = 3;//незамкнутые :линия(Line), кривая(freeLine). 

  public Element(Piace piace, int kind, Color color, boolean full) 
    {
      this.piace = piace;
      this.kind = kind;
      this.color = color;
      this.full = full;
    }

  public void add(int number) //добавить трехмерную точку с номером(number) к элементу(element).
    {
      field.addElement(new Integer(number));
    }

  public Polygon getShade(Point offset, String view)
    { 
      Polygon poly = new Polygon();
      if(field.size() == 0)  
         return(poly);    
      int xp, yp, zp, t;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          xp = Math.round(((Point3D)piace.points3D.elementAt(t)).x);
          yp = Math.round(((Point3D)piace.points3D.elementAt(t)).y);
          zp = Math.round(((Point3D)piace.points3D.elementAt(t)).z);
          if ((view.equals("view Face"))||(view.equals("shade")))    
            poly.addPoint(offset.x + xp, offset.y + yp); 
          else if (view.equals("view Right"))    
            poly.addPoint(offset.x + zp, offset.y + yp); 
          else if (view.equals("view Up"))    
            poly.addPoint(offset.x + xp, offset.y - zp);       
        }
      if ((kind == Line)||(kind == freeLine))
        {
          for (int i=field.size()-2; i>=0; i--) 
           {
            t = ((Integer)field.elementAt(i)).intValue();
            xp = Math.round(((Point3D)piace.points3D.elementAt(t)).x);
            yp = Math.round(((Point3D)piace.points3D.elementAt(t)).y); 
            zp = Math.round(((Point3D)piace.points3D.elementAt(t)).z);
            if (view.equals("view Face"))    
              poly.addPoint(offset.x + xp, offset.y + yp); 
            else if (view.equals("view Right"))    
              poly.addPoint(offset.x + zp, offset.y + yp); 
            else if (view.equals("view Up"))    
              poly.addPoint(offset.x + xp, offset.y - zp);
           } 
        } 
      return(poly);
    }

  public void draw(Graphics g, Point offset, String view)
    {
      g.setColor(color);
      if ((full)&&((kind == Poly)||(kind == freePoly))) //?     
        g.fillPolygon(getShade(offset, view));
      else  
        g.drawPolygon(getShade(offset, view));  
    } 
 }
///////////////////////////////////////////////////////////////////////////////////////////////////
 class Side extends Element {                 //сторона фигуры(figure).
 
  Vector elements = new Vector();          //вектор элементов стороны.
  boolean normal_Left;                     //показатель нормали(normal_Left).

  public Side(Piace piace, int kind, Color color, boolean full) 
    {
      super(piace, kind, color, full);
    }
 
  public Vector3D normal()    //вычисление вектора нормали к стороне(side).
    { 
      int a = ((Integer)field.elementAt(0)).intValue();             //номер нулевой точки элемента.
      int b = ((Integer)field.elementAt(field.size()/2)).intValue();//№ средней точки элемента.
      int c = ((Integer)field.elementAt(field.size()-1)).intValue();//№ последней точки элемента.

      float x = ((Point3D)piace.points3D.elementAt(b)).x - 
                ((Point3D)piace.points3D.elementAt(a)).x;
      float y = ((Point3D)piace.points3D.elementAt(b)).y -
                ((Point3D)piace.points3D.elementAt(a)).y;
      float z = ((Point3D)piace.points3D.elementAt(b)).z - 
                ((Point3D)piace.points3D.elementAt(a)).z;
      Vector3D A = new Vector3D(x, y, z); //вектор проведенный из середины эл-та в начало.

      x = ((Point3D)piace.points3D.elementAt(c)).x - 
                ((Point3D)piace.points3D.elementAt(a)).x;
      y = ((Point3D)piace.points3D.elementAt(c)).y -
                ((Point3D)piace.points3D.elementAt(a)).y;
      z = ((Point3D)piace.points3D.elementAt(c)).z - 
                ((Point3D)piace.points3D.elementAt(a)).z;
      Vector3D B = new Vector3D(x, y, z); //вектор проведенный из конца эл-та в начало.
      if (normal_Left)           //если показатель левосторонний.
        return(A.cross(B));
      else                       //если показатель правосторонний.
        return(B.cross(A));
    }

  public boolean backface(boolean flag)//определение лицевой поверхности элемента.
    {
      if ((field.size() < 3)||(!flag)) //если флаг разрешения(flag) выкл.
        return true;                   
      if (normal().z < 0.0f)           //если нормаль к поверхности направлена на нас.
        return true;  
      else                             //если нормаль к поверхности направлена от нас.
        return false;  
    }

  public float weight()
    {
      if(field.size() == 0)  
        return(0f);    
      int t;
      float sumZ = 0f;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          sumZ += ((Point3D)piace.points3D.elementAt(t)).z; //подумать над sum**2;
        }
      return(sumZ / field.size());     
    } 

  public void draw(Graphics g, Point offset, String view)
    {
      super.draw(g, offset, view);
      for (int i=0; i<elements.size(); i++)
         {
           Element element = (Element)elements.elementAt(i);        
           element.draw(g, offset, view);
         }      
    } 
 }
/////////////////////////////////////////////////////////////////////////////////////////
 class Figure {
   
   Vector sides = new Vector();
   boolean flag_backface;

   public void draw(Graphics g, Point offset, String view)
    {
      for (int i=0; i<sides.size(); i++)
        {
          Side side = (Side)sides.elementAt(i);
          if (side.backface(flag_backface))           
            side.draw(g, offset, view);
        }
    } 
}

///////////////////////////////////////////////////////////////////////////////////////////////
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
   int next, tab, Xstart, Ystart, kind, step, stepZ;   //??
   float turn, turnSum, rotate;  //?
   int tab_add = -1;   //?
   floatPanel FP;   //red
   boolean net, full;
    
   public void init()
    {
      figures.addElement(figure = new Figure());
      figure.sides.addElement(select = new Side(this, kind, color, full));
      FP = new floatPanel(this, "Float Panel");  //red
      addMouseMotionListener(this);
      addMouseListener(this);
      addKeyListener(this);
      setBackground(Color.white);
      rotate = step = stepZ = 3;   //?
    }

   public void SavePoint3D(int i, int x, int y)
    {
      select.add(i);
      if(i == points3D.size())//если точка новая, то:
        points3D.addElement(current = new Point3D(x - offset.x, y - offset.y, 0));//current?
      message();             
    }

   public int numberPoint(int x, int y)//
    {
      Rectangle r = new Rectangle(x-5, y-5, 10, 10);
      int x0, y0;
      for (int i = 0; i < points3D.size(); i++)
       {
        x0 = Math.round(((Point3D)points3D.elementAt(i)).x) + offset.x;
        y0 = Math.round(((Point3D)points3D.elementAt(i)).y) + offset.y; 
        if (r.contains(x0, y0))                                 
           return i;                                                     
       }
      return points3D.size();                    
    }

   public void mousePressed(MouseEvent e) 
    {
      int x = e.getX(), y = e.getY();           
      if (offset == null)
        offset = new Point(x, y);                          
      if(select.field.size() == 0)
        { 
          if(select instanceof Side)
            SavePoint3D(numberPoint(x, y), x, y);
          else if(select instanceof Element)
            SavePoint3D(points3D.size(), x, y);
        }
      Xstart = x; Ystart = y;                   //?
      repaint(x-5, y-5, 10, 10);                //?
      Shade.mouseDown(this, x, y);              //?
    }

    public void mouseDragged(MouseEvent e) 
    { 
      int x = e.getX(), y = e.getY();
      switch (kind)
        {
         case Element.freePoly:
         case Element.freeLine:
             if (kind == Element.freePoly) 
                Shade.mouseDrag(this, x, y);//?
             else if (kind == Element.freeLine) //move to Shade:class!!!
               {
                Graphics g = getGraphics();
                g.setColor(color);
                g.drawLine(x, y, Xstart, Ystart);
                g.dispose();
               }
             Rectangle r = new Rectangle(x-5, y-5, 10, 10);             
             int x0 = Math.round(((Point3D)points3D.lastElement()).x) + offset.x;
             int y0 = Math.round(((Point3D)points3D.lastElement()).y) + offset.y; 
             if (!(r.contains(x0, y0)))
                SavePoint3D(points3D.size() , x, y);                  
             break;
         case Element.Poly:
         case Element.Line:
             Shade.mouseDrag(this, x, y);             //?
             break;
        } 
      Xstart = x; Ystart = y;
    }

   public void mouseReleased(MouseEvent e) 
    { 
      int x = e.getX(), y = e.getY();
      switch (kind)
       {
        case Element.Poly:
        case Element.Line:
          if(select instanceof Side)
            SavePoint3D(numberPoint(x, y), x, y);
          else if(select instanceof Element)
            SavePoint3D(points3D.size(), x, y); 
        break;          
       }
      Rectangle rec = select.getShade(offset, "view Face").getBounds();
      repaint(rec.x-5, rec.y-5, rec.width+10, rec.height+10);       
    }

   public void message()
     {System.out.println("3Dpoints:"+points3D.size()+";sides:"+figure.sides.size()+";select:"+select.field.size());}

   public void mouseMoved(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) {}

   public void keyTyped(KeyEvent e)
    {
      char key = e.getKeyChar();

      if (key == 's')
        {
         figure.sides.addElement(select = new Side(this, kind, color, full));
         message();
        }

      if (key == 'e')
        {
         if(select instanceof Side) 
           copy = (Side)select;        
         copy.elements.addElement(select = new Element(this, kind, color, full));
         message();
        }

      if (key == 'f')
        {
         figures.addElement(figure = new Figure());
         figure.sides.addElement(select = new Side(this, kind, color, full));
         message();
        }

 /*     else if ((key == 'c')&&(select.field.size() > 1)) 
        {
          int t;
          float xc, yc, zc;
          copy = new Side(this, select.kind, select.color, select.full);       
          for (int i=0; i<select.field.size(); i++) 
            {        
              t = ((Integer)select.field.elementAt(i)).intValue();
              copy.add(points3D.size());
              del.addElement(new Boolean(true));
              xc = ((Point3D)points3D.elementAt(t)).x;
              yc = ((Point3D)points3D.elementAt(t)).y;
              zc = ((Point3D)points3D.elementAt(t)).z;
              points3D.addElement(new Point3D(xc, yc, zc));  
            }
          select = copy;
          figure.sides.addElement(select);
          tab = figure.sides.size();
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
        }

      else if (key == e.VK_TAB)  
        {
          tab = Math.min(Math.max(0, tab + tab_add), figure.sides.size()-1); 
          select = (Side)figure.sides.elementAt(tab);
          //FP.setControl();
          tab_flag = true;
        }

      else if ((key == 'd')&&(points3D.size() != 0)&&(!tab_flag)) 
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

      else if (key == '+')
        {rotate = Math.abs(rotate); tab_add = 1;}
      else if (key == '-')
        {rotate = - Math.abs(rotate); tab_add = -1;}
      else if (key == '*')
        {rotate = - rotate; stepZ = - stepZ; tab_add = - tab_add;}
      
      else if ((key == 'n')&&(points3D.size() != 0))
        {
          if (tab_add == -1)
            next = Math.min(points3D.size()-1, ++next);
          else if (tab_add == 1)
            next = Math.max(0, --next);
          float translateX, translateY, translateZ;      
          translateX = ((Point3D)points3D.elementAt(next)).x;
          translateY = ((Point3D)points3D.elementAt(next)).y;
          translateZ = ((Point3D)points3D.elementAt(next)).z;
          offset = new Point(offset.x + Math.round(translateX),
                            offset.y + Math.round(translateY));
          matrix.identity();
          matrix.translate(-translateX, -translateY, -translateZ);
          for (int i=0; i<points3D.size(); i++)
           {
             current = matrix.transform((Point3D)points3D.elementAt(i)); 
             points3D.setElementAt(current, i);
           }
        } 

      else if (key == 'x') 
        {
         matrix.identity();
         matrix.rotateX(rotate);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.setElementAt(current, i);
          }  
        }

       else if (key == 'y') 
        {
         matrix.identity();
         matrix.rotateY(rotate);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.setElementAt(current, i);
          }    
        }

       else if (key == 'z') 
         {
          matrix.identity();
          matrix.rotateZ(rotate);
          for (int i=0; i<points3D.size(); i++)
           {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.setElementAt(current, i);
           } 
         }

       else if (key == '0') 
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
 
      else if (key == '1')
        {
         int t;
         matrix.identity();
         matrix.rotateX(rotate);
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.setElementAt(current, t);
          }  
        }

      else if (key == '2')
        {
         int t;
         matrix.identity();
         matrix.rotateY(rotate);
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.setElementAt(current, t);
          }  
        }
      else if (key == '3')
        {
         int t;
         matrix.identity();
         matrix.rotateZ(rotate);
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.setElementAt(current, t);
          }  
        }

      else if (key == '7')
        {select.normal_Left = !(select.normal_Left);}
      else
        return;
       repaint(); */   
     }

   public void keyPressed(KeyEvent e) 
     {/*
       int key = e.getKeyCode();

       if ((key == e.VK_UP)&& !e.isControlDown()&& !e.isShiftDown())
         {offset = new Point(offset.x, offset.y - step);}
       else if ((key == e.VK_DOWN)&& !e.isControlDown()&& !e.isShiftDown())
         {offset = new Point(offset.x, offset.y + step);}
       else if ((key == e.VK_LEFT)&& !e.isControlDown()&& !e.isShiftDown())
         {offset = new Point(offset.x - step, offset.y);}
       else if ((key == e.VK_RIGHT)&& !e.isControlDown()&& !e.isShiftDown())
         {offset = new Point(offset.x + step, offset.y);}
       
       else if ((key == e.VK_UP)&& e.isControlDown())
        {
         int t;
         matrix.identity();
         matrix.scale(1f, 1.1f, 1f);
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
          }            
        }
       else if ((key == e.VK_DOWN)&& e.isControlDown())
        {
         int t;
         matrix.identity();
         matrix.scale(1f, 1f/1.1f, 1f);
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
          }            
        }
      else if ((key == e.VK_RIGHT)&& e.isControlDown())
        {
         int t;
         matrix.identity();
         matrix.scale(1.1f, 1f, 1f);
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
          }            
        }
      else if ((key == e.VK_LEFT)&& e.isControlDown())
        {
         int t;
         matrix.identity();
         matrix.scale(1f/1.1f, 1f, 1f);
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
          }            
        } 

       else if ((key == e.VK_UP)&& e.isShiftDown())
        {
         matrix.identity();
         matrix.scale(1f, 1.1f, 1f);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }
        }
       else if ((key == e.VK_DOWN)&& e.isShiftDown())
        {
         matrix.identity();
         matrix.scale(1f, 1f/1.1f, 1f);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }
        }
      else if ((key == e.VK_RIGHT)&& e.isShiftDown())
        { 
         matrix.identity();
         matrix.scale(1.1f, 1f, 1f);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }
        }
      else if ((key == e.VK_LEFT)&& e.isShiftDown())
        {
         matrix.identity();
         matrix.scale(1f/1.1f, 1f, 1f);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }
        } 

      else if (key == e.VK_PAGE_UP)
        {
         matrix.identity();
         matrix.scale(1.1f, 1.1f, 1.1f);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }
        }
      else if (key == e.VK_PAGE_DOWN)
        {
         matrix.identity();
         matrix.scale(1f/1.1f, 1f/1.1f, 1f/1.1f);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }
        }
       else
        return;
       repaint();  */    
     }

   public void keyReleased(KeyEvent e) {}

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
       Vector copy_sides; 
       for (int i=0; i<figures.size(); i++)
         {
           Figure figure = (Figure)figures.elementAt(i);
           copy_sides = (Vector)(figure.sides.clone());
           sort(figure.sides);
           figure.draw(g, offset, "view Face");
           figure.sides = copy_sides;
         }       

       g.setColor(Color.black);
       if (offset != null)
        g.fillOval(offset.x - 5, offset.y - 5, 10, 10); 
       int t, xs, ys;
       if(net)
         {     
           for (int i=0; i<points3D.size(); i++) 
            {
              xs = Math.round(((Point3D)points3D.elementAt(i)).x);
              ys = Math.round(((Point3D)points3D.elementAt(i)).y);
              g.drawOval(offset.x + xs - 5, offset.y + ys - 5, 10, 10); 
            } 
         }

        g.setColor(Color.red); 
        for (int i=0; i<select.field.size(); i++) 
         {
           t = ((Integer)select.field.elementAt(i)).intValue();
           xs = Math.round(((Point3D)points3D.elementAt(t)).x);
           ys = Math.round(((Point3D)points3D.elementAt(t)).y);
           g.drawOval(offset.x + xs - 5, offset.y + ys - 5, 10, 10); 
         } 
     } 

   public void sort(Vector s)
     {
       int j = figure.sides.size();
       while(--j > 0)
        { 
         for (int i=0; i<j; i++)
          {
           if (((Side)s.elementAt(i)).weight() < ((Side)s.elementAt(i+1)).weight())
             {     
               Side temp = (Side)s.elementAt(i);
               s.setElementAt((Side)s.elementAt(i+1), i); 
               s.setElementAt(temp, i+1);
             } 
          }
        }
   }  
 }    