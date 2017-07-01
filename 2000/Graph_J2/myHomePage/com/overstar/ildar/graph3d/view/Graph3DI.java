package com.overstar.ildar.graph3d.view;

 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;
 import com.overstar.ildar.graph3d.model.*;
 import com.overstar.ildar.graph3d.util.*;
 import com.overstar.ildar.graph3d.math.*;
  public class Graph3DI extends Applet implements Runnable {   
   public Piace piace;
   public SouthPanel SP;
   public Thread anim = null;

   public void destroy() {
     piace.FP.dispose();
     removeAll();
   }
   public String getAppletInfo() {
     return "Graph3DI.2000(author Ildar Shafigulline)";
   }
   public void init()  {
     setLayout(new BorderLayout());
     piace = new Piace(this);
     SP = new SouthPanel(piace);
     add("Center", piace);
     add("South", SP);
     add("West", new WestPanel(new WestPanelListener(piace)));
     add("East", new EastPanel(new EastPanelListener(piace)));
     add("North", new NorthPanel(piace));
     ScenarioWriter.scenarioPaint(this);
     SP.setControl(); 
   }
public void run() { //метод run потока анимации
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    while (Thread.currentThread() == anim) {
        for (int i = 0; i < piace.getPiace3D().getFigures().size(); i ++) {
            Figure figure = (Figure) piace.getPiace3D().getFigures().elementAt(i);
            figure.scenario(piace.matrix);
        }
        piace.repaint();
        try {
            anim.sleep(100);
        }
        catch (InterruptedException e) {
        }
    }
}
public void start() {
    if (anim == null)
        anim = new Thread(this);
    if (!anim.isAlive())
        anim.start();
}
   public void stop() {
     anim = null;
   }
}
