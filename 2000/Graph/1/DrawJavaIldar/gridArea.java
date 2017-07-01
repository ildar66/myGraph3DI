 import java.awt.*;
 import java.awt.image.*;  

 public class gridArea extends Canvas implements ImageObserver  {

   int width, height;
   Image grid;  
   int spacing = 5;  
   int half = spacing/2;

   public gridArea(Component c, int x, int y, int size) 
    {
      super();

      width = x;
      height = y;
      spacing = size;

      grid = c.createImage(x,y);
      Graphics g = grid.getGraphics();
      g.setColor(Color.white);
      g.fillRect(0,0,width,height);
      g.setColor(Color.black);

      for (int j = 0; j < height; j += spacing)
        for (int k = 0; k < width; k += spacing)
          g.drawLine(k,j,k,j);

      g.dispose();
       
    }  

   public int snap(int x)
    {
      return((x + half)/spacing * spacing);
    }

   public void draw(Graphics g)
    {
      g.drawImage(grid, 0, 0, this);
    }
 }
 
        
