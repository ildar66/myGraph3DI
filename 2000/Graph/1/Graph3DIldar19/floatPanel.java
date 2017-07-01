 import java.awt.*;
 import java.awt.event.*;

 public class floatPanel extends Frame implements ActionListener, ItemListener {
   MenuBar menubar = new MenuBar();
   Menu rotate = new Menu("Rotate", true);
   Menu turn = new Menu("Turn", true);
   Menu step = new Menu("Step", true);
   Choice kind = new Choice();
   Choice view = new Choice();
   Checkbox checkbox[];
   Button backface = new Button("Backface ON");
   Button net = new Button("Net ON");  
   Button full = new Button(" Full ");
   String viewer = "view Right";
   Figure figure;
   Point zero;
   String menuI[][] = {{"rotate=1", "rotate=3", "rotate=5"},
                       {"turn.1", "turn.2", "turn.3", "turn.4", "turn.5", "turn.6"},
                       {"stepX(Y)=1","stepX(Y)=3","stepX(Y)=5","stepZ=1","stepZ=3","stepZ=5"}};
   MenuItem mI[][];   

   floatPanel(Figure f, String title)               
    { 
      super(title);  
      figure = f;
      addWindowListener(new ClosePanel(this));
      setLayout(new BorderLayout());  

      setMenuBar(menubar);
      menubar.add(rotate); menubar.add(step); menubar.add(turn);
      mI = new MenuItem[menuI.length][];
      for (int i=0; i < menuI.length; i++)
        {
         mI[i] = new MenuItem[menuI[i].length];
         for (int k=0; k < menuI[i].length; k++)
          {
             mI[i][k] = new MenuItem(menuI[i][k]);
             mI[i][k].addActionListener(this);
          }
       }
      rotate.add(mI[0][0]); rotate.add(mI[0][1]); rotate.add(mI[0][2]);
      step.add(mI[2][0]); step.add(mI[2][1]); step.add(mI[2][2]); step.addSeparator();
      step.add(mI[2][3]); step.add(mI[2][4]); step.add(mI[2][5]);
      turn.add(mI[1][0]); turn.add(mI[1][1]); turn.add(mI[1][2]);
      turn.add(mI[1][3]); turn.add(mI[1][4]); turn.add(mI[1][5]);

      Panel north_panel = new Panel();
      kind.add("Polygon"); kind.add("free Polygon"); kind.add("Line"); kind.add("free Line");
      kind.addItemListener(this);
      north_panel.add(kind);
      net.addActionListener(this);
      north_panel.add(net);
      view.add("view Right"); view.add("view Up"); view.add("view Face");
      view.addItemListener(this);      
      north_panel.add(view);
      backface.addActionListener(this);
      north_panel.add(backface);
      add("North", north_panel);      
 
      Panel color_panel = new Panel();
      color_panel.add(full);
      full.addActionListener(this);
      CheckboxGroup group = new CheckboxGroup();
      Color CL[] = {Color.white, Color.orange, Color.pink, Color.green,
         Color.red, Color.cyan, Color.yellow, Color.blue,Color.magenta,
              Color.lightGray, Color.gray,Color.darkGray, Color.black}; 
      checkbox = new Checkbox[CL.length];     
      for (int i=0; i<checkbox.length; i++)
       {         
         color_panel.add(checkbox[i] = new Checkbox(null, group, true));
         checkbox[i].setBackground(CL[i]);
         checkbox[i].addItemListener(this);       
       } 
      add("South", color_panel);

      pack();
      show();
    }

  public void paint(Graphics g)
    {
      zero = new Point(getSize().width/2, getSize().height/2);   
      g.fillOval(zero.x - 3, zero.y - 3, 6, 6);
      g.setColor(Color.green); 
      g.fillPolygon(figure.select.getShade(zero, viewer));
      for (int i=0; i<figure.sides.size(); i++)
       {
         Side side = (Side)figure.sides.elementAt(i);
         g.setColor(side.color);
         g.drawPolygon(side.getShade(zero, viewer));  
       }
    } 

  public void actionPerformed(ActionEvent evt)
    {
      String obj = evt.getActionCommand();
      if (evt.getSource() instanceof Button)
        {
         if (obj.equals(" Full "))
           {full.setLabel("Empty"); figure.full = true; figure.select.full = true;}    
         else if (obj.equals("Empty"))
           {full.setLabel(" Full ");figure.full = false;figure.select.full = false;}
         else if (obj.equals("Net ON"))
           { figure.net = true; net.setLabel("Net OFF");}
         else if (obj.equals("Net OFF")) 
           { figure.net = false; net.setLabel("Net ON");}
         else if (obj.equals("Backface ON"))
           { figure.flag_backface = true; backface.setLabel("Backface OFF");}
         else if (obj.equals("Backface OFF")) 
           { figure.flag_backface = false; backface.setLabel("Backface ON");}
        }

      else if (evt.getSource() instanceof MenuItem)
        { 
           if (obj.equals("stepX(Y)=1"))
            figure.step = 1;        
           else if (obj.equals("stepX(Y)=3"))
            figure.step = 3;        
           else if (obj.equals("stepX(Y)=5"))
            figure.step = 5;     
           else if (obj.equals("stepZ=1"))
            figure.stepZ = 1;        
           else if (obj.equals("stepZ=3"))
            figure.stepZ = 3;        
           else if (obj.equals("stepZ=5"))
            figure.stepZ = 5; 
           else if (obj.equals("turn.1"))
            {figure.turn = 11.25f; figure.turnSum = 0;}
           else if (obj.equals("turn.2"))
            {figure.turn = 22.50f; figure.turnSum = 0;}
           else if (obj.equals("turn.3"))
            {figure.turn = 45.00f; figure.turnSum = 0;} 
           else if (obj.equals("turn.4"))
            {figure.turn = 60.00f; figure.turnSum = 0;}
           else if (obj.equals("turn.5"))
            {figure.turn = 90.00f; figure.turnSum = 0;}
           else if (obj.equals("turn.6"))
            {figure.turn = 120.0f; figure.turnSum = 0;}
           else if (obj.equals("rotate=1"))
            figure.rotate = 1; 
           else if (obj.equals("rotate=3"))
            figure.rotate = 3;
           else if (obj.equals("rotate=5"))
            figure.rotate = 5; 
        }
      figure.repaint();
      figure.requestFocus();
    } 

  public void itemStateChanged(ItemEvent evt)
    {  
      if (evt.getSource() instanceof Checkbox)
        {          
         figure.color = ((Component)evt.getSource()).getBackground();
         if(figure.select != null)
          figure.select.color = ((Component)evt.getSource()).getBackground();
        }
      if (evt.getSource() == view)
        { viewer = view.getSelectedItem();}
      if (evt.getSource() == kind)
        { figure.kind = figure.select.kind = kind.getSelectedIndex();}
      figure.requestFocus();
      figure.repaint();
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
      kind.select(figure.select.kind);
      if (figure.select.full)
        full.setLabel("Empty");    
      else
        full.setLabel(" Full ");   
    }
 }
 
  class ClosePanel extends WindowAdapter {
   floatPanel FP;
   public ClosePanel(floatPanel FP)
     {this.FP = FP;}
   public void windowClosing(WindowEvent e)
     {
       FP.dispose();
     } 
 } 