 import java.util.Vector;
 import java.awt.*;

 class Figure extends Side {  //Фигура(Figure), состоящая из сторон(Side).
  boolean flag_backface;        // фпаг показа обратной стороны сторон фигуры.
  boolean net;                  // сетка на точках сторон фигуры.
  boolean isRevolving = true,   // будет ли вращаться в анимации.
          isTranslation;        // будет ли поступательно перемещаться в анимации.
  float animXmax=360f ,       // maximume вращательного движения.
        animYmax=360f ;       // maximume вращательного движения.
  float animXcur, animYcur;     // текущее положение вращательного движения.
  int stepX, stepY, stepZ;      // шаги поступательного перемещения.
  int maxX,  maxY,  maxZ;       // maximume поступательного перемещения.
//////////////////////////////////////////////////////////////////////////////////////////////////////
  public Figure(Piace piace, int kind, Color color, boolean full, boolean net, boolean flag_backface)
   {
	 super(piace, kind, color, full, false);
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
  	if( (this.a != this.b) ) {
          piace.rotate(this, this.a, this.b, this.animY);
          animYcur+=Math.abs(animY);
          if(animYcur>animYmax)
           {
             animY = -animY;
             animYcur = 0.0f;
           }
        } 
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