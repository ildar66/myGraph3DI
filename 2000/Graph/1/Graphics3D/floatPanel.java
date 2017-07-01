 import java.awt.*;

 public class floatPanel extends Frame {

   MenuBar menubar = new MenuBar();
   Menu kind = new Menu("Kind", true); 
   Menu rotate = new Menu("Rotate", true);
   Menu step = new Menu("Step", true);
   Menu net = new Menu("Net", true);   
   Menu backface = new Menu("Backface", true);       
   Checkbox checkbox[];
   Figure figure;
   Point zero;

   floatPanel(Figure f, String title)               
    { 
      super(title);  
      figure = f;

      setMenuBar(menubar);
      menubar.add(kind); menubar.add(net); menubar.add(rotate);  
      menubar.add(step); menubar.add(backface);
      kind.add("Polygon"); kind.add("free Polygon"); kind.add("Line"); 
      kind.add("free Line");           
      rotate.add("turn=1"); rotate.add("turn=3"); rotate.add("turn=5");
      step.add("stepX(Y)=1"); step.add("stepX(Y)=3");                  
      step.add("stepX(Y)=5"); step.addSeparator();
      step.add("stepZ=1"); step.add("stepZ=3"); step.add("stepZ=5");
      net.add("net ON"); net.add("net OFF");
      backface.add("backface ON"); backface.add("backface OFF");

      setLayout(new BorderLayout());

      Panel color_panel = new Panel();
      color_panel.add(new Button(" full "));
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
      zero = new Point(size().width/2, size().height/3);
      g.fillOval(zero.x - 5, zero.y - 5, 10, 10);  
      if ((figure.select != null)&&(figure.select.field.size() != 0))
       {
        Rectangle r = figure.select.getShadeZOY(zero).getBoundingBox();
        g.clearRect(r.x, r.y, r.width+1, r.height+1);
        g.setColor(Color.green); 
        g.fillPolygon(figure.select.getShadeZOY(zero)); 
        figure.select.drawControl(g, zero);
       }          
     for (int i=0; i<figure.sides.size(); i++)
      ((Side)figure.sides.elementAt(i)).drawControl(g, zero);
     return;  
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
            {figure.net = false; figure.repaint();}
           if (obj.equals("net OFF")) 
            {figure.net = true; figure.repaint();}
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
            {figure.rotate = 1; figure.turn=5;}     
           if (obj.equals("turn=3"))
            {figure.rotate = 3; figure.turn=10;}
           if (obj.equals("turn=5"))
            {figure.rotate = 5; figure.turn=20;}     
           if (obj.equals("Polygon"))
            {figure.kind = 0; figure.select.kind = 0;}          
           if (obj.equals("free Polygon"))
            {figure.kind = 1; figure.select.kind = 1;}        
           if (obj.equals("Line"))
            {figure.kind = 2; figure.select.kind = 2;}        
           if (obj.equals("free Line"))
            {figure.kind = 3; figure.select.kind = 3;}        
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

/*   public void setColor(Color color)
    {
      for(int i=0; i<checkbox.length; i++)
       {
        if(checkbox[i].getBackground()== color)
           checkbox[i].setState(true);
       }
    }*/

   public boolean handleEvent(Event evt)
    {
      if(evt.id == Event.WINDOW_DESTROY)
        dispose();
       return super.handleEvent(evt);
    }
 }  
