package com.overstar.ildar.graph3d.model;

import java.util.*;
import java.awt.*;
import com.overstar.ildar.graph3d.math.*;
import com.overstar.ildar.graph3d.view.Piace;
/**
* Insert the type's description here.
* Creation date: (17.10.2003 11:27:29)
* @author: Shafigullin Ildar
*/
public abstract class Swarm implements Dynamic, Shape {
    protected Vector field = new Vector(); //набор номеров(number) трехмерных точек.
    protected Piace3D piace3D; //ссылка на панель 3D
    protected AnimData anim = new AnimData(); 
 
/**
 * Swarm constructor comment.
 */
public Swarm(Piace3D piace3D) {
  this.piace3D = piace3D;
}
/**
 * Добавляет к полю(field) ссылку на трехмерную точку с номером(number).
 * Creation date: (17.10.2003 11:40:40)
 * @param number int
 */
public void add(int number) {
    field.add(new Integer(number));
}
/**
 * Insert the method's description here.
 * Creation date: (22.10.2003 14:18:34)
 * @return com.overstar.ildar.graph3d.model.Swarm
 */
public abstract Swarm copy();
/**
 * Insert the method's description here.
 * Creation date: (24.10.2003 10:19:14)
 */
public abstract void deleteContents();
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 13:05:06)
 * @return com.overstar.ildar.graph3d.model.AnimData
 */
public AnimData getAnim() {
	return anim;
}
/**
 * Insert the method's description here.
 * Creation date: (21.10.2003 12:55:36)
 * @return java.util.Vector
 */
public Vector getCopyField() {
    Vector newField = new Vector(field.size());
    for (int i = 0, num = 0; i < field.size(); i++) {
        num = ((Integer) field.elementAt(i)).intValue();
        newField.addElement(new Integer(piace3D.getPoints3D().size()));
        Point3D newPoint3D = Point3D.copy((Point3D) piace3D.getPoints3D().elementAt(num));
        piace3D.getPoints3D().addElement(newPoint3D);
    }
    return newField;
}
/**
 * Insert the method's description here.
 * Creation date: (25.10.2003 16:48:16)
 * @return java.util.Vector
 */
public java.util.Vector getField() {
	return field;
}
/**
 * Сеть точек роя.
 * Creation date: (17.10.2003 11:50:40)
 */
public void net(Graphics g, Point offset) //сеть точек роя.
{
    for (int i = 0, t = 0; i < field.size(); i++) {
        t = ((Integer) field.elementAt(i)).intValue();
        Point3D p = (Point3D) piace3D.getPoints3D().elementAt(t);
        g.drawOval(
            offset.x + Math.round(p.x) - 5,
            offset.y + Math.round(p.y) - 5,
            10,
            10);
    }
}
/**
 * Bычисление вектора нормали к трем точкам роя.
 * по показателю нормали "normal_Left".
 * Creation date: (17.10.2003 11:53:59)
 */
protected Vector3D normal(boolean normal_Left, int n1, int n2, int n3) {
    int Na = ((Integer) field.elementAt(n1)).intValue(); //номер n1 точки в piace.
    int Nb = ((Integer) field.elementAt(n2)).intValue(); //№ n2 точки в piace.
    int Nc = ((Integer) field.elementAt(n3)).intValue(); //№ n3 точки в piace.

    Point3D pA = (Point3D) piace3D.getPoints3D().elementAt(Na);
    Point3D pB = (Point3D) piace3D.getPoints3D().elementAt(Nb);
    Point3D pC = (Point3D) piace3D.getPoints3D().elementAt(Nc);
    
    //вектор проведенный из середины эл-та в начало.
    Vector3D A = new Vector3D(pB.x - pA.x, pB.y - pA.y, pB.z - pA.z);
    //вектор проведенный из конца эл-та в начало.
    Vector3D B = new Vector3D(pC.x - pA.x, pC.y - pA.y, pC.z - pA.z);

    if (normal_Left) //если показатель левосторонний.
        return (A.cross(B));
    else             //если показатель правосторонний.
        return (B.cross(A));
}
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 10:53:49)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param angleX float
 * @param angleY float
 * @param angleZ float
 */
public void rotate(Matrix3D matrix, float angleX, float angleY, float angleZ) {
    matrix.identity();
    if (angleX != 0)
        matrix.rotate(angleX, "X", false);
    if (angleY != 0)
        matrix.rotate(angleY, "Y", false);
    if (angleZ != 0)
        matrix.rotate(angleZ, "Z", false);
    transform(matrix);
}
/**
 * Insert the method's description here.
 * Creation date: (29.10.2003 12:07:15)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param zero com.overstar.ildar.graph3d.math.Point3D
 * @param angleX float
 * @param angleY float
 * @param angleZ float
 */
public void rotate(Matrix3D matrix, int z, float angleX, float angleY, float angleZ) {
    matrix.identity();
    Point3D zero = (Point3D) piace3D.getPoints3D().get(z);
    matrix.translate(-zero.x, -zero.y, -zero.z);
    if (angleX != 0)
        matrix.rotate(angleX, "X", false);
    if (angleY != 0)
        matrix.rotate(angleY, "Y", false);
    if (angleZ != 0)
        matrix.rotate(angleZ, "Z", false);
    matrix.translate(zero.x, zero.y, zero.z);
    transform(matrix);
}
/**
 * Insert the method's description here.
 * Creation date: (29.10.2003 12:26:39)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param axis Axis3D
 * @param angle float
 */
public void rotate(Matrix3D matrix, Axis3D axis, float angle) {
    matrix.identity();
    Point3D pA = (Point3D) piace3D.getPoints3D().get(axis.zero);
    Point3D pB = (Point3D) piace3D.getPoints3D().get(axis.end);
    // вектор оси вращения
    Vector3D nV = new Vector3D(pB.x - pA.x, pB.y - pA.y, pB.z - pA.z);
    matrix.translate(-pA.x, -pA.y, -pA.z);
    matrix.rotate(Math.atan2(nV.y, nV.z), "X", true);
    matrix.rotate(
        -Math.atan2(nV.x, Math.sqrt(nV.y * nV.y + nV.z * nV.z)) + Math.PI,
        "Y",
        true);
    matrix.rotate(angle, "Z", false);
    matrix.rotate(
        Math.atan2(nV.x, Math.sqrt(nV.y * nV.y + nV.z * nV.z)) - Math.PI,
        "Y",
        true);
    matrix.rotate(-Math.atan2(nV.y, nV.z), "X", true);
    matrix.translate(pA.x, pA.y, pA.z); 
    transform(matrix);
}
/**
 * вращение Element-a "e" вокруг оси координат"axis"
 * на угол "rotate".
 * Creation date: (17.10.2003 13:06:51)
 */
///////////////////////////////////////////////////////Блок вращения элементов:
public void rotate(Matrix3D matrix, String axis, float angle) {
    matrix.identity();
    matrix.rotate(angle, axis, false);
    transform(matrix);
}
/**
 * Масштабирования элементов:
 * масштабирование по осям X, Y, Z
 * Creation date: (11.10.2001 15:08:18)
 */
public void scale(Matrix3D matrix, float x, float y, float z) {
    matrix.identity();
    matrix.scale(x, y, z);
    transform(matrix);
}
/**
 * Insert the method's description here.
 * Creation date: (30.10.2003 14:32:53)
 */
public void scenario(Matrix3D matrix) {
    if ((anim.isRevolvingAxis) && (anim.axis != null)) {
        rotate(matrix, anim.axis, anim.animY);
    }
    if (anim.isRevolving) {
        if (anim.offset != -1)
            rotate(matrix, anim.offset, anim.animX, anim.animY, 0f);
        else
            rotate(matrix, anim.animX, anim.animY, 0f);
        /**
        animXcur += Math.abs(animX);
        animYcur += Math.abs(animY);
        if ((animXcur > animXmax) || (animYcur > animYmax)) {
            animX = -animX;
            animY = -animY;
            animXcur = animYcur = 0.0f;
        }
        */
    }
    if (anim.isTranslation)
        translate(matrix, anim.stepX, anim.stepY, anim.stepZ);
    if (anim.isScale) {
        scale(matrix, anim.scaleX, anim.scaleY, anim.scaleZ);
    }
}
/**
 * Insert the method's description here.
 * Creation date: (25.10.2003 16:48:16)
 * @param newField java.util.Vector
 */
public void setField(java.util.Vector newField) {
	field = newField;
}
/**
 * метод, вызываемый после изменения СК
 * Creation date: (11.10.2001 15:17:50)
 */
///////////////////////////////////////////////////////общий Блок:
  protected void transform(Matrix3D matrix) //метод, вызываемый после изменения СК
	 {
	   for (int i=0, t=0; i<field.size(); i++)      
		{
		  t = ((Integer)field.elementAt(i)).intValue();
		  matrix.transform((Point3D)piace3D.getPoints3D().elementAt(t));
		}
	 }
/**
 * перемещение на расстояние "dx", "dy", "dz"
 * Creation date: (11.10.2001 15:29:58)
 */
public void translate(Matrix3D matrix, int dx, int dy, int dz) //
{
    matrix.identity();
    matrix.translate(dx, dy, dz);
    transform(matrix);
}
/**
 * Используется для сортировки очереди рисования роя.
 * Creation date: (17.10.2003 12:01:17)
 */
public float weight() 
{
    if (field.size() == 0)
        return (0f);
    float sumZ = 0f;
    for (int i = 0, t = 0; i < field.size(); i++) {
        t = ((Integer) field.elementAt(i)).intValue();
        sumZ += ((Point3D) piace3D.getPoints3D().elementAt(t)).z; //подумать над sum**2;
    }
    return (sumZ / field.size());
}
}
