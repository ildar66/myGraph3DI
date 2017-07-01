 import java.util.Vector;
 import java.awt.*;

 class Figure extends Swarm {       //Фигура(Figure), состоящая из сторон(Side).
  boolean flag_backface = false,        // фпаг показа обратной стороны сторон фигуры.
          net           = false,        // сетка на точках сторон фигуры.
          isRevolving   = true ,        // будет ли вращаться в анимации.
	  isTranslation = false;        // будет ли поступательно перемещаться в анимации.

  float animXmax=360f ,                 // maximume вращательного движения.
	animYmax=360f ,         // maximume вращательного движения.
	animXcur, animYcur;     // текущее положение вращательного движения.

  int stepX, stepY, stepZ;      // шаги поступательного перемещения.
  int maxX,  maxY,  maxZ;       // maximume поступательного перемещения.

  Vector elements = new Vector();
///////////////////////////////////////////////////////////////////////////////////////////////////////
  public Figure(Piace piace, boolean net, boolean flag_backface)
   {
	 super(piace);
	 this.net = net;
	 this.flag_backface = flag_backface;
   }                  

//////////////////////////////////////////////////////////////////////////////////////////////////////
  void draw(Graphics g, Point offset, String view) //рисование фигуры.
   {
	 for (int i=0; i<elements.size(); i++)
	   {
		 Side side = (Side)elements.elementAt(i);   //прорисовка сторон, принадлежащей фигуре.
		 if (side.backface(flag_backface))
		   side.draw(g, offset, view);
	   }
   }            
//////////////////////////////////////////////////////////////////////////////////////////////////////
  void scenario() {

	if(isRevolving)
	 {
  	        if( (this.a != this.b) )
		  piace.rotate(this, this.a, this.b, this.animY);
		else {
		  piace.rotate(this);
		  animXcur+=Math.abs(animX); animYcur+=Math.abs(animY);
		  if((animXcur>animXmax)||(animYcur>animYmax))
		   {
			 animX = -animX; animY = -animY;
			 animXcur = animYcur = 0.0f;
		   }
		}
	 }
	if(isTranslation)
	   piace.translate(this, stepX ,stepY , stepZ);

/*   for(int k=0; k<elements.size(); k++)
	 {
	  	Side theSide = (Side)elements.elementAt(k);
  	if( (theSide.a != 0) && (theSide.b != 0) && (theSide.a != theSide.b) && (theSide.animY != 0) )
		  piace.rotate(theSide, theSide.a, theSide.b, theSide.animY);
	 }
*/
  }        
//////////////////////////////////////////////////////////////////////////////////////////////////////
 }