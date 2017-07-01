package com.overstar.ildar.graph3d.view;

import java.awt.event.*;
import java.awt.*;
import com.overstar.ildar.graph3d.model.*;
/**
* Insert the type's description here.
* Creation date: (16.10.2003 12:46:00)
* @author: Shafigullin Ildar
*/
public class NorthPanelListener  implements ActionListener, ItemListener {
    NorthPanel northPanel;
/**
 * Insert the method's description here.
 * Creation date: (16.10.2003 12:57:14)
 * @param param com.overstar.ildar.graph3d.model.Piace
 */
public NorthPanelListener(NorthPanel northPanel) {
	this.northPanel = northPanel;
	}
    public void actionPerformed(ActionEvent ev) {
        String obj = ev.getActionCommand();
        if ((obj.equals("Esc")) && (northPanel.piace.getPiace3D().getPoints3D().size() != 0)) {
            northPanel.piace.delete(northPanel.piace.select);
            northPanel.piace.graph.SP.setControl();
        } else if (
            (obj.equals("new Element('e')")) && (northPanel.piace.select.getField().size() != 0)) {
            northPanel.piace.newElement();
        } else if ((obj.equals("new Side('s')")) && (northPanel.piace.select.getField().size() != 0)) {
            northPanel.piace.newSide();
        } else if (
            (obj.equals("new Figure('f')")) && ( northPanel.piace.select.getField().size() != 0)) {
             northPanel.piace.newFigure();
        }
          else if ((obj.equals("FP"))&&(!northPanel.piace.FP.isVisible()))                                                  	
            {northPanel.piace.FP.setVisible(true); }     
        else if (obj.equals("FP")) {
            northPanel.piace.FP.setVisible(true);
        } else if (obj.equals("Move")) //переключение режима.
            {
            northPanel.piace.regimeMove = true;
            northPanel.regime.setLabel("Paint");
            northPanel.regime.setBackground(Color.yellow);
            northPanel.newFigure.setEnabled(false);
            northPanel.newSide.setEnabled(false);
            northPanel.newElement.setEnabled(false);
            northPanel.side.setEnabled(true);
            northPanel.figure.setEnabled(true);
            northPanel.all.setEnabled(true);
        } else if (obj.equals("Paint")) //переключение режима.
            {
            northPanel.piace.regimeMove = false;
            northPanel.regime.setLabel("Move");
            northPanel.regime.setBackground(Color.green);
            northPanel.newFigure.setEnabled(true);
            northPanel.newSide.setEnabled(true);
            northPanel.newElement.setEnabled(true);
            northPanel.side.setEnabled(false);
            northPanel.figure.setEnabled(false);
            northPanel.all.setEnabled(false);
        } else if (obj.equals("Start")) //включение анимации.
            {
            if (northPanel.piace.graph.anim == null) {
                northPanel.piace.graph.start();
                northPanel.anim.setLabel("Stop");
                northPanel.anim.setBackground(Color.red);
            }
        } else if (obj.equals("Stop")) //выкл. анимации.
            if (northPanel.piace.graph.anim != null) {
                northPanel.piace.graph.stop();
                northPanel.anim.setLabel("Start");
                northPanel.anim.setBackground(Color.blue);
            } else
                return;

        if (northPanel.piace.select.getField().size() != 0)
            Shade.mouseUp(northPanel.piace);
        northPanel.piace.repaint();
    }
    public void itemStateChanged(ItemEvent evt) {
        if (evt.getSource() instanceof Checkbox) {
            Checkbox checkbox = (Checkbox) evt.getSource();
            if ((checkbox == northPanel.side) && (northPanel.piace.select.getField().size() != 0)) {
                Move.choice = 1;
            } else if ((checkbox == northPanel.figure) && (northPanel.piace.figure.getField().size() != 0)) {
                Move.choice = 2;
            } else if ((checkbox == northPanel.all) && (northPanel.piace.figure.getField().size() != 0)) {
                Move.choice = 3;
            }
        }
    }
}
