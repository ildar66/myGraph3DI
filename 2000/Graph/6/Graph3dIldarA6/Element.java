 import java.util.Vector;
 import java.awt.*;

 class Element  {                                //������� �������(Side) ������(figure). 
 
  Piace piace;                                   //������ �� ������.
  boolean full;                                  //����������(full) ������������� ��������. 
  Color color;                                   //����(color) ��������.
  int kind;                                      //���(kind) ��������:{Poly, freePoly, Line, freeLine}.
  Vector field = new Vector();                   //����� �������(number) ���������� ����� �� ������� ������� �������.
  public static final int Poly = 0, freePoly = 1;//���������:�������������(Poly),������(freePoly).
  public static final int Line = 2, freeLine = 3;//����������� :�����(Line), ������(freeLine). 
  Vector elements = new Vector();                //������ ���������(Elements) �� ������� ������� ���� �������.

  public Element(Piace piace, int kind, Color color, boolean full) 
    {
      this.piace = piace;
      this.kind = kind;
      this.color = color;
      this.full = full;
    }

  public void add(int number)   //�������� ������ �� ���������� ����� � �������(number) � ��������(element).
    {
      field.addElement(new Integer(number));
    }

  public Polygon getShade(Point offset, String view)//���� �������� ������������ view(�������)
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

  public void draw(Graphics g, Point offset, String view)//��������� ���� �������� ������������ (view)�������
    {                                                    //� ������ ������� (offset).
      g.setColor(color);
      g.drawPolygon(getShade(offset, view));  
      if ((full)&&((kind == Poly)||(kind == freePoly)))//������ ������������ �������.
        g.fillPolygon(getShade(offset, view));
      if ((full)&&((kind == Line)||(kind == freeLine)))//������ ������� �����.
        {    
         g.drawPolygon(getShade(new Point(offset.x+1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x-1, offset.y), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y+1), view));
         g.drawPolygon(getShade(new Point(offset.x, offset.y-1), view));
        }
    } 
 }
///////////////////////////////////////////////////////////////////////////////////////////////////
