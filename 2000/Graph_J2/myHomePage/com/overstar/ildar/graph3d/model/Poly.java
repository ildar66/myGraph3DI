package com.overstar.ildar.graph3d.model;

import java.awt.*;
import com.overstar.ildar.graph3d.math.*;
import com.overstar.ildar.graph3d.view.Piace;
/**
 * Insert the type's description here.
 * Creation date: (23.10.2001 12:00:34)
 * @author: Shafigullin Ildar
 */
public class Poly extends Element {
/**
 * Insert the method's description here.
 * Creation date: (23.10.2001 12:02:12)
 */
  public Poly(Piace3D piace3D, Color color, boolean free, boolean full)  {
	super(piace3D, color, free, full);    
  }
/**
 * Insert the method's description here.
 * Creation date: (21.10.2003 12:26:03)
 * @return com.overstar.ildar.graph3d.model.Element
 */
public Swarm copy() {
	Poly copy = new Poly(piace3D, color, free, full);
	copy.field = getCopyField();
	return copy;
}
/**
 * Insert the method's description here.
 * Creation date: (23.10.2001 12:02:59)
 */
  public void draw(Graphics g, Point offset, String view) //рисование тени Элемента относительно (view)взгляда
	{                                              //с точкой отсчета (offset).
	  g.setColor(color);
	  if (full) //рисует заполненнный полигон.
		g.fillPolygon(getShade(offset, view));
	  else
	    g.drawPolygon(getShade(offset, view));  	    	
	}
/**
 * Insert the method's description here.
 * Creation date: (23.10.2001 12:05:45)
 */
/**
 * Insert the method's description here.
 * Creation date: (23.10.2001 11:46:25)
 */
  public Polygon getShade(Point offset, String view)  {  //тень элемента относительно view(взгляда) 
	Polygon poly = new Polygon();   
	for (int i=0, t=0; i<field.size(); i++) 
	  {
		t = ((Integer)field.get(i)).intValue();
		Point3D current = (Point3D)piace3D.getPoints3D().get(t);
		if ((view.equals("view Face")) || (view.equals("shade")))    
		  poly.addPoint(offset.x + Math.round(current.x), offset.y + Math.round(current.y)); 
		else if (view.equals("view Right"))    
		  poly.addPoint(offset.x + Math.round(current.z), offset.y + Math.round(current.y)); 
		else if (view.equals("view Up"))    
		  poly.addPoint(offset.x + Math.round(current.x), offset.y - Math.round(current.z));       
	  }
 	return(poly);
  }
}
