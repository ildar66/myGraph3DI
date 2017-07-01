 import java.applet.*;
 import java.awt.*;
 import java.util.*;

 class Side  {  
  Figure figure;
  Vector field = new Vector();
  Vector3D normal;
  boolean visible;
  Color color;

  public Side(Figure f, Color c) 
    {
      figure = f;
      color = c;
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

      normal = A.cross(B);
    }

  public void backface(Matrix3D m)
    {    
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
      g.setColor(color);      
      g.drawPolygon(getShadeXOY(offset)); 
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
   Side select, copy;
   Vector sides = new Vector();
   Matrix3D matrix = new Matrix3D();
   Color color = Color.red;
   int next;
   int step = 3;
   int rotate = 3;
   floatPanel FP; 

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
          select = new Side(this, color);
          select.add(0);          
          repaint();
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
             select.add(i);
             Graphics g = getGraphics();
             g.drawOval(x-5, y-5, 10, 10);
             Rectangle rc = select.getShadeXOY(offset).getBoundingBox();
             repaint(rc.x, rc.y, rc.width+1, rc.height+1);
             g.dispose();
System.out.println(points3D.size()+";"+select.field.size());
             return true;
            }        
       }
      current = new Point3D(x - offset.x, y - offset.y, 0);
      select.add(points3D.size()); 
      points3D.addElement(current);
      Graphics g = getGraphics();
      g.drawOval(x-5, y-5, 10, 10);
      Rectangle rec = select.getShadeXOY(offset).getBoundingBox();
      repaint(rec.x, rec.y, rec.width+1, rec.height+1); 
      System.out.println(points3D.size());             //  
      g.dispose();
System.out.println(points3D.size()+ ";" +select.field.size());
      return true;         
    }

   public boolean keyDown(Event evt, int key)
    {
      if ((key == 's')&&(select.field.size() > 1)) 
        { 
         sides.addElement(select);      
         select = new Side(this, color);          
        }

      if ((key == 'c')&&(select.field.size() > 1)) 
        {
          int t;
          float xc, yc, zc;
          copy = new Side(this, select.color);       
          for (int i=0; i<select.field.size(); i++) 
            {
              t = ((Integer)select.field.elementAt(i)).intValue();
              copy.add(points3D.size());
              xc = ((Point3D)points3D.elementAt(t)).x;
              yc = ((Point3D)points3D.elementAt(t)).y;
              zc = ((Point3D)points3D.elementAt(t)).z;
              points3D.addElement(new Point3D(xc, yc, zc));  
            }
          select = copy;
          System.out.println(points3D.size()+ ";" +select.field.size());         }

      if (key == Event.UP)
        {offset = new Point(offset.x, offset.y-step);}
      if (key == Event.DOWN)
        {offset = new Point(offset.x, offset.y+step);}
      if (key == Event.LEFT)
        {offset = new Point(offset.x-step, offset.y);}
      if (key == Event.RIGHT)
        {offset = new Point(offset.x+step, offset.y);}

      if (key == '+')
        {rotate = Math.abs(rotate);}
      if (key == '-')
        {rotate = - Math.abs(rotate);}
      if (key == '*')
        {rotate = - rotate;}
      
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