 import java.util.Vector;
 import java.awt.*;   

	  abstract class Element extends Swarm {             //элемент стороны(Side) фигуры(Figure). 

  boolean full,                                     //показатель(full) заполненности элемента.
		  free;                                     //free
  Color color;                                     //цвет(color) элемента.





                                          

                
						  
                
                

  abstract Polygon getShade(Point offset, String view);  //тень элемента относительно view(взгл€да) 
		                                            

  abstract void draw(Graphics g, Point offset, String view); //рисование тени Ёлемента относительно (view)взгл€да
	                                                        //с точкой отсчета (offset). 
   public Element(Piace piace, Color color, boolean free, boolean full)  {
	super(piace);    
	this.color = color;
	this.free = free;
	this.full = full;
  }        //  int kind;                                //тип(kind) элемента:{Poly, freePoly, Line, freeLine}.
//  public static final int Poly = 0, freePoly = 1,  //замкнутые:многоугольник(Poly),крива€(freePoly).
//						  Line = 2, freeLine = 3;  //незамкнутые :лини€(Line), крива€(freeLine). 
 }