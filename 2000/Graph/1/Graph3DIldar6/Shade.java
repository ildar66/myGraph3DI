 import java.awt.*;

 public class Shade { 

   static int x_anchor, y_anchor;
   public static final int Poly = 0;
   public static final int freePoly = 1;
   public static final int Line= 2;
   public static final int freeLine = 3; 

   public static void mouseDown(Figure f, int x, int y)
    {
      x_anchor = x;
      y_anchor = y;
   } 

  public static void mouseDrag(Figure f, int x, int y)
   {
      drawbox(f, x_anchor, y_anchor);
      x_anchor = x;
      y_anchor = y;
      drawbox(f, x_anchor, y_anchor);
   }
  public static void  mouseUp(Figure f, int x, int y)
   {
      drawbox(f, x_anchor, y_anchor);
   }
  public static void drawbox(Figure f, int x, int y)
   {
     Graphics g = f.getGraphics();
     g.setXORMode(f.getBackground());
     f.setForeground(f.color);
     int xp, yp, t;
     t = ((Integer)f.select.field.lastElement()).intValue();
     xp = Math.round(((Point3D)f.points3D.elementAt(t)).x);
     yp = Math.round(((Point3D)f.points3D.elementAt(t)).y);
     g.drawLine(f.offset.x + xp, f.offset.y + yp, x, y);
     if ((f.kind != Line)&&(f.select.field.size() > 2))
      { 
        t = ((Integer)f.select.field.elementAt(0)).intValue();
        xp = Math.round(((Point3D)f.points3D.elementAt(t)).x);
        yp = Math.round(((Point3D)f.points3D.elementAt(t)).y);
        g.drawLine(f.offset.x + xp, f.offset.y + yp, x, y);
      } 
     if ((f.kind != Line)&&(f.full))
      {
        Polygon poly = f.select.getShade(f.offset, "shade");
        poly.addPoint(x, y);
        g.fillPolygon(poly);
      }
     g.dispose();
   } 
}  