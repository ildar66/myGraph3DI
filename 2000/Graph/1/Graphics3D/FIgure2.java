 import java.applet.*;
 import java.awt.*;
 import java.util.*;

 class Side  { 
  Figure figure;
  Vector field = new Vector();
  Vector3D normal;
  boolean visible = true;
  boolean full;
  Color color;

  public Side(Figure f, Color c, boolean full) 
    {
      figure = f;
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
      t = ((Integer)field.elementAt(0)).intValue();
      xp = Math.round(((Point3D)figure.points3D.elementAt(t)).x);
      yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);  
      poly.addPoint(offset.x + xp, offset.y + yp);
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
      t = ((Integer)field.elementAt(0)).intValue();
      xp = Math.round(((Point3D)figure.points3D.elementAt(t)).z);
      yp = Math.round(((Point3D)figure.points3D.elementAt(t)).y);  
      poly.addPoint(offset.x + xp, offset.y + yp);
      return(poly);
    } 

  public void draw(Graphics g, Point offset)
    {
      backface();
      if (visible)
        { 
          g.setColor(color);
          if (full)      
            g.fillPolygon(getShadeXOY(offset)); 
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
   Side select, copy, tab_copy;
   Vector sides = new Vector();
   Vector del = new Vector();
   Matrix3D matrix = new Matrix3D();
   Color color = Color.black;
   int next, tab, Xstart, Ystart;
   int step = 3;
   int stepZ = 3;
   int rotate = 3;
   int tab_add = -1;
   floatPanel FP;
   boolean move, net, full, tab_flag; 

   public void init()
    {
      FP = new floatPanel(this, "Float Panel"); 
    }

   public boolean mouseDown(Event evt, int x, int y)
    {       
      if (points3D.size() == 0)
        {
          offset = new Point(x, y); 
          points3D.addElement(current = new Point3D(0, 0, 0));
          select = new Side(this, color, full);
          select.add(0);
          del.addElement(new Boolean(true));      //    
          repaint();
          System.out.print(points3D.size()+";"+sides.size()+";");
          System.out.println("sel.field.size:"+select.field.size());
          return true;
        }       
      Rectangle r = new Rectangle(x-5, y-5, 10, 10);
      int x0, y0;
      for (int i = 0; i < points3D.size(); i++)
       {
         x0 = Math.round(((Point3D)points3D.elementAt(i)).x) + offset.x;
         y0 = Math.round(((Point3D)points3D.elementAt(i)).y) + offset.y; 
         if (r.inside(x0, y0)) 
          {  
            if(!select.field.contains(new Integer(i))&&(!tab_flag))
             { 
               select.add(i);
               del.addElement(new Boolean(false));  //
               Graphics g = getGraphics();
               g.drawOval(x-5, y-5, 10, 10);
               r = select.getShadeXOY(offset).getBoundingBox();
               repaint(r.x, r.y, r.width+1, r.height+1);
               g.dispose();
             }        
            move = true;
            Xstart = x;
            Ystart = y;
            System.out.print(points3D.size()+";"+sides.size()+";");
            System.out.println("sel.field.size:"+select.field.size());   
            return true;            
          }                 
       }
      if (tab_flag)
        return true; 
      current = new Point3D(x - offset.x, y - offset.y, 0);    
      select.add(points3D.size());
      del.addElement(new Boolean(true));//
      points3D.addElement(current);        
      Graphics g = getGraphics();
      g.drawOval(x-5, y-5, 10, 10);
      Rectangle rec = select.getShadeXOY(offset).getBoundingBox();
      repaint(rec.x, rec.y, rec.width+1, rec.height+1); 
      g.dispose();
      System.out.print(points3D.size()+";"+sides.size()+";");
      System.out.println("sel.field.size:"+select.field.size()); 
      return true;         
    }

   public boolean mouseDrag(Event evt, int x, int y)
    {
      if (move)
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
        }
      else if (!tab_flag)      
        {
          current = new Point3D(x - offset.x, y - offset.y, 0);    
          select.add(points3D.size());
          del.addElement(new Boolean(true));
          points3D.addElement(current);        
          Graphics g = getGraphics();
          Rectangle rec = select.getShadeXOY(offset).getBoundingBox();
          repaint(rec.x, rec.y, rec.width+1, rec.height+1); 
          g.dispose(); 
          System.out.print(points3D.size()+";"+sides.size()+";");
          System.out.println("sel.field.size:"+select.field.size());
        }
      return true;      
    }

   public boolean mouseUp(Event evt, int x, int y)
    {
      move = false;
      return true;       
    }

   public boolean keyDown(Event evt, int key)
    {
      if ((key == 's')&&(select != null)&&(select.field.size() > 1)) 
        { 
         sides.addElement(select);
         tab = sides.size();      
         select = new Side(this, color, full);          
        }

      if ((key == 'c')&&(select != null)&&(select.field.size() > 1)) 
        {
          int t;
          float xc, yc, zc;
          copy = new Side(this, color, full);       
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

      if (key == 9)  // code<TAB>==9;
        {
          if (!tab_flag)
            tab_copy = select;
          tab = Math.min(Math.max(0, tab + tab_add), sides.size()-1); 
          select = (Side)sides.elementAt(tab);
          tab_flag = true;
        } 

      if (key == 'o')  // code<TAB>==9;
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
       if ((select != null)&&(select.field.size() != 0))
         {
          int t, xs, ys;
          for (int i=0; i<select.field.size(); i++) 
            {
              t = ((Integer)select.field.elementAt(i)).intValue();
              xs = Math.round(((Point3D)points3D.elementAt(t)).x);
              ys = Math.round(((Point3D)points3D.elementAt(t)).y);  
     //         g.drawOval(offset.x + xs - 5, offset.y + ys - 5, 10, 10); 
            }
          Rectangle rec = select.getShadeXOY(offset).getBoundingBox();
          g.clearRect(rec.x, rec.y, rec.width+1, rec.height+1);
          g.setColor(Color.green); 
          g.fillPolygon(select.getShadeXOY(offset)); 
          select.draw(g, offset);
          g.setColor(color);  
         } 
       for (int i=0; i<sides.size(); i++)
         ((Side)sides.elementAt(i)).draw(g, offset);
       FP.repaint();
     } 
 }