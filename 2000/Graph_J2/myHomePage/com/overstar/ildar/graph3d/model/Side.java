package com.overstar.ildar.graph3d.model;

/**������� ������(figure).*/
import java.util.*;
import java.awt.*;
import com.overstar.ildar.graph3d.math.*;
import com.overstar.ildar.graph3d.view.Piace;
public class Side extends Poly {
    /**������ ���������(Elements) �� ������� ������� ���� �������.*/
    private Vector elements = new Vector();
    private boolean normal_Left; //���������� �������(normal_Left).
  public Side(Piace3D piace3D, Color color, boolean free, boolean full, boolean normal_Left) 
	{
	  super(piace3D, color, free, full);
	  this.normal_Left = normal_Left;
	}
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 13:36:14)
 */
public void addElement(Element element) {
	elements.add(element);
}
public boolean backface(boolean flag) //����������� ������� ����������� ��������.
{
    if ((field.size() < 3)
        || (!flag)) //���� ���� ����������(flag) ����.��� ������ ��������� normal().
        return true;
    if (normal().z < 0.0f) //���� ������� � ����������� ���������� �� ���.
        return true;
    else //���� ������� � ����������� ���������� �� ���.
        return false;
}
/**
 * Insert the method's description here.
 * Creation date: (21.10.2003 14:15:16)
 * @return com.overstar.ildar.graph3d.model.Element
 */
public Swarm copy() {
    Side copy = new Side(piace3D, color, free, full, normal_Left);
    copy.field = getCopyField();
    for (int i = 0; i < elements.size(); i++){
    	copy.elements.add(((Element)elements.elementAt(i)).copy());
    }
    return copy;
}
/**
 * Insert the method's description here.
 * Creation date: (24.10.2003 10:43:33)
 */
public void deleteContents() {
    while (field.size() != 0)
        piace3D.deletelast3D(this, true);
    while (elements.size() != 0)
        removeElement((Element) elements.lastElement());
}
  public void draw(Graphics g, Point offset, String view) //��������� �������.
	{
	  super.draw(g, offset, view);
	  for (int i=0; i<elements.size(); i++)
		 {
		   Element element = (Element)elements.elementAt(i);        
		   element.draw(g, offset, view);
		 }      
	}
/**
 * //����� ����� �����:n, ������:old.
 * Creation date: (21.10.2003 12:55:36)
 * @return java.util.Vector
 */
public Vector getCopyField() {
    Hashtable hash = new Hashtable();
    Vector newField = new Vector(field.size());
    for (int i = 0, old = 0, n = 0; i < field.size(); i++) {
        old = ((Integer) field.elementAt(i)).intValue();
        //���� ������������ ����� ����� �������,��:
        if (hash.containsKey(new Integer(old)))
            newField.add((Integer) hash.get(new Integer(old)));
        else {
            n = piace3D.getPoints3D().size();
            newField.addElement(new Integer(n));
            hash.put(new Integer(old), new Integer(n));
            Point3D newPoint3D = Point3D.copy((Point3D) piace3D.getPoints3D().elementAt(old));
            piace3D.getPoints3D().addElement(newPoint3D);
        }
    }
    return newField;
}
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 13:35:19)
 * @return java.util.Vector
 */
public java.util.Vector getElements() {
	return elements;
}
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 13:46:59)
 * @return boolean
 */
public boolean isNormal_Left() {
	return normal_Left;
}
  public Vector3D normal()        //���������� ������� ������� � �������(side).
	{ 
	  int a = ((Integer)field.elementAt(0)).intValue();             //����� ������� ����� ��������.
	  int b = ((Integer)field.elementAt(field.size()/2)).intValue();//� ������� ����� ��������.
	  int c = ((Integer)field.elementAt(3*field.size()/4)).intValue();//� ��������� ����� ��������.
	  
	  Point3D pA = (Point3D)piace3D.getPoints3D().elementAt(a);
	  Point3D pB = (Point3D)piace3D.getPoints3D().elementAt(b);
	  Point3D pC = (Point3D)piace3D.getPoints3D().elementAt(c);

	  Vector3D A = new Vector3D(pB.x-pA.x, pB.y-pA.y, pB.z-pA.z); //������ ����������� �� �������� ��-�� � ������.
	  Vector3D B = new Vector3D(pC.x-pA.x, pC.y-pA.y, pC.z-pA.z); //������ ����������� �� ����� ��-�� � ������.

	  if (normal_Left)           //���� ���������� �������������.
		return(A.cross(B));
	  else                       //���� ���������� ��������������.
		return(B.cross(A));
	}
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 13:37:05)
 */
public void removeElement(Element elem) {
	elem.deleteContents();
	elements.remove(elem);
}
/**
 * Insert the method's description here.
 * Creation date: (31.10.2003 13:46:59)
 * @param newNormal_Left boolean
 */
public void setNormal_Left(boolean newNormal_Left) {
	normal_Left = newNormal_Left;
}
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 19:16:50)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 */
public void transform(Matrix3D matrix) {
    //super.transform(matrix);
    //������������� ������ ��������������� ������� "field" �������:
    Vector temp = new Vector();
    for (int i = 0; i < field.size(); i++) {
        Integer cur = (Integer) field.elementAt(i);
        if (!temp.contains(cur))
            matrix.transform((Point3D) piace3D.getPoints3D().elementAt(cur.intValue()));
        temp.add(cur);
    }
    for (int i = 0; i < elements.size(); i++) {
        ((Element) elements.elementAt(i)).transform(matrix);
    }
}
}
