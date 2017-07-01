 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;

 public class Piace extends Panel implements MouseListener, MouseMotionListener { 
  
   Graph3DI graph;                                 //������
   Vector points3D = new Vector();                 //������ ���������� �����(3D).
   Vector figures = new Vector();                  //������ ���� �����.
   Figure figure;                                  //������� ������.
   Point offset;                                   //������ �������.
   Element select;                                 //������� �������.
   Side copy;                                      //������������ ��� ���������� ������� �������(Side).
   Matrix3D matrix = new Matrix3D();               //��� �������� ���� �����3D
   int  tab, x0, y0, kind, step, zero;                   //tab-��� ��������� �� ��������� ����;kind-��� ��������;step-����������� �������� �� ����.(x0,y0)-���� ��������� ���������� �����.
   float turn, turnSum, rotate;                    //turn-���������� ����� ��������,turnSum-���� �������� � "reconstruct()"
   int tab_add = 1;                                //��� ��������� �� ���������.
   boolean full, flag_backface, regimeMove; //new  //"�������������", "���� ������ �������" � "�����" ��������������. ���������,
   boolean net = true;                             //"����" ������ ������,
   Color color = Color.black;                      //�������� ���� ���������.
   Rectangle rect = new Rectangle(0, 0, 8, 8);     //������� �� �������� ���� � ���������� ������� ������ 3D�����.
   transient Image offScreenImage;                           //��� ������� �����������.
   transient Graphics offScreen;                             //��� ������� �����������.

   public Piace(Graph3DI graph)
    {
      this.graph = graph; 
      addMouseMotionListener(this);
      addMouseListener(this);
      figures.addElement(figure = new Figure(this, kind, color, full, net, flag_backface));
      figure.elements.addElement(select = new Side(this, kind, color, full, false));
      setBackground(Color.white);
      rotate = step  = 3;                        
    }

   public void mousePressed(MouseEvent e) 
    {
      int x = e.getX(), y = e.getY();
      Move.mouseDown(this,x,y);  //����� �����������.  
    }

    public void mouseDragged(MouseEvent e)               
    { 
      int x = e.getX(), y = e.getY();
      Move.mouseDrag(this,x,y);  //����� �����������.
    }

   public void mouseReleased(MouseEvent e) 
    { 
      Move.mouseUp(this);        //����� �����������.
    }

   public void mouseMoved(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) {}

   protected void SavePoint3D(int i, int x, int y)  //������ ����� ����� ��������, "i" - ����� �����
    {
      if(select.field.contains(new Integer(i)))  //������ �������� � ������� ��������� �����: 
       {                                         //����� ��������� �������� ������ �������� ������.
        select.add(points3D.size());
        figure.add(points3D.size()); 
        points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(i)));
       }
      else
        select.add(i);
      if(i == points3D.size())   //���� ����� �����, ��:
       {
        points3D.addElement(new Point3D(x - offset.x, y - offset.y, 0));     
        figure.add(i); 
       }
       x0 = x; y0 = y;           //���������� ��������� ��������� ���������� 3D�����.
    }

   int numberPoint(int x, int y)  //���������� ����� ����� ������� � ������������(x,y)
    {                                   
      rect.translate(x - rect.x - 4, y - rect.y - 4);            
      if(select instanceof Side)
        for (int i = points3D.size()-1; i >= 0 ; i--)
         {
          x = Math.round(((Point3D)points3D.elementAt(i)).x) + offset.x;
          y = Math.round(((Point3D)points3D.elementAt(i)).y) + offset.y; 
          if (rect.contains(x, y))                                 
            return i;                                                     
         }
      return points3D.size();                    
    }

/*   private void message()
     {

      for(int i=0; i<points3D.size(); i++)
       {
         Point3D thePoint3D = (Point3D)points3D.elementAt(i); 
         System.out.println("Point3D N "+ i + " : " + thePoint3D);  
       }

      System.out.println("3Dpoints:"+points3D.size()+ "; figures:"+figures.size() + "; sides in figure:"+figure.elements.size()
                         +";Points in figure:"+figure.field.size() + ";select:" + select.field.size()+ "; elements in select:"+select.elements.size()+figure);
     } 
*/                                                                                  
/////////////////////////////////////////////////////���� ������ ������� "new ..." 
   void copy(Element e) //����������� �������� "�"
     {
       Hashtable hash = new Hashtable(); 
       copyElement(e, hash);         
     }
   private void copyElement(Element e, Hashtable hash)  //����������� ����������� �������� "�"
     {
      if(e instanceof Figure) //����������� ������ 
       { 
        figures.addElement(figure = new Figure(this, e.kind, e.color, e.full, figure.net, figure.flag_backface));
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Side)e.elements.elementAt(i), hash);//����������� ������ ������(figure)
       }       	     
      else if(e instanceof Side) //����������� �������
       { 
        figure.elements.addElement(select = new Side(this, e.kind, e.color, e.full, ((Side)e).normal_Left));
        copyPoint3D(e.field, hash);
        copy = (Side)select;    //���������� "select" � "copy"
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Element)e.elements.elementAt(i), hash);//����������� ��������� ������� select
       }       	     
      else if(e instanceof Element) //����������� Element.
       { 
        copy.elements.addElement(select = new Element(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, hash);
       }         
     }
   private void copyPoint3D(Vector vector, Hashtable hash) //����������� 3D����� "vector"
     {
      for (int i=0, t=0, n=0; i<vector.size(); i++)
       {
        t = ((Integer)vector.elementAt(i)).intValue();
        if((select instanceof Side)&&(hash.containsKey(new Integer(t)))) //���� ������������ ����� ������� ������ ��-�� ,��:
          select.add(((Integer)hash.get(new Integer(t))).intValue());   //����� ����� �����, ��������������� ������(t).
        else         //���� 3D����� �� ���������� � ����� Element-e:
         {
          n = points3D.size();
          select.add(n);
          figure.add(n);
          hash.put(new Integer(t), new Integer(n));   //����� ����� �����-n, ������-t
          points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(t)));
         }
       } 
     }  
///////////////////////////////////////////////////////���� �������� ���������:
   void newElement() //�������� ������ �������� �������-select
     {
       if(select instanceof Side)  //���������� �������-select � "copy"
         copy = (Side)select;        
       copy.elements.addElement(select = new Element(this, kind, color, full));
     }
   void newSide()   //�������� ����� ������� ������� ������(figure)
     {
       figure.elements.addElement(select = new Side(this, kind, color, full, false));
     }
   void newFigure()  //�������� ����� ������(figure)
     {
       figures.addElement(figure = new Figure(this, kind, Color.black, full, net, flag_backface));
       figure.elements.addElement(select = new Side(this, kind, color, full, false));
     }
//////////////////////////////////////////////////���� ��������� �� ���������:
   void next_offset()  ////����� ��� �������� 
     {
       next(select.field);    //����� �������� "tab"
       zero = ((Integer)select.field.elementAt(tab)).intValue(); 
       Point3D p = (Point3D)points3D.elementAt(zero);
       offset.translate(Math.round(p.x), Math.round(p.y));   //��������� ��� �������� � ����� "p"
       matrix.identity();
       matrix.translate(-p.x, -p.y, -p.z);                   //��������� ��������� ����(points3D)
       for (int i=0; i<points3D.size(); i++)
         matrix.transform((Point3D)points3D.elementAt(i)); 
     }
   void nextSide()      ////��������� ������� ������
     {
       next(figure.elements);  //����� �������� "tab"
       select = (Side)figure.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   void nextElement()   ////��������� ������� �������-select
     {
       if(select instanceof Side) 
         copy = (Side)select;  //���������� �������(select-Side) � "copy"      
       if(copy.elements.size() == 0)
         return;
       next(copy.elements);   //����� �������� "tab"
       select = (Element)copy.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   void nextFigure()   ////��������� ������.
     {
       next(figures);              //����� �������� "tab"
       figure = (Figure)figures.elementAt(tab);
       if(!(figure.elements.contains(select)))
         select = (Side)figure.elements.elementAt(0);
       graph.SP.setControl();  
     }
   private void next(Vector vector) //��������� "tab" - ������ �������� "vector"
     {                             //� �������� ��� �������.
       tab = Math.min(Math.max(-1, tab + tab_add), vector.size());
       if(tab == vector.size())
         tab = 0;  
       else if(tab == -1)
         tab = vector.size()-1; 
     }
//////////////////////////////////////////////////////���� ������������� ���������:
   void reconstruct(Element elem, String axis)   //��������� ������ ��������, axis-��� ��������.
     { 
       Point3D p;
       int k = select.field.size();  //���-�� ����� ��������
       matrix.identity();
       matrix.rotate(turn, axis, true);
       for (int i=0, t=0; i<k; i++) 
         {        
           t = ((Integer)select.field.elementAt(i)).intValue();
           select.add(points3D.size());
           figure.add(points3D.size());
           points3D.addElement(p = matrix.transform(Point3D.copy((Point3D)points3D.elementAt(t))));  
         }
       turnSum += turn;        //turnSum-�������� ������ �� 360 ��������.          
       turn *= 2.0f;           //turn-���� �������� ������.
     }   
///////////////////////////////////////////////////////���� �������� ���������:
   synchronized void rotate(String axis, Element e, float rotate)  //�������� Element-a "e" ������ ��� "axis"
     {                                                             //�� ���� "rotate"
       matrix.identity();
       matrix.rotate(rotate, axis, false);               
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure)))  //���� 3D����� ��������� "elem" ������� "Side"
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           rotate(axis, elem, rotate);                    //???�������� �� transform(matrix, elem.field);
         }  
     } 
///////////////////////////////////////////////////////���� ����������� ���������:
   synchronized void rotate(Side theSide)     //new : �������� Element-a "e" 
     {                                                           
      matrix.identity();
      if(theSide.numberZero != -1)
       {
        Point3D p = (Point3D)points3D.elementAt(theSide.numberZero);
        matrix.translate(-p.x, -p.y, -p.z);                      //��������� ��������� ����(points3D)
        matrix.rotate(theSide.animX, "X", false);               
        matrix.rotate(theSide.animY, "Y", false);               
        matrix.translate(p.x, p.y, p.z);                         //��������� ��������� ����(points3D)
       }
      else
       {
        matrix.rotate(theSide.animX, "X", false);               
        matrix.rotate(theSide.animY, "Y", false);
       }               
      transform(matrix, theSide.field);
     }    
///////////////////////////////////////////////////////���� ����������� ���������:
   synchronized void translate(Element e, int dx, int dy, int dz)  //����������� "e" �� ���������� "dx", "dy", "dz"
     {
       matrix.identity();
       matrix.translate(dx, dy, dz);  
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //����������� ��������� ������� "e" �� ���������� "dx", "dy", "dz"
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           translate(elem, dx, dy, dz);  ///???transform(matrix, e.field);
         }  
     } 
///////////////////////////////////////////////////////���� ��������������� ���������:
   synchronized void scale( Element e, float x, float y, float z) //��������������� "�" �� ���� X, Y, Z
     {
       matrix.identity();
       matrix.scale(x, y, z);
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //��������������� ��������� ������� "�" �� ���� X, Y, Z
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           scale(elem, x, y, z);  //???transform(matrix, e.field);
         }  
     } 
///////////////////////////////////////////////////////���� "face" ������ ������.
   void face(Side side, boolean alone) //�������� ������� �� ����������� ���������.
     {
       Vector3D nV = side.normal();
       matrix.identity();
       matrix.rotate(Math.atan2(nV.z, nV.x),"Y", true);
       matrix.rotate(-Math.atan2(nV.y, Math.sqrt(nV.x*nV.x + nV.z*nV.z)),"Z", true);
       matrix.rotate(Math.PI/2, "Y", true);
       if(alone)
         {
          transform(matrix, side.field);
          for (int k=0; k<side.elements.size(); k++)
           transform(matrix, ((Element)side.elements.elementAt(k)).field);
         }
       else
          transform(matrix, figure.field);
     } 
///////////////////////////////////////////////////////����� ����:
  private void transform(Matrix3D m, Vector v) //�����, ���������� ����� ��������� ��
     {
       for (int i=0, t=0; i<v.size(); i++)      
        {t = ((Integer)v.elementAt(i)).intValue(); m.transform((Point3D)points3D.elementAt(t));}
     }    
///////////////////////////////////////////////////////���� ��� ��������� ���������:
   public void update(Graphics g)      //� �������������� ������� �����������                    
     {
       if(offScreen == null)  
        {
         offScreenImage = createImage(getSize().width, getSize().height);  
         offScreen = offScreenImage.getGraphics();
        }                    
       offScreen.setColor(getBackground());
       offScreen.fillRect(0, 0, getSize().width, getSize().height);
       paint(offScreen); //�������� ������������ �����������.
       g.drawImage(offScreenImage, 0, 0, this); //� ����� ��� �� �����.
    //   requestFocus();  //������ ������ ����������� �������                                   
     }                                                  
   public void paint(Graphics g)
     { 
      sort(figures);              //����������  �����.
      for (int i=0; i<figures.size(); i++)
       {
        Figure figure = (Figure)figures.elementAt(i);
        sort(figure.elements);                //���������� ������ ������
        figure.draw(g, offset, "view Face");  //��������� ������(figure)
        if(figure.net)            //���������� ������� ������� ����� ������ ������
          {
            g.setColor(Color.black); 
            for(int k=0; k<figure.elements.size(); k++)
              net(g, ((Side)figure.elements.elementAt(k)).field);
          } 
       }       
     if (offset != null)                 //��������� ����������� ������ ������ �������(offset)
       {g.setColor(Color.black); g.fillOval(offset.x - 3, offset.y - 3, 6, 6);} 
     if (select instanceof Side)         //��������� �������-select
       {g.setColor(Color.red); net(g, select.field);}
     else if(select instanceof Element)  //��������� ��������-select
       {g.setColor(Color.green); net(g, copy.field);}
     if (select.field.size() != 0)       //�������� �� ��������� ����� "select"
       {
        g.setColor(Color.red);
        int t = ((Integer)select.field.lastElement()).intValue();
        Point3D p = (Point3D)points3D.elementAt(t); 
        g.drawOval(offset.x + Math.round(p.x) - 3, offset.y + Math.round(p.y) - 3, 6, 6);
        x0 = Math.round(p.x)+offset.x; y0 = Math.round(p.y)+offset.y;       //��������� x0, y0.
       } 
     }
   private void net(Graphics g, Vector vector)  //����� ��� "vector"-a
    { 
     for (int i=0, t=0; i<vector.size(); i++) 
      {
        t = ((Integer)vector.elementAt(i)).intValue();
        Point3D p = (Point3D)points3D.elementAt(t);
        g.drawOval(offset.x + Math.round(p.x) - 5, offset.y + Math.round(p.y) - 5, 10, 10); 
      } 
    }
   private void sort(Vector v)                  //���������� "vector"-a �� ����������� ��� ��������� 
     {
       int j = v.size();
       while(--j > 0)
        { 
         for (int i=0; i<j; i++)
          {
           if (((Side)v.elementAt(i)).weight() < ((Side)v.elementAt(i+1)).weight())
            {     
             Side temp = (Side)v.elementAt(i);
             v.setElementAt((Side)v.elementAt(i+1), i); 
             v.setElementAt(temp, i+1);
            } 
          }
        }
     }
 }    