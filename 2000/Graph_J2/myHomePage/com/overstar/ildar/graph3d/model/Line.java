package com.overstar.ildar.graph3d.model;

import java.awt.*;
import com.overstar.ildar.graph3d.math.*;
import com.overstar.ildar.graph3d.view.Piace;
/**
 * Insert the type's description here.
 * Creation date: (23.10.2001 11:36:37)
 * @author: Shafigullin Ildar
 */
public class Line extends Element {
    /**
     * Insert the method's description here.
     * Creation date: (23.10.2001 11:37:14)
     */
    public Line(Piace3D piace3D, Color color, boolean free, boolean full) {
        super(piace3D, color, free, full);
    }
/**
 * Insert the method's description here.
 * Creation date: (21.10.2003 12:15:47)
 */
public Swarm copy() {
	Line copy = new Line(piace3D, color, free, full);
	copy.field = getCopyField();
	return copy;
}
/**
 * Рисование тени Элемента относительно (view)взгляда с точкой отсчета (offset).
 * Creation date: (23.10.2001 11:41:43)
 */
public void draw(Graphics g, Point offset, String view) {
    g.setColor(color);
    g.drawPolygon(getShade(offset, view));
    if (full) //рисует толстую линию.
        {
        g.drawPolygon(getShade(new Point(offset.x + 1, offset.y), view));
        g.drawPolygon(getShade(new Point(offset.x - 1, offset.y), view));
        g.drawPolygon(getShade(new Point(offset.x, offset.y + 1), view));
        g.drawPolygon(getShade(new Point(offset.x, offset.y - 1), view));
    }
}
/**
 * Insert the method's description here.
 * Creation date: (23.10.2001 11:46:25)
 */
public Polygon getShade(
    Point offset,
    String view) { //тень элемента относительно view(взгляда) 
    Polygon poly = new Polygon();
    for (int i = 0, t = 0; i < field.size(); i++) {
        t = ((Integer) field.get(i)).intValue();
        Point3D current = (Point3D) piace3D.getPoints3D().get(t);
        if ((view.equals("view Face")) || (view.equals("shade")))
            poly.addPoint(
                offset.x + Math.round(current.x),
                offset.y + Math.round(current.y));
        else if (view.equals("view Right"))
            poly.addPoint(
                offset.x + Math.round(current.z),
                offset.y + Math.round(current.y));
        else if (view.equals("view Up"))
            poly.addPoint(
                offset.x + Math.round(current.x),
                offset.y - Math.round(current.z));
    }
    //возврат по field для отображения линии.
    for (int i = field.size() - 2, t = 0; i >= 0; i--) {
        t = ((Integer) field.get(i)).intValue();
        Point3D current = (Point3D) piace3D.getPoints3D().get(t);
        if (view.equals("view Face"))
            poly.addPoint(
                offset.x + Math.round(current.x),
                offset.y + Math.round(current.y));
        else if (view.equals("view Right"))
            poly.addPoint(
                offset.x + Math.round(current.z),
                offset.y + Math.round(current.y));
        else if (view.equals("view Up"))
            poly.addPoint(
                offset.x + Math.round(current.x),
                offset.y - Math.round(current.z));
    }
    return (poly);
}
}
