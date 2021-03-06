 import java.util.Vector;
 import java.awt.*;   

 class Element {             //������� �������(Side) ������(Figure). 
  Piace piace;                                     //������ �� Panel Piace.
  boolean full;                                    //����������(full) ������������� ��������. 
  Color color;                                     //����(color) ��������.
  int kind;                                        //���(kind) ��������:{Poly, freePoly, Line, freeLine}.
  Vector field = new Vector();                     //����� �������(number) ���������� ����� �� ������� ������� �������.
  Vector elements = new Vector();                  //������ ���������(Elements) �� ������� ������� ���� �������.
  public static final int Poly = 0, freePoly = 1;  //���������:�������������(Poly),������(freePoly).
  public static final int Line = 2, freeLine = 3;  //����������� :�����(Line), ������(freeLine). 
  protected float animX, animY;          // ���� ��������
  protected int numberZero = -1;

  public Element(Piace piace, int kind, Color color, boolean full)  {
    this.piace = piace;
    this.kind = kind;
    this.color = color;
    this.full = full;
  }

  void add(int number)  {   //��������� � ����(field) ������ �� ���������� ����� � �������(number).                           
    field.addElement(new Integer(number));
  }

  Polygon getShade(Point offset, String view)  {  //���� �������� ������������ view(�������) 
    Polygon poly = new Polygon();
    int offsetx = offset.x;
    int offsety = offset.y;
    if(numberZero != -1) 
     {
       Point3D p = (Point3D)piace.points3D.elementAt(numberZero);
       offsetx += (int)p.x;
       offsety += (int)p.y;
     }  
    for (int i=0, t=0; i<field.size(); i++) 
      {
        t = ((Integer)field.elementAt(i)).intValue();
        Point3D current = (Point3D)piace.points3D.elementAt(t);
        if ((view.equals("view Face")) || (view.equals("shade")))    
          poly.addPoint(offsetx + Math.round(current.x), offsety + Math.round(current.y)); 
        else if (view.equals("view Right"))    
          poly.addPoint(offsetx + Math.round(current.z), offsety + Math.round(current.y)); 
        else if (view.equals("view Up"))    
          poly.addPoint(offsetx + Math.round(current.x), offsety - Math.round(current.z));       
      }
    if ((kind == Line)||(kind == freeLine))        //������� �� field ��� ����������� �����.
      {
        for (int i=field.size()-2,t=0; i>=0; i--) 
         {
          t = ((Integer)field.elementAt(i)).intValue();
          Point3D current = (Point3D)piace.points3D.elementAt(t);
          if (view.equals("view Face"))    
            poly.addPoint(offsetx + Math.round(current.x), offsety + Math.round(current.y)); 
          else if (view.equals("view Right"))    
            poly.addPoint(offsetx + Math.round(current.z), offsety + Math.round(current.y)); 
          else if (view.equals("view Up"))    
            poly.addPoint(offsetx + Math.round(current.x), offsety - Math.round(current.z));
         } 
      } 
    return(poly);
  }

  void draw(Graphics g, Point offset, String view) //��������� ���� �������� ������������ (view)�������
    {                                              //� ������ ������� (offset).
      g.setColor(color);
      g.drawPolygon(getShade(offset, view));  
      if ((full)&&((kind == Poly)||(kind == freePoly)))  //������ ������������ �������.
        g.fillPolygon(getShade(offset, view));
      if ((full)&&((kind == Line)||(kind == freeLine)))  //������ ������� �����.
        {    
         g.drawPolygon(getShade(new Point(offset.x+1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x-1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y+1), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y-1), view));
        }
    }

/*  protected void setSector(float xtheta, float ytheta, int t) {
   animX = xtheta;
   animY = ytheta;
   numberZero = t;
  }  */
 }
///////////////////////////////////////////////////////////////////////////////////////////////////
