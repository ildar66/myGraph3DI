 import java.applet.*;
 import java.awt.*;

 public class Graph3DI extends Applet  {   
   Piace piace;
   
   public void init()
    {
      setLayout(new BorderLayout());
      piace = new Piace();
      add("Center", piace);
      add("South",new SouthPanel(piace));
      add("West", new MovePanel(piace));
      add("North", new NorthPanel(piace));
    }

 }    