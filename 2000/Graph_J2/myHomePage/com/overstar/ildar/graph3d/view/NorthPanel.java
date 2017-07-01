package com.overstar.ildar.graph3d.view;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import com.overstar.ildar.graph3d.model.*;
/////////////////////////////////////////////////////////////////////////////////Северная панель
class NorthPanel extends Panel {
    Button regime, newFigure, newSide, newElement, Esc, FP, anim;
    Checkbox side, figure, all;
    Piace piace;
    public NorthPanel(Piace piace) {
	    this.piace = piace;
	    NorthPanelListener listener = new NorthPanelListener(this);
        add(Esc = new Button("Esc"));
        Esc.addActionListener(listener);
        add(newFigure = new Button("new Figure('f')"));
        newFigure.addActionListener(listener);
        newFigure.setEnabled(false);
        add(newSide = new Button("new Side('s')"));
        newSide.addActionListener(listener);
        newSide.setEnabled(false);
        add(newElement = new Button("new Element('e')"));
        newElement.addActionListener(listener);
        newElement.setEnabled(false);
        add(FP = new Button("FP"));
        FP.addActionListener(listener);
        add(regime = new Button("Paint"));
        regime.addActionListener(listener);
        regime.setBackground(Color.green);
        add(anim = new Button("Stop"));
        anim.addActionListener(listener);
        anim.setBackground(Color.blue);
        CheckboxGroup group = new CheckboxGroup();
        add(side = new Checkbox("Side", group, false));
        side.addItemListener(listener);
        add(figure = new Checkbox("Figure", group, true));
        figure.addItemListener(listener);
        add(all = new Checkbox("All", group, false));
        all.addItemListener(listener);
    }
}
