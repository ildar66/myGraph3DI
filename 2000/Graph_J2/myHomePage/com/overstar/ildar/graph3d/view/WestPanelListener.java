package com.overstar.ildar.graph3d.view;

import com.overstar.ildar.graph3d.model.*;
import com.overstar.ildar.graph3d.math.*;
import java.awt.event.*;
import java.awt.*;
/**
* Insert the type's description here.
* Creation date: (16.10.2003 13:27:49)
* @author: Shafigullin Ildar
*/
public class WestPanelListener implements java.awt.event.ActionListener {
    Piace piace;
/**
 * WestPanelListener constructor comment.
 */
public WestPanelListener(Piace piace) {
    this.piace = piace;
}
	 public void actionPerformed(ActionEvent ev) {
	   String obj = ev.getActionCommand();

	   if (obj.equals("'x'"))
		  piace.figure.rotate(piace.matrix, "X", piace.rotate);
	   else if (obj.equals("'y'")) 
		   piace.figure.rotate(piace.matrix, "Y", piace.rotate);
	   else if (obj.equals("'z'"))
		   piace.figure.rotate(piace.matrix, "Z", piace.rotate);

	   else if (obj.equals("'X'"))
		  piace.getPiace3D().rotate(piace.matrix, Matrix3D.axisX, piace.rotate);
	   else if (obj.equals("'Y'"))  
		  piace.getPiace3D().rotate(piace.matrix, Matrix3D.axisY, piace.rotate);
	   else if (obj.equals("'Z'")) 
		  piace.getPiace3D().rotate(piace.matrix, Matrix3D.axisZ, piace.rotate);

	   else if (obj.equals("'1'")) 
		   piace.select.rotate(piace.matrix, "X", piace.rotate);
	   else if (obj.equals("'2'")) 
		  piace.select.rotate(piace.matrix, "Y", piace.rotate);
	   else if (obj.equals("'3'")) 
		  piace.select.rotate(piace.matrix, "Z", piace.rotate); 

	   else if (obj.equals("'+'"))
		  {piace.tab_add = 1; piace.rotate = Math.abs(piace.rotate); piace.step = Math.abs(piace.step);}         
	   else if (obj.equals("'-'"))
		  {piace.tab_add = -1; piace.rotate = - Math.abs(piace.rotate); piace.step = -Math.abs(piace.step);}
	   else if (obj.equals("'*'"))
		  {piace.tab_add = - piace.tab_add; piace.rotate = - piace.rotate; piace.step = - piace.step;}        

	   else if (obj.equals("Up"))       
		   {piace.offset = new Point(piace.offset.x, piace.offset.y - Math.abs(piace.step));}
	   else if (obj.equals("Down")) 
		   {piace.offset = new Point(piace.offset.x, piace.offset.y + Math.abs(piace.step));}
	   else if (obj.equals("<-")) 
		   {piace.offset = new Point(piace.offset.x - Math.abs(piace.step), piace.offset.y);}
	   else if (obj.equals("->")) 
		   {piace.offset = new Point(piace.offset.x + Math.abs(piace.step), piace.offset.y);}
	   
	   else if (obj.equals("Up "))
		   piace.select.translate(piace.matrix, 0, -Math.abs(piace.step), 0);
	   else if (obj.equals("Down "))
		   piace.select.translate(piace.matrix, 0, Math.abs(piace.step), 0);
	   else if (obj.equals("-> "))
		   piace.select.translate(piace.matrix, Math.abs(piace.step), 0, 0);
	   else if (obj.equals("<- "))
		   piace.select.translate(piace.matrix, -Math.abs(piace.step), 0, 0); 
	   else if (obj.equals("End "))
		   piace.select.translate(piace.matrix, 0, 0, Math.abs(piace.step));           
	   else if (obj.equals("Home "))
		   piace.select.translate(piace.matrix, 0, 0, -Math.abs(piace.step));          

	   else if (obj.equals(" Up"))
		   piace.figure.translate(piace.matrix, 0, -Math.abs(piace.step), 0);
	   else if (obj.equals(" Down"))
		   piace.figure.translate(piace.matrix, 0, Math.abs(piace.step), 0); 
	   else if (obj.equals(" ->"))
		   piace.figure.translate(piace.matrix, Math.abs(piace.step), 0, 0); 
	   else if (obj.equals(" <-"))
		   piace.figure.translate(piace.matrix, -Math.abs(piace.step), 0, 0); 
	   else if (obj.equals(" End"))
		   piace.figure.translate(piace.matrix, 0, 0, Math.abs(piace.step));           
	   else if (obj.equals(" Home"))
		   piace.figure.translate(piace.matrix, 0, 0, -Math.abs(piace.step));         

	   else if ((obj.equals("Side(Tab)")) && (piace.select.getField().size() != 0))  
		  {piace.nextSide();}
	   else if ((obj.equals("Element")) && (piace.select.getField().size() != 0))  
		  {piace.nextElement();}

	   else return;  

	   if(piace.select.getField().size() != 0) 
		 Shade.mouseUp(piace); 
	   piace.repaint();        // piace.requestFocus();
	 }
}
