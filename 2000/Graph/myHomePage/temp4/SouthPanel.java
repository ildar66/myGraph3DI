 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

  import javax.swing.*;  ////////////////////////////////////////////////////////////ёжна€ панель
   class SouthPanel extends JPanel implements ActionListener, ItemListener{   
	 Piace piace;
	 JRadioButton checkbox[];
	 JComboBox kind = new JComboBox();
	 JButton net, full, backface;  



	 public SouthPanel(Piace piace)    {
	  setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
	  this.piace = piace;
	  
	  kind.addItem("Polygon"); kind.addItem("free Polygon"); kind.addItem("Line"); kind.addItem("free Line");
	  add(kind); kind.addItemListener(this); 
	   
	  full = makeButton(" Full ");
	  
	  ButtonGroup group = new ButtonGroup();
	  Color CL[] = {Color.white, Color.orange, Color.pink, Color.green,
		 Color.red, Color.cyan, Color.yellow, Color.blue,Color.magenta,
			  Color.lightGray, Color.gray,Color.darkGray, Color.black}; 
	  checkbox = new JRadioButton[CL.length];     
	  for (int i=0; i<checkbox.length; i++)
	   {         
		 add(checkbox[i] = new JRadioButton("", true));
		 checkbox[i].setBackground(CL[i]);
		 checkbox[i].addItemListener(this);
		 group.add(checkbox[i]);
	   }
	   
	  net = makeButton("Net OFF");
	  backface = makeButton("Backface ON");
	 }  

	 public void actionPerformed(ActionEvent ev)  {
	   String obj = ev.getActionCommand();

	   if (obj.equals(" Full "))
		 {full.setText("Empty"); piace.full = piace.select.full = true;}    
	   else if (obj.equals("Empty"))
		 {full.setText(" Full "); piace.full = piace.select.full = false;}
	   else if (obj.equals("Net ON"))
		 {net.setText("Net OFF"); piace.net = piace.figure.net = true;}
	   else if (obj.equals("Net OFF")) 
		 {net.setText("Net ON"); piace.net = piace.figure.net = false;}
	   else if (obj.equals("Backface ON"))
		 {backface.setText("Backface OFF"); piace.flag_backface = piace.figure.flag_backface = true;}
	   else if (obj.equals("Backface OFF")) 
		 {backface.setText("Backface ON"); piace.flag_backface = piace.figure.flag_backface = false;}

	   else return;

	   piace.repaint();  
	 }

	 public void itemStateChanged(ItemEvent evt)   {  
	   if (evt.getSource() instanceof JRadioButton)
		 {          
		  piace.color = ((Component)evt.getSource()).getBackground();
		  if(piace.select != null)
		   piace.select.color = ((Component)evt.getSource()).getBackground();
		 }
	   else if (evt.getSource() == kind)
		 { piace.kind = piace.select.kind = kind.getSelectedIndex();
			 System.out.println("kind.getSelectedIndex()" + kind.getSelectedIndex());}

	   else return;  

	   piace.repaint(); 
	 } 

	 void setControl()
	  {
		for(int i=0; i<checkbox.length; i++)
		 {
		  if(checkbox[i].getBackground()== piace.select.color)          
			  checkbox[i].setSelected(true);
		 }
		piace.color = piace.select.color;
		piace.kind = piace.select.kind;
		piace.full = piace.select.full;
		kind.setSelectedIndex(piace.select.kind);
		if (piace.select.full)
		  full.setText("Empty");    
		else
		  full.setText(" Full ");   
		if (piace.figure.net)
		  net.setText("Net OFF");  
		else
		  net.setText("Net ON");   
		if (piace.figure.flag_backface)
		  backface.setText("Backface OFF");  
		else
		  backface.setText("Backface ON");   
	  }
   /**
 * Insert the method's description here.
 * Creation date: (19.10.2001 15:22:57)
 */
	 protected JButton makeButton(String name)
	 {
	   JButton button = new JButton(name);
	   button.setMargin(new java.awt.Insets(2, 5, 2, 5));
	   button.addActionListener(this);
	   add(button);
	   return button;
	 }}