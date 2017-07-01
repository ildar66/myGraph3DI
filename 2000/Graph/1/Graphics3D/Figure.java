 import java.applet.*;
 import java.awt.*;
 import java.util.*;

 class Side  { 
  Figure figure;
  Vector field = new Vector();
  Vector3D normal;
  boolean full;
  boolean visible = true;
  Color color;
  int kind;
  public static final int Poly = 0;
  public static final int freePoly = 1;
  public static final int Line= 2;
  public static final int freeLine = 3; 

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

  public void normal()
    { 
      int a = ((Integer)field.elementAt(0)).intValue();
      int b = ((Integer)field.elementAt(1)).intValue();
      int c = ((Integer)field.elementAt(2)).intValue();

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

      normal = B.cross(A);
    }

  public void backface()
    { 
      if (field.size() < 3)
        return; 
      normal();
      Vector3D o = new Vector3D(0.0f, 0.0f, -1.0f);
      if (normal.dot(o) < 0.0f)
        visible = false;
      else
        visible = true;        
    }

  public Polygon getShadeXOY(Point offset)
    {
      Polygon poly = new Polygon();
      int xp, yp, t;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          xp = Math.round(((Point3D)figure.points3D.elementAt(t)).x);
          yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);  
          poly.addPoint(offset.x + xp, offset.y + yp); 
        }
      if ((kind == Poly)||(kind == freePoly))
        {
          t = ((Integer)field.elementAt(0)).intValue();
          xp = Math.round(((Point3D)figure.points3D.elementAt(t)).x);
          yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);  
          poly.addPoint(offset.x + xp, offset.y + yp);
        }
      return(poly);
    }

  public Polygon getShade(Point offset)
    {
      Polygon poly = new Polygon();
      int xp, yp, t;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          xp = Math.round(((Point3D)figure.points3D.elementAt(t)).x);
          yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);  
          poly.addPoint(offset.x + xp, offset.y + yp); 
        }
      return(poly);
    }

  public Polygon getShadeZOY(Point offset)
    {
      Polygon poly = new Polygon();
      int xp, yp, t;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          xp = Math.round(((Point3D)figure.points3D.elementAt(t)).z);
          yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);  
          poly.addPoint(offset.x + xp, offset.y + yp); 
        }
      if ((kind == Poly)||(kind == freePoly))
        {
          t = ((Integer)field.elementAt(0)).intValue();
          xp = Math.round(((Point3D)figure.points3D.elementAt(t)).z);
          yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);  
          poly.addPoint(offset.x + xp, offset.y + yp);
        }
      return(poly);
    } 

  public void draw(Graphics g, Point offset)
    {
      if (visible)
        { 
          g.setColor(color);
          if ((full)&&(kind != Line))      
            g.fillPolygon(getShadeXOY(offset)); 
          else
            g.drawPolygon(getShadeXOY(offset)); 
        }
    } 

  public void drawControl(Graphics g, Point offset)
    {
      g.setColor(color);      
      g.drawPolygon(getShadeZOY(offset)); 
    }
 }
//////////////////////////////////////////////////////////////
 public class Figure extends Applet  {   
 
   Vector points3D = new Vector();
   Point offset = new Point(0, 0);
   Point3D current;
   Side copy, tab_copy;
   Vector sides = new Vector();
   Vector del = new Vector();
   Matrix3D matrix = new Matrix3D();
   Color color = Color.black;
   int next, tab, Xstart, Ystart, Xmouse, Ymouse, kind;
   int step = 3;
   int stepZ = 3;
   int rotate = 3;
   int turn = 5;//
   int tab_add = -1;
   floatPanel FP;
   boolean move, net, full, tab_flag, flag_backface;
   Side select = new Side(this, kind, color, full);
   public static final int Poly = 0;
   public static final int freePoly = 1;
   public static final int Line= 2;
   public static final int freeLine = 3; 

   public void init()
    {
      FP = new floatPanel(this, "Float Panel"); 
    }

   public boolean mouseDown(Event evt, int x, int y)
    {
     if (points3D.size() == 0)
       {offset = new Point(x, y); repaint();}                          
     Rectangle r = new Rectangle(x-5, y-5, 10, 10);
     int x0, y0;
     for (int i = 0; i < points3D.size(); i++)
      {
       x0 = Math.round(((Point3D)points3D.elementAt(i)).x) + offset.x;
       y0 = Math.round(((Point3D)points3D.elementAt(i)).y) + offset.y; 
       if (r.inside(x0, y0)) 
         {  
           if(!select.field.contains(new Integer(i))&&(!tab_flag)&&
              (select.field.size() == 0))
            { 
             select.add(i);
             del.addElement(new Boolean(false)); 
            }        
           move = true;        
           Xstart = x; Ystart = y;
           Xmouse = x; Ymouse = y; //             
           System.out.print(points3D.size()+";"+sides.size()+";");
           System.out.println("sel.field.size:"+select.field.size());
           Shade.mouseDown(this, x, y);   
           return true;            
         }
      }       
     if ((tab_flag)||(select.field.size() != 0))
       return true; 
     current = new Point3D(x - offset.x, y - offset.y, 0);    
     select.add(points3D.size());
     del.addElement(new Boolean(true));
     points3D.addElement(current);
     Xmouse = x; Ymouse = y; //
     System.out.print(points3D.size()+";"+sides.size()+";");
     System.out.println("sel.field.size:"+select.field.size());
     Shade.mouseDown(this, x, y);      
     return true;     
    }

   public boolean mouseDrag(Event evt, int x, int y)
    {      
      if ((move)&&((evt.modifiers & Event.CTRL_MASK) != 0))
        {
         int t;
         float translateX, translateY;
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
         Xstart = x;
         Ystart = y;
         repaint();
         return true;
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
                g.drawLine(x, y, Xmouse, Ymouse);
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
      Xmouse = x;
      Ymouse = y;         
      return true;      
    }

   public boolean mouseUp(Event evt, int x, int y)
    {
      //Shade.mouseUp(this, x, y);           
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
             if (r.inside(x0, y0)) 
               {  
                 if(!select.field.contains(new Integer(i))&&(!tab_flag))
                  { 
                   select.add(i);
                   del.addElement(new Boolean(false));
                   Rectangle rec = select.getShadeXOY(offset).getBoundingBox();
                   repaint(rec.x-5, rec.y-5, rec.width+10, rec.height+10);     
                  }   
                 System.out.print(points3D.size()+";"+sides.size()+";");
                 System.out.println("sel.field.size:S"+select.field.size()); 
                 return true;            
               }
            }       
           if (tab_flag)
             return true; 
           current = new Point3D(x - offset.x, y - offset.y, 0);    
           select.add(points3D.size());
           del.addElement(new Boolean(true));
           points3D.addElement(current);     
           break;          
       }
      Rectangle rec = select.getShadeXOY(offset).getBoundingBox();
      repaint(rec.x-5, rec.y-5, rec.width+10, rec.height+10);       
      System.out.print(points3D.size()+";"+sides.size()+";");
      System.out.println("sel.field.size::::"+select.field.size()); 
      return true;      
    }

   public boolean keyDown(Event evt, int key)
    {
      if ((key == 's')&&(select != null)&&(!tab_flag)) 
        { 
         sides.addElement(select);
         tab = sides.size();      
         select = new Side(this, kind, color, full);
         System.out.print(points3D.size()+";"+sides.size()+";");
         System.out.println("sel.field.size:"+select.field.size());           
        }

      if ((key == 'c')&&(select != null)&&(select.field.size() > 1)) 
        {
          int t;
          float xc, yc, zc;
          copy = new Side(this, select.kind, select.color, select.full);       
          for (int i=0; i<select.field.size(); i++) 
            {        
              t = ((Integer)select.field.elementAt(i)).intValue();
              copy.add(points3D.size());
              del.addElement(new Boolean(true));//
              xc = ((Point3D)points3D.elementAt(t)).x;
              yc = ((Point3D)points3D.elementAt(t)).y;
              zc = ((Point3D)points3D.elementAt(t)).z;
              points3D.addElement(new Point3D(xc, yc, zc));  
            }
          select = copy;
          System.out.print(points3D.size()+";"+sides.size()+";");
          System.out.println("sel.field.size:"+select.field.size()); 
        }

      if (key == 'a') 
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
          turn *= 2;                 
        }

      if (key == 9)  // code<TAB>==9;
        {
          if (!tab_flag)
            tab_copy = select;
          tab = Math.min(Math.max(0, tab + tab_add), sides.size()-1); 
          select = (Side)sides.elementAt(tab);
          tab_flag = true;
        } 

      if (key == 'o')  
        {
          select = tab_copy;
          tab_flag = false;
          tab = sides.size();    
        } 

      if ((key == 'd')&&(select != null)&&(points3D.size() != 0)) 
        { 
          if (sides.contains(select))                
            return true;
          if (select.field.size() == 0)
            {
              select = (Side)sides.lastElement();
              sides.removeElement(sides.lastElement());
              System.out.print(points3D.size()+";"+sides.size()+";");
              System.out.println("sel.field.size:"+select.field.size());
              repaint();   
              return true; 
            }           
          if (((Boolean)del.lastElement()).booleanValue())
            points3D.removeElementAt(points3D.size() - 1);
          select.field.removeElementAt(select.field.size() - 1); 
          del.removeElementAt(del.size() - 1);
          System.out.print(points3D.size()+";"+sides.size()+";");
          System.out.println("sel.field.size:"+select.field.size());   
        }

      if (key == Event.UP)
        {offset = new Point(offset.x, offset.y - step);}
      if (key == Event.DOWN)
        {offset = new Point(offset.x, offset.y + step);}
      if (key == Event.LEFT)
        {offset = new Point(offset.x - step, offset.y);}
      if (key == Event.RIGHT)
        {offset = new Point(offset.x + step, offset.y);}

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
             points3D.removeElementAt(i); 
             points3D.insertElementAt(current, i); 
           }
        } 

      if (key == 'x') 
        {
         matrix.identity();
         matrix.rotateX(rotate);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }  
        }

       if (key == 'y') 
        {
         matrix.identity();
         matrix.rotateY(rotate);
         for (int i=0; i<points3D.size(); i++)
          {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
          }    
        }

       if (key == 'z') 
         {
          matrix.identity();
          matrix.rotateZ(rotate);
          for (int i=0; i<points3D.size(); i++)
           {
            current = matrix.transform((Point3D)points3D.elementAt(i)); 
            points3D.removeElementAt(i); 
            points3D.insertElementAt(current, i); 
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
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
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
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
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
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
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
            points3D.removeElementAt(t); 
            points3D.insertElementAt(current, t); 
          }  
        }
       repaint();            
       return true;        
     }

   public void paint(Graphics g)
     {
       g.fillOval(offset.x - 5, offset.y - 5, 10, 10); 
       for (int i=0; i<sides.size(); i++)
         {
           Side side = (Side)sides.elementAt(i);
           if (flag_backface)
             side.backface();
           else
             side.visible = true; 
           side.draw(g, offset);
         }
       if ((select != null)&&(select.field.size() != 0))
         {
          select.draw(g, offset);
          int t, xs, ys;
          for (int i=0; i<select.field.size(); i++) 
            {
              t = ((Integer)select.field.elementAt(i)).intValue();
              xs = Math.round(((Point3D)points3D.elementAt(t)).x);
              ys = Math.round(((Point3D)points3D.elementAt(t)).y);
              if ((kind == Poly)||(kind == Line))  
                g.drawOval(offset.x + xs - 5, offset.y + ys - 5, 10, 10); 
            }
         } 
       FP.repaint();
     } 
 }
          //Rectangle rec = select.getShadeXOY(offset).getBoundingBox();
          //g.clearRect(rec.x, rec.y, rec.width+1, rec.height+1);   
          //g.setColor(Color.green); 
          //g.fillPolygon(select.getShadeXOY(offset));          
          //g.setColor(color);  