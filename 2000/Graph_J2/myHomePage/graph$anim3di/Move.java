 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;
 public class Move {   //движение элементов с помощью мыши.
  private static boolean typeTranslate,
                         isControlDown;
  static int choice = 2;
  static int x0, y0, startX, startY; 
  private static float xtheta, ytheta;  

  static void mouseDown(Piace piace, int x, int y, MouseEvent e)
	{ 
          x0  = startX = x; y0  = startY = y;
	  if(piace.select.field.contains(new Integer(piace.numberPoint(x, y))))  //тень точки принадлежит стороне-select.
             typeTranslate = true;  //установка типа движения(перемещение||вращения)
          if(e.isControlDown())
             isControlDown=true;    //установка режима настройка
      	  if(typeTranslate)       //режим перемещения
	   {
              if(isControlDown)      //настройка 
               {
                if( (choice == 1) && (piace.figure.field.contains(new Integer(piace.numberPoint(x, y)))) )
                  piace.select.setA(piace.numberPoint(x, y));
                else if((choice == 2) && (piace.figure.field.contains(new Integer(piace.numberPoint(x, y)))) ) {
                  piace.figure.setA(piace.numberPoint(x, y));
                } 
                else if(choice == 3) {}
               }
           }
          else                    //режим вращения.
           {
              if(isControlDown) {    //настройка
                 if(choice==1) {
                 }
                 else if(choice==2){
                   piace.figure.animXmax = piace.figure.animYmax = 0.0f;
                 }
                 else if(choice==3){
                 }
              }   
           }
	} 
  static void mouseDrag(Piace piace, int x, int y, MouseEvent e)
	{
	 if(typeTranslate)       //режим перемещения
	  {
              if(isControlDown) {   //настройка 
              }
              else {                //обычный
         	 if(choice==1)
  		   piace.translate(piace.select, x-x0, y-y0, 0); 
  	         else if(choice==2)
  		   piace.translate(piace.figure, x-x0, y-y0, 0);
  	         else if(choice==3)	{
  		   piace.matrix.identity(); 
  		   piace.matrix.translate(x-x0, y-y0, 0);
  		   for (int i=0; i<piace.points3D.size(); i++)
  		     piace.matrix.transform((Point3D)piace.points3D.elementAt(i));
  	         }
              }
	  }
	 else                    //режим вращения.
	  {
	   xtheta = (y - y0) * 360.0f / piace.getSize().width;
	   ytheta = (x0 - x) * 360.0f / piace.getSize().height;
           if(isControlDown) {   //настройка 
               if(choice==1) {
               }
               else if(choice==2){
                if(piace.figure.a != piace.figure.b) {
                  piace.rotate(piace.figure, piace.figure.a, piace.figure.b, ytheta);
                }
                else {  
                  piace.rotate("X", piace.figure, xtheta); 
                  piace.rotate("Y", piace.figure, ytheta);
                }  
                piace.figure.animXmax += Math.abs(xtheta);
                piace.figure.animYmax += Math.abs(ytheta);
               }
               else if(choice==3){
               }
           }
           else {                  //обычный
       	       if(choice==1)
		{
       		 if(piace.select instanceof Side) 
       		  { piace.rotate("X", piace.select, xtheta); piace.rotate("Y", piace.select, ytheta);}
       		 else if(piace.select instanceof Element) 
       		  { piace.rotate("X", piace.copy, xtheta); piace.rotate("Y", piace.copy, ytheta);}
		}
               else if(choice==2)
                {
                 if(piace.figure.a != piace.figure.b) {
                   piace.rotate(piace.figure, piace.figure.a, piace.figure.b, ytheta);
                 }
                 else {  
                   piace.rotate("X", piace.figure, xtheta); 
                   piace.rotate("Y", piace.figure, ytheta);
                 } 
                } 
               else if(choice==3)
       		{
       		 piace.matrix.identity(); 
       		 piace.matrix.rotate(xtheta,"X", false);
       		 piace.matrix.rotate(ytheta,"Y", false);
       		 for (int i=0; i<piace.points3D.size(); i++) 
       		   piace.matrix.transform((Point3D)piace.points3D.elementAt(i));
       		}
           }
	  }
	 x0 = x; y0 = y;
	 piace.repaint(); 
   }         
  static void mouseUp(Piace piace, int x, int y, MouseEvent e)
	{
          if(typeTranslate) {   //режим перемещения
            if(isControlDown)      //настройка ?//if(piace.select.field.contains(new Integer(piace.numberPoint(x, y)))) 
             {
              if( (choice == 1) && (piace.figure.field.contains(new Integer(piace.numberPoint(x, y)))) )
                piace.select.setB(piace.numberPoint(x, y));
              else if( (choice == 2) && (piace.figure.field.contains(new Integer(piace.numberPoint(x, y)))) ) {
                piace.figure.setB(piace.numberPoint(x, y));
// System.out.println("piace.figure.b" + piace.figure.b); //temp
              } 
              else if(choice == 3) {}
             }

          } //System.out.println("piace.select.a: " + piace.select.a + "; piace.select.b: " + piace.select.b);  

          else  {               //режим вращения
            if (choice == 1)
             {
       	      ((Side)piace.select).setAnimAngle(xtheta, ytheta); 
              if(isControlDown)
      	        ((Side)piace.select).numberZero = piace.zero;
             }
            else if(choice == 2)
             {
    	      piace.figure.setAnimAngle(xtheta, ytheta);
              if(isControlDown) {
     	        piace.figure.numberZero = piace.zero;
              }
             }  // System.out.println("anim: " + piace.figure.animXmax + " ; " + piace.figure.animYmax);  
          }
	  typeTranslate = false;
          isControlDown = false;
	  piace.repaint(); 
	}
 static void mouseClicked(Piace piace, int x, int y) {
	  System.out.println("mouseClicked");
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