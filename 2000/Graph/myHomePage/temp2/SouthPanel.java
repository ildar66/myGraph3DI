 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

    ///////////////////////////////////////////////////////////////////////////////ёжна€ панель
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