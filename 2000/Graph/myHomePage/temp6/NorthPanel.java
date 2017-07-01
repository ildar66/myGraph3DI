 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

   import javax.swing.*;   /////////////////////////////////////////////////////////////////////////////////Северная панель
   class NorthPanel extends JPanel implements ActionListener, ItemListener{
	 Piace piace; 
	 JButton regime, newFigure, newSide, newElement, Esc, FP, anim;
	 JRadioButton side, figure, all;

	 public NorthPanel(Piace piace)    {
	   this.piace = piace;
	   Esc = makeButton("Esc", true); 
	   newFigure = makeButton("new Figure('f')", false);           
	   newSide = makeButton("new Side('s')", false);           
	   newElement = makeButton("new Element('e')", false);          
	   FP = makeButton("FP", true);          
	   regime = makeButton("Paint", true); regime.setBackground(Color.green);
	   anim = makeButton("Stop", true);  anim.setBackground(Color.blue); 
	    
	   ButtonGroup group = new ButtonGroup();
	   side = makeRadioButton("Side", group, false);
	   figure = makeRadioButton("Figure", group, true);
	   all = makeRadioButton("All", group, false);
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
		 {piace.regimeMove = true; regime.setText("Paint"); regime.setBackground(Color.yellow);
		  newFigure.setEnabled(false);newSide.setEnabled(false);newElement.setEnabled(false);
		  side.setEnabled(true);figure.setEnabled(true);all.setEnabled(true);}
	   else if (obj.equals("Paint"))    //переключение режима.
		 {piace.regimeMove = false; regime.setText("Move"); regime.setBackground(Color.green);
		  newFigure.setEnabled(true);newSide.setEnabled(true);newElement.setEnabled(true);
		  side.setEnabled(false);figure.setEnabled(false);all.setEnabled(false);}
	   else if (obj.equals("Start"))     //включение анимации.
	    {
		  if(piace.graph.anim == null) {
		    piace.graph.start();
		    anim.setText("Stop");
		    anim.setBackground(Color.red);
		  }
	    }
	   else if (obj.equals("Stop"))    //выкл. анимации.
		  if(piace.graph.anim != null) {
		    piace.graph.stop();
		    anim.setText("Start");
		    anim.setBackground(Color.blue);
		  }
	   else return;  

	   if(piace.select.field.size() != 0) 
		 Shade.mouseUp(piace); 
	   piace.repaint();     
	 } 

	 public void itemStateChanged(ItemEvent evt)   {  
	   if (evt.getSource() instanceof JRadioButton)
		 { 
		   JRadioButton button = (JRadioButton)evt.getSource();
		   if ((button == side)&&(piace.select.field.size() != 0))
			  {Move.choice = 1;}
		   else if ((button == figure)&&(piace.figure.field.size() != 0))
			  {Move.choice = 2;}
		   else if ((button == all)&&(piace.figure.field.size() != 0))
			  {Move.choice = 3;}   
		 }
	 } 
   /**
 * Insert the method's description here.
 * Creation date: (19.10.2001 12:43:33)
 */
	 protected JButton makeButton(String name, boolean isEnabled)
	 {
	   JButton button = new JButton(name);
	   button.setMargin(new java.awt.Insets(2, 5, 2, 5));
	   button.addActionListener(this);
	   button.setEnabled(isEnabled);
	   add(button);
	   return button;
	 }/**
 * Insert the method's description here.
 * Creation date: (19.10.2001 14:45:05)
 */
public JRadioButton makeRadioButton(String name, ButtonGroup bg, boolean isSelected) {
  JRadioButton  button = new JRadioButton(name, isSelected);
  bg.add(button);
  button.setMargin(new java.awt.Insets(2, 5, 2, 5));
  button.addItemListener(this);
  add(button);
  return button;
}}