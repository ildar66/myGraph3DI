package com.overstar.ildar.graph3d.model;

import java.awt.*;
/**
 * Insert the type's description here.
 * Creation date: (17.10.2003 9:38:03)
 * @author: Shafigullin Ildar
 */
public interface Shape {
    /**
    * Insert the method's description here.
    * Creation date: (17.10.2003 9:40:54)
    * @param g java.awt.Graphics
    * @param offset java.awt.Point
    * @param view java.lang.String
    */
    public void draw(Graphics g, Point offset, String view);
}