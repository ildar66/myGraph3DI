package com.overstar.ildar.graph3d.model;

import com.overstar.ildar.graph3d.math.*;
/**
* Insert the type's description here.
* Creation date: (30.10.2003 14:21:28)
* @author: Shafigullin Ildar
*/
public class AnimData {
    // будет ли вращаться в анимации.
    public boolean isRevolving = false;
    public boolean isRevolvingAxis = false;
    // будет ли поступательно перемещаться в анимации.
    public boolean isTranslation = false;
    // будет ли масштабироваться в анимации.
    public boolean isScale = false;

    public float animX, animY; // углы анимации 
    //private float animXmax = 360f; // maximume вращательного движения.
    //private float animYmax = 360f; // maximume вращательного движения.
    //private float animXcur, animYcur; // текущее положение вращательного движения.
    public int offset = -1; // номер осевой точки вращения
    public Axis3D axis; // ось вращения
    public int stepX, stepY, stepZ; // шаги поступательного перемещения.
    //private int maxX, maxY, maxZ; // max-ы поступательного перемещения.
    public float scaleX, scaleY, scaleZ; // шаги поступательного перемещения.
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 13:04:23)
 */
public void setAnimAngle(float animX, float animY) { //углы анимации
    this.animX = animX;
    this.animY = animY;
}
}
