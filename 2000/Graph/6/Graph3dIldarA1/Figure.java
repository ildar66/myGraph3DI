 import java.util.Vector;
 import java.awt.*;
 class Figure extends Side{
   boolean flag_backface, net;
   public Figure(Piace piace, int kind, Color color, boolean full) 
    {
      super(piace, kind, color, full);
    }  

   public void draw(Graphics g, Point offset, String view)
    {
      for (int i=0; i<elements.size(); i++)
        {
          Side side = (Side)elements.elementAt(i);
          if (side.backface(flag_backface))           
            side.draw(g, offset, view);
        }
    } 
}

///////////////////////////////////////////////////////////////////////////////////////////////
