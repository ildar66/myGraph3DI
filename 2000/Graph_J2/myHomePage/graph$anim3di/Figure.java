 import java.util.Vector;
 import java.awt.*;

 class Figure extends Side {  //������(Figure), ��������� �� ������(Side).
  boolean flag_backface;        // ���� ������ �������� ������� ������ ������.
  boolean net;                  // ����� �� ������ ������ ������.
  boolean isRevolving = true,   // ����� �� ��������� � ��������.
          isTranslation;        // ����� �� ������������� ������������ � ��������.
  float animXmax=360f ,       // maximume ������������� ��������.
        animYmax=360f ;       // maximume ������������� ��������.
  float animXcur, animYcur;     // ������� ��������� ������������� ��������.
  int stepX, stepY, stepZ;      // ���� ��������������� �����������.
  int maxX,  maxY,  maxZ;       // maximume ��������������� �����������.
//////////////////////////////////////////////////////////////////////////////////////////////////////
  public Figure(Piace piace, int kind, Color color, boolean full, boolean net, boolean flag_backface)
   {
	 super(piace, kind, color, full, false);
	 this.net = net;
	 this.flag_backface = flag_backface;
   }
//////////////////////////////////////////////////////////////////////////////////////////////////////
  void draw(Graphics g, Point offset, String view) //��������� ������.
   {
	 for (int i=0; i<elements.size(); i++)
	   {
		 Side side = (Side)elements.elementAt(i);   //���������� ������, ������������� ������.
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