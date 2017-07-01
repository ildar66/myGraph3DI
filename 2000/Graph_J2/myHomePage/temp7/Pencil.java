
import java.awt.Point;
/**
 * Insert the type's description here.
 * Creation date: (16.10.2001 11:49:21)
 * @author: Shafigullin Ildar
 */
public class Pencil {
/**
 * Insert the method's description here.
 * Creation date: (16.10.2001 11:49:55)
 */
   static void mouseDown(Piace piace, int x, int y) 
	{
		piace.rect.translate(x - piace.rect.x - 4, y - piace.rect.y - 4);  //"rect" ������� �� �������� ����
		Shade.mouseDown(piace, x, y);   //������ �������� ����            
		if (piace.offset == null)            //�������� ����������� ������ ������� ���������(��)
		   piace.offset = new Point(x, y);  
		if(piace.select.field.size() == 0)   //������ ������ ������ ����� ��������-select
		  piace.savePoint3D(piace.numberPoint(x, y) , x, y);                
		piace.repaint(x-5, y-5, 10, 10); 
	}
/**
 * Insert the method's description here.
 * Creation date: (16.10.2001 12:03:20)
 */
	static void mouseDrag(Piace piace, int x, int y)               
	{ 
	   piace.rect.translate(x - piace.rect.x - 4, y - piace.rect.y - 4);  //"rect" ������� �� �������� ����          
	   if ( (piace.select.free)&&(!(piace.rect.contains(piace.x0, piace.y0))) )
		  piace.savePoint3D(piace.numberPoint(x, y) , x, y);                  
	   Shade.mouseDrag(piace, x, y);  //����������� ��������� ����
	}
/**
 * Insert the method's description here.
 * Creation date: (16.10.2001 12:09:29)
 */
   static void mouseUp(Piace piace, int x, int y) 
	{ 
		piace.rect.translate(x - piace.rect.x - 4, y - piace.rect.y - 4);   //"rect" ������� �� �������� ����.                         //
		if(!piace.rect.contains(piace.x0, piace.y0))                        //�������� ���������� ����� �������.
		  piace.savePoint3D(piace.numberPoint(x, y) , x, y);                             
		piace.repaint();
		Shade.mouseUp(piace);          //��������� �������� ����.
	}
}
