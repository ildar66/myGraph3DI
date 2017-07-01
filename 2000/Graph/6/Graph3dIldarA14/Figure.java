 import java.util.Vector;
 import java.awt.*;
 class Figure extends Side{  //Фигура, состоящая из сторон(Side) с элементами.
 boolean flag_backface, net; //flag_backface - фпаг показа обратной стороны сторон фигуры 
                             //net- сетка на точках фигуры.
 public Figure(Piace piace, int kind, Color color, boolean full) 
  {
    super(piace, kind, color, full);
  }  
 public void draw(Graphics g, Point offset, String view) //рисование фигуры.
  {
    for (int i=0; i<elements.size(); i++)
      {
        Side side = (Side)elements.elementAt(i);
        if (side.backface(flag_backface))           
          side.draw(g, offset, view);
      }
  } 
}
///////////////////////////////////////////////////////////////////////////////////////////////
