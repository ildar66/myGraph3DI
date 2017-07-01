 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

      /////////////////////////////////////////////////////////////////////////////////Северная панель
   class NorthPanel extends Panel implements ActionListener, ItemListener{
	 Piace piace; 
	 Button regime, newFigure, newSide, newElement, Esc, FP, anim;
	 Checkbox side, figure, all;

	 public NorthPanel(Piace piace)    {
	   this.piace = piace;
	   add(Esc = new Button("Esc")); Esc.addActionListener(this);           
	   add(newFigure = new Button("new Figure('f')")); newFigure.addActionListener(this);newFigure.setEnabled(false);           
	   add(newSide = new Button("new Side('s')")); newSide.addActionListener(this);newSide.setEnabled(false);           
	   add(newElement = new Button("new Element('e')")); newElement.addActionListener(this);newElement.setEnabled(false);           
	   add(FP = new Button("FP")); FP.addActionListener(this);           
	   add(regime = new Button("Paint")); regime.addActionListener(this); regime.setBackground(Color.green);
	   add(anim = new Button("Stop")); anim.addActionListener(this); anim.setBackground(Color.blue);          	   
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
		  newFigure.setEnabled(false);newSide.setEnabled(false);newElement.setEnabled(false);
		  side.setEnabled(true);figure.setEnabled(true);all.setEnabled(true);}
	   else if (obj.equals("Paint"))    //переключение режима.
		 {piace.regimeMove = false; regime.setLabel("Move"); regime.setBackground(Color.green);
		  newFigure.setEnabled(true);newSide.setEnabled(true);newElement.setEnabled(true);
		  side.setEnabled(false);figure.setEnabled(false);all.setEnabled(false);}
	   else if (obj.equals("Start"))     //включение анимации.
	    {
		  if(piace.graph.anim == null) {
		    piace.graph.start();
		    anim.setLabel("Stop");
		    anim.setBackground(Color.red);
		  }
	    }
	   else if (obj.equals("Stop"))    //выкл. анимации.
		  if(piace.graph.anim != null) {
		    piace.graph.stop();
		    anim.setLabel("Start");
		    anim.setBackground(Color.blue);
		  }
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