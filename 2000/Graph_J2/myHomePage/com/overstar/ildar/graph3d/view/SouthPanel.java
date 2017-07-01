package com.overstar.ildar.graph3d.view;

 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;
 import com.overstar.ildar.graph3d.model.*;
  ///////////////////////////////////////////////////////////////////////////////ёжна€ панель
   class SouthPanel extends Panel implements ActionListener, ItemListener{   
	 Piace piace;
	 Checkbox checkbox[];
	 public Choice kind = new Choice();
     Button net, full, free, backface;
   
 public SouthPanel(Piace piace)    {
	  setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
	  this.piace = piace;

	  free = makeButton("Free");
	  
	  kind.add("Polygon"); kind.add("Line");
	  add(kind); kind.addItemListener(this);
	  
	  full = makeButton("Empty"); 

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
	  net = makeButton("Net OFF");
	  backface = makeButton("Backface ON");
	 }
public void actionPerformed(ActionEvent ev) {
    String obj = ev.getActionCommand();

    if (obj.equals(" Full ")) {
        full.setLabel("Empty");
        piace.full = true;
        piace.select.setFull(true);
    }
    else if (obj.equals("Empty")) {
        full.setLabel(" Full ");
        piace.full = false;
        piace.select.setFull(false);
    }
    else if (obj.equals("Free")) {
        free.setLabel("Unfree");
        piace.free = true;
        piace.select.setFree(true);
    }
    else if (obj.equals("Unfree")) {
        free.setLabel("Free");
        piace.free = false;
        piace.select.setFree(false) ;
    }
    else if (obj.equals("Net ON")) {
        net.setLabel("Net OFF");
        piace.net = true;
        piace.figure.setNet(true);
    }
    else if (obj.equals("Net OFF")) {
        net.setLabel("Net ON");
        piace.net = false;
        piace.figure.setNet(false);
    }
    else if (obj.equals("Backface ON")) {
        backface.setLabel("Backface OFF");
        piace.flag_backface = true;
        piace.figure.setFlag_backface(true);
    }
    else if (obj.equals("Backface OFF")) {
        backface.setLabel("Backface ON");
        piace.flag_backface = false;
        piace.figure.setFlag_backface(false);
    }
    else
        return;

    piace.repaint();
}
public void itemStateChanged(ItemEvent evt) {
    if (evt.getSource() instanceof Checkbox) {
        piace.color = ((Component) evt.getSource()).getBackground();
        if (piace.select != null)
            piace.select.setColor(((Component) evt.getSource()).getBackground());
    }
    else if( (evt.getSource() == kind)&&(!(piace.select instanceof Side)) ){
        int number = kind.getSelectedIndex();
        Element temp = piace.select;
        if (number == 0) {
            piace.select =
                new Poly(piace.getPiace3D(), temp.getColor(), temp.isFree(), temp.isFull());
        }
        else if (number == 1) {
            piace.select =
                new Line(piace.getPiace3D(), temp.getColor(), temp.isFree(), temp.isFull());
        }
        piace.select.setField(temp.getField());
        piace.copy.getElements().addElement(piace.select);
        piace.copy.getElements().remove(temp);
    }
    else
        return;

    piace.repaint();
}
/**
 * Insert the method's description here.
 * Creation date: (19.10.2001 15:22:57)
 */
protected Button makeButton(String name) {
    Button button = new Button(name);
    button.addActionListener(this);
    add(button);
    return button;
}
	 public void setControl()
	  {
		for(int i=0; i<checkbox.length; i++)
		 {
		  if(checkbox[i].getBackground()== piace.select.getColor())          
			  checkbox[i].setState(true);
		 }
		piace.color = piace.select.getColor();
		piace.full = piace.select.isFull();

		if (piace.select.isFull())
		  full.setLabel("Empty");    
		else
		  full.setLabel(" Full ");   
		if (piace.figure.isNet())
		  net.setLabel("Net OFF");  
		else
		  net.setLabel("Net ON");   
		if (piace.figure.isFlag_backface())
		  backface.setLabel("Backface OFF");  
		else
		  backface.setLabel("Backface ON");   
	  }
}
