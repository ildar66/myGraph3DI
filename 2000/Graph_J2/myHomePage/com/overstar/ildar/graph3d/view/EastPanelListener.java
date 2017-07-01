package com.overstar.ildar.graph3d.view;

import com.overstar.ildar.graph3d.model.*;
import java.awt.event.*;
/**
* Insert the type's description here.
* Creation date: (16.10.2003 12:07:29)
* @author: Shafigullin Ildar
*/
public class EastPanelListener implements java.awt.event.ActionListener {
    Piace piace;
/**
 * EastPanelListener constructor comment.
 */
public EastPanelListener(Piace piace) {
    this.piace = piace;
}
public void actionPerformed(ActionEvent ev) {
    String obj = ev.getActionCommand();

    if ((obj.equals("'L'"))
        && (piace.select instanceof Side)
        && (piace.select.getField().size() >= 3)) {
        piace.face((Side) piace.select, false);
    }
    else if (
        (obj.equals("'l'"))
            && (piace.select instanceof Side)
            && (piace.select.getField().size() >= 3)) {
        piace.face((Side) piace.select, true);
    }
    else if ((obj.equals("'0'")) && (piace.select instanceof Side)) {
        piace.copy = (Side) piace.select;
        piace.copy.setNormal_Left(!(piace.copy.isNormal_Left()));
    }

    else if ((obj.equals("'c'")) && (piace.select.getField().size() > 1)) {
        piace.copy(piace.select);
    }
    else if ((obj.equals("'C'")) && (piace.select.getField().size() > 1)) {
        piace.copy(piace.figure);
    }

    else if (obj.equals("PageUp"))
        piace.figure.scale(piace.matrix, 1.1f, 1.1f, 1.1f);
    else if (obj.equals("PageDown"))
        piace.figure.scale(piace.matrix, 1f / 1.1f, 1f / 1.1f, 1f / 1.1f);
    else if ((obj.equals("'4'")) && (piace.step > 0))
        piace.select.scale(piace.matrix, 1.1f, 1f, 1f);
    else if ((obj.equals("'4'")) && (piace.step < 0))
        piace.select.scale(piace.matrix, 1f / 1.1f, 1f, 1f);
    else if ((obj.equals("'5'")) && (piace.step > 0))
        piace.select.scale(piace.matrix, 1f, 1.1f, 1f);
    else if ((obj.equals("'5'")) && (piace.step < 0))
        piace.select.scale(piace.matrix, 1f, 1f / 1.1f, 1f);
    else if ((obj.equals("'6'")) && (piace.step > 0))
        piace.select.scale(piace.matrix, 1f, 1f, 1.1f);
    else if ((obj.equals("'6'")) && (piace.step < 0))
        piace.select.scale(piace.matrix, 1f, 1f, 1f / 1.1f);

    else if ((obj.equals("'7'")) && (piace.step > 0))
        piace.figure.scale(piace.matrix, 1.1f, 1f, 1f);
    else if ((obj.equals("'7'")) && (piace.step < 0))
        piace.figure.scale(piace.matrix, 1f / 1.1f, 1f, 1f);
    else if ((obj.equals("'8'")) && (piace.step > 0))
        piace.figure.scale(piace.matrix, 1f, 1.1f, 1f);
    else if ((obj.equals("'8'")) && (piace.step < 0))
        piace.figure.scale(piace.matrix, 1f, 1f / 1.1f, 1f);
    else if ((obj.equals("'9'")) && (piace.step > 0))
        piace.figure.scale(piace.matrix, 1f, 1f, 1.1f);
    else if ((obj.equals("'9'")) && (piace.step < 0))
        piace.figure.scale(piace.matrix, 1f, 1f, 1f / 1.1f);

    else if (
        (obj.equals("Del")) && (piace.getPiace3D().getPoints3D().size() != 0)) {
        piace.getPiace3D().deletelast3D(piace.select, true);
        piace.graph.SP.setControl();
    }
    else if (
        (obj.equals("Ctrl+Del")) && (piace.getPiace3D().getPoints3D().size() != 0)) {
        piace.delete(piace.select);
        piace.graph.SP.setControl();
    }
    else if (
        (obj.equals("Shift+Del")) && (piace.getPiace3D().getPoints3D().size() != 0)) {
        piace.delete(piace.figure);
        piace.graph.SP.setControl();
    }

    else if ((obj.equals("Figure")) && (piace.select.getField().size() != 0)) {
        piace.nextFigure();
    }
    else if (
        (obj.equals("Offset('n')")) && (piace.select.getField().size() != 0)) {
        piace.next_offset();
    }
    else
        return;

    if (piace.select.getField().size() != 0)
        Shade.mouseUp(piace);
    piace.repaint(); //     piace.requestFocus();
}
}
