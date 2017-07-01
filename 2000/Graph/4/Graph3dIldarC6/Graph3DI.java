 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

 public class Graph3DI extends Applet  {   
   Piace piace;
   SouthPanel SP;
   public void init()
    {
      piace = new Piace(this);
      SP = new SouthPanel(piace);
      setLayout(new BorderLayout());
      add("Center", piace);
      add("South",SP);
      add("West", new MovePanel(piace));
      add("East", new EastPanel(piace));
      add("North", new NorthPanel(piace));
    }
 }
/////////////////////////////////////////////////////////////////////////////
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
        if (piace.figure.flag_backface)
          backface.setLabel("Backface OFF");  
        else
          backface.setLabel("Backface ON");   
      }
   }     
 /////////////////////////////////////////////////////////////////////////////////
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
         {piace.escape(); piace.graph.SP.setControl();}  
       else if ((obj.equals("new Element('e')"))&&(piace.select.field.size() != 0))
          {piace.newElement();}
       else if ((obj.equals("new Side('s')"))&&(piace.select.field.size() != 0))
          {piace.newSide(); }
       else if ((obj.equals("new Figure('f')"))&&(piace.select.field.size() != 0))                                                  	
          {piace.newFigure(); }     
       piace.repaint();     
     }
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
       makeButton("Side(Tab)", gridbag, c);
       makeButton("Element", gridbag, c);
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

       if (obj.equals("Up"))       //new
           {piace.offset = new Point(piace.offset.x, piace.offset.y - Math.abs(piace.step));}
       if (obj.equals("Down")) 
           {piace.offset = new Point(piace.offset.x, piace.offset.y + Math.abs(piace.step));}
       if (obj.equals("<-")) 
           {piace.offset = new Point(piace.offset.x - Math.abs(piace.step), piace.offset.y);}
       if (obj.equals("->")) 
           {piace.offset = new Point(piace.offset.x + Math.abs(piace.step), piace.offset.y);}
       
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
           piace.translate(piace.figure, 0, 0, -Math.abs(piace.step));         //and new

       if ((obj.equals("Side(Tab)")) && (piace.select.field.size() != 0))  
          {piace.nextSide();}
       if ((obj.equals("Element")) && (piace.select.field.size() != 0))  
          {piace.nextElement();}

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

       if ((obj.equals("'L'"))&&(piace.select instanceof Side)&&(piace.select.field.size() != 0))
          {piace.face((Side)piace.select, false);}
       if ((obj.equals("'l'"))&&(piace.select instanceof Side)&&(piace.select.field.size() != 0))
          {piace.face((Side)piace.select, true);}    
       if ((obj.equals("'0'"))&&(piace.select instanceof Side))
          {piace.copy = (Side)piace.select; piace.copy.normal_Left = !(piace.copy.normal_Left);}

       if ((obj.equals("'c'")) && (piace.select.field.size() > 1)) 
           {piace.copy(piace.select);}
       if ((obj.equals("'C'")) && (piace.select.field.size() > 1)) 
          {piace.copy(piace.figure);}

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

       if ((obj.equals("Del")) && (piace.points3D.size() != 0))         
         {piace.deletelast3D(piace.select); piace.graph.SP.setControl();}                         
       if ((obj.equals("Ctrl+Del")) && (piace.points3D.size() != 0))         
         {piace.delete(piace.select); piace.escape(); piace.graph.SP.setControl();}                         
       if ((obj.equals("Shift+Del")) && (piace.points3D.size() != 0) && (piace.figures.size() > 1))         
         {piace.delete(piace.figure); piace.escape(); piace.graph.SP.setControl();}  
                      
       if ((obj.equals("Figure")) && (piace.select.field.size() != 0))  
          {piace.nextFigure();}
       if ((obj.equals("Offset('n')"))&&(piace.select.field.size() != 0))
          {piace.next_offset();}      

       if(piace.select.field.size() != 0) 
         Shade.mouseUp(piace); 
       piace.repaint();  //     piace.requestFocus();
     }
 }
/////////////////////////////////////////////////////////////////////////////////////////////////////   
   