 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

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

	   else if (obj.equals("Up"))       
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