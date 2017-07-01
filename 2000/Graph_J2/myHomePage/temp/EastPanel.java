 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

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