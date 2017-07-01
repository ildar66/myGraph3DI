 import java.applet.*;
 import java.awt.*;

 public class Graph3DI extends Applet  {   
   Piace piace;
   SouthPanel SP;
   public void init()
    {
      setLayout(new BorderLayout());
      piace = new Piace(this);
      SP = new SouthPanel(piace);
      add("Center", piace);
      add("South",SP);
      add("West", new MovePanel(piace));
      add("North", new NorthPanel(piace));
    }

 }    