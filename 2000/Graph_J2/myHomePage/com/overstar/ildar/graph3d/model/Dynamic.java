package com.overstar.ildar.graph3d.model;

import com.overstar.ildar.graph3d.math.*;
/**
 * Insert the type's description here.
 * Creation date: (17.10.2003 9:48:23)
 * @author: Shafigullin Ildar
 */
public interface Dynamic {
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 10:53:20)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param angleX float
 * @param angleY float
 * @param angleZ float
 */
void rotate(Matrix3D matrix, float angleX, float angleY, float angleZ);
/**
 * Insert the method's description here.
 * Creation date: (29.10.2003 12:05:36)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param zero int
 * @param angleX float
 * @param angleY float
 * @param angleZ float
 */
public void rotate(Matrix3D matrix, int zero, float angleX, float angleY, float angleZ);
/**
 * Insert the method's description here.
 * Creation date: (29.10.2003 12:25:22)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param axis Axis
 * @param angle float
 */
public void rotate(Matrix3D matrix, Axis3D axis, float angle);
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 10:57:36)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param axis java.lang.String
 * @param angle float
 */
public void rotate(Matrix3D matrix, String axis, float angle);
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 10:09:06)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param x float
 * @param y float
 * @param z float
 */
public void scale(Matrix3D matrix, float x, float y, float z);
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 10:09:00)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param x int
 * @param y int
 * @param z int
 */
public void translate(Matrix3D matrix, int dx, int dy, int dz);
}
