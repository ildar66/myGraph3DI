 import java.awt.*;
 import java.util.*;

 public class rubRect {   
  static int x_last;
  static int y_last;
  static Rectangle rub;
  public static final int LINE = 2; 
  public static void mouseMove(Component c, int x, int y, int rub_size)
   {
     drawbox(c, x_last, y_last, rub_size);
     x_last = x;
     y_last = y;
     drawbox(c, x_last, y_last, rub_size);
   }

   public static void mouseDown(Component c,int x, int y, int rub_size)
   {     
     rub = new Rectangle(x-rub_size/2, y-rub_size/2, rub_size,                                                       rub_size);    
   } 

  public static void mouseDrag(Component c, Vector rects, int x, int y,
                                                          int rub_size)
   {     
      drawbox(c, x_last, y_last, rub_size);      
      int xF, yF;
      for (int i = 0; i < rects.size(); i++)
       {
        colorMode colorM = (colorMode) rects.elementAt(i);
        xF =(colorM).x;
        yF =(colorM).y;
        if((colorM.mode == LINE)&&(rub.inside(xF, yF)))
          rects.removeElementAt(i);
       }
      rub.translate(x - x_last,y - y_last);
      Graphics g = c.getGraphics();
      g.setColor(Color.white);
      g.fillRect(x-rub_size/2, y-rub_size/2, rub_size, rub_size);
      g.dispose(); 
      x_last = x;
      y_last = y;
      drawbox(c, x_last, y_last, rub_size);
   }
 
  public static void drawbox(Component c, int x, int y, int rub_size)
   {
     Graphics g = c.getGraphics();
     g.setXORMode(Color.white);
     g.fillRect(x-rub_size/2, y-rub_size/2, rub_size, rub_size);
     g.dispose();
   }
 }