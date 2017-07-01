 import java.awt.*;

 public class colorMode extends Rectangle  {

 Color color;    
 int mode;
 int transA, transB;
 boolean full;
 public static final int RECT = 0;
 public static final int OVAL = 1;
 public static final int LINE = 2;

  public colorMode(int x, int y, int w, int h, int A, int B, int m,
                                                Color c, boolean f) 
   {
     super(x, y, w, h);
     color = c;
     mode = m;
     full = f;
     transA = A;
     transB = B;
   }
   
  public void draw(Graphics g)
   {    
     g.setColor(color);
     switch (mode)
      {
       case RECT: if(full)      
                   g.fillRoundRect(x,y,width,height,transA*width/100,
                                                  transB*height/100);
                  else
                   g.drawRoundRect(x,y,width,height,transA*width/100,
                                                  transB*height/100);
                  break;

       case OVAL: if (full)
                   g.fillArc(x, y, width, height, transA*360/100,                                                     transB*360/100);
                  else   
                   g.drawArc(x, y, width, height, transA*360/100,
                                                 transB*360/100);
                  break;

        case LINE: if (full)
                    {
                     g.drawLine(x+1, y, width+1, height);
                     g.drawLine(x-1, y, width-1, height);
                     g.drawLine(x, y+1, width, height+1);
                     g.drawLine(x, y-1, width, height-1);
                    }
                   g.drawLine(x, y, width, height);                 
                   break;
      }
   }
}   