 import java.awt.event.*;  
 import java.util.Vector;
 import java.applet.*;
 import java.awt.*;

 class Side  {
 
  Figure figure;
  Vector field = new Vector();
  Vector3D normal;
  boolean full, normal_Left;
  Color color;
  int kind;
  public static final int Poly = 0, freePoly = 1;
  public static final int Line = 2, freeLine = 3; 

  public Side(Figure f, int k, Color c, boolean full) 
    {
      figure = f;
      kind = k;
      color = c;
      this.full = full;
    }

  public void add(int number)
    {
      field.addElement(new Integer(number));
    } 

  public Vector3D normal()
    { 
      int a = ((Integer)field.elementAt(0)).intValue();
      int b = ((Integer)field.elementAt(field.size()/2)).intValue();
      int c = ((Integer)field.elementAt(field.size()-1)).intValue();

      float x = ((Point3D)figure.points3D.elementAt(b)).x - 
                ((Point3D)figure.points3D.elementAt(a)).x;
      float y = ((Point3D)figure.points3D.elementAt(b)).y -
                ((Point3D)figure.points3D.elementAt(a)).y;
      float z = ((Point3D)figure.points3D.elementAt(b)).z - 
                ((Point3D)figure.points3D.elementAt(a)).z;
      Vector3D A = new Vector3D(x, y, z);

      x = ((Point3D)figure.points3D.elementAt(c)).x - 
                ((Point3D)figure.points3D.elementAt(a)).x;
      y = ((Point3D)figure.points3D.elementAt(c)).y -
                ((Point3D)figure.points3D.elementAt(a)).y;
      z = ((Point3D)figure.points3D.elementAt(c)).z - 
                ((Point3D)figure.points3D.elementAt(a)).z;
      Vector3D B = new Vector3D(x, y, z); 
      if (normal_Left)
        return(normal = A.cross(B));
      else
        return(normal = B.cross(A));
    }

  public boolean backface(boolean flag)
    {
      if ((field.size() < 3)||(!flag))
        return true;
      if (normal().z < 0.0f)
        return true;  
      else
        return false;  
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
          xp = Math.round(((Point3D)figure.points3D.elementAt(t)).x);
          yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);
          zp = Math.round(((Point3D)figure.points3D.elementAt(t)).z);
          if ((view.equals("view Face"))||(view.equals("shade")))    
            poly.addPoint(offset.x + xp, offset.y + yp); 
          else if (view.equals("view Right"))    
            poly.addPoint(offset.x + zp, offset.y + yp); 
          else if (view.equals("view Up"))    
            poly.addPoint(offset.x + xp, offset.y - zp);       
        }
      if ((kind == Line)||(kind == freeLine))
        {
          for (int i=field.size()-1; i>=0; i--) 
           {
            t = ((Integer)field.elementAt(i)).intValue();
            xp = Math.round(((Point3D)figure.points3D.elementAt(t)).x);
            yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y); 
            zp = Math.round(((Point3D)figure.points3D.elementAt(t)).z);
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

  public float weight()
    {
      if(field.size() == 0)  
        return(0f);    
      int t;
      float sumZ = 0f;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          sumZ += ((Point3D)figure.points3D.elementAt(t)).z; 
        }
      return(sumZ / field.size());     
    } 

  public void draw(Graphics g, Point offset, String view)
    {
      g.setColor(color);
      if ((full)&&((kind == Poly)||(kind == freePoly)))      
        g.fillPolygon(getShade(offset, view)); 
      g.drawPolygon(getShade(offset, view));  
    } 
 }
/////////////////////////////////////////////////////////////////////////////////////////
 public class Figure extends Applet implements MouseListener, MouseMotionListener,                                                                      KeyListener  {   
 
   Vector points3D = new Vector();
   Vector sides = new Vector();
   Vector del = new Vector();
   Point offset;
   Point3D current = null;
   Side copy;
   Matrix3D matrix = new Matrix3D();
   Color color = Color.black;
   int next, tab, Xstart, Ystart, kind, step, stepZ;
   float turn, turnSum, rotate;  
   int tab_add = -1;
   floatPanel FP = null;
   boolean move, net, full, tab_flag, flag_backface;
   Side select = new Side(this, kind, color, full);
   public static final int Poly = 0;
   public static final int freePoly = 1;
   public static final int Line= 2;
   public static final int freeLine = 3; 

   public void init()
    {
      FP = new floatPanel(this, "Float Panel");
      addMouseMotionListener(this);
      addMouseListener(this);
      addKeyListener(this);
      setBackground(Color.white);
      rotate = step = stepZ = 3;   
    }

   public void mousePressed(MouseEvent e) 
    {
     int x = e.getX();
     int y = e.getY();
     if (points3D.size() == 0)
       {offset = new Point(x, y); repaint();}                          
     Rectangle r = new Rectangle(x-5, y-5, 10, 10);
     int x0, y0;
     for (int i = 0; i < points3D.size(); i++)
      {
       x0 = Math.round(((Point3D)points3D.elementAt(i)).x) + offset.x;
       y0 = Math.round(((Point3D)points3D.elementAt(i)).y) + offset.y; 
       if (r.contains(x0, y0)) 
         {  
           if(!select.field.contains(new Integer(i))&&(!tab_flag)&&
              (select.field.size() == 0))
            { 
             select.add(i);
             del.addElement(new Boolean(false));
             sides.addElement(select);
             tab = sides.size();       
            }        
           move = true;        
           Xstart = x; Ystart = y;  
           System.out.print(points3D.size()+";"+sides.size()+";");
           System.out.println("sel.field.size:"+select.field.size());
           Shade.mouseDown(this, x, y);
         }
      }       
     if ((tab_flag)||(select.field.size() != 0))
       return ; 
     current = new Point3D(x - offset.x, y - offset.y, 0);    
     select.add(points3D.size());
     del.addElement(new Boolean(true));
     points3D.addElement(current);
     sides.addElement(select);
     tab = sides.size();      
     Xstart = x; Ystart = y;
     System.out.print(points3D.size()+";"+sides.size()+";");
     System.out.println("sel.field.size:"+select.field.size());
     Shade.mouseDown(this, x, y); 
    }

    public void mouseDragged(MouseEvent e) 
    { 
      int x = e.getX();
      int y = e.getY();     
      if ((move)&&((e.getModifiers() & Event.CTRL_MASK) != 0))
        {
         int t, translateX, translateY;
         translateX = x - Xstart;  
         translateY = y - Ystart;
         matrix.identity();
         matrix.translate(translateX, translateY, 0f); 
         for (int i=0; i<select.field.size(); i++)
          {
            t = ((Integer)select.field.elementAt(i)).intValue();
            current = matrix.transform((Point3D)points3D.elementAt(t)); 
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
          }
         Xstart = x; Ystart = y;         
         repaint();
         return ;
        } 

      switch (kind)
        {
         case freePoly:
         case freeLine:
           if (!tab_flag)      
            {
             if (kind == freePoly)
                Shade.mouseDrag(this, x, y);
             else if (kind == freeLine)
               {
                Graphics g = getGraphics();
                g.setColor(color);
                g.drawLine(x, y, Xstart, Ystart);
                g.dispose();
               }       
             current = new Point3D(x - offset.x, y - offset.y, 0);    
             select.add(points3D.size());
             del.addElement(new Boolean(true));
             points3D.addElement(current);
            }
           break;
         case Poly:
         case Line:
           Shade.mouseDrag(this, x, y);             
           break;
        } 
      Xstart = x; Ystart = y;
    }

   public void mouseReleased(MouseEvent e) 
    { 
      int x = e.getX();
      int y = e.getY();
      move = false;
      switch (kind)
       {
        case Poly:
        case Line:
           Rectangle r = new Rectangle(x-5, y-5, 10, 10);
           int x0, y0;
           for (int i = 0; i < points3D.size(); i++)
            {
             x0 = Math.round(((Point3D)points3D.elementAt(i)).x)+offset.x;   
             y0 = Math.round(((Point3D)points3D.elementAt(i)).y)+offset.y; 
             if (r.contains(x0, y0)) 
               {  
                 if(!select.field.contains(new Integer(i))&&(!tab_flag))
                  { 
                   select.add(i);
                   del.addElement(new Boolean(false));
                   Rectangle rec = select.getShade(offset, "view Face").getBounds();
                   repaint(rec.x-5, rec.y-5, rec.width+10, rec.height+10);     
                  }   
                 System.out.print(points3D.size()+";"+sides.size()+";");
                 System.out.println("sel.field.size:S"+select.field.size()); 
                 return ;            
               }
            }       
           if (tab_flag)
             return ; 
           current = new Point3D(x - offset.x, y - offset.y, 0);    
           select.add(points3D.size());
           del.addElement(new Boolean(true));
           points3D.addElement(current);     
           break;          
       }
      Rectangle rec = select.getShade(offset, "view Face").getBounds();
      repaint(rec.x-5, rec.y-5, rec.width+10, rec.height+10);       
      System.out.print(points3D.size()+";"+sides.size()+";");
      System.out.println("sel.field.size::::"+select.field.size());       
    }

   public void mouseMoved(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) {}

   public void keyTyped(KeyEvent e)
    {
      char key = e.getKeyChar();
      if (key == 's')
        { 
         select = new Side(this, kind, color, full);
         tab_flag = false;
         System.out.print(points3D.size()+";"+sides.size()+";");
         System.out.println("sel.field.size:"+select.field.size());           
        }

      if ((key == 'c')&&(select.field.size() > 1)) 
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
          sides.addElement(select);
          tab = sides.size();
          System.out.print(points3D.size()+";"+sides.size()+";");
          System.out.println("sel.field.size:"+select.field.size()); 
        }

      if ((key == 'a')&&(turnSum < 360)) 
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

      if (key == 9)  // code<TAB>==9;
        {
          tab = Math.min(Math.max(0, tab + tab_add), sides.size()-1); 
          select = (Side)sides.elementAt(tab);
          FP.setControl();
          tab_flag = true;
        } 

      if ((key == 'd')&&(points3D.size() != 0)&&(!tab_flag)) 
        {
          if (select.field.size() == 0)
            {
              sides.removeElement(select);
              select = (Side)sides.lastElement();
              FP.setControl();
              System.out.print(points3D.size()+";"+sides.size()+";");
              System.out.println("sel.field.size:"+select.field.size());         
              repaint();  
              return ;
            }           
          if (((Boolean)del.lastElement()).booleanValue())
             points3D.removeElementAt(points3D.size() - 1);
          select.field.removeElementAt(select.field.size() - 1); 
          del.removeElementAt(del.size() - 1);
          if ((points3D.size() == 0))
             {sides.removeElement(select); offset = null;}      
          System.out.print(points3D.size()+";"+sides.size()+";");
          System.out.println("sel.field.size:"+select.field.size());   
        }


      if (key == '+')
        {rotate = Math.abs(rotate); tab_add = 1;}
      if (key == '-')
        {rotate = - Math.abs(rotate); tab_add = -1;}
      if (key == '*')
        {rotate = - rotate; stepZ = - stepZ; tab_add = - tab_add;}
      
      if (key == 'n')
        {
          if (next < points3D.size()-1)
            next++;
          else
            next = 0;
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

      if (key == 'x') 
        {
         matrix.identity();
         matrix.rotateX(rotate);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.setElementAt(current, i);
          }  
        }

       if (key == 'y') 
        {
         matrix.identity();
         matrix.rotateY(rotate);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.setElementAt(current, i);
          }    
        }

       if (key == 'z') 
         {
          matrix.identity();
          matrix.rotateZ(rotate);
          for (int i=0; i<points3D.size(); i++)
           {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.setElementAt(current, i);
           } 
         }

       if (key == '0') 
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
 
      if (key == '1')
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

      if (key == '2')
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
      if (key == '3')
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

      if (key == '7')
        {select.normal_Left = !(select.normal_Left);}

       repaint();    
     }

   public void keyPressed(KeyEvent e) 
     {
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
       repaint();      
     }

   public void keyReleased(KeyEvent e) {}

   public void paint(Graphics g)
     {
       Vector copy_sides = (Vector)(sides.clone());
       sort(sides);
       for (int i=0; i<sides.size(); i++)
         {
           Side side = (Side)sides.elementAt(i);
           if (side.backface(flag_backface))           
             side.draw(g, offset, "view Face");
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

       FP.repaint();
       sides = copy_sides;
       requestFocus();
     } 

   public void sort(Vector s)
     {
       int j = sides.size();
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