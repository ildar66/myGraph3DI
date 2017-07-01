 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;

 public class Piace extends Panel implements MouseListener, MouseMotionListener, KeyListener { 
  
   Graph3DI graph;                                 //������
   Vector points3D = new Vector();                 //������ ���������� �����(3D).
   Vector figures = new Vector();                  //������ ���� �����.
   Figure figure;                                  //������� ������.
   Point offset;                                   //������ �������.
   Element select;                                 //������� �������.
   Side copy;                                      //������������ ��� ���������� ������� �������(Side).
   Matrix3D matrix = new Matrix3D();               //��� �������� ���� �����3D
   int  tab, x0, y0, kind, step;                   //tab-��� ��������� �� ��������� ����;kind-��� ��������;step-����������� �������� �� ����.(x0,y0)-���� ��������� ���������� �����.
   float turn, turnSum, rotate;                    //turn-���������� ����� ��������,turnSum-���� �������� � "reconstruct()"
   int tab_add = 1;                                //��� ��������� �� ���������.
   floatPanel FP;                                  //red? ������ ������������.      
   boolean full, flag_backface;                    //"�������������" � "���� ������ �������" ���������,
   boolean net = true;                             //"����" ������ ������,
   Color color = Color.black;                      //�������� ���� ���������.
   Rectangle rect = new Rectangle(0, 0, 8, 8);     //������� �� �������� ���� � ���������� ������� ������ 3D�����.
   Image offScreenImage;                           //��� ������� �����������.
   Graphics offScreen;                             //��� ������� �����������.

   public Piace(Graph3DI graph)
    {
      this.graph = graph; 
      addMouseMotionListener(this);
      addMouseListener(this);
      addKeyListener(this);
      figures.addElement(figure = new Figure(this, kind, color, full, net, flag_backface));
      figure.elements.addElement(select = new Side(this, kind, color, full));
      setBackground(Color.white);
      rotate = step  = 3;                        
      FP = new floatPanel(this, "view Right :Shell Figure and full selected Side");  
    }

   public void mousePressed(MouseEvent e) 
    {
      int x = e.getX(), y = e.getY();
      rect.translate(x - rect.x - 4, y - rect.y - 4);  //"rect" ������� �� �������� ����
      Shade.mouseDown(this, x, y);              
      if (offset == null)            //�������� ����������� ������ ������� ���������(��)
         offset = new Point(x, y);  
      if(select.field.size() == 0)   //������ ������ ������ ����� ��������-select
        SavePoint3D(numberPoint(x, y) , x, y);                
      repaint(x-5, y-5, 10, 10);                
    }

    public void mouseDragged(MouseEvent e)               
    { 
      int x = e.getX(), y = e.getY();
      rect.translate(x - rect.x - 4, y - rect.y - 4);  //"rect" ������� �� �������� ����          
      if (((select.kind == Element.freeLine)||(select.kind == Element.freePoly))&&(!(rect.contains(x0, y0))))
        SavePoint3D(numberPoint(x, y) , x, y);                  
      Shade.mouseDrag(this, x, y);             
    }

   public void mouseReleased(MouseEvent e) 
    { 
      int x = e.getX(), y = e.getY();
      rect.translate(x - rect.x - 4, y - rect.y - 4);     //"rect" ������� �� �������� ����                         //
      if(!rect.contains(x0, y0))                          //�������� ���������� ����� �������.
        SavePoint3D(numberPoint(x, y) , x, y);                             
      Rectangle rec = select.getShade(offset, "view Face").getBounds();
      repaint(rec.x-5, rec.y-5, rec.width+10, rec.height+10);
      Shade.mouseUp(this);       
    }

   public void mouseMoved(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) {}

   public void SavePoint3D(int i, int x, int y)  //������ ����� ����� ��������, "i" - ����� �����
    {
      select.add(i);
      if(i == points3D.size())   //���� ����� �����, ��:
       {
        points3D.addElement(new Point3D(x - offset.x, y - offset.y, 0));     
        figure.add(i); 
       }
       x0 = x; y0 = y;           //���������� ��������� ��������� ���������� 3D�����.
    }

   public int numberPoint(int x, int y)  //���������� ����� ����� � ������������(x,y)
    {                                   
      rect.translate(x - rect.x - 4, y - rect.y - 4);            
      if(select instanceof Side)
        for (int i = points3D.size()-1; i >= 0 ; i--)
         {
          x = Math.round(((Point3D)points3D.elementAt(i)).x) + offset.x;
          y = Math.round(((Point3D)points3D.elementAt(i)).y) + offset.y; 
          if (rect.contains(x, y))))                                 
            return i;                                                     
         }
      return points3D.size();                    
    }

   public void message()
     {System.out.println("3Dpoints:"+points3D.size()+ "; figures:"+figures.size() + "; sides in figure:"+figure.elements.size()
                         +";Points in figure:"+figure.field.size() + ";select:" + select.field.size()+ "; elements in select:"+select.elements.size());}
                                                                                  
   public void keyTyped(KeyEvent e)
     {
      char key = e.getKeyChar();

      if ((key == 'e')&&(select.field.size() != 0))
         {newElement();}
      else if ((key == 's')&&(select.field.size() != 0))
         {newSide(); }
      else if ((key == 'f')&&(select.field.size() != 0))                                                  	
         {newFigure(); }

      else if ((key == 'n')&&(select.field.size() != 0))
         {next_offset();}      

      else if (key == '+')
         {tab_add = 1; rotate = Math.abs(rotate); step = Math.abs(step);}         
      else if (key == '-')
         {tab_add = -1; rotate = - Math.abs(rotate); step = -Math.abs(step);}
      else if (key == '*')
         {tab_add = - tab_add; rotate = - rotate; step = - step;}        

      else if ((key == 'c')&&(select.field.size() > 1)) 
         {copy(select);}
      else if ((key == 'C') && (select.field.size() > 1)) 
         {copy(figure);}

      else if ((key == 'A')&&(turnSum < (float)(1.5*Math.PI))&&(select.field.size() != 0))
         {reconstruct(select, "Z");}
      else if ((key == 'B')&&(turnSum < (float)(1.5*Math.PI))&&(select.field.size() != 0))
         {reconstruct(select, "Y");}
      else if ((key == 'D')&&(turnSum < (float)(1.5*Math.PI))&&(select.field.size() != 0))
         {reconstruct(select, "X");}

      else if ((key == 'L')&&(select instanceof Side)&&(select.field.size() != 0))
         {face((Side)select, false);}
      else if ((key == 'l')&&(select instanceof Side)&&(select.field.size() != 0))
         {face((Side)select, true);}

      else if (key == 'x')
         rotate("X", figure);
      else if (key == 'y') 
         rotate("Y", figure);
      else if (key == 'z')
         rotate("Z", figure);  

      else if (key == 'X')
         { matrix.identity(); matrix.rotate(rotate,"X", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
      else if (key == 'Y')  
         { matrix.identity(); matrix.rotate(rotate,"Y", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}
      else if (key == 'Z') 
         { matrix.identity(); matrix.rotate(rotate,"Z", false);for (int i=0; i<points3D.size(); i++) matrix.transform((Point3D)points3D.elementAt(i));}

      else if (key == '1') 
         rotate("X", select);
      else if (key == '2') 
         rotate("Y", select);
      else if (key == '3') 
         rotate("Z", select); 

      else if ((key == '4') && (step > 0))
         {scale(select, 1.1f, 1f, 1f);} 
      else if ((key == '4') && (step < 0))
         {scale(select, 1f/1.1f, 1f, 1f);} 
      else if ((key == '5') && (step > 0))
         {scale(select, 1f, 1.1f, 1f);}
      else if ((key == '5') && (step < 0))
         {scale(select, 1f, 1f/1.1f, 1f);}
      else if ((key == '6') && (step > 0))
         {scale(select, 1f, 1f, 1.1f);}
      else if ((key == '6') && (step < 0))
         {scale(select, 1f, 1f, 1f/1.1f);}

      else if ((key == '7') && (step > 0))
         {scale(figure, 1.1f, 1f, 1f);} 
      else if ((key == '7') && (step < 0))
         {scale(figure, 1f/1.1f, 1f, 1f);} 
      else if ((key == '8') && (step > 0))
         {scale(figure, 1f, 1.1f, 1f);}
      else if ((key == '8') && (step < 0))
         {scale(figure, 1f, 1f/1.1f, 1f);}
      else if ((key == '9') && (step > 0))
         {scale(figure, 1f, 1f, 1.1f);}
      else if ((key == '9') && (step < 0))
         {scale(figure, 1f, 1f, 1f/1.1f);}

      else if ((key == '0')&&(select instanceof Side))
         {copy = (Side)select; copy.normal_Left = !(copy.normal_Left);} 

      else if (key == 'm')
        message();
             
      else return;  

      if(select.field.size() != 0) 
        Shade.mouseUp(this); 
      repaint();   
     }

   public void keyPressed(KeyEvent e) 
     {
       int key = e.getKeyCode();

       if ((key == e.VK_UP) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x, offset.y - Math.abs(step));}
       else if ((key == e.VK_DOWN) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x, offset.y + Math.abs(step));}
       else if ((key == e.VK_LEFT) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x - Math.abs(step), offset.y);}
       else if ((key == e.VK_RIGHT) && !e.isControlDown() && !e.isShiftDown())
           {offset = new Point(offset.x + Math.abs(step), offset.y);}
       
       else if ((key == e.VK_UP)&& e.isControlDown())
           translate(select, 0, -Math.abs(step), 0);
       else if ((key == e.VK_DOWN)&& e.isControlDown())
           translate(select, 0, Math.abs(step), 0);
       else if ((key == e.VK_RIGHT)&& e.isControlDown())
           translate(select, Math.abs(step), 0, 0);
       else if ((key == e.VK_LEFT)&& e.isControlDown())
           translate(select, -Math.abs(step), 0, 0); 
       else if ((key == e.VK_END) && e.isControlDown())
           translate(select, 0, 0, Math.abs(step));          
       else if ((key == e.VK_HOME) && e.isControlDown())
           translate(select, 0, 0, -Math.abs(step));       

       else if ((key == e.VK_UP)&& e.isShiftDown())
           translate(figure, 0, -Math.abs(step), 0);
       else if ((key == e.VK_DOWN)&& e.isShiftDown())
           translate(figure, 0, Math.abs(step), 0); 
       else if ((key == e.VK_RIGHT)&& e.isShiftDown())
           translate(figure, Math.abs(step), 0, 0); 
       else if ((key == e.VK_LEFT)&& e.isShiftDown())
           translate(figure, -Math.abs(step), 0, 0); 
       else if ((key == e.VK_END) && e.isShiftDown())
           translate(figure, 0, 0, Math.abs(step));           
       else if ((key == e.VK_HOME) && e.isShiftDown())
           translate(figure, 0, 0, -Math.abs(step));          

       else if ((key == e.VK_TAB) && !e.isControlDown() && !e.isShiftDown() && (select.field.size() != 0))  
          {nextSide();}
       else if ((key == e.VK_TAB) && e.isControlDown() && (select.field.size() != 0))  
          {nextElement();}
       else if ((key == e.VK_TAB) && e.isShiftDown() && (select.field.size() != 0))  
          {nextFigure();}

       else if (key == e.VK_PAGE_UP)
         {scale(figure, 1.1f, 1.1f, 1.1f);}
       else if (key == e.VK_PAGE_DOWN)
         {scale(figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);}

       else if ((key == e.VK_DELETE) && !e.isControlDown() && !e.isShiftDown() && (points3D.size() != 0))         
         {deletelast3D(select); graph.SP.setControl();}                         
       else if ((key == e.VK_DELETE) && e.isControlDown() && (points3D.size() != 0))         
         {delete(select); graph.SP.setControl();}  // escape();                       
       else if ((key == e.VK_DELETE) && e.isShiftDown() && (points3D.size() != 0))         
         {delete(figure); graph.SP.setControl();}  // escapeFigure();                       

       else if ((key == e.VK_ESCAPE)&&(points3D.size() != 0)) 
         {delete(select); graph.SP.setControl();}  //escape();

       else return;
        
       if(select.field.size() != 0) 
         Shade.mouseUp(this); 
       repaint();     
     }

   public void keyReleased(KeyEvent e) {}
/////////////////////////////////////////////////////////���� �������� ���������:
   public void delete(Element e)   //�������� �������� "e"
     {
       while(e.field.size() != 0)
         deletelast3D(e);
       if(e instanceof Figure)        //�������� ������"e"
        {
         while(e.elements.size() != 0)
           delete((Side)e.elements.lastElement());  //�������� ������ ������"e"
        } 
       if(e instanceof Side)          //�������� �������"e"
        {
         copy = (Side)e;    //���������� "e" � "copy"
         while(e.elements.size() != 0)
           delete((Element)e.elements.lastElement());  //�������� ��������� �������"e"
         figure.elements.removeElement(e); 
         if(figure.elements.size() == 0)               //������� � ���������� ������
          {
           figures.removeElement(figure);
           if(figures.size() != 0)                       
             figure = (Figure)figures.lastElement();
           else 
             newFigure();
          }
         select = (Side)figure.elements.lastElement(); //��������� �������-select
        } 
        else if(e instanceof Element)                  //�������� ��������"�"
        {                                             
         copy.elements.removeElement(e);               //��������� �������-select
         select = copy;
        } 
     }
   public void deletelast3D(Element e)  //�������� ��������� 3D����� �������� "e"
     {
       if(e.field.size() != 0)
        {
         int t = ((Integer)e.field.lastElement()).intValue();
         e.field.removeElementAt(e.field.size()-1);      //new //old : e.field.removeElement((Integer)e.field.lastElement());
         if(!reference(t))   //���� ��� ������ ������ �� 3D����� � ������� "t" , �� ��������;
           deletePoint3D(t);
        }
     }
   public void deletePoint3D(int t)          //�������� 3D����� � ������� "t"
     {
       points3D.removeElementAt(t);
       for (int i=0; i<figures.size(); i++)
         trim3D(t, (Figure)figures.elementAt(i));
     }  
   public void trim3D(int t, Element elem)   //������������ �������� "elem" ����� �������� "t"-�� 3D�����
     {
       elem.field.removeElement(new Integer(t));
       for (int k=0, n=0; k<elem.field.size(); k++)
        {
          n = ((Integer)elem.field.elementAt(k)).intValue();
          if(n>t)
            elem.field.setElementAt(new Integer(n-1), k); 
        }
       for (int i=0; i<elem.elements.size(); i++)   //������������ ������������ �������� "elem"
         trim3D(t, (Element)elem.elements.elementAt(i));
     }    
   public boolean reference(int t)          //��������: ��������� �� ���� �� ������ �� ����� ����� "t"
     {
      for (int i=0; i<figures.size(); i++)  //"i" - ������� ����� ������
       {
         Figure figure = (Figure)figures.elementAt(i);
         for (int k=0; k<figure.elements.size(); k++)    //"k"- ������� ������� � "i"-�� ������ 
          {
           Side side = (Side)figure.elements.elementAt(k);
           for (int m=0, n=0; m<side.field.size(); m++)  //"m"- ������� 3D����� � "k"-�� �������
            {
              n = ((Integer)side.field.elementAt(m)).intValue();//"n"- ����� "m"-�� 3D�����
              if(n==t)                                          
                return true;    //���� ������, �� return true;
            }
          }
       }                                                        
      return false;   //���� �� ������, �� return false
     }
/////////////////////////////////////////////////////���� ������ ������� "new ..." 
   public void copy(Element e) //����������� �������� "�"
     {
       Hashtable hash = new Hashtable(); 
       copyElement(e, hash);         
     }
   public void copyElement(Element e, Hashtable hash)  //����������� ����������� �������� "�"
     {
      if(e instanceof Figure) //����������� ������ 
       { 
        figures.addElement(figure = new Figure(this, e.kind, e.color, e.full, figure.net, figure.flag_backface));
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Side)e.elements.elementAt(i), hash);//����������� ������ ������(figure)
       }       	     
      else if(e instanceof Side) //����������� �������
       { 
        figure.elements.addElement(select = new Side(this, e.kind, e.color, e.full));
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
   public void copyPoint3D(Vector vector, Hashtable hash) //����������� 3D����� "vector"
     {
      for (int i=0, t=0, n=0; i<vector.size(); i++)
       {
        t = ((Integer)vector.elementAt(i)).intValue();
        if((select instanceof Side)&&(hash.containsKey(new Integer(t)))) //���� ������������ ����� ������� ������ ��-�� ,��:
          select.add(((Integer)hash.get(new Integer(t))).intValue());   //����� ����� �����, ��������������� ������(t).
        else   //���� 3D����� �� ���������� � ����� Element-e:
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
   public void newElement() //�������� ������ �������� �������-select
     {
       if(select instanceof Side)  //���������� �������-select � "copy"
         copy = (Side)select;        
       copy.elements.addElement(select = new Element(this, kind, color, full));
       message();
     }
   public void newSide()   //�������� ����� ������� ������� ������(figure)
     {
       figure.elements.addElement(select = new Side(this, kind, color, full));
       message();
     }
   public void newFigure()  //�������� ����� ������(figure)
     {
       figures.addElement(figure = new Figure(this, kind, Color.black, full, net, flag_backface));
       figure.elements.addElement(select = new Side(this, kind, color, full));
       message();
     }
//////////////////////////////////////////////////���� ��������� �� ���������:
   public void next_offset()  ////����� ��� �������� 
     {
       next(select.field);    //����� �������� "tab"
       int t = ((Integer)select.field.elementAt(tab)).intValue(); 
       Point3D p = (Point3D)points3D.elementAt(t);
       offset.translate(Math.round(p.x), Math.round(p.y));   //��������� ��� �������� � ����� "p"
       matrix.identity();
       matrix.translate(-p.x, -p.y, -p.z);                   //��������� ��������� ����(points3D)
       for (int i=0; i<points3D.size(); i++)
         matrix.transform((Point3D)points3D.elementAt(i)); 
     }
   public void nextSide()      ////��������� ������� ������
     {
       next(figure.elements);  //����� �������� "tab"
       select = (Side)figure.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   public void nextElement()   ////��������� ������� �������-select
     {
       if(select instanceof Side) 
         copy = (Side)select;  //���������� �������(select-Side) � "copy"      
       if(copy.elements.size() == 0)
         return;
       next(copy.elements);   //����� �������� "tab"
       select = (Element)copy.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   public void nextFigure()   ////��������� ������.
     {
       next(figures);              //����� �������� "tab"
       figure = (Figure)figures.elementAt(tab);
       if(!(figure.elements.contains(select)))
         select = (Side)figure.elements.elementAt(0);
       graph.SP.setControl();  
     }
   public void next(Vector vector) //��������� "tab" - ������ �������� "vector"
     {                             //� �������� ��� �������.
       tab = Math.min(Math.max(-1, tab + tab_add), vector.size());
       if(tab == vector.size())
         tab = 0;  
       else if(tab == -1)
         tab = vector.size()-1; 
     }
//////////////////////////////////////////////////////���� ������������� ���������:
   public void reconstruct(Element elem, String axis)   //��������� ������ ��������, axis-��� ��������.
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
   public void rotate(String axis, Element e)            //�������� Element-a "e" ������ ��� "axis"
     {                                                   //�� ���� "rotate"
       matrix.identity();
       matrix.rotate(rotate, axis, false);               
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) // ���� 3D����� ��������� "elem" ������� "Side"
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           rotate(axis, elem);
         }  
     } 
///////////////////////////////////////////////////////���� ����������� ���������:
   public void translate(Element e, int dx, int dy, int dz)  //����������� "e" �� ���������� "dx", "dy", "dz"
     {
       matrix.identity();
       matrix.translate(dx, dy, dz);  
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //����������� ��������� ������� "e" �� ���������� "dx", "dy", "dz"
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           translate(elem, dx, dy, dz);  
         }  
     } 
///////////////////////////////////////////////////////���� ��������������� ���������:
   public void scale( Element e, float x, float y, float z) //��������������� "�" �� ���� X, Y, Z
     {
       matrix.identity();
       matrix.scale(x, y, z);
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //��������������� ��������� ������� "�" �� ���� X, Y, Z
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           scale(elem, x, y, z);
         }  
     } 
///////////////////////////////////////////////////////���� "face" ������ ������.
   public void face(Side side, boolean alone) //�������� ������� �� ����������� ���������.
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
///////////////////////////////////////////////////////����� ����:
   public void transform(Matrix3D m, Vector v) //�����, ���������� ����� ��������� ��
     {
       for (int i=0, t=0; i<v.size(); i++)   //��� ����� ��������� ����� � ������� 
        { t = ((Integer)v.elementAt(i)).intValue(); m.transform((Point3D)points3D.elementAt(t)); }
     }    
/*   public void transform(Matrix3D m, Vector v) //�����, ���������� ����� ��������� ��
      {                                          //� ������ ��������� ����� � �������. 
       Integer iT;  
       Vector   temp = new Vector(); 
       for (int i=0; i<v.size(); i++)
        {
          iT = (Integer)v.elementAt(i);
          if(!temp.contains(iT))
            m.transform((Point3D)points3D.elementAt(iT.intValue()));
          temp.addElement(iT); 
        }
     }         */
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
       paint(offScreen);
       g.drawImage(offScreenImage, 0, 0, this);
       FP.repaint();
       requestFocus();                                     
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
        x0 = Math.round(p.x)+offset.x; y0 = Math.round(p.y)+offset.y; //new
       } 
     }
   public void net(Graphics g, Vector vector)  //����� ��� "vector"-a
    { 
     for (int i=0, t=0; i<vector.size(); i++) 
      {
        t = ((Integer)vector.elementAt(i)).intValue();
        Point3D p = (Point3D)points3D.elementAt(t);
        g.drawOval(offset.x + Math.round(p.x) - 5, offset.y + Math.round(p.y) - 5, 10, 10); 
      } 
    }
   public void sort(Vector v)                  //���������� "vector"-a �� ����������� ��� ��������� 
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