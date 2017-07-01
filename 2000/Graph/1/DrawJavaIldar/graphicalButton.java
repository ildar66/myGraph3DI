 import java.awt.*;
 import java.applet.*;
 import java.awt.image.*;

 public class graphicalButton extends Canvas implements
 ImageObserver {

   Image image_up;
   Image image_down;
   String name;
   static boolean DOWN = false;
   static boolean UP = true;
   boolean is_up = true;

   public graphicalButton(String n, Image up, Image down)
    {
      image_up = up;
      image_down = down;
      name = n;
    }

   public Dimension minimumSize()
    {
      return(new Dimension(25,25));
    }

   public Dimension preferredSize()
    {
      return(minimumSize());
    }

   public boolean mouseDown(Event evt, int x, int y)
    {
      is_up = graphicalButton.DOWN;
      repaint();
      return(true);
    }

   public boolean mouseUp(Event evt, int x, int y)
    {
      if (inside(x,y))
       {
         deliverEvent(new Event(this, evt.when,
           Event.ACTION_EVENT, x, y, evt.key,
                    evt.modifiers, name));
       }
      else
       {
         is_up = graphicalButton.UP;
         repaint();
         System.out.println(name + "Abort");
       }

      return(true);
    }
  public void setButton(boolean state)
    {
      is_up = state;
      repaint();
    }

  public void update(Graphics g)
    {
      paint(g);
    }
  public void paint(Graphics g)
    {
      if (is_up)
        g.drawImage(image_up, 0, 0, 25, 25, this);
      else
        g.drawImage(image_down, 0, 0, 25, 25, this);
    }
 }
 
        
