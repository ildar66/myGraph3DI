 import java.awt.event.*;  
 import java.util.*;
 import java.awt.*;

 public class Piace extends Panel implements MouseListener, MouseMotionListener { 
  
   Graph3DI graph;                                 //апплет
   Vector points3D = new Vector();                 //Вектор физических точек(3D).
   Vector figures = new Vector();                  //Вектор всех фигур.
   Figure figure;                                  //Текущая фигура.
   Point offset;                                   //Начало отсчета.
   Element select;                                 //Текущий элемент.
   Side copy;                                      //Используется для сохранения текущей стороны(Side).
   Matrix3D matrix = new Matrix3D();               //Для динамики мира точек3D
   int  tab, x0, y0, kind, step, zero;                   //tab-для навигации по элементам мира;kind-вид элемента;step-перемещение элемента по осям.(x0,y0)-тень последней записанная точка.
   float turn, turnSum, rotate;                    //turn-приращение угола поворота,turnSum-угол поворота в "reconstruct()"
   int tab_add = 1;                                //для навигации по элементам.
   boolean full, flag_backface, regimeMove; //new  //"заполненность", "флаг задней стороны" и "режим" редактирования. Элементов,
   boolean net = true;                             //"сеть" сторон фигуры,
   Color color = Color.black;                      //исходный цвет Элементов.
   Rectangle rect = new Rectangle(0, 0, 8, 8);     //следует за нажатием мыши и определяет частоту записи 3Dточек.
   transient Image offScreenImage;                           //для двойной буферизации.
   transient Graphics offScreen;                             //для двойной буферизации.

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
      Move.mouseDown(this,x,y);  //режим перемещения.  
    }

    public void mouseDragged(MouseEvent e)               
    { 
      int x = e.getX(), y = e.getY();
      Move.mouseDrag(this,x,y);  //режим перемещения.
    }

   public void mouseReleased(MouseEvent e) 
    { 
      Move.mouseUp(this);        //режим перемещения.
    }

   public void mouseMoved(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) {}

   protected void SavePoint3D(int i, int x, int y)  //запись новой точки Элемента, "i" - номер точки
    {
      if(select.field.contains(new Integer(i)))  //против создания в стороне повторных точек: 
       {                                         //чтобы корректно работали методы динамики сторон.
        select.add(points3D.size());
        figure.add(points3D.size()); 
        points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(i)));
       }
      else
        select.add(i);
      if(i == points3D.size())   //если точка новая, то:
       {
        points3D.addElement(new Point3D(x - offset.x, y - offset.y, 0));     
        figure.add(i); 
       }
       x0 = x; y0 = y;           //сохранение координат последней записанной 3Dточки.
    }

   int numberPoint(int x, int y)  //возвращает номер точки стороны с координатами(x,y)
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
/////////////////////////////////////////////////////Блок отмены методов "new ..." 
   void copy(Element e) //копирование элемента "е"
     {
       Hashtable hash = new Hashtable(); 
       copyElement(e, hash);         
     }
   private void copyElement(Element e, Hashtable hash)  //рекурсивное копирование элемента "е"
     {
      if(e instanceof Figure) //копирование фигуры 
       { 
        figures.addElement(figure = new Figure(this, e.kind, e.color, e.full, figure.net, figure.flag_backface));
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Side)e.elements.elementAt(i), hash);//копирование сторон фигуры(figure)
       }       	     
      else if(e instanceof Side) //копирование стороны
       { 
        figure.elements.addElement(select = new Side(this, e.kind, e.color, e.full, ((Side)e).normal_Left));
        copyPoint3D(e.field, hash);
        copy = (Side)select;    //сохранение "select" в "copy"
        for (int i=0; i<e.elements.size(); i++) 
          copyElement((Element)e.elements.elementAt(i), hash);//копирование элементов стороны select
       }       	     
      else if(e instanceof Element) //копирование Element.
       { 
        copy.elements.addElement(select = new Element(this, e.kind, e.color, e.full));
        copyPoint3D(e.field, hash);
       }         
     }
   private void copyPoint3D(Vector vector, Hashtable hash) //копирование 3Dточек "vector"
     {
      for (int i=0, t=0, n=0; i<vector.size(); i++)
       {
        t = ((Integer)vector.elementAt(i)).intValue();
        if((select instanceof Side)&&(hash.containsKey(new Integer(t)))) //если существующая точка стороны нового эл-та ,то:
          select.add(((Integer)hash.get(new Integer(t))).intValue());   //номер новой точки, соответствующей старой(t).
        else         //если 3Dточка не существует в новом Element-e:
         {
          n = points3D.size();
          select.add(n);
          figure.add(n);
          hash.put(new Integer(t), new Integer(n));   //номер новой точки-n, старой-t
          points3D.addElement(Point3D.copy((Point3D)points3D.elementAt(t)));
         }
       } 
     }  
///////////////////////////////////////////////////////Блок создания элементов:
   void newElement() //создание нового элемента стороны-select
     {
       if(select instanceof Side)  //сохранение стороны-select в "copy"
         copy = (Side)select;        
       copy.elements.addElement(select = new Element(this, kind, color, full));
     }
   void newSide()   //создание новой стороны текущей фигуры(figure)
     {
       figure.elements.addElement(select = new Side(this, kind, color, full, false));
     }
   void newFigure()  //создание новой фигуры(figure)
     {
       figures.addElement(figure = new Figure(this, kind, Color.black, full, net, flag_backface));
       figure.elements.addElement(select = new Side(this, kind, color, full, false));
     }
//////////////////////////////////////////////////Блок навигации по элементам:
   void next_offset()  ////смена оси вращения 
     {
       next(select.field);    //смена значения "tab"
       zero = ((Integer)select.field.elementAt(tab)).intValue(); 
       Point3D p = (Point3D)points3D.elementAt(zero);
       offset.translate(Math.round(p.x), Math.round(p.y));   //переводит ось вращения в точку "p"
       matrix.identity();
       matrix.translate(-p.x, -p.y, -p.z);                   //изменение координат мира(points3D)
       for (int i=0; i<points3D.size(); i++)
         matrix.transform((Point3D)points3D.elementAt(i)); 
     }
   void nextSide()      ////следующая сторона фигуры
     {
       next(figure.elements);  //смена значения "tab"
       select = (Side)figure.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   void nextElement()   ////следующий элемент стороны-select
     {
       if(select instanceof Side) 
         copy = (Side)select;  //сохранение стороны(select-Side) в "copy"      
       if(copy.elements.size() == 0)
         return;
       next(copy.elements);   //смена значения "tab"
       select = (Element)copy.elements.elementAt(tab);
       graph.SP.setControl();  
     }
   void nextFigure()   ////следующая фигура.
     {
       next(figures);              //смена значения "tab"
       figure = (Figure)figures.elementAt(tab);
       if(!(figure.elements.contains(select)))
         select = (Side)figure.elements.elementAt(0);
       graph.SP.setControl();  
     }
   private void next(Vector vector) //изменение "tab" - номера элемента "vector"
     {                             //в пределах его размера.
       tab = Math.min(Math.max(-1, tab + tab_add), vector.size());
       if(tab == vector.size())
         tab = 0;  
       else if(tab == -1)
         tab = vector.size()-1; 
     }
//////////////////////////////////////////////////////Блок реконструкции элементов:
   void reconstruct(Element elem, String axis)   //формирует фигуры вращения, axis-ось вращения.
     { 
       Point3D p;
       int k = select.field.size();  //кол-во точек элемента
       matrix.identity();
       matrix.rotate(turn, axis, true);
       for (int i=0, t=0; i<k; i++) 
         {        
           t = ((Integer)select.field.elementAt(i)).intValue();
           select.add(points3D.size());
           figure.add(points3D.size());
           points3D.addElement(p = matrix.transform(Point3D.copy((Point3D)points3D.elementAt(t))));  
         }
       turnSum += turn;        //turnSum-контроль выхода за 360 градусов.          
       turn *= 2.0f;           //turn-угол поворота фигуры.
     }   
///////////////////////////////////////////////////////Блок вращения элементов:
   synchronized void rotate(String axis, Element e, float rotate)  //вращение Element-a "e" вокруг оси "axis"
     {                                                             //на угол "rotate"
       matrix.identity();
       matrix.rotate(rotate, axis, false);               
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure)))  //учет 3Dточек элементов "elem" стороны "Side"
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           rotate(axis, elem, rotate);                    //???поменять на transform(matrix, elem.field);
         }  
     } 
///////////////////////////////////////////////////////Блок перемещения элементов:
   synchronized void rotate(Side theSide)     //new : вращение Element-a "e" 
     {                                                           
      matrix.identity();
      if(theSide.numberZero != -1)
       {
        Point3D p = (Point3D)points3D.elementAt(theSide.numberZero);
        matrix.translate(-p.x, -p.y, -p.z);                      //изменение координат мира(points3D)
        matrix.rotate(theSide.animX, "X", false);               
        matrix.rotate(theSide.animY, "Y", false);               
        matrix.translate(p.x, p.y, p.z);                         //изменение координат мира(points3D)
       }
      else
       {
        matrix.rotate(theSide.animX, "X", false);               
        matrix.rotate(theSide.animY, "Y", false);
       }               
      transform(matrix, theSide.field);
     }    
///////////////////////////////////////////////////////Блок перемещения элементов:
   synchronized void translate(Element e, int dx, int dy, int dz)  //перемещение "e" на расстояние "dx", "dy", "dz"
     {
       matrix.identity();
       matrix.translate(dx, dy, dz);  
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //перемещение элементов стороны "e" на расстояние "dx", "dy", "dz"
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           translate(elem, dx, dy, dz);  ///???transform(matrix, e.field);
         }  
     } 
///////////////////////////////////////////////////////Блок масштабирования элементов:
   synchronized void scale( Element e, float x, float y, float z) //масштабирование "е" по осям X, Y, Z
     {
       matrix.identity();
       matrix.scale(x, y, z);
       transform(matrix, e.field);
       if((e instanceof Side)&&(!(e instanceof Figure))) //масштабирование элементов стороны "е" по осям X, Y, Z
        for (int k=0; k<e.elements.size(); k++)
         {
           Element elem =(Element)e.elements.elementAt(k);
           scale(elem, x, y, z);  //???transform(matrix, e.field);
         }  
     } 
///////////////////////////////////////////////////////Блок "face" сторон фигуры.
   void face(Side side, boolean alone) //положить сторону на поверхность рисования.
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
///////////////////////////////////////////////////////общий Блок:
  private void transform(Matrix3D m, Vector v) //метод, вызываемый после изменения СК
     {
       for (int i=0, t=0; i<v.size(); i++)      
        {t = ((Integer)v.elementAt(i)).intValue(); m.transform((Point3D)points3D.elementAt(t));}
     }    
///////////////////////////////////////////////////////Блок для рисования элементов:
   public void update(Graphics g)      //с использованием двойной буферизации                    
     {
       if(offScreen == null)  
        {
         offScreenImage = createImage(getSize().width, getSize().height);  
         offScreen = offScreenImage.getGraphics();
        }                    
       offScreen.setColor(getBackground());
       offScreen.fillRect(0, 0, getSize().width, getSize().height);
       paint(offScreen); //создание внеэкранного изображения.
       g.drawImage(offScreenImage, 0, 0, this); //и вывод его на экран.
    //   requestFocus();  //мешает работе приоритетов потоков                                   
     }                                                  
   public void paint(Graphics g)
     { 
      sort(figures);              //сортировка  фигур.
      for (int i=0; i<figures.size(); i++)
       {
        Figure figure = (Figure)figures.elementAt(i);
        sort(figure.elements);                //сортировка сторон фигуры
        figure.draw(g, offset, "view Face");  //рисование фигуры(figure)
        if(figure.net)            //прорисовка пустыми овалами точек сторон фигуры
          {
            g.setColor(Color.black); 
            for(int k=0; k<figure.elements.size(); k++)
              net(g, ((Side)figure.elements.elementAt(k)).field);
          } 
       }       
     if (offset != null)                 //выделение заполненным овалом начала отсчета(offset)
       {g.setColor(Color.black); g.fillOval(offset.x - 3, offset.y - 3, 6, 6);} 
     if (select instanceof Side)         //выделение стороны-select
       {g.setColor(Color.red); net(g, select.field);}
     else if(select instanceof Element)  //выделение элемента-select
       {g.setColor(Color.green); net(g, copy.field);}
     if (select.field.size() != 0)       //указание на последнюю точку "select"
       {
        g.setColor(Color.red);
        int t = ((Integer)select.field.lastElement()).intValue();
        Point3D p = (Point3D)points3D.elementAt(t); 
        g.drawOval(offset.x + Math.round(p.x) - 3, offset.y + Math.round(p.y) - 3, 6, 6);
        x0 = Math.round(p.x)+offset.x; y0 = Math.round(p.y)+offset.y;       //коррекция x0, y0.
       } 
     }
   private void net(Graphics g, Vector vector)  //сетка для "vector"-a
    { 
     for (int i=0, t=0; i<vector.size(); i++) 
      {
        t = ((Integer)vector.elementAt(i)).intValue();
        Point3D p = (Point3D)points3D.elementAt(t);
        g.drawOval(offset.x + Math.round(p.x) - 5, offset.y + Math.round(p.y) - 5, 10, 10); 
      } 
    }
   private void sort(Vector v)                  //сортировка "vector"-a по удаленности его элементов 
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