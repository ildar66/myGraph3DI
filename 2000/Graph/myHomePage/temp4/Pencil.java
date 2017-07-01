import java.awt.Point;

public class Pencil {
   static void mouseDown(Piace piace, int x, int y) 
	{
		piace.rect.translate(x - piace.rect.x - 4, y - piace.rect.y - 4);  //"rect" следует за нажатием мыши
		Shade.mouseDown(piace, x, y);   //начало создания тени            
		if (piace.offset == null)            //создание отображения центра системы координат(СК)
		   piace.offset = new Point(x, y);  
		if(piace.select.field.size() == 0)   //запись только первой точки элемента-select
		  piace.savePoint3D(piace.numberPoint(x, y) , x, y);                
		piace.repaint(x-5, y-5, 10, 10); 
	}
	static void mouseDrag(Piace piace, int x, int y)               
	{ 
	   piace.rect.translate(x - piace.rect.x - 4, y - piace.rect.y - 4);  //"rect" следует за нажатием мыши          
	   if (((piace.select.kind == Element.freeLine)||(piace.select.kind == Element.freePoly))&&(!(piace.rect.contains(piace.x0, piace.y0))))
		  piace.savePoint3D(piace.numberPoint(x, y) , x, y);                  
	   Shade.mouseDrag(piace, x, y);  //перемещение созданной тени
	}
   static void mouseUp(Piace piace, int x, int y) 
	{ 
		piace.rect.translate(x - piace.rect.x - 4, y - piace.rect.y - 4);   //"rect" следует за нажатием мыши.                         //
		if(!piace.rect.contains(piace.x0, piace.y0))                        //контроль расстояния между точками.
		  piace.savePoint3D(piace.numberPoint(x, y) , x, y);                             
		piace.repaint();
		Shade.mouseUp(piace);          //окончание создания тени.
	}
}
