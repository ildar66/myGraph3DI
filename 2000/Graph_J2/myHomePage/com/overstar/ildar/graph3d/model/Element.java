package com.overstar.ildar.graph3d.model;

import com.overstar.ildar.graph3d.math.*;
import com.overstar.ildar.graph3d.view.Piace;
/**
* элемент стороны(Side) фигуры(Figure)
* Creation date: (17.10.2003 9:48:23)
* @author: Shafigullin Ildar
*/
import java.util.Vector;
import java.awt.*;

public abstract class Element extends Swarm {
    protected boolean full; //показатель(full) заполненности элемента.
    protected boolean free; //характер тени.
    protected Color color; //цвет(color) элемента.
    public Element(Piace3D piace3D, Color color, boolean free, boolean full) {
        super(piace3D);
        this.color = color;
        this.free = free;
        this.full = full;
    }
/**
 * Insert the method's description here.
 * Creation date: (24.10.2003 10:27:04)
 */
public void deleteContents() {
    while (field.size() != 0)
        piace3D.deletelast3D(this, false);
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 16:51:55)
 * @return java.awt.Color
 */
public java.awt.Color getColor() {
	return color;
}
/**
 * Insert the method's description here.
 * Creation date: (19.10.2003 16:02:03)
 * @return java.awt.Polygon
 * @param offset java.awt.Point
 * @param view java.lang.String
 */
public abstract Polygon getShade(Point offset, String view);
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 16:57:14)
 * @return boolean
 */
public boolean isFree() {
	return free;
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 16:53:59)
 * @return boolean
 */
public boolean isFull() {
	return full;
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 16:51:55)
 * @param newColor java.awt.Color
 */
public void setColor(java.awt.Color newColor) {
	color = newColor;
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 16:57:14)
 * @param newFree boolean
 */
public void setFree(boolean newFree) {
	free = newFree;
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 16:53:59)
 * @param newFull boolean
 */
public void setFull(boolean newFull) {
	full = newFull;
}
}
