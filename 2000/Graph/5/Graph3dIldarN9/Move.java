 import java.awt.*;
 public class Move {   //движение элементов с помощью мыши.
  static boolean choiceSelect, regimeMove;
  static int x0, y0;
  public static void mouseDown(Piace piace, int x, int y)
    { 
      x0  = x; y0  = y;
      if(piace.figure.field.contains(new Integer(piace.numberPoint(x, y))))   
         regimeMove = true;
      if(piace.select.field.contains(new Integer(piace.numberPoint(x, y))))
         choiceSelect = true;
    } 
  public static void mouseDrag(Piace piace, int x, int y)
    {
      if(regimeMove)
       {
        if(choiceSelect)
          piace.translate(piace.select, x-x0, y-y0, 0);
        else
          piace.translate(piace.figure, x-x0, y-y0, 0);
       }
      else
       {
        float xtheta = (y - y0) * (360.0f / piace.getSize().width);
        float ytheta = (x0 - x) * (360.0f / piace.getSize().height);
        piace.matrix.identity(); 
        piace.matrix.rotate(xtheta,"X", false);
        piace.matrix.rotate(ytheta,"Y", false);
        for (int i=0; i<piace.points3D.size(); i++) 
          piace.matrix.transform((Point3D)piace.points3D.elementAt(i));
       }
      x0 = x; y0 = y;
      piace.repaint(); 
    }
  public static void mouseUp(Piace piace)
    {
      piace.repaint(); regimeMove = false; choiceSelect = false; 
    }
}  