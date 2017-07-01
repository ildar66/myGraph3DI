 import java.awt.*;

 public class moveMode {   
  static int x_last;
  static int y_last;

  public static void mouseDown(Component c, int x, int y, colorMode r)
   {
      x_last = x;
      y_last = y;            
      drawbox(c, r);
   } 
  public static void mouseUp(Component c, int x, int y, colorMode r)
   {
      drawbox(c, r);
      r.translate(x - x_last,y - y_last);
   }

  public static void mouseDrag(Component c, int x, int y, colorMode r)
   {
      drawbox(c, r);
      r.translate(x - x_last,y - y_last); 
      x_last = x;
      y_last = y;
      drawbox(c, r);
   }

  public static void drawbox(Component c, colorMode r)
   {
     Graphics g = c.getGraphics();
     g.setXORMode(Color.white);
     r.draw(g);
     g.dispose();
   }
 }
  
  