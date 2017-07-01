 import java.util.Vector;
 import java.awt.*;   

 class Element  {             //элемент стороны(Side) фигуры(Figure). 
  Piace piace;                                     //ссылка на Panel Piace.
  boolean full;                                    //показатель(full) заполненности элемента. 
  Color color;                                     //цвет(color) элемента.
  int kind;                                        //тип(kind) элемента:{Poly, freePoly, Line, freeLine}.
  Vector field = new Vector();                     //набор номеров(number) трехмерных точек из которых состоит элемент.
  Vector elements = new Vector();                  //вектор элементов(Elements) из которых состоит этот Ёлемент.
  public static final int Poly = 0, freePoly = 1;  //замкнутые:многоугольник(Poly),крива€(freePoly).
  public static final int Line = 2, freeLine = 3;  //незамкнутые :лини€(Line), крива€(freeLine). 

  public Element(Piace piace, int kind, Color color, boolean full)  {
    this.piace = piace;
    this.kind = kind;
    this.color = color;
    this.full = full;
  }

  void add(int number)  {   //добавл€ет к полю(field) ссылку на трехмерную точку с номером(number).                           
    field.addElement(new Integer(number));
  }

  Polygon getShade(Point offset, String view)  {  //тень элемента относительно view(взгл€да) 
    Polygon poly = new Polygon();   
    for (int i=0, t=0; i<field.size(); i++) 
      {
        t = ((Integer)field.elementAt(i)).intValue();
        Point3D current = (Point3D)piace.points3D.elementAt(t);
        if ((view.equals("view Face")) || (view.equals("shade")))    
          poly.addPoint(offset.x + Math.round(current.x), offset.y + Math.round(current.y)); 
        else if (view.equals("view Right"))    
          poly.addPoint(offset.x + Math.round(current.z), offset.y + Math.round(current.y)); 
        else if (view.equals("view Up"))    
          poly.addPoint(offset.x + Math.round(current.x), offset.y - Math.round(current.z));       
      }
    if ((kind == Line)||(kind == freeLine))        //возврат по field дл€ отображени€ линии.
      {
        for (int i=field.size()-2,t=0; i>=0; i--) 
         {
          t = ((Integer)field.elementAt(i)).intValue();
          Point3D current = (Point3D)piace.points3D.elementAt(t);
          if (view.equals("view Face"))    
            poly.addPoint(offset.x + Math.round(current.x), offset.y + Math.round(current.y)); 
          else if (view.equals("view Right"))    
            poly.addPoint(offset.x + Math.round(current.z), offset.y + Math.round(current.y)); 
          else if (view.equals("view Up"))    
            poly.addPoint(offset.x + Math.round(current.x), offset.y - Math.round(current.z));
         } 
      } 
    return(poly);
  }

  void draw(Graphics g, Point offset, String view) //рисование тени Ёлемента относительно (view)взгл€да
    {                                              //с точкой отсчета (offset).
      g.setColor(color);
      g.drawPolygon(getShade(offset, view));  
      if ((full)&&((kind == Poly)||(kind == freePoly)))  //рисует заполненнный полигон.
        g.fillPolygon(getShade(offset, view));
      if ((full)&&((kind == Line)||(kind == freeLine)))  //рисует толстую линию.
        {    
         g.drawPolygon(getShade(new Point(offset.x+1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x-1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y+1), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y-1), view));
        }
    } 
 }
///////////////////////////////////////////////////////////////////////////////////////////////////
