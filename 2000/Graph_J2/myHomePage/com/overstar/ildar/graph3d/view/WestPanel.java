package com.overstar.ildar.graph3d.view;

 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;
 import com.overstar.ildar.graph3d.model.*;
 import com.overstar.ildar.graph3d.math.*;
   ////////////////////////////////////////////////////////////////////////////////////Панель Динамики.
   class WestPanel extends Panel{
	 Piace piace;
 
public WestPanel(java.awt.event.ActionListener listener) {
    this.piace = piace;
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    //setFont(new Font("Helvetica", Font.BOLD, 11)); 
    setLayout(gridbag);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(0, 0, 0, 0);

    c.gridwidth = GridBagConstraints.REMAINDER;
    makeLabel("Rotate:", gridbag, c, Color.blue);
    c.gridwidth = 1;
    makeButton("'X'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("'Y'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("'Z'", gridbag, c, listener);
    c.gridwidth = 1;
    makeButton("'x'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("'y'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("'z'", gridbag, c, listener);
    c.gridwidth = 1;
    makeButton("'1'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("'2'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("'3'", gridbag, c, listener);

    makeLabel("Direct:", gridbag, c, Color.blue);
    c.gridwidth = 1;
    makeButton("'+'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("'-'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("'*'", gridbag, c, listener);

    makeLabel("Transfer:", gridbag, c, Color.blue);
    makeLabel("  Piace", gridbag, c, Color.green);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("Up", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("->", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("Down", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("<-", gridbag, c, listener);
    makeLabel("Figure Shift", gridbag, c, Color.green);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton(" Up", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton(" ->", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton(" Down", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton(" <-", gridbag, c, listener);
    makeButton(" End", gridbag, c, listener);
    makeButton(" Home", gridbag, c, listener);
    makeLabel("Select Ctrl", gridbag, c, Color.green);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("Up ", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("-> ", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("Down ", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("<- ", gridbag, c, listener);
    makeButton("End ", gridbag, c, listener);
    makeButton("Home ", gridbag, c, listener);

    makeLabel("Next:", gridbag, c, Color.blue);
    makeButton("Side(Tab)", gridbag, c, listener);
    makeButton("Element", gridbag, c, listener);
}
	 protected void makeButton(String name, GridBagLayout gridbag, GridBagConstraints c, java.awt.event.ActionListener listener)
	 {
	   Button button = new Button(name);
	   button.addActionListener(listener);
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
}
