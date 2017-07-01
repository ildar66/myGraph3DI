 import java.awt.*;

 public class floatPanel extends Frame {

   MenuBar menubar = new MenuBar();
   Menu kind = new Menu("Kind", true);
   Menu view = new Menu("View", true);  
   Menu rotate = new Menu("Rotate", true);
   Menu step = new Menu("Step", true);
   Menu backface = new Menu("Backface", true);
   Menu net = new Menu("Net", true);
   Button button = new Button(" full ");          
   Checkbox checkbox[];
   String viewer = "view Right";
   Figure figure;
   Point zero;

   floatPanel(Figure f, String title)               
    { 
      super(title);  
      figure = f;

      setMenuBar(menubar);
      menubar.add(kind); menubar.add(view); menubar.add(rotate);  
      menubar.add(step); menubar.add(backface); menubar.add(net);
      kind.add("Polygon"); kind.add("free Polygon"); kind.add("Line"); 
      kind.add("free Line");     
      view.add("view Right"); view.add("view Up"); view.add("view Face");
      rotate.add("turn=1"); rotate.add("turn=3"); rotate.add("turn=5");
      step.add("stepX(Y)=1"); step.add("stepX(Y)=3");                  
      step.add("stepX(Y)=5"); step.addSeparator();
      step.add("stepZ=1"); step.add("stepZ=3"); step.add("stepZ=5");
      net.add("net ON"); net.add("net OFF");
      backface.add("backface ON"); backface.add("backface OFF");

      setLayout(new BorderLayout());

      Panel color_panel = new Panel();
      color_panel.add(button);
      CheckboxGroup group = new CheckboxGroup();
      Color CL[] = {Color.white, Color.orange, Color.pink, Color.green,
         Color.red, Color.cyan, Color.yellow, Color.blue,Color.magenta,
              Color.lightGray, Color.gray,Color.darkGray, Color.black}; 
      checkbox = new Checkbox[CL.length];     
      for (int i=0; i<checkbox.length; i++)
       {         
         color_panel.add(checkbox[i] = new Checkbox(null,group,true));
         checkbox[i].setBackground(CL[i]);          
       } 
      add("South", color_panel); 

      pack();
      show();
    }

  public void paint(Graphics g)
    {
      zero = new Point(size().width/2, size().height/2);
      g.fillOval(zero.x - 5, zero.y - 5, 10, 10);
      g.setColor(Color.green); 
      g.fillPolygon(figure.select.getShade(zero, viewer));
      for (int i=0; i<figure.sides.size(); i++)
       {
         Side side = (Side)figure.sides.elementAt(i);
         g.setColor(side.color);
         g.drawPolygon(side.getShade(zero, viewer));  
       }
    } 

 public boolean action(Event evt, Object obj)
    {     
      if (evt.target instanceof Button)
        {
          if (obj.equals(" full "))
            {
              ((Button)evt.target).setLabel("empty");
              figure.full = true;
              figure.select.full = true;
            }
          else if (obj.equals("empty")) 
            {
              ((Button)evt.target).setLabel(" full ");
              figure.full = false;
              figure.select.full = false;
            }
        }

      else if (evt.target instanceof MenuItem)
        {
           if (obj.equals("net ON"))
            {figure.net = true; figure.repaint();}
           if (obj.equals("net OFF")) 
            {figure.net = false; figure.repaint();}
           if (obj.equals("backface ON"))
            {figure.flag_backface = true; figure.repaint();}
           if (obj.equals("backface OFF")) 
            {figure.flag_backface = false; figure.repaint();}
           if (obj.equals("stepX(Y)=1"))
            {figure.step = 1;}        
           if (obj.equals("stepX(Y)=3"))
            {figure.step = 3;}        
           if (obj.equals("stepX(Y)=5"))
            {figure.step = 5;}     
           if (obj.equals("stepZ=1"))
            {figure.stepZ = 1;}        
           if (obj.equals("stepZ=3"))
            {figure.stepZ = 3;}        
           if (obj.equals("stepZ=5"))
            {figure.stepZ = 5;}                   
           if (obj.equals("turn=1"))
            {figure.rotate = 1; figure.turn = 5.714f;  figure.turnSum = 0;} 
           if (obj.equals("turn=3"))
            {figure.rotate = 3; figure.turn = 11.613f; figure.turnSum = 0;}
           if (obj.equals("turn=5"))
            {figure.rotate = 5; figure.turn = 24.000f; figure.turnSum = 0;} 
           if (obj.equals("Polygon"))
            {figure.kind = 0; figure.select.kind = 0;}          
           if (obj.equals("free Polygon"))
            {figure.kind = 1; figure.select.kind = 1;}        
           if (obj.equals("Line"))
            {figure.kind = 2; figure.select.kind = 2;}        
           if (obj.equals("free Line"))
            {figure.kind = 3; figure.select.kind = 3;}   
           if (obj.equals("view Right"))
            {viewer = "view Right";}        
           if (obj.equals("view Up"))
            {viewer = "view Up";}        
           if (obj.equals("view Face"))
            {viewer = "view Face";}        
        } 
     
      else if (evt.target instanceof Checkbox)
        {          
         figure.color = ((Component)evt.target).getBackground();
         if(figure.select != null)
          figure.select.color = ((Component)evt.target).getBackground();
        }
      figure.repaint();
      return true;
    } 

   public void setControl()
    {
      for(int i=0; i<checkbox.length; i++)
       {
        if(checkbox[i].getBackground()== figure.select.color)          
            checkbox[i].setState(true);
       }
      figure.color = figure.select.color;
      figure.kind = figure.select.kind;
      figure.full = figure.select.full;
      if (figure.select.full)
        button.setLabel("empty");    
      else
        button.setLabel(" full ");   
    }

   public boolean handleEvent(Event evt)
    {
      if(evt.id == Event.WINDOW_DESTROY)
        dispose();
       return super.handleEvent(evt);
    }
 }  
