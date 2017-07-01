package com.overstar.ildar.graph3d.view;

import java.applet.*;
import java.awt.*;
import com.overstar.ildar.graph3d.model.*;

///////////////////////////////////////////////////////////////////////////////Западная панель
class EastPanel extends Panel {
public EastPanel(java.awt.event.ActionListener listener) {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    // setFont(new Font("Helvetica", Font.PLAIN, 14));
    setLayout(gridbag);
    c.fill = GridBagConstraints.HORIZONTAL; //c.weightx = 3.0;
    c.insets = new Insets(0, 0, 0, 0);

    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    makeLabel("Face:", gridbag, c, Color.blue);
    c.gridwidth = 1;
    makeButton("'L'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
    makeButton("'l'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    makeButton("'0'", gridbag, c, listener);

    makeLabel("Scale", gridbag, c, Color.blue);
    makeLabel("Select", gridbag, c, Color.green);
    c.gridwidth = 1;
    makeButton("'4'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
    makeButton("'5'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    makeButton("'6'", gridbag, c, listener);
    makeLabel("Figure", gridbag, c, Color.green);
    c.gridwidth = 1;
    makeButton("'7'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeButton("'8'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("'9'", gridbag, c, listener);
    makeLabel("Figure All", gridbag, c, Color.green);
    makeButton("PageUp", gridbag, c, listener);
    makeButton("PageDown", gridbag, c, listener);

    makeLabel("Delete:", gridbag, c, Color.blue);
    makeLabel("Last Point", gridbag, c, Color.green);
    makeButton("Del", gridbag, c, listener);
    makeLabel("Select", gridbag, c, Color.green);
    makeButton("Ctrl+Del", gridbag, c, listener);
    makeLabel("Figure", gridbag, c, Color.green);
    makeButton("Shift+Del", gridbag, c, listener);

    makeLabel("Copy:", gridbag, c, Color.blue);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeLabel("Select", gridbag, c, Color.green);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("'c'", gridbag, c, listener);
    c.gridwidth = GridBagConstraints.RELATIVE;
    makeLabel("Figure", gridbag, c, Color.green);
    c.gridwidth = GridBagConstraints.REMAINDER;
    makeButton("'C'", gridbag, c, listener);

    makeLabel("Next:", gridbag, c, Color.blue);
    makeButton("Figure", gridbag, c, listener);
    makeButton("Offset('n')", gridbag, c, listener);
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
