package com.overstar.ildar.graph3d.view;

import com.overstar.ildar.graph3d.math.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import com.overstar.ildar.graph3d.model.*;
public class Move { //движение элементов с помощью мыши.
    private static boolean typeTranslate;
    public static int choice = 2;
    static int x0, y0, startX, startY;
    private static float xtheta, ytheta;

 static void mouseClicked(Piace piace, int x, int y) {
    m : for (Enumeration e = piace.getPiace3D().getFigures().elements(); e.hasMoreElements();) {
        Figure figure = (Figure) e.nextElement();
        int t = piace.numberPoint(figure.getField(), x, y);
        if (t < piace.getPiace3D().getPoints3D().size()) {
            Point3D p = (Point3D) piace.getPiace3D().getPoints3D().elementAt(t);
            piace.offset.translate(Math.round(p.x), Math.round(p.y));
            //переводит ось вращения в точку "p"
            piace.getPiace3D().translate(piace.matrix, (int)-p.x, (int)-p.y, (int)-p.z);
            break m;
        }
        for (Enumeration list2 = figure.getSwarms().elements(); list2.hasMoreElements(); ) {
            Side side = (Side) list2.nextElement();
            if (side.getShade(piace.offset, "shade").contains(new Point(x, y))) {
                piace.figure = figure;
                piace.select = side;
                break m;
            }
        }
    }
    piace.graph.SP.setControl();
    piace.repaint();
}
static void mouseDown(Piace piace, int x, int y, MouseEvent e) {
    x0 = startX = x;
    y0 = startY = y;
    int t = piace.numberPoint(piace.select.getField(), x, y);
    //если тень точки принадлежит стороне-select:
    if (t < piace.getPiace3D().getPoints3D().size()) {
        typeTranslate = true;
        //if(e.isControlDown())  
        //piace.select.setA(piace.numberPoint(x, y));
    }
}
static void mouseDrag(Piace piace, int x, int y, MouseEvent e) {
    if (typeTranslate) //режим перемещения
        {
        if (!e.isControlDown()) {
            if (choice == 1) {
                if (piace.select instanceof Side)
                    piace.select.translate(piace.matrix, x - x0, y - y0, 0);
                else if (piace.select instanceof Element)
                    piace.copy.translate(piace.matrix, x - x0, y - y0, 0);
            }
            else if (choice == 2)
                piace.figure.translate(piace.matrix, x - x0, y - y0, 0);
            else if (choice == 3) {
                piace.getPiace3D().translate(piace.matrix, x - x0, y - y0, 0);
            }
        }
    }
    else //режим вращения.
        {
        xtheta = (y - y0) * (360.0f / piace.getSize().width);
        ytheta = (x0 - x) * (360.0f / piace.getSize().height);
        if (choice == 1) {
            if (piace.select instanceof Side) {
                piace.select.rotate(piace.matrix, xtheta, ytheta, 0f);
            }
            else if (piace.select instanceof Element) {
                piace.copy.rotate(piace.matrix, xtheta, ytheta, 0f);
            }
        }
        else if (choice == 2) {
            piace.figure.rotate(piace.matrix, xtheta, ytheta, 0f);
        }
        else if (choice == 3) {
            piace.getPiace3D().rotate(piace.matrix, xtheta, ytheta, 0f);
        }
    }
    x0 = x;
    y0 = y;
    piace.repaint();
}
static void mouseUp(Piace piace, int x, int y, MouseEvent e) {
    if (typeTranslate) { //режим перемещения
        //if(e.isControlDown()) 
        //  piace.select.setB(piace.numberPoint(x, y)); 
    } //System.out.println("piace.select.a: " + piace.select.a + "; piace.select.b: " + piace.select.b);  

    else { //режим вращения
	    /**
        if (choice == 1) {
            if (piace.select.getClass() == Side.class)
                 ((Side) piace.select).setAnimAngle(xtheta, ytheta);
            else
                 ((Side) piace.copy).setAnimAngle(xtheta, ytheta);
            if (isControlDown) {
                if (piace.select.getClass() == Side.class)
                     ((Side) piace.select).numberZero = piace.zero;
                else
                     ((Side) piace.copy).numberZero = piace.zero;
            }
        } else if (choice == 2) {
            piace.figure.setAnimAngle(xtheta, ytheta);
            if (isControlDown) {
                piace.figure.numberZero = piace.zero;
            }
        } */  
    }
    piace.repaint();
    typeTranslate = false;
}
}
