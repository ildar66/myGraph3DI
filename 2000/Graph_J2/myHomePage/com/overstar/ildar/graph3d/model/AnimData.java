package com.overstar.ildar.graph3d.model;

import com.overstar.ildar.graph3d.math.*;
/**
* Insert the type's description here.
* Creation date: (30.10.2003 14:21:28)
* @author: Shafigullin Ildar
*/
public class AnimData {
    // ����� �� ��������� � ��������.
    public boolean isRevolving = false;
    public boolean isRevolvingAxis = false;
    // ����� �� ������������� ������������ � ��������.
    public boolean isTranslation = false;
    // ����� �� ���������������� � ��������.
    public boolean isScale = false;

    public float animX, animY; // ���� �������� 
    //private float animXmax = 360f; // maximume ������������� ��������.
    //private float animYmax = 360f; // maximume ������������� ��������.
    //private float animXcur, animYcur; // ������� ��������� ������������� ��������.
    public int offset = -1; // ����� ������ ����� ��������
    public Axis3D axis; // ��� ��������
    public int stepX, stepY, stepZ; // ���� ��������������� �����������.
    //private int maxX, maxY, maxZ; // max-� ��������������� �����������.
    public float scaleX, scaleY, scaleZ; // ���� ��������������� �����������.
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 13:04:23)
 */
public void setAnimAngle(float animX, float animY) { //���� ��������
    this.animX = animX;
    this.animY = animY;
}
}
