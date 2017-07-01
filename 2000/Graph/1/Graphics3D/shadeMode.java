 import java.awt.*;

 public class shadeMode { 

   static int x_anchor, y_anchor;
   static int x_mouse, y_mouse;   
   public static final int RECT = 0;
   public static final int OVAL = 1;
   public static final int LINE = 2;
   public static final int POINT = 3;

   public static void mouseDown(Component c, int x, int y, int mode,
                               boolean full, int transA, int transB)
    {
      x_anchor = x;
      y_anchor = y;
      x_mouse = x;
      y_mouse = y;
      drawbox(c, mode, full, transA, transB);
   } 

  public static void mouseDrag(Component c, int x, int y, int mode,
                              boolean full, int transA, int transB)
   {
      drawbox(c, mode, full, transA, transB);
      x_mouse = x;
      y_mouse = y;
      drawbox(c, mode, full, transA, transB);
   }
  public static Rectangle  mouseUp(Component c, int x, int y, int mode,                                   boolean full, int transA, int transB)
   {
      drawbox(c, mode, full, transA, transB);
      switch (mode)
       {
         case LINE:
                  if ((x == x_anchor) && (y == y_anchor))
                    return(null);         
                  return(new Rectangle(x_anchor, y_anchor, x, y));               
         default:
                  if ((x == x_anchor) || (y == y_anchor))
                    return(null);         
                  int xc = Math.min(x_anchor, x); 
                  int yc = Math.min(y_anchor, y);                  
                  return(new Rectangle(xc, yc, Math.abs(x-x_anchor),
                             Math.abs(y-y_anchor)));                 
       }
   }
  public static void drawbox(Component c, int mode, boolean full,
                                          int transA, int transB)
   {
     Graphics g = c.getGraphics();
     g.setXORMode(Color.white);
     int x = Math.min(x_anchor, x_mouse); 
     int y = Math.min(y_anchor, y_mouse);
     switch (mode)
      {
        case RECT:
          int width = Math.abs(x_mouse-x_anchor);
          int height = Math.abs(y_mouse-y_anchor);
          if (full)
            g.fillRoundRect(x, y, width, height, transA*width/100,                                                      transB*height/100); 
          g.drawRoundRect(x, y, width, height, transA*width/100,                                                      transB*height/100); 
          break;
        case OVAL:
          if (full)
            g.fillArc(x, y, Math.abs(x_mouse-x_anchor),
             Math.abs(y_mouse-y_anchor), transA*360/100, transB*360/100); 
          g.drawArc(x, y, Math.abs(x_mouse-x_anchor),
            Math.abs(y_mouse-y_anchor), transA*360/100 , transB*360/100);
          break;
        case LINE:
          if (full)
           {
            g.drawLine(x_anchor+1, y_anchor, x_mouse+1, y_mouse);
            g.drawLine(x_anchor-1, y_anchor, x_mouse-1, y_mouse);
            g.drawLine(x_anchor, y_anchor+1, x_mouse, y_mouse+1);
            g.drawLine(x_anchor, y_anchor-1, x_mouse, y_mouse-1);
           }
          g.drawLine(x_anchor, y_anchor, x_mouse, y_mouse);
          break;
      } 
     g.dispose();
   } 
}  