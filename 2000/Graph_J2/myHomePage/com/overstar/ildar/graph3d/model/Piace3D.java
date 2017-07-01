package com.overstar.ildar.graph3d.model;

import com.overstar.ildar.graph3d.math.*;
import java.util.*;
/**
* Insert the type's description here.
* Creation date: (23.10.2003 14:21:17)
* @author: Shafigullin Ildar
*/
public class Piace3D implements Dynamic{
    private Vector points3D = new Vector(); //¬ектор физических точек(3D).
    private Vector figures = new Vector(); //¬ектор всех фигур.
/**
 * Piace3D constructor comment.
 */
public Piace3D() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (25.10.2003 12:17:07)
 */
public void addFigure(Figure fig) {
    figures.addElement(fig);
}
//удаление последней 3Dточки элемента "e"
public void deletelast3D(Swarm e, boolean check) {
	int size = e.getField().size();
    if (size != 0) {
        int t = ((Integer) e.getField().get(size - 1)).intValue();
        e.getField().remove(size - 1);
        if (check) {
            //если нет других ссылок на 3Dточку с номером "t" , то удаление; 
            if (!reference(t))
                deletePoint3D(t);
        }
        else
            deletePoint3D(t);
    }
}
   private void deletePoint3D(int t)          //удаление 3Dточки с номером "t"
	 {
	   points3D.removeElementAt(t);
	   for (int i=0; i<figures.size(); i++)
		 trim3D(t, (Figure)figures.elementAt(i));
	 }
/**
 * Insert the method's description here.
 * Creation date: (23.10.2003 14:32:39)
 * @return java.util.Vector
 */
public java.util.Vector getFigures() {
	return figures;
}
/**
 * Insert the method's description here.
 * Creation date: (23.10.2003 14:29:10)
 * @return java.util.Vector
 */
public java.util.Vector getPoints3D() {
	return points3D;
}
   private boolean reference(int t)          //проверка: ссылаетс€ ли одна из сторон на точку номер "t"
	 {
	  for (int i=0; i<figures.size(); i++)  //"i" - текущий номер фигуры
	   {
		 Figure figure = (Figure)figures.elementAt(i);
		 for (int k=0; k<figure.getSwarms().size(); k++)    //"k"- текуща€ сторона в "i"-ой фигуре 
		  {
		   Side side = (Side)figure.getSwarms().elementAt(k);
		   for (int m=0, n=0; m<side.getField().size(); m++)  //"m"- текуща€ 3Dточка в "k"-ой стороне
			{
			  n = ((Integer)side.getField().elementAt(m)).intValue();//"n"- номер "m"-ой 3Dточки
			  if(n==t)                                          
				return true;    //если совпал, то return true;
			}
		  }
	   }                                                        
	  return false;   //если не совпал, то return false
	 }
public void removeFigure(Figure fig) //удаление элемента "e"
{
    fig.deleteContents();
    figures.remove(fig);
}
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 10:56:06)
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
    Point3D zero = (Point3D) points3D.get(z);
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
 * Creation date: (29.10.2003 12:26:50)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param axis Axis3D 
 * @param angle float
 */
public void rotate(Matrix3D matrix, Axis3D axis, float angle) {
    matrix.identity();
    Point3D pA = (Point3D) points3D.get(axis.zero);
    Point3D pB = (Point3D) points3D.get(axis.end);
    // вектор оси вращени€
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
 * Insert the method's description here.
 * Creation date: (24.10.2003 22:19:08)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param axis java.lang.String
 * @param angle float
 */
public void rotate(Matrix3D matrix, String axis, float angle) {
    matrix.identity();
    matrix.rotate(angle, axis, false);
    transform(matrix);
}
/**
 * Insert the method's description here.
 * Creation date: (24.10.2003 22:37:18)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param x float
 * @param y float
 * @param z float
 */
public void scale(Matrix3D matrix, float x, float y, float z) {
    matrix.identity();
    matrix.scale(x, y, z);
    transform(matrix);
}
/**
 * Insert the method's description here.
 * Creation date: (24.10.2003 22:35:27)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 */
public void transform(Matrix3D matrix) {
    for (int i = 0; i < points3D.size(); i++)
        matrix.transform((Point3D) points3D.elementAt(i));
}
/**
 * Insert the method's description here.
 * Creation date: (24.10.2003 22:55:29)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 * @param dx int
 * @param dy int
 * @param dz int
 */
public void translate(Matrix3D matrix, int dx, int dy, int dz) {
    matrix.identity();
    matrix.translate(dx, dy, dz);
    transform(matrix);
}
//упор€дочение элемента "elem" после удалени€ "t"-ой 3Dточки
private void trim3D(int t, Swarm elem) {
    elem.getField().remove(new Integer(t));
    for (int k = 0, n = 0; k < elem.getField().size(); k++) {
        n = ((Integer) elem.getField().get(k)).intValue();
        if (n > t)
            elem.getField().set(k, new Integer(n - 1));
    }
    if (elem instanceof Side)
        //упор€дочение подЁлементов элемента "elem"
        for (int i = 0; i < ((Side) elem).getElements().size(); i++)
            trim3D(t, (Element) ((Side) elem).getElements().get(i));
    else if (elem instanceof Figure)
        //упор€дочение подЁлементов элемента "elem"
        for (int i = 0; i < ((Figure) elem).getSwarms().size(); i++)
            trim3D(t, (Side) ((Figure) elem).getSwarms().get(i));

}
}
