 import java.awt.*;
    public class Shade {   ////////////////////////////////////отображение теней в режиме рисования.
  static int x_anchor, y_anchor;

  static void mouseDown(Piace piace, int x, int y)  //начало создания тени элемента-select.
	{ 
	  if (piace.select.field.size() == 0)
		{ x_anchor  = x; y_anchor  = y;}
	} 
  static void mouseDrag(Piace piace, int x, int y)  //перемещение тени при рисовании.
	{
	  drawbox(piace, x, y);
	  x_anchor = x;
	  y_anchor = y;
	  drawbox(piace, x, y);
	}
  static void mouseUp(Piace piace)                  //окончание создание тени.
	{
	  int t = ((Integer)piace.select.field.get(piace.select.field.size()-1)).intValue();
	  x_anchor = piace.offset.x + Math.round(((Point3D)piace.points3D.get(t)).x);
	  y_anchor = piace.offset.y + Math.round(((Point3D)piace.points3D.get(t)).y);
	}
  private static void drawbox(Piace piace, int x, int y) 
	{
	 int t = ((Integer)piace.select.field.get(0)).intValue(); //первая точка элемента-select.
	 int x0 = Math.round(((Point3D)piace.points3D.get(t)).x);
	 int y0 = Math.round(((Point3D)piace.points3D.get(t)).y);

		 t = ((Integer)piace.select.field.get(piace.select.field.size()-1)).intValue();  //последняя точка элемента-select.
	 int xp = Math.round(((Point3D)piace.points3D.get(t)).x);
	 int yp = Math.round(((Point3D)piace.points3D.get(t)).y);

	 Graphics g = piace.getGraphics();               
	 g.setColor(piace.color);            
	 if((piace.select.getClass() == Poly.class)&&(piace.select.field.size() == 2))  //для стирания последней линии.
	  g.drawLine(piace.offset.x + x0, piace.offset.y + y0, piace.offset.x + xp, piace.offset.y + yp);

	 if ( (piace.select.getClass() == Line.class)&&(piace.select.free) ) //режим рисования PaintMode 
	  {
		g.drawLine(x, y, x_anchor, y_anchor);    //толстая линия
		if (piace.full)
		 {
		  g.drawLine(x + 1, y, x_anchor + 1, y_anchor);
		  g.drawLine(x, y + 1, x_anchor, y_anchor + 1);
		  g.drawLine(x - 1, y, x_anchor - 1, y_anchor);
		  g.drawLine(x, y - 1, x_anchor, y_anchor - 1);
		 }
	  }  
 
	 else                                //режим рисования XORMode
	  {
		g.setXORMode(Color.white);        
		piace.setForeground(piace.color);
		if ( (piace.select.getClass() == Poly.class)&&(piace.select.free) )//(piace.kind == Element.freePoly)
		  g.drawLine(x, y, x_anchor, y_anchor);
		else 
		  g.drawLine(piace.offset.x + xp, piace.offset.y + yp, x_anchor, y_anchor);
		if( (piace.select.getClass() != Line.class) && (piace.select.field.size() >= 2))//((piace.kind != Element.Line)&&(piace.select.field.size() >= 2))
		  g.drawLine(piace.offset.x + x0, piace.offset.y + y0, x_anchor, y_anchor);
		if ( (piace.select.getClass() == Line.class)&&(piace.select.full) )//((piace.kind == Element.Line)&&(piace.full))    //толстая линия
		  {
		   g.drawLine(piace.offset.x + xp + 1, piace.offset.y + yp, x_anchor + 1, y_anchor);
		   g.drawLine(piace.offset.x + xp, piace.offset.y + yp + 1, x_anchor, y_anchor + 1);
		   g.drawLine(piace.offset.x + xp - 1, piace.offset.y + yp, x_anchor - 1, y_anchor);
		   g.drawLine(piace.offset.x + xp, piace.offset.y + yp - 1, x_anchor, y_anchor - 1);
		  }
		if ( (piace.select.getClass() != Line.class) && (piace.select.full))//((piace.kind != Element.Line)&&(piace.full))    //заполненный полигон.
		 {
		   Polygon poly = piace.select.getShade(piace.offset, "shade");
		   poly.addPoint(x_anchor, y_anchor);
		   g.fillPolygon(poly);
		 }
	  }
	 g.dispose();
	}
}