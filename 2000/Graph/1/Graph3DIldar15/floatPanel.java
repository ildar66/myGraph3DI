 import java.awt.*;
 import java.awt.event.*;

 public class floatPanel extends Frame implements ActionListener,
      ItemListener {

   MenuBar menubar = new MenuBar();
   Menu kind = new Menu("Kind", true);
   Menu view = new Menu("View", true);  
   Menu rotate = new Menu("Rotate", true);
   Menu turn = new Menu("Turn", true);
   Menu step = new Menu("Step", true);
   Menu backface = new Menu("Backface", true);
   Menu net = new Menu("Net", true);
   Button button = new Button(" full ");
   String viewer = "view Right";        
   Checkbox checkbox[];
   Figure figure;
   Point zero;
   String smI[][] = {{"Polygon", "free Polygon", "Line", "free Line"},
                    {"view Right", "view Up", "view Face"},
                    {"rotate=1", "rotate=3", "rotate=5"},
                    {"turn.1", "turn.2", "turn.3", "turn.4", "turn.5", "turn.6"},
                    {"stepX(Y)=1", "stepX(Y)=3", "stepX(Y)=5"},
                    {"stepZ=1", "stepZ=3", "stepZ=5"},
                    {"net ON", "net OFF"},
                    {"backface ON", "backface OFF"}};
   MenuItem mI[][];   

   floatPanel(Figure f, String title)               
    { 
      super(title);  
      figure = f;
      addWindowListener(new ClosePanel(this));  

      setMenuBar(menubar);
      menubar.add(kind); menubar.add(view); menubar.add(rotate);  
      menubar.add(step); menubar.add(backface); menubar.add(net);
      menubar.add(turn);
      mI = new MenuItem[smI.length][];
      for (int i=0; i < smI.length; i++)
        {
          mI[i] = new MenuItem[smI[i].length];
        }    
      for (int i=0; i < smI.length; i++)
        {
         for (int k=0; k < smI[i].length; k++)
          {
             mI[i][k] = new MenuItem(smI[i][k]);
             mI[i][k].addActionListener(this);
          }
       }
      kind.add(mI[0][0]); kind.add(mI[0][1]); kind.add(mI[0][2]); kind.add(mI[0][3]);
      view.add(mI[1][0]); view.add(mI[1][1]); view.add(mI[1][2]);
      rotate.add(mI[2][0]);rotate.add(mI[2][1]);rotate.add(mI[2][2]);
      turn.add(mI[3][0]); turn.add(mI[3][1]); turn.add(mI[3][2]);
      turn.add(mI[3][3]); turn.add(mI[3][4]); turn.add(mI[3][5]);
      step.add(mI[4][0]); step.add(mI[4][1]); step.add(mI[4][2]); step.addSeparator();
      step.add(mI[5][0]); step.add(mI[5][1]); step.add(mI[5][2]);
      net.add(mI[6][0]); net.add(mI[6][1]);
      backface.add(mI[7][0]); backface.add(mI[7][1]);

      setLayout(new BorderLayout());

      Panel color_panel = new Panel();
      color_panel.add(button);
      button.addActionListener(this);
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

  public void actionPerformed(ActionEvent evt)
    {
      String obj = evt.getActionCommand();
      if (evt.getSource() instanceof Button)
        {
         if (obj.equals(" full "))
           {
             button.setLabel("empty");
             figure.full = true;
             figure.select.full = true;
           }
         else 
           {
             button.setLabel(" full ");
             figure.full = false;
             figure.select.full = false;
           } 
        }

      else if (evt.getSource() instanceof MenuItem)
        { 
           if (obj.equals("net ON"))
            figure.net = true;
           else if (obj.equals("net OFF")) 
            figure.net = false;
           else if (obj.equals("backface ON"))
            figure.flag_backface = true;
           else if (obj.equals("backface OFF")) 
            figure.flag_backface = false;
           else if (obj.equals("stepX(Y)=1"))
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
           else if (obj.equals("Polygon"))
            {figure.kind = 0; figure.select.kind = 0;}          
           else if (obj.equals("free Polygon"))
            {figure.kind = 1; figure.select.kind = 1;}        
           else if (obj.equals("Line"))
            {figure.kind = 2; figure.select.kind = 2;}        
           else if (obj.equals("free Line"))
            {figure.kind = 3; figure.select.kind = 3;}   
           else if (obj.equals("view Right"))
            viewer = "view Right";        
           else if (obj.equals("view Up"))
            viewer = "view Up";        
           else if (obj.equals("view Face"))
            viewer = "view Face";
        } 
      figure.requestFocus();
      figure.repaint();
    } 


  public void itemStateChanged(ItemEvent evt)
    {  
      if (evt.getSource() instanceof Checkbox)
        {          
         figure.color = ((Component)evt.getSource()).getBackground();
         if(figure.select != null)
          figure.select.color = ((Component)evt.getSource()).getBackground();
        }
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
      if (figure.select.full)
        button.setLabel("empty");    
      else
        button.setLabel(" full ");   
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