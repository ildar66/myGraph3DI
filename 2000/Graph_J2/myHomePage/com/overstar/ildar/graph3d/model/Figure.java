package com.overstar.ildar.graph3d.model;

//Фигура(Figure), состоящая из сторон(Side).
import java.util.*;
import java.awt.*;
import com.overstar.ildar.graph3d.math.*;
import com.overstar.ildar.graph3d.view.Piace;
public class Figure extends Swarm {
    private Vector swarms = new Vector();
    private boolean flag_backface; // фпаг показа обратной стороны сторон фигуры. 
    private boolean net; // сетка на точках сторон фигуры.

  public Figure(Piace3D piace3D, boolean net, boolean flag_backface)
   {
	 super(piace3D);
	 this.net = net;
	 this.flag_backface = flag_backface;
   }
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 12:10:02)
 * @param newSwarms java.util.Vector
 */
public void addSwarm(Swarm swarm) {
    swarms.add(swarm);
    for (int i = 0; i < swarm.field.size(); i++) {
        Integer n = (Integer)swarm.field.get(i);
        if (!field.contains(n))
            field.addElement(n);
    }
}
/**
 * Insert the method's description here.
 * Creation date: (21.10.2003 21:07:06)
 * @return com.overstar.ildar.graph3d.model.Figure
 */
public Swarm copy() {
    Hashtable hash = new Hashtable();
    Figure copy = new Figure(piace3D, net, flag_backface);
    for (int i = 0; i < swarms.size(); i++) {
        //copy.addSwarm(((Element) swarms.elementAt(i)).copy());
        Side oldSide = (Side) swarms.elementAt(i);
        Side newSide = new Side(oldSide.piace3D, oldSide.color, oldSide.free, oldSide.full, oldSide.isNormal_Left());
        newSide.field = getCopyFieldForSide(oldSide, hash);
        for (int ii = 0; ii < oldSide.getElements().size(); ii++) {
            newSide.getElements().add(((Element) oldSide.getElements().elementAt(ii)).copy());
        }
        copy.addSwarm(newSide);
    }
    return copy;
}
/**
 * Insert the method's description here.
 * Creation date: (24.10.2003 12:00:40)
 */
public void deleteContents() {
    while (swarms.size() != 0)
        removeSwarm((Swarm) swarms.lastElement());
    field.removeAllElements();
}
  public void draw(Graphics g, Point offset, String view) //рисование фигуры.
   {
	 for (int i=0; i<swarms.size(); i++)
	   {
		 Side side = (Side)swarms.elementAt(i);   //прорисовка сторон, принадлежащей фигуре.
		 if (side.backface(flag_backface))           
		   side.draw(g, offset, view);
	   }
   }
/**
 * //номер новой точки:n, старой:old.
 * Creation date: (21.10.2003 12:55:36)
 * @return java.util.Vector
 */
private Vector getCopyFieldForSide(Side side,  Hashtable hash) {
    Vector newField = new Vector(side.field.size());
    for (int i = 0, old = 0, n = 0; i < side.field.size(); i++) {
        old = ((Integer) side.field.elementAt(i)).intValue();
        //если существующая точка новой стороны,то:
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
 * Creation date: (17.10.2003 12:19:21)
 * @return java.util.Vector
 */
public java.util.Vector getSwarms() {
	return swarms;
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 17:30:28)
 * @return boolean
 */
public boolean isFlag_backface() {
	return flag_backface;
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 17:30:28)
 * @return boolean
 */
public boolean isNet() {
	return net;
}
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 12:10:02)
 * @return java.util.Vector
 */
public void removeSwarm(Swarm swarm) {
	swarm.deleteContents();
	swarms.remove(swarm);
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 17:30:28)
 * @param newFlag_backface boolean
 */
public void setFlag_backface(boolean newFlag_backface) {
	flag_backface = newFlag_backface;
}
/**
 * Insert the method's description here.
 * Creation date: (18.10.2003 17:30:28)
 * @param newNet boolean
 */
public void setNet(boolean newNet) {
	net = newNet;
}
/**
 * Insert the method's description here.
 * Creation date: (17.10.2003 14:20:16)
 * @param matrix com.overstar.ildar.graph3d.math.Matrix3D
 */
public void transform(Matrix3D matrix) {
    super.transform(matrix);
    for (int i = 0; i < swarms.size(); i++) {
        //((Swarm) swarms.elementAt(i)).transform(matrix);
        Side side = (Side) swarms.elementAt(i);
        for (int ii = 0; ii < side.getElements().size(); ii++) {
            ((Element) side.getElements().elementAt(ii)).transform(matrix);
        }
    }
}
}
