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
//   Button backface = new Button("Backface ON");  //
   Button net = new Button("Net ON");  
   Button full = new Button(" Full ");
   String viewer = "view Right";
   Piace piace;
   Point zero;
   String menuI[][] = {{"rotate=1", "rotate=3", "rotate=5"},
                       {"turn.1", "turn.2", "turn.3", "turn.4", "turn.5", "turn.6"},
                       {"step=1","step=3","step=5"}};
   MenuItem mI[][];   

   floatPanel(Piace piace, String title)               
    { 
      super(title);  
      this.piace = piace;
      addWindowListener(new ClosePanel(this));  // setLayout(new BorderLayout());  
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
      step.add(mI[2][0]);   step.add(mI[2][1]);   step.add(mI[2][2]); 
      turn.add(mI[1][0]);   turn.add(mI[1][1]);   turn.add(mI[1][2]);
      turn.add(mI[1][3]);   turn.add(mI[1][4]);   turn.add(mI[1][5]);
      
      Button b;
      Panel north_panel = new Panel(new FlowLayout(FlowLayout.CENTER, 2, 2));
      north_panel.add(b = new Button("Esc")); b.addActionListener(this);
      kind.add("Polygon"); kind.add("free Polygon"); kind.add("Line"); kind.add("free Line");
      north_panel.add(kind);     kind.addItemListener(this);  
      north_panel.add(b = new Button("new Element")); b.addActionListener(this);
      north_panel.add(b = new Button("new Side")); b.addActionListener(this);
      north_panel.add(b = new Button("new Figure")); b.addActionListener(this);
      view.add("view Right");view.add("view Up"); view.add("view Face");
      north_panel.add(view);     view.addItemListener(this);      
      add("North", north_panel); //north_panel.add(backface); backface.addActionListener(this);
 
      Panel color_panel = new Panel(new FlowLayout(FlowLayout.CENTER, 2, 2));
      color_panel.add(full); full.addActionListener(this);
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
      color_panel.add(net); net.addActionListener(this);  //new

      add("West", new MovePanel());

      add("East", new EastPanel());

      pack();
      show();
    }
  public void paint(Graphics g)
    {
      zero = new Point(getSize().width/2, getSize().height/2);   
      if(piace.select.field.size() != 0)
       {
        g.setColor(piace.select.color); 
        g.fillPolygon(piace.select.getShade(zero, viewer));
        for (int i=0; i<piace.figure.elements.size(); i++)
         {
          Side side = (Side)piace.figure.elements.elementAt(i);
          g.setColor(side.color);
          g.drawPolygon(side.getShade(zero, viewer));  
         }
       }
      g.fillOval(zero.x - 3, zero.y - 3, 6, 6);
    } 

  public void actionPerformed(ActionEvent evt)
    {
      String obj = evt.getActionCommand();
      if (evt.getSource() instanceof Button)
        {
         if (obj.equals(" Full "))
           {full.setLabel("Empty"); piace.full = true; piace.select.full = true;}    
         else if (obj.equals("Empty"))
           {full.setLabel(" Full "); piace.full = false;piace.select.full = false;}
         else if (obj.equals("Net ON"))
           {net.setLabel("Net OFF"); piace.net = piace.figure.net = true;}
         else if (obj.equals("Net OFF")) 
           {net.setLabel("Net ON"); piace.net = piace.figure.net = false;}
  //       else if (obj.equals("Backface ON"))
  //         {backface.setLabel("Backface OFF");piace.flag_backface = piace.figure.flag_backface = true;}
  //       else if (obj.equals("Backface OFF")) 
  //         {backface.setLabel("Backface ON"); piace.flag_backface = piace.figure.flag_backface = false;}
        }
      else if (evt.getSource() instanceof MenuItem)
        { 
         if (obj.equals("step=1"))
           piace.step = 1;        
         else if (obj.equals("step=3"))
           piace.step = 3;        
         else if (obj.equals("step=5"))
           piace.step = 5;     
         else if (obj.equals("turn.1"))  
           {piace.turn = 11.25f; piace.turnSum = 0;}
         else if (obj.equals("turn.2")) 
           {piace.turn = 22.50f; piace.turnSum = 0;}
         else if (obj.equals("turn.3"))
           {piace.turn = 45.00f; piace.turnSum = 0;} 
         else if (obj.equals("turn.4"))
           {piace.turn = 60.00f; piace.turnSum = 0;}
         else if (obj.equals("turn.5"))
           {piace.turn = 90.00f; piace.turnSum = 0;}
         else if (obj.equals("turn.6"))
           {piace.turn = 120.0f; piace.turnSum = 0;}
         else if (obj.equals("rotate=1"))
           piace.rotate = 1; 
         else if (obj.equals("rotate=3"))
           piace.rotate = 3;
         else if (obj.equals("rotate=5"))
           piace.rotate = 5; 
        }
      piace.repaint();
      piace.requestFocus();
    } 

  public void itemStateChanged(ItemEvent evt)
    {  
      if (evt.getSource() instanceof Checkbox)
        {          
         piace.color = ((Component)evt.getSource()).getBackground();
         if(piace.select != null)
          piace.select.color = ((Component)evt.getSource()).getBackground();
        }
      if (evt.getSource() == view)
        { viewer = view.getSelectedItem();}
      if (evt.getSource() == kind)
        { piace.kind = piace.select.kind = kind.getSelectedIndex();}
      piace.requestFocus();
      piace.repaint();
    } 

   public void setControl()
    {
      for(int i=0; i<checkbox.length; i++)
       {
        if(checkbox[i].getBackground()== piace.select.color)          
            checkbox[i].setState(true);
       }
      piace.color = piace.select.color;
      piace.kind = piace.select.kind;
      piace.full = piace.select.full;
      kind.select(piace.select.kind);
      if (piace.select.full)
        full.setLabel("Empty");    
      else
        full.setLabel(" Full ");   
      if (piace.figure.net)
        net.setLabel("Net OFF");  
      else
        net.setLabel("Net ON");   
    //  if (piace.figure.flag_backface)
    //    backface.setLabel("Backface OFF");    
    //  else
    //    backface.setLabel("Backface ON");
    }
 }
 
  class ClosePanel extends WindowAdapter {
   floatPanel FP;
   public ClosePanel(floatPanel FP)
     {this.FP = FP;}
   public void windowClosing(WindowEvent e)
     { FP.dispose(); } 
 }
/////////////////////////////////////////////////////////////////////////////
 /*class DrawControls extends Panel implements ItemListener {
    DrawPanel target;

    public DrawControls(DrawPanel target) {
	this.target = target;
	setLayout(new FlowLayout());
	setBackground(Color.lightGray);
	target.setForeground(Color.red);
	CheckboxGroup group = new CheckboxGroup();
	Checkbox b;
	add(b = new Checkbox(null, group, false));
	b.addItemListener(this);
	b.setBackground(Color.red);
	add(b = new Checkbox(null, group, false));
	b.addItemListener(this);
	b.setBackground(Color.green);
	add(b = new Checkbox(null, group, false));
	b.addItemListener(this);
	b.setBackground(Color.blue);
	add(b = new Checkbox(null, group, false));
	b.addItemListener(this);
	b.setBackground(Color.pink);
	add(b = new Checkbox(null, group, false));
	b.addItemListener(this);
	b.setBackground(Color.orange);
	add(b = new Checkbox(null, group, true));
	b.addItemListener(this);
	b.setBackground(Color.black);
	target.setForeground(b.getForeground());
	Choice shapes = new Choice();
	shapes.addItemListener(this);
	shapes.addItem("Lines");
	shapes.addItem("Points");
	shapes.setBackground(Color.lightGray);
	add(shapes);
    }

    public void paint(Graphics g) {
	Rectangle r = getBounds();

	g.setColor(Color.lightGray);
	g.draw3DRect(0, 0, r.width, r.height, false);
    }

  public void itemStateChanged(ItemEvent e) {
    if (e.getSource() instanceof Checkbox) {
      target.setForeground(((Component)e.getSource()).getBackground());
    } else if (e.getSource() instanceof Choice) {
      String choice = (String) e.getItem();
      if (choice.equals("Lines")) {
	target.setDrawMode(DrawPanel.LINES);
      } else if (choice.equals("Points")) {
	target.setDrawMode(DrawPanel.POINTS);
      }
    }
  }
}
///////////////////////////////////////////////////////////////////////
class ArcControls extends Panel
                  implements ActionListener {
    TextField s;
    TextField e;
    ArcCanvas canvas;

    public ArcControls(ArcCanvas canvas) {
	Button b = null;

	this.canvas = canvas;
	add(s = new TextField("0", 4));
	add(e = new TextField("45", 4));
	b = new Button("Fill");
	b.addActionListener(this);
	add(b);
	b = new Button("Draw");
	b.addActionListener(this);
	add(b);
    }

    public void actionPerformed(ActionEvent ev) {
	String label = ev.getActionCommand();

	canvas.redraw(label.equals("Fill"),
	              Integer.parseInt(s.getText().trim()),
	              Integer.parseInt(e.getText().trim()));
    }
}    */
/////////////////////////////////////////////////////////////////////
 class MovePanel extends Panel {
     protected void makeButton(String name, GridBagLayout gridbag, GridBagConstraints c)
     {
       Button button = new Button(name);
       gridbag.setConstraints(button, c);
       add(button);
     }
     protected void makeLabel(String name, GridBagLayout gridbag, GridBagConstraints c, Color color)
     {
       Label label = new Label(name);
       label.setBackground(color);
       gridbag.setConstraints(label, c);
       add(label);
     }
     public MovePanel() 
     {
       GridBagLayout gridbag = new GridBagLayout();
       GridBagConstraints c = new GridBagConstraints(); // setFont(new Font("Helvetica", Font.PLAIN, 14));
       setLayout(gridbag);
       c.fill = GridBagConstraints.HORIZONTAL; //  c.weightx = 3.0;
       c.insets = new Insets(1, 0, 1, 1);
          c.gridwidth = GridBagConstraints.REMAINDER; //end row

       makeLabel("Rotate:", gridbag, c, Color.blue);
          c.gridwidth = 1;
       makeButton("'X'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'Y'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'Z'", gridbag, c);
          c.gridwidth = 1;
       makeButton("'x'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'y'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'z'", gridbag, c);
          c.gridwidth = 1;
       makeButton("'1'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'2'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'3'", gridbag, c);

       makeLabel("Transfer:", gridbag, c, Color.blue);
       c.fill = GridBagConstraints.NONE; //  c.weightx = 3.0;
       makeLabel("Piace", gridbag, c, Color.green);
       c.fill = GridBagConstraints.HORIZONTAL; //  c.weightx = 3.0;
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'Up'", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER; //next-to-last in row
       makeButton("'->'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'Down'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'<-'", gridbag, c); // setSize(300, 100);//f.pack(); f.setSize(f.getPreferredSize()); f.show();   
       makeLabel("  Figure(Shift)", gridbag, c, Color.green);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton(" 'Up'", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER; //next-to-last in row
       makeButton(" '->'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton(" 'Down' ", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton(" '<-'", gridbag, c);
       makeLabel("  Select(Ctrl)", gridbag, c, Color.green);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'Up' ", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER; //next-to-last in row
       makeButton("'->' ", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'Down' ", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'<-' ", gridbag, c);

       makeLabel("Next:", gridbag, c, Color.blue);
       makeButton("'Element'", gridbag, c);
       makeButton("'Side'", gridbag, c);
       makeButton("'Figure'", gridbag, c);
       makeButton("'Offset'", gridbag, c);
     }
 }

 class EastPanel extends Panel {
     protected void makeButton(String name, GridBagLayout gridbag, GridBagConstraints c)
     {
       Button button = new Button(name);
       gridbag.setConstraints(button, c);
       add(button);
     }
     protected void makeLabel(String name, GridBagLayout gridbag, GridBagConstraints c, Color color)
     {
       Label label = new Label(name);
       label.setBackground(color);
       gridbag.setConstraints(label, c);
       add(label);
     }
     public EastPanel() 
     {
       GridBagLayout gridbag = new GridBagLayout();
       GridBagConstraints c = new GridBagConstraints(); // setFont(new Font("Helvetica", Font.PLAIN, 14));
       setLayout(gridbag);
       c.fill = GridBagConstraints.HORIZONTAL; //  c.weightx = 3.0;
       c.insets = new Insets(1, 0, 1, 1);
          c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeLabel("Scale", gridbag, c, Color.blue);
       makeLabel("Figure", gridbag, c, Color.green);
          c.gridwidth = 1;
       makeButton("'8'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'9'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'0'", gridbag, c);
       makeLabel("Select", gridbag, c, Color.green);
          c.gridwidth = 1;
       makeButton("'4'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'5'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'6'", gridbag, c);
       makeLabel("Piace(Page)", gridbag, c, Color.green);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("Down", gridbag, c);
   	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("Up", gridbag, c);

       makeLabel("Delete:", gridbag, c, Color.blue);
       makeLabel("Last Point", gridbag, c, Color.green);
       makeButton("'Delete'", gridbag, c);
       makeLabel("Select<Ctrl>", gridbag, c, Color.green);
       makeButton("'Delete'", gridbag, c);
       makeLabel("Figure<Shift>", gridbag, c, Color.green);
       makeButton("'Delete'", gridbag, c);

       makeLabel("Copy:", gridbag, c, Color.blue);
       makeLabel("Select|Figure", gridbag, c, Color.green);
          c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
       makeButton("'c'", gridbag, c);
    	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'C'", gridbag, c);

       makeLabel("Face:", gridbag, c, Color.blue);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("'L'", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'l'", gridbag, c);
     }
 }   
 