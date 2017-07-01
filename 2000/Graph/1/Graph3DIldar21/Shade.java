 import java.awt.*;

 public class Shade { 

   static int x_anchor, y_anchor;
   public static final int Poly = 0;
   public static final int freePoly = 1;
   public static final int Line= 2;
   public static final int freeLine = 3; 

   public static void mouseDown(Piace piace, int x, int y)
    {
      x_anchor = x;
      y_anchor = y;
   } 

  public static void mouseDrag(Piace piace, int x, int y)
   {
      drawbox(piace, x_anchor, y_anchor);
      x_anchor = x;
      y_anchor = y;
      drawbox(piace, x_anchor, y_anchor);
   }
  public static void  mouseUp(Piace piace, int x, int y)
   {
      drawbox(piace, x_anchor, y_anchor);
   }
  public static void drawbox(Piace piace, int x, int y)
   {
     Graphics g = piace.getGraphics();
     g.setXORMode(Color.white);
     piace.setForeground(piace.color);
     int xp, yp, t;
     t = ((Integer)piace.select.field.lastElement()).intValue();
     xp = Math.round(((Point3D)piace.points3D.elementAt(t)).x);
     yp = Math.round(((Point3D)piace.points3D.elementAt(t)).y);
     g.drawLine(piace.offset.x + xp, piace.offset.y + yp, x, y);
     if ((piace.kind != Line)&&(piace.select.field.size() > 2))
      { 
        t = ((Integer)piace.select.field.elementAt(0)).intValue();
        xp = Math.round(((Point3D)piace.points3D.elementAt(t)).x);
        yp = Math.round(((Point3D)piace.points3D.elementAt(t)).y);
        g.drawLine(piace.offset.x + xp, piace.offset.y + yp, x, y);
      } 
     if ((piace.kind != Line)&&(piace.full))
      {
        Polygon poly = piace.select.getShade(piace.offset, "shade");
        poly.addPoint(x, y);
        g.fillPolygon(poly);
      }
     g.dispose();
   } 
}  