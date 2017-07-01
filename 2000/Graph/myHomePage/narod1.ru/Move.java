 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;

 public class Move {   //движение элементов с помощью мыши.
  private static boolean typeTranslate;
  static int choice = 2;
  static int x0, y0, startX, startY; 
  private static float xtheta, ytheta;  

  static void mouseDown(Piace piace, int x, int y, MouseEvent e)
	{ 
	  x0  = startX = x; y0  = startY = y;
	  if(piace.select.field.contains(new Integer(piace.numberPoint(x, y))))  //тень точки принадлежит стороне-select.
	   {
            typeTranslate = true;
            if(e.isControlDown())  
              piace.select.setA(piace.numberPoint(x, y));
           }
	} 
  static void mouseDrag(Piace piace, int x, int y, MouseEvent e)
	{
	 if(typeTranslate)       //режим перемещения
	  {
            if(!e.isControlDown()) {
       	      if(choice==1)
		 piace.translate(piace.select, x-x0, y-y0, 0); 
	      else if(choice==2)
		 piace.translate(piace.figure, x-x0, y-y0, 0);
	      else if(choice==3)
		{
		 for (int i=0; i<piace.figures.size(); i++) { 
		   Figure fig = (Figure)piace.figures.elementAt(i);
                   piace.translate(fig, x-x0, y-y0, 0);
                 }
		}
            }
	  }
	 else                 //режим вращения.
	  {
	   xtheta = (y - y0) * (360.0f / piace.getSize().width);
	   ytheta = (x0 - x) * (360.0f / piace.getSize().height);
	   if(choice==1)
		{
       		 if(piace.select instanceof Side) 
       		  { piace.rotate("X", piace.select, xtheta); piace.rotate("Y", piace.select, ytheta);}
       		 else if(piace.select instanceof Element) 
       		  { piace.rotate("X", piace.copy, xtheta); piace.rotate("Y", piace.copy, ytheta);}
		}
	   else if(choice==2)
                {piace.rotate("X", piace.figure, xtheta); piace.rotate("Y", piace.figure, ytheta);}
	   else if(choice==3)
		{
		 for (int i=0; i<piace.figures.size(); i++) { 
		   Figure fig = (Figure)piace.figures.elementAt(i);
                   piace.rotate("X", fig, xtheta); 
                   piace.rotate("Y", fig, ytheta);
                 }
		}
	  }
	 x0 = x; y0 = y;
	 piace.repaint(); 
   }         
  static void mouseUp(Piace piace, int x, int y, MouseEvent e)
	{
          if(typeTranslate) {   //режим перемещения
            if(e.isControlDown()) 
              piace.select.setB(piace.numberPoint(x, y)); 
          } //System.out.println("piace.select.a: " + piace.select.a + "; piace.select.b: " + piace.select.b);  

          else  {               //режим вращения
            if (choice == 1)
             {
       	      ((Side)piace.select).setAnimAngle(xtheta, ytheta); 
              if(e.isControlDown())
      	        ((Side)piace.select).numberZero = piace.zero;
             }
            else if(choice == 2)
             {
    	      piace.figure.setAnimAngle(xtheta, ytheta);
              if(e.isControlDown())
     	        piace.figure.numberZero = piace.zero;
             }  //System.out.println("piace.zero: " + piace.zero + "; numberZeroFigure: " + piace.figure.numberZero + "; numberZeroSelect: " + piace.select.numberZero);  
          }
	  piace.repaint(); 
	  typeTranslate = false;
	}
 static void mouseClicked(Piace piace, int x, int y) {
	  for(Enumeration list = piace.figures.elements(); list.hasMoreElements();) {
	    Figure figure= (Figure)list.nextElement();	
	    for(Enumeration list2 = figure.elements.elements(); list2.hasMoreElements();) {
	      Side side= (Side)list2.nextElement();	  
	      if (side.getShade(piace.offset,"shade").contains(new Point(x, y))) {
	        piace.figure = figure; 
	        piace.select = side;
	        break;
	      } 
	    }  
	  }
	piace.graph.SP.setControl();
	piace.repaint();
 }
}