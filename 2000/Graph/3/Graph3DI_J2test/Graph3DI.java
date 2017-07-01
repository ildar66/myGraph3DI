 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

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
      add("East", new EastPanel(piace));
      add("North", new NorthPanel(piace));
    }

   public void destroy()
    {
     removeAll();
    }
 
   public String getAppletInfo() 
    {
     return "Graph3DI.2000(avtor Ildar Shafigullin)";
    }
 }
///////////////////////////////////////////////////////////////////////////////Южная панель
   class SouthPanel extends Panel implements ActionListener, ItemListener{   
     Piace piace;
     Checkbox checkbox[];
     Choice kind = new Choice();
     Button net = new Button("Net OFF");  
     Button full = new Button(" Full ");
     Button backface = new Button("Backface ON");

     public SouthPanel(Piace piace)    {
      setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
      this.piace = piace;
      kind.add("Polygon"); kind.add("free Polygon"); kind.add("Line"); kind.add("free Line");
      add(kind); kind.addItemListener(this);  
      add(full); full.addActionListener(this);
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
      add(net); net.addActionListener(this);
      add(backface); backface.addActionListener(this);
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
       else if (obj.equals("Backface ON"))
         {backface.setLabel("Backface OFF"); piace.flag_backface = piace.figure.flag_backface = true;}
       else if (obj.equals("Backface OFF")) 
         {backface.setLabel("Backface ON"); piace.flag_backface = piace.figure.flag_backface = false;}

       else return;

       piace.repaint();  
     }

     public void itemStateChanged(ItemEvent evt)   {  
       if (evt.getSource() instanceof Checkbox)
         {          
          piace.color = ((Component)evt.getSource()).getBackground();
          if(piace.select != null)
           piace.select.color = ((Component)evt.getSource()).getBackground();
         }
       else if (evt.getSource() == kind)
         { piace.kind = piace.select.kind = kind.getSelectedIndex();}

       else return;  

       piace.repaint(); 
     } 

     void setControl()
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
        if (piace.figure.flag_backface)
          backface.setLabel("Backface OFF");  
        else
          backface.setLabel("Backface ON");   
      }
   }     
 /////////////////////////////////////////////////////////////////////////////////Северная панель
   class NorthPanel extends Panel implements ActionListener, ItemListener{
     Piace piace; 
     Button regime, newFigure, newSide, newElement, Esc, FP;
     Checkbox side, figure, all;

     public NorthPanel(Piace piace)    {
       this.piace = piace;
       add(Esc = new Button("Esc")); Esc.addActionListener(this);           
       add(newFigure = new Button("new Figure('f')")); newFigure.addActionListener(this);           
       add(newSide = new Button("new Side('s')")); newSide.addActionListener(this);           
       add(newElement = new Button("new Element('e')")); newElement.addActionListener(this);           
       add(FP = new Button("FP")); FP.addActionListener(this);           
       add(regime = new Button("Move")); regime.addActionListener(this); regime.setBackground(Color.green);          
       CheckboxGroup group = new CheckboxGroup();
       add(side = new Checkbox("Side", group, false)); side.addItemListener(this);          
       add(figure = new Checkbox("Figure", group, true)); figure.addItemListener(this);           
       add(all = new Checkbox("All", group, false)); all.addItemListener(this);           
     }

     public void actionPerformed(ActionEvent ev)  {
       String obj = ev.getActionCommand();
       if ((obj.equals("Esc"))&&(piace.points3D.size() != 0)) 
         {piace.delete(piace.select); piace.graph.SP.setControl();}  
       else if ((obj.equals("new Element('e')"))&&(piace.select.field.size() != 0))
          {piace.newElement();}
       else if ((obj.equals("new Side('s')"))&&(piace.select.field.size() != 0))
          {piace.newSide(); }
       else if ((obj.equals("new Figure('f')"))&&(piace.select.field.size() != 0))                                                  	
          {piace.newFigure(); }     
       else if ((obj.equals("FP"))&&(!piace.FP.isVisible()))                                                  	
          {piace.FP.setVisible(true); }     
       else if (obj.equals("Move"))     //переключение режима.
         {piace.regimeMove = true; regime.setLabel("Paint"); regime.setBackground(Color.yellow);
          newFigure.setEnabled(false);newSide.setEnabled(false);newElement.setEnabled(false);}
       else if (obj.equals("Paint"))    //переключение режима.
         {piace.regimeMove = false; regime.setLabel("Move"); regime.setBackground(Color.green);
          newFigure.setEnabled(true);newSide.setEnabled(true);newElement.setEnabled(true);}

       else return;  

       if(piace.select.field.size() != 0) 
         Shade.mouseUp(piace); 
       piace.repaint();     
     } 

     public void itemStateChanged(ItemEvent evt)   {  
       if (evt.getSource() instanceof Checkbox)
         { 
           Checkbox checkbox = (Checkbox)evt.getSource();
           if ((checkbox == side)&&(piace.select.field.size() != 0))
              {Move.choice = 1;}
           else if ((checkbox == figure)&&(piace.figure.field.size() != 0))
              {Move.choice = 2;}
           else if ((checkbox == all)&&(piace.figure.field.size() != 0))
              {Move.choice = 3;}   
         }
     } 
   }     
 ////////////////////////////////////////////////////////////////////////////////////Панель Динамики.
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
       makeButton("Side(Tab)", gridbag, c);
       makeButton("Element", gridbag, c);
     }  

     public void actionPerformed(ActionEvent ev) {
       String obj = ev.getActionCommand();

       if (obj.equals("'x'"))
          piace.rotate("X", piace.figure, piace.rotate);
       else if (obj.equals("'y'")) 
          piace.rotate("Y", piace.figure, piace.rotate);
       else if (obj.equals("'z'"))
          piace.rotate("Z", piace.figure, piace.rotate);  

       else if (obj.equals("'X'"))
          { piace.matrix.identity(); piace.matrix.rotate(piace.rotate,"X", false);for (int i=0; i<piace.points3D.size(); i++) piace.matrix.transform((Point3D)piace.points3D.elementAt(i));}
       else if (obj.equals("'Y'"))  
          { piace.matrix.identity(); piace.matrix.rotate(piace.rotate,"Y", false);for (int i=0; i<piace.points3D.size(); i++) piace.matrix.transform((Point3D)piace.points3D.elementAt(i));}
       else if (obj.equals("'Z'")) 
          { piace.matrix.identity(); piace.matrix.rotate(piace.rotate,"Z", false);for (int i=0; i<piace.points3D.size(); i++) piace.matrix.transform((Point3D)piace.points3D.elementAt(i));}

       else if (obj.equals("'1'")) 
          piace.rotate("X", piace.select, piace.rotate);
       else if (obj.equals("'2'")) 
          piace.rotate("Y", piace.select, piace.rotate);
       else if (obj.equals("'3'")) 
          piace.rotate("Z", piace.select, piace.rotate); 

       else if (obj.equals("'+'"))
          {piace.tab_add = 1; piace.rotate = Math.abs(piace.rotate); piace.step = Math.abs(piace.step);}         
       else if (obj.equals("'-'"))
          {piace.tab_add = -1; piace.rotate = - Math.abs(piace.rotate); piace.step = -Math.abs(piace.step);}
       else if (obj.equals("'*'"))
          {piace.tab_add = - piace.tab_add; piace.rotate = - piace.rotate; piace.step = - piace.step;}        

       else if (obj.equals("Up"))       //new
           {piace.offset = new Point(piace.offset.x, piace.offset.y - Math.abs(piace.step));}
       else if (obj.equals("Down")) 
           {piace.offset = new Point(piace.offset.x, piace.offset.y + Math.abs(piace.step));}
       else if (obj.equals("<-")) 
           {piace.offset = new Point(piace.offset.x - Math.abs(piace.step), piace.offset.y);}
       else if (obj.equals("->")) 
           {piace.offset = new Point(piace.offset.x + Math.abs(piace.step), piace.offset.y);}
       
       else if (obj.equals("Up "))
           piace.translate(piace.select, 0, -Math.abs(piace.step), 0);
       else if (obj.equals("Down "))
           piace.translate(piace.select, 0, Math.abs(piace.step), 0);
       else if (obj.equals("-> "))
           piace.translate(piace.select, Math.abs(piace.step), 0, 0);
       else if (obj.equals("<- "))
           piace.translate(piace.select, -Math.abs(piace.step), 0, 0); 
       else if (obj.equals("End "))
           piace.translate(piace.select, 0, 0, Math.abs(piace.step));           
       else if (obj.equals("Home "))
           piace.translate(piace.select, 0, 0, -Math.abs(piace.step));          

       else if (obj.equals(" Up"))
           piace.translate(piace.figure, 0, -Math.abs(piace.step), 0);
       else if (obj.equals(" Down"))
           piace.translate(piace.figure, 0, Math.abs(piace.step), 0); 
       else if (obj.equals(" ->"))
           piace.translate(piace.figure, Math.abs(piace.step), 0, 0); 
       else if (obj.equals(" <-"))
           piace.translate(piace.figure, -Math.abs(piace.step), 0, 0); 
       else if (obj.equals(" End"))
           piace.translate(piace.figure, 0, 0, Math.abs(piace.step));           
       else if (obj.equals(" Home"))
           piace.translate(piace.figure, 0, 0, -Math.abs(piace.step));         

       else if ((obj.equals("Side(Tab)")) && (piace.select.field.size() != 0))  
          {piace.nextSide();}
       else if ((obj.equals("Element")) && (piace.select.field.size() != 0))  
          {piace.nextElement();}

       else return;  

       if(piace.select.field.size() != 0) 
         Shade.mouseUp(piace); 
       piace.repaint();        // piace.requestFocus();
     }
 }
///////////////////////////////////////////////////////////////////////////////Западная панель
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
       makeLabel("Face:", gridbag, c, Color.blue);
          c.gridwidth = 1;
       makeButton("'L'", gridbag, c);
          c.gridwidth = GridBagConstraints.RELATIVE;  //next-to-last in row
       makeButton("'l'", gridbag, c);
  	  c.gridwidth = GridBagConstraints.REMAINDER; //end row
       makeButton("'0'", gridbag, c);

       makeLabel("Scale", gridbag, c, Color.blue);
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
       makeLabel("Figure All", gridbag, c, Color.green);
       makeButton("PageUp", gridbag, c);
       makeButton("PageDown", gridbag, c);
 
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

       makeLabel("Next:", gridbag, c, Color.blue);
       makeButton("Figure", gridbag, c);
       makeButton("Offset('n')", gridbag, c);         
     } 
     public void actionPerformed(ActionEvent ev) {
       String obj = ev.getActionCommand();

       if ((obj.equals("'L'"))&&(piace.select instanceof Side)&&(piace.select.field.size() >= 3))
          {piace.face((Side)piace.select, false);}
       else if ((obj.equals("'l'"))&&(piace.select instanceof Side)&&(piace.select.field.size() >= 3))
          {piace.face((Side)piace.select, true);}    
       else if ((obj.equals("'0'"))&&(piace.select instanceof Side))
          {piace.copy = (Side)piace.select; piace.copy.normal_Left = !(piace.copy.normal_Left);}

       else if ((obj.equals("'c'")) && (piace.select.field.size() > 1)) 
           {piace.copy(piace.select);}
       else if ((obj.equals("'C'")) && (piace.select.field.size() > 1)) 
          {piace.copy(piace.figure);}

       else if (obj.equals("PageUp"))
         {piace.scale(piace.figure, 1.1f, 1.1f, 1.1f);}
       else if (obj.equals("PageDown"))
         {piace.scale(piace.figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);}
       else if ((obj.equals("'4'")) && (piace.step > 0))
          {piace.scale(piace.select, 1.1f, 1f, 1f);} 
       else if ((obj.equals("'4'")) && (piace.step < 0))
          {piace.scale(piace.select, 1f/1.1f, 1f, 1f);} 
       else if ((obj.equals("'5'")) && (piace.step > 0))
          {piace.scale(piace.select, 1f, 1.1f, 1f);}
       else if ((obj.equals("'5'")) && (piace.step < 0))
          {piace.scale(piace.select, 1f, 1f/1.1f, 1f);}
       else if ((obj.equals("'6'")) && (piace.step > 0))
          {piace.scale(piace.select, 1f, 1f, 1.1f);}
       else if ((obj.equals("'6'")) && (piace.step < 0))
          {piace.scale(piace.select, 1f, 1f, 1f/1.1f);}

       else if ((obj.equals("'7'")) && (piace.step > 0))
          {piace.scale(piace.figure, 1.1f, 1f, 1f);} 
       else if ((obj.equals("'7'")) && (piace.step < 0))
          {piace.scale(piace.figure, 1f/1.1f, 1f, 1f);} 
       else if ((obj.equals("'8'")) && (piace.step > 0))
          {piace.scale(piace.figure, 1f, 1.1f, 1f);}
       else if ((obj.equals("'8'")) && (piace.step < 0))
          {piace.scale(piace.figure, 1f, 1f/1.1f, 1f);}
       else if ((obj.equals("'9'")) && (piace.step > 0))
          {piace.scale(piace.figure, 1f, 1f, 1.1f);}
       else if ((obj.equals("'9'")) && (piace.step < 0))
          {piace.scale(piace.figure, 1f, 1f, 1f/1.1f);}

       else if ((obj.equals("Del")) && (piace.points3D.size() != 0))         
         {piace.deletelast3D(piace.select); piace.graph.SP.setControl();}                         
       else if ((obj.equals("Ctrl+Del")) && (piace.points3D.size() != 0))         
         {piace.delete(piace.select); piace.graph.SP.setControl();}                         
       else if ((obj.equals("Shift+Del")) && (piace.points3D.size() != 0))         
         {piace.delete(piace.figure); piace.graph.SP.setControl();}  
                      
       else if ((obj.equals("Figure")) && (piace.select.field.size() != 0))  
          {piace.nextFigure();}
       else if ((obj.equals("Offset('n')"))&&(piace.select.field.size() != 0))
          {piace.next_offset();}      

       else return;  

       if(piace.select.field.size() != 0) 
         Shade.mouseUp(piace); 
       piace.repaint();  //     piace.requestFocus();
     }
 }
/////////////////////////////////////////////////////////////////////////////////////////////////////   
   