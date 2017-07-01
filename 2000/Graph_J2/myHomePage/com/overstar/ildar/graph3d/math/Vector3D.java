package com.overstar.ildar.graph3d.math;

public class Vector3D { //���������� ������.
    public float x, y, z; //���������� �������.

public Vector3D(float x0, float y0, float z0) {
    x = x0;
    y = y0;
    z = z0;
}
public Vector3D cross(Vector3D A) //��������� ������������ �� ������ �.
{
    float cx = (y * A.z) - (z * A.y);
    float cy = (z * A.x) - (x * A.z);
    float cz = (x * A.y) - (y * A.x);
    return (new Vector3D(cx, cy, cz));
}
public float dot(Vector3D A) //�������� ������������ � �������� �.
{
    return (A.x * x + A.y * y + A.z * z);
}
public float mag() //���������� ������ �������.
{
    return ((float) Math.sqrt(x * x + y * y + z * z));
}
public String toString() //���������� � �������.
{
    return (new String("(" + x + "," + y + "," + z + ")"));
}
}