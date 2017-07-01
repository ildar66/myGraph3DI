 
 import java.awt.*;

 import java.util.*; class Figure extends Swarm {  //Фигура(Figure), состоящая из сторон(Side).
  boolean flag_backface = false,        // фпаг показа обратной стороны сторон фигуры.
		  net           = false;        // сетка на точках сторон фигуры.







//////////////////////////////////////////////////////////////////////////////////////////////////////
  void draw(Graphics g, Point offset, String view) //рисование фигуры.
   {
	 for (int i=0; i<elements.size(); i++)
	   {
		 Side side = (Side)elements.get(i);   //прорисовка сторон, принадлежащей фигуре.
		 if (side.backface(flag_backface))
		   side.draw(g, offset, view);
	   }
   }               
//////////////////////////////////////////////////////////////////////////////////////////////////////
  void scenario() {

	if(isRevolving)
	 {
  	if( (this.a != this.b) )
		  this.rotate(piace.matrix, this.a, this.b, this.animY);
		else {
		  this.rotate(piace.matrix);
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
   ArrayList elements = new ArrayList();//////////////////////////////////////////////////////////////////////////////////////////////////////
  public Figure(Piace piace, boolean net, boolean flag_backface)
   {
	 super(piace);
	 this.net = net;
	 this.flag_backface = flag_backface;
   }                  //////////////////////////////////////////////////////////////////////////////////////////////////////
 }