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
      north_panel.add(b = new Button("new Element('e')")); b.addActionListener(this);
      north_panel.add(b = new Button("new Side('s')")); b.addActionListener(this);
      north_panel.add(b = new Button("new Figure('f')")); b.addActionListener(this);
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

      add("East", new EastPanel(piace));

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
      g.setColor(Color.black);
      g.fillOval(zero.x - 4, zero.y - 4, 8, 8);
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
         else if ((obj.equals("Esc"))&&(piace.points3D.size() != 0)) 
           {piace.escape(); setControl();}  
         else if ((obj.equals("new Element('e')"))&&(piace.select.field.size() != 0))
            {piace.newElement();}
         else if ((obj.equals("new Side('s')"))&&(piace.select.field.size() != 0))
            {piace.newSide(); }
         else if ((obj.equals("new Figure('f')"))&&(piace.select.field.size() != 0))                                                  	
            {piace.newFigure(); }     
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
      piace.repaint(); // piace.requestFocus();
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
      piace.repaint(); // piace.requestFocus();
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
    }
 }
//////////////////////////////////////////////////////////////////////////////////////////////////// 
  class ClosePanel extends WindowAdapter {
   floatPanel FP;
   public ClosePanel(floatPanel FP)
     {this.FP = FP;}
   public void windowClosing(WindowEvent e)
     { FP.dispose(); } 
 }
////////////////////////////////////////////////////////////////////////////////////////////////////
   class MovePanel extends Panel implements ActionListener{
     Piace piace;
     protected void makeButton(String name, GridBagLayout gridbag, GridBagConstraints c)
     {
       Button button = new Button(name);
       button.addActionListener(this);
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
     public MovePanel(Piace piace) 
     {
       this.piace = piace;
       GridBagLayout gridbag = new GridBagLayout();
       GridBagConstraints c = new GridBagConstraints();//setFont(new Font("Helvetica", Font.BOLD, 11)); 
       setLayout(gridbag);
       c.fill = GridBagConstraints.HORIZONTAL; 
       c.insets = new Insets(0, 0, 0, 0);

          c.gridwidth = GridBagConstraints.REMAINDER; 
       makeLabel("Rotate:", gridbag, c, Color.blue);
          c.gridwidth = 1;
       makeButton("'X'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("'Y'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'Z'", gridbag, c);
          c.gridwidth = 1;
       makeButton("'x'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("'y'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'z'", gridbag, c);
          c.gridwidth = 1;
       makeButton("'1'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("'2'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER;
       makeButton("'3'", gridbag, c);

       makeLabel("Direct:", gridbag, c, Color.blue);
          c.gridwidth = 1;
       makeButton("'+'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("'-'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'*'", gridbag, c);

       makeLabel("Transfer:", gridbag, c, Color.blue);
       makeLabel("  Piace", gridbag, c, Color.green);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("Up", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER;
       makeButton("->", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("Down", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER;
       makeButton("<-", gridbag, c);   
       makeLabel("Figure Shift", gridbag, c, Color.green);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton(" Up", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton(" ->", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton(" Down", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER;
       makeButton(" <-", gridbag, c);
       makeButton(" End", gridbag, c);
       makeButton(" Home", gridbag, c);
       makeLabel("Select Ctrl", gridbag, c, Color.green);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("Up ", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER;
       makeButton("-> ", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("Down ", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER;
       makeButton("<- ", gridbag, c);
       makeButton("End ", gridbag, c);
       makeButton("Home ", gridbag, c);

       makeLabel("Next:", gridbag, c, Color.blue);
       makeButton("Element", gridbag, c);
       makeButton("Side(Tab)", gridbag, c);
       makeButton("Figure", gridbag, c);
       makeButton("Offset('n')", gridbag, c);         
     }  

     public void actionPerformed(ActionEvent ev) {
       String obj = ev.getActionCommand();
       if (obj.equals("'x'"))
          piace.rotate("X", piace.figure);
       if (obj.equals("'y'")) 
          piace.rotate("Y", piace.figure);
       if (obj.equals("'z'"))
          piace.rotate("Z", piace.figure);  

       if (obj.equals("'X'"))
          { piace.matrix.identity(); piace.matrix.rotate(piace.rotate,"X", false);for (int i=0; i<piace.points3D.size(); i++) piace.matrix.transform((Point3D)piace.points3D.elementAt(i));}
       if (obj.equals("'Y'"))  
          { piace.matrix.identity(); piace.matrix.rotate(piace.rotate,"Y", false);for (int i=0; i<piace.points3D.size(); i++) piace.matrix.transform((Point3D)piace.points3D.elementAt(i));}
       if (obj.equals("'Z'")) 
          { piace.matrix.identity(); piace.matrix.rotate(piace.rotate,"Z", false);for (int i=0; i<piace.points3D.size(); i++) piace.matrix.transform((Point3D)piace.points3D.elementAt(i));}

       if (obj.equals("'1'")) 
          piace.rotate("X", piace.select);
       if (obj.equals("'2'")) 
          piace.rotate("Y", piace.select);
       if (obj.equals("'3'")) 
          piace.rotate("Z", piace.select); 

       if (obj.equals("'+'"))
          {piace.tab_add = 1; piace.rotate = Math.abs(piace.rotate); piace.step = Math.abs(piace.step);}         
       if (obj.equals("'-'"))
          {piace.tab_add = -1; piace.rotate = - Math.abs(piace.rotate); piace.step = -Math.abs(piace.step);}
       if (obj.equals("'*'"))
          {piace.tab_add = - piace.tab_add; piace.rotate = - piace.rotate; piace.step = - piace.step;}        

       if ((obj.equals("Side(Tab)")) && (piace.select.field.size() != 0))  
          {piace.nextSide();}
       if ((obj.equals("Element")) && (piace.select.field.size() != 0))  
          {piace.nextElement();}
       if ((obj.equals("Figure")) && (piace.select.field.size() != 0))  
          {piace.nextFigure();}
       if ((obj.equals("Offset('n')"))&&(piace.select.field.size() != 0))
          {piace.next_offset();}      

       if (obj.equals("Up"))       //new
           {piace.offset = new Point(piace.offset.x, piace.offset.y - piace.step);}
       if (obj.equals("Down")) 
           {piace.offset = new Point(piace.offset.x, piace.offset.y + piace.step);}
       if (obj.equals("<-")) 
           {piace.offset = new Point(piace.offset.x - piace.step, piace.offset.y);}
       if (obj.equals("->")) 
           {piace.offset = new Point(piace.offset.x + piace.step, piace.offset.y);}
       
       if (obj.equals("Up "))
           piace.translate(piace.select, 0, -Math.abs(piace.step), 0);
       if (obj.equals("Down "))
           piace.translate(piace.select, 0, Math.abs(piace.step), 0);
       if (obj.equals("-> "))
           piace.translate(piace.select, Math.abs(piace.step), 0, 0);
       if (obj.equals("<- "))
           piace.translate(piace.select, -Math.abs(piace.step), 0, 0); 
       if (obj.equals("End "))
           piace.translate(piace.select, 0, 0, Math.abs(piace.step));           
       if (obj.equals("Home "))
           piace.translate(piace.select, 0, 0, -Math.abs(piace.step));          

       if (obj.equals(" Up"))
           piace.translate(piace.figure, 0, -Math.abs(piace.step), 0);
       if (obj.equals(" Down"))
           piace.translate(piace.figure, 0, Math.abs(piace.step), 0); 
       if (obj.equals(" ->"))
           piace.translate(piace.figure, Math.abs(piace.step), 0, 0); 
       if (obj.equals(" <-"))
           piace.translate(piace.figure, -Math.abs(piace.step), 0, 0); 
       if (obj.equals(" End"))
           piace.translate(piace.figure, 0, 0, Math.abs(piace.step));           
       if (obj.equals(" Home"))
           piace.translate(piace.figure, 0, 0, -Math.abs(piace.step));           //and new
 
       if(piace.select.field.size() != 0) 
         Shade.mouseUp(piace); 
       piace.repaint();        // piace.requestFocus();
     }
 }
////////////////////////////////////////////////////////////////////////////////////////////////////
 class EastPanel extends Panel implements ActionListener{
     Piace piace;
     protected void makeButton(String name, GridBagLayout gridbag, GridBagConstraints c)
     {
       Button button = new Button(name);
       button.addActionListener(this);
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
     public EastPanel(Piace piace) 
     {
       this.piace = piace;
       GridBagLayout gridbag = new GridBagLayout();
       GridBagConstraints c = new GridBagConstraints(); // setFont(new Font("Helvetica", Font.PLAIN, 14));
       setLayout(gridbag);
       c.fill = GridBagConstraints.HORIZONTAL;        //c.weightx = 3.0;
       c.insets = new Insets(0, 0, 0, 0);
          c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeLabel("Scale", gridbag, c, Color.blue);
       makeLabel("Piace", gridbag, c, Color.green);
       makeButton("PageUp", gridbag, c);
       makeButton("PageDown", gridbag, c);
       makeLabel("Select", gridbag, c, Color.green);
          c.gridwidth = 1;
       makeButton("'4'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE;  //next-to-last in row
       makeButton("'5'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'6'", gridbag, c);
       makeLabel("Figure", gridbag, c, Color.green);
          c.gridwidth = 1;
       makeButton("'7'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE;  
       makeButton("'8'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'9'", gridbag, c);

       makeLabel("Delete:", gridbag, c, Color.blue);
       makeLabel("Last Point", gridbag, c, Color.green);
       makeButton("Del", gridbag, c);
       makeLabel("Select", gridbag, c, Color.green);
       makeButton("Ctrl+Del", gridbag, c);
       makeLabel("Figure", gridbag, c, Color.green);
       makeButton("Shift+Del", gridbag, c);

       makeLabel("Copy:", gridbag, c, Color.blue);
          c.gridwidth = GridBagConstraints.RELATIVE;  
       makeLabel("Select", gridbag, c, Color.green);
    	  c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'c'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE;  
       makeLabel("Figure", gridbag, c, Color.green);
    	  c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'C'", gridbag, c);

       makeLabel("Face:", gridbag, c, Color.blue);
          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeButton("'L'", gridbag, c);
          c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'l'", gridbag, c);

       makeLabel("BackFace:", gridbag, c, Color.blue);
       makeButton("ON", gridbag, c);
       makeButton("OFF", gridbag, c);     
       makeButton("Sign('0')", gridbag, c);

          c.gridwidth = GridBagConstraints.RELATIVE; 
       makeLabel("Form:", gridbag, c, Color.blue);
  	  c.gridwidth = GridBagConstraints.REMAINDER; 
       makeButton("'A'", gridbag, c);     
     } 
     public void actionPerformed(ActionEvent ev) {
       String obj = ev.getActionCommand();

       if ((obj.equals("'c'")) && (piace.select.field.size() > 1)) 
          {piace.copy(piace.select);}
       if ((obj.equals("'C'")) && (piace.select.field.size() > 1)) 
          {piace.copy(piace.figure);}

       if ((obj.equals("'A'"))&&(piace.turnSum < 360)&&(piace.select.field.size() != 0))
          {piace.reconstruct_A(piace.select);}

       if ((obj.equals("'L'"))&&(piace.select instanceof Side)&&(piace.select.field.size() != 0))
          {piace.face((Side)piace.select, false);}
       if ((obj.equals("'l'"))&&(piace.select instanceof Side)&&(piace.select.field.size() != 0))
          {piace.face((Side)piace.select, true);}

       if (obj.equals("PageUp"))
         {piace.scale(piace.figure, 1.1f, 1.1f, 1.1f);}
       if (obj.equals("PageDown"))
         {piace.scale(piace.figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);}
       if ((obj.equals("'4'")) && (piace.step > 0))
          {piace.scale(piace.select, 1.1f, 1f, 1f);} 
       if ((obj.equals("'4'")) && (piace.step < 0))
          {piace.scale(piace.select, 1f/1.1f, 1f, 1f);} 
       if ((obj.equals("'5'")) && (piace.step > 0))
          {piace.scale(piace.select, 1f, 1.1f, 1f);}
       if ((obj.equals("'5'")) && (piace.step < 0))
          {piace.scale(piace.select, 1f, 1f/1.1f, 1f);}
       if ((obj.equals("'6'")) && (piace.step > 0))
          {piace.scale(piace.select, 1f, 1f, 1.1f);}
       if ((obj.equals("'6'")) && (piace.step < 0))
          {piace.scale(piace.select, 1f, 1f, 1f/1.1f);}

       if ((obj.equals("'7'")) && (piace.step > 0))
          {piace.scale(piace.figure, 1.1f, 1f, 1f);} 
       if ((obj.equals("'7'")) && (piace.step < 0))
          {piace.scale(piace.figure, 1f/1.1f, 1f, 1f);} 
       if ((obj.equals("'8'")) && (piace.step > 0))
          {piace.scale(piace.figure, 1f, 1.1f, 1f);}
       if ((obj.equals("'8'")) && (piace.step < 0))
          {piace.scale(piace.figure, 1f, 1f/1.1f, 1f);}
       if ((obj.equals("'9'")) && (piace.step > 0))
          {piace.scale(piace.figure, 1f, 1f, 1.1f);}
       if ((obj.equals("'9'")) && (piace.step < 0))
          {piace.scale(piace.figure, 1f, 1f, 1f/1.1f);}

       if (obj.equals("ON"))
          {piace.flag_backface = piace.figure.flag_backface = true;}
       if (obj.equals("OFF")) 
          {piace.flag_backface = piace.figure.flag_backface = false;}
       if ((obj.equals("Sign('0')"))&&(piace.select instanceof Side))
          {piace.copy = (Side)piace.select; piace.copy.normal_Left = !(piace.copy.normal_Left);} 

       if ((obj.equals("Del")) && (piace.points3D.size() != 0))         
         {piace.deletelast3D(piace.select); piace.escape(); piace.FP.setControl();}                         
       if ((obj.equals("Ctrl+Del")) && (piace.select.field.size() != 0))         
         {piace.delete(piace.select); piace.escape(); piace.FP.setControl();}                         
       if ((obj.equals("Shift+Del")) && (piace.select.field.size() != 0) && (piace.figures.size() > 1))         
         {piace.delete(piace.figure); piace.escape(); piace.FP.setControl();}  
                      
       if(piace.select.field.size() != 0) 
         Shade.mouseUp(piace); 
       piace.repaint();  //     piace.requestFocus();
     }
 }   
/////////////////////////////////////////////////////////////////////////////
   class NorthPanel extends Panel implements ActionListener{
     Piace piace;
     public NorthPanel(Piace piace)    {
       this.piace = piace;
       makeButton("Esc");
       makeButton("new Side('s')");
       makeButton("new Element('e')");
       makeButton("new Figure('f')");
     }
     protected void makeButton(String name)  {
       Button button = new Button(name);
       button.addActionListener(this);
       add(button);
     }
     public void actionPerformed(ActionEvent ev)  {
       String obj = ev.getActionCommand();
       if ((obj.equals("Esc"))&&(piace.points3D.size() != 0)) 
         {piace.escape(); } //setControl();??? 
       else if ((obj.equals("new Element('e')"))&&(piace.select.field.size() != 0))
          {piace.newElement();}
       else if ((obj.equals("new Side('s')"))&&(piace.select.field.size() != 0))
          {piace.newSide(); }
       else if ((obj.equals("new Figure('f')"))&&(piace.select.field.size() != 0))                                                  	
          {piace.newFigure(); }     
       piace.repaint();  //     piace.requestFocus();
     }
   }     
/////////////////////////////////////////////////////////////////////////////
   class SouthPanel extends Panel implements ActionListener, ItemListener{
     Piace piace;
     Checkbox checkbox[];
     Choice kind = new Choice();
     Button net = new Button("Net ON");  
     Button full = new Button(" Full ");

     public SouthPanel(Piace piace)    {
      setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
      this.piace = piace;
      kind.add("Polygon"); kind.add("free Polygon"); kind.add("Line"); kind.add("free Line");
      add(kind); kind.addItemListener(this);  
      CheckboxGroup group = new CheckboxGroup();
      Color CL[] = {Color.white, Color.orange, Color.pink, Color.green,
         Color.red, Color.cyan, Color.yellow, Color.blue,Color.magenta,
              Color.lightGray, Color.gray,Color.darkGray, Color.black}; 
      checkbox = new Checkbox[CL.length];     
      for (int i=0; i<checkbox.length; i++)
       {         
         add(checkbox[i] = new Checkbox(null, group, true));
         checkbox[i].setBackground(CL[i]);
         checkbox[i].addItemListener(this);       
       } 
      add(full); full.addActionListener(this);
      add(net); net.addActionListener(this);
     }  

     public void actionPerformed(ActionEvent ev)  {
       String obj = ev.getActionCommand();
       if (obj.equals(" Full "))
         {full.setLabel("Empty"); piace.full = piace.select.full = true;}    
       else if (obj.equals("Empty"))
         {full.setLabel(" Full "); piace.full = piace.select.full = false;}
       else if (obj.equals("Net ON"))
         {net.setLabel("Net OFF"); piace.net = piace.figure.net = true;}
       else if (obj.equals("Net OFF")) 
         {net.setLabel("Net ON"); piace.net = piace.figure.net = false;}
       piace.repaint();  
     }

     public void itemStateChanged(ItemEvent evt)   {  
       if (evt.getSource() instanceof Checkbox)
         {          
          piace.color = ((Component)evt.getSource()).getBackground();
          if(piace.select != null)
           piace.select.color = ((Component)evt.getSource()).getBackground();
         }
       if (evt.getSource() == kind)
         { piace.kind = piace.select.kind = kind.getSelectedIndex();}
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
      }
   }     
 /////////////////////////////////////////////////////////////////////////////////
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
}  */
