 public class Move {   //�������� ��������� � ������� ����.
  private static boolean typeTranslate;
  static int choice = 2;
  static int x0, y0, startX, startY; 
  private static float xtheta, ytheta;

  static void mouseDown(Piace piace, int x, int y)
    { 
      x0  = startX = x; y0  = startY = y;
      if(piace.select.field.contains(new Integer(piace.numberPoint(x, y))))  //���� ����� ����������� �������-select.
         typeTranslate = true;
    } 
  static void mouseDrag(Piace piace, int x, int y)
    {
     if(typeTranslate)       //����� �����������
      {
       if(choice==1)
         piace.translate(piace.select, x-x0, y-y0, 0); 
       else if(choice==2)
         piace.translate(piace.figure, x-x0, y-y0, 0);
       else if(choice==3)
        {
         piace.matrix.identity(); 
         piace.matrix.translate(x-x0, y-y0, 0);
         for (int i=0; i<piace.points3D.size(); i++)
           piace.matrix.transform((Point3D)piace.points3D.elementAt(i));
        }
      }
     else                 //����� ��������.
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
         piace.matrix.identity(); 
         piace.matrix.rotate(xtheta,"X", false);
         piace.matrix.rotate(ytheta,"Y", false);
         for (int i=0; i<piace.points3D.size(); i++) 
           piace.matrix.transform((Point3D)piace.points3D.elementAt(i));
        }
      }
     x0 = x; y0 = y;
     piace.repaint(); 
   }
  static void mouseUp(Piace piace)
    {
      piace.repaint(); 
      typeTranslate = false;

      if(piace.graph.anim != null) 
        piace.figure.setAnimAngle(xtheta, ytheta);  
/*      else 	 {
        xtheta = (y0 - startY) * (360.0f / piace.getSize().width);   //��� �������
        ytheta = (startX - x0) * (360.0f / piace.getSize().height);
        int t = ((Integer)piace.select.field.elementAt(piace.tab)).intValue(); 
        piace.select.setSector(xtheta, ytheta, t);  
      } */
    }
}  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////