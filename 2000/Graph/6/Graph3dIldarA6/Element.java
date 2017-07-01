 import java.util.Vector;
 import java.awt.*;

 class Element  {                                //элемент стороны(Side) фигуры(figure). 
 
  Piace piace;                                   //ссылка на апплет.
  boolean full;                                  //показатель(full) заполненности элемента. 
  Color color;                                   //цвет(color) элемента.
  int kind;                                      //тип(kind) элемента:{Poly, freePoly, Line, freeLine}.
  Vector field = new Vector();                   //набор номеров(number) трехмерных точек из которых состоит элемент.
  public static final int Poly = 0, freePoly = 1;//замкнутые:многоугольник(Poly),крива€(freePoly).
  public static final int Line = 2, freeLine = 3;//незамкнутые :лини€(Line), крива€(freeLine). 
  Vector elements = new Vector();                //вектор элементов(Elements) из которых состоит этот Ёлемент.

  public Element(Piace piace, int kind, Color color, boolean full) 
    {
      this.piace = piace;
      this.kind = kind;
      this.color = color;
      this.full = full;
    }

  public void add(int number)   //добавить ссылку на трехмерную точку с номером(number) к элементу(element).
    {
      field.addElement(new Integer(number));
    }

  public Polygon getShade(Point offset, String view)//тень элемента относительно view(взгл€да)
    { 
      Polygon poly = new Polygon();
      if(field.size() == 0)  
         return(poly);    
      int xp, yp, zp, t;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          xp = Math.round(((Point3D)piace.points3D.elementAt(t)).x);
          yp = Math.round(((Point3D)piace.points3D.elementAt(t)).y);
          zp = Math.round(((Point3D)piace.points3D.elementAt(t)).z);
          if ((view.equals("view Face"))||(view.equals("shade")))    
            poly.addPoint(offset.x + xp, offset.y + yp); 
          else if (view.equals("view Right"))    
            poly.addPoint(offset.x + zp, offset.y + yp); 
          else if (view.equals("view Up"))    
            poly.addPoint(offset.x + xp, offset.y - zp);       
        }
      if ((kind == Line)||(kind == freeLine))
        {
          for (int i=field.size()-2; i>=0; i--) 
           {
            t = ((Integer)field.elementAt(i)).intValue();
            xp = Math.round(((Point3D)piace.points3D.elementAt(t)).x);
            yp = Math.round(((Point3D)piace.points3D.elementAt(t)).y); 
            zp = Math.round(((Point3D)piace.points3D.elementAt(t)).z);
            if (view.equals("view Face"))    
              poly.addPoint(offset.x + xp, offset.y + yp); 
            else if (view.equals("view Right"))    
              poly.addPoint(offset.x + zp, offset.y + yp); 
            else if (view.equals("view Up"))    
              poly.addPoint(offset.x + xp, offset.y - zp);
           } 
        } 
      return(poly);
    }

  public void draw(Graphics g, Point offset, String view)//рисование тени Ёлемента относительно (view)взгл€да
    {                                                    //с точкой отсчета (offset).
      g.setColor(color);
      g.drawPolygon(getShade(offset, view));  
      if ((full)&&((kind == Poly)||(kind == freePoly)))//рисует заполненнный полигон.
        g.fillPolygon(getShade(offset, view));
      if ((full)&&((kind == Line)||(kind == freeLine)))//рисует толстую линию.
        {    
         g.drawPolygon(getShade(new Point(offset.x+1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x-1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y+1), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y-1), view));
        }
    } 
 }
///////////////////////////////////////////////////////////////////////////////////////////////////
