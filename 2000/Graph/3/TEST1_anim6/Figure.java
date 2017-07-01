 import java.util.Vector;
 import java.awt.*;

 class Figure extends Side {    //Фигура(Figure), состоящая из сторон(Side).
  boolean flag_backface;                 // флаг показа обратной стороны сторон фигуры. 
  boolean net;                           // сетка на точках сторон фигуры.

  public Figure(Piace piace, int kind, Color color, boolean full, boolean net, boolean flag_backface) 
   {
     super(piace, kind, color, full);
     this.net = net;
     this.flag_backface = flag_backface;
   }  

  void draw(Graphics g, Point offset, String view) // рисование фигуры.
   {
     for (int i=0; i<elements.size(); i++)
       {
         Side side = (Side)elements.elementAt(i);  // прорисовка сторон, принадлежащей фигуре.
         if (side.backface(flag_backface))           
           side.draw(g, offset, view);
       }
   }

  protected void setAnimAngle(float animX, float animY) {
    this.animX = animX;
    this.animY = animY;
  }

  public String toString()  {                //информация о углах анимации.
    return ("animX= " + animX + " ;animY= " + animY + " ;numberZero=" + numberZero);  
  }
 }
///////////////////////////////////////////////////////////////////////////////////////////////
