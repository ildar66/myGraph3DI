package com.overstar.ildar.graph3d.view;

import java.awt.event.*;
import java.util.*;
import java.awt.*;
import com.overstar.ildar.graph3d.model.*;
import com.overstar.ildar.graph3d.math.*;

public class Piace extends Panel
    implements MouseListener, MouseMotionListener, KeyListener {

    public Graph3DI graph;                         //апплет.
    private Piace3D piace3D = new Piace3D();       //модель.
    public Figure figure;                          //Текущая фигура.
    public Point offset;                           //Начало отсчета.
    public Element select;                         //Текущий элемент.
    public Side copy;                              //Используется для сохранения текущей стороны(Side).
    public Matrix3D matrix = new Matrix3D();       //Для динамики мира точек3D
    int tab, x0, y0;                               //tab-для навигации по элементам мира;(x0,y0)-тень последней записанная точка.
    public int step, zero;                         //step-перемещение элемента по осям.
    public float turn, turnSum, rotate;            //turn-приращение угола поворота,turnSum-угол поворота в "reconstruct()"
    public int tab_add = 1;                        //для навигации по элементам.
    public FloatPanel FP;                          // панель инструментов.      
    public boolean full, free, flag_backface;      //"заполненность", "флаг задней стороны" 
    public boolean regimeMove = true;              //"режим" редактирования Элементов.
    public boolean net = true;                     //"сеть" сторон фигуры,
    public Color color = Color.black;              //исходный цвет Элементов.
    Rectangle rect = new Rectangle(0, 0, 8, 8);    //следует за нажатием мыши и определяет частоту записи 3Dточек.
    Image offScreenImage;                          //для двойной буферизации.
    Graphics offScreen;                            //для двойной буферизации.
public Piace(Graph3DI graph) {
    this.graph = graph;
    addMouseMotionListener(this);
    addMouseListener(this);
    addKeyListener(this);
    setBackground(Color.white);
    newFigure();
    rotate = step = 3;
    FP = new FloatPanel(this, "view Right :Shell Figure and full selected Side");
}
/////////////////////////////////////////////////////Блок отмены методов "new ..." 
public void copy(Swarm e) //копирование элемента "е"
{
    if (e instanceof Figure) {
        piace3D.addFigure(figure = (Figure) e.copy());
        select = (Element) figure.getSwarms().lastElement();
    }
    else if (e instanceof Side) {
        select = (Element) e.copy();
        figure.addSwarm(select);
    }
    else if (e instanceof Element) {
        select = (Element) e.copy();
        copy.addElement(select);
    }
}
/////////////////////////////////////////////////////////Блок удаления элементов:
public void delete(Swarm e) //удаление элемента "e"
{
    //удаление фигуры"e"
    if (e instanceof Figure) {
        piace3D.removeFigure((Figure) e);
        if (piace3D.getFigures().size() != 0) {
            figure = (Figure) piace3D.getFigures().lastElement();
            select = (Side) figure.getSwarms().lastElement();
        }
        else
            newFigure();
    }
    //удаление стороны"e"
    else if (e instanceof Side) {
        figure.removeSwarm(e);
        if (figure.getSwarms().size() == 0) //переход к предыдущей фигуре
            delete(figure);
        else
            select = (Side) figure.getSwarms().lastElement();
    }
    //удаление элемента"е"
    else if (e instanceof Element) {
        copy.removeElement((Element) e); //установка стороны-select
        select = copy;
    }
}
///////////////////////////////////////////////////////Блок "face" сторон фигуры.
//положить сторону на поверхность рисования.
public void face(Side side, boolean alone) {
    next_offset(); 
    Vector3D nV = side.normal();
    matrix.identity();
    matrix.rotate(Math.atan2(nV.z, nV.x), "Y", true);
    matrix.rotate(-Math.atan2(nV.y, Math.sqrt(nV.x * nV.x + nV.z * nV.z)), "Z", true);
    matrix.rotate(Math.PI / 2, "Y", true);
    if (alone) {
        side.transform(matrix);
    }
    else
        figure.transform(matrix);
}
/**
 * Insert the method's description here.
 * Creation date: (23.10.2003 14:58:07)
 * @return com.overstar.ildar.graph3d.model.Piace3D
 */
public com.overstar.ildar.graph3d.model.Piace3D getPiace3D() {
	return piace3D;
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
		   select.translate(matrix, 0, -Math.abs(step), 0);
	   else if ((key == e.VK_DOWN)&& e.isControlDown())
		   select.translate(matrix, 0, Math.abs(step), 0);
	   else if ((key == e.VK_RIGHT)&& e.isControlDown())
		   select.translate(matrix, Math.abs(step), 0, 0);
	   else if ((key == e.VK_LEFT)&& e.isControlDown())
		   select.translate(matrix, -Math.abs(step), 0, 0); 
	   else if ((key == e.VK_END) && e.isControlDown())
		   select.translate(matrix, 0, 0, Math.abs(step));          
	   else if ((key == e.VK_HOME) && e.isControlDown())
		   select.translate(matrix, 0, 0, -Math.abs(step));       

	   else if ((key == e.VK_UP)&& e.isShiftDown())
		   figure.translate(matrix, 0, -Math.abs(step), 0);
	   else if ((key == e.VK_DOWN)&& e.isShiftDown())
		   figure.translate(matrix, 0, Math.abs(step), 0); 
	   else if ((key == e.VK_RIGHT)&& e.isShiftDown())
		   figure.translate(matrix, Math.abs(step), 0, 0); 
	   else if ((key == e.VK_LEFT)&& e.isShiftDown())
		   figure.translate(matrix, -Math.abs(step), 0, 0); 
	   else if ((key == e.VK_END) && e.isShiftDown())
		   figure.translate(matrix, 0, 0, Math.abs(step));           
	   else if ((key == e.VK_HOME) && e.isShiftDown())
		   figure.translate(matrix, 0, 0, -Math.abs(step));          

	   else if ((key == e.VK_TAB) && !e.isControlDown() && !e.isShiftDown() && (select.getField().size() != 0))  
		  {nextSide();}
	   else if ((key == e.VK_TAB) && e.isControlDown() && (select.getField().size() != 0))  
		  {nextElement();}
	   else if ((key == e.VK_TAB) && e.isShiftDown() && (select.getField().size() != 0))  
		  {nextFigure();}

	   else if (key == e.VK_PAGE_UP)
		 figure.scale(matrix, 1.1f, 1.1f, 1.1f);
	   else if (key == e.VK_PAGE_DOWN)
         figure.scale(matrix, 1f/1.1f, 1f/1.1f, 1f/1.1f);
      
	   else if ((key == e.VK_DELETE) && !e.isControlDown() && !e.isShiftDown() && (piace3D.getPoints3D().size() != 0))         
		 {piace3D.deletelast3D(select, true); graph.SP.setControl();}                         
	   else if ((key == e.VK_DELETE) && e.isControlDown() && (piace3D.getPoints3D().size() != 0))         
		 {delete(select); graph.SP.setControl();}                         
	   else if ((key == e.VK_DELETE) && e.isShiftDown() && (piace3D.getPoints3D().size() != 0))         
		 {delete(figure); graph.SP.setControl();}                        

	   else if ((key == e.VK_ESCAPE)&&(piace3D.getPoints3D().size() != 0)) 
		 {delete(select); graph.SP.setControl();}  

	   else return;
		
	   if(select.getField().size() != 0) 
		 Shade.mouseUp(this);      //коррекция окончания создания тени.
	   repaint();     
	 }
   public void keyReleased(KeyEvent e) {}
   public void keyTyped(KeyEvent e)
	 {
	  char key = e.getKeyChar();

	  if ((key == 'e')&&(select.getField().size() != 0))
		 {newElement();}
	  else if ((key == 's')&&(select.getField().size() != 0))
		 {newSide(); }
	  else if ((key == 'f')&&(select.getField().size() != 0))                                                  	
		 {newFigure(); }

	  else if ((key == 'n')&&(select.getField().size() != 0))
		 {next_offset();}      

	  else if (key == '+')
		 {tab_add = 1; rotate = Math.abs(rotate); step = Math.abs(step);}         
	  else if (key == '-')
		 {tab_add = -1; rotate = - Math.abs(rotate); step = -Math.abs(step);}
	  else if (key == '*')
		 {tab_add = - tab_add; rotate = - rotate; step = - step;}        

	  else if ((key == 'c')&&(select.getField().size() > 1)) 
		 {copy(select);}
	  else if ((key == 'C') && (select.getField().size() > 1)) 
		 {copy(figure);}

	  else if ((key == 'A')&&(turnSum < (float)(1.5*Math.PI))&&(select.getField().size() != 0))
		 {reconstruct(select, "Z");}
	  else if ((key == 'B')&&(turnSum < (float)(1.5*Math.PI))&&(select.getField().size() != 0))
		 {reconstruct(select, "Y");}
	  else if ((key == 'D')&&(turnSum < (float)(1.5*Math.PI))&&(select.getField().size() != 0))
		 {reconstruct(select, "X");}

	  else if ((key == 'L')&&(select instanceof Side)&&(select.getField().size() >= 3))
		 {face((Side)select, false);}
	  else if ((key == 'l')&&(select instanceof Side)&&(select.getField().size() >= 3))
		 {face((Side)select, true);}

	  else if (key == 'x')
		 figure.rotate(matrix, Matrix3D.axisX, rotate);
	  else if (key == 'y') 
		 figure.rotate(matrix, Matrix3D.axisY, rotate);
	  else if (key == 'z')
		 figure.rotate(matrix, Matrix3D.axisZ, rotate);  

	  else if (key == 'X')
		 piace3D.rotate(matrix, Matrix3D.axisX, rotate);
	  else if (key == 'Y')  
		 piace3D.rotate(matrix, Matrix3D.axisY, rotate);
	  else if (key == 'Z') 
		 piace3D.rotate(matrix, Matrix3D.axisZ, rotate);

          //else if (key == 'r') 
          //   rotate(select, select.a, select.b, rotate);  //new

	  else if (key == '1') 
		 select.rotate(matrix, Matrix3D.axisX, rotate);
	  else if (key == '2') 
		 select.rotate(matrix, Matrix3D.axisY, rotate);
	  else if (key == '3') 
		 select.rotate(matrix, Matrix3D.axisZ, rotate);

	  else if ((key == '4') && (step > 0))
		 select.scale(matrix, 1.1f, 1f, 1f); 
	  else if ((key == '4') && (step < 0))
		 select.scale(matrix, 1f/1.1f, 1f, 1f); 
	  else if ((key == '5') && (step > 0))
		 select.scale(matrix, 1f, 1.1f, 1f); 
	  else if ((key == '5') && (step < 0))
		 select.scale(matrix, 1f, 1f/1.1f, 1f); 
	  else if ((key == '6') && (step > 0))
		 select.scale(matrix, 1f, 1f, 1.1f); 
	  else if ((key == '6') && (step < 0))
		 select.scale(matrix, 1f, 1f, 1f/1.1f); 

	  else if ((key == '7') && (step > 0))
		 figure.scale(matrix, 1.1f, 1f, 1f); 
	  else if ((key == '7') && (step < 0))
		 figure.scale(matrix,1f/1.1f, 1f, 1);  
	  else if ((key == '8') && (step > 0))
		 figure.scale(matrix, 1f, 1.1f, 1f); 
	  else if ((key == '8') && (step < 0))
		 figure.scale(matrix, 1f, 1f/1.1f, 1f); 
	  else if ((key == '9') && (step > 0))
		 figure.scale(matrix, 1f, 1f, 1.1f); 
	  else if ((key == '9') && (step < 0))
		 figure.scale(matrix, 1f, 1f, 1f/1.1f); 

	  else if ((key == '0')&&(select instanceof Side))
		 {copy = (Side)select; copy.setNormal_Left(!(copy.isNormal_Left()));} 

	  else if (key == 'm')
		message();
			 
	  else return;  

	  if(select.getField().size() != 0) 
		Shade.mouseUp(this);             //коррекция окончания создания тени.
	  repaint();   
	 }
private void message() {
    int countElements = 0;
    if (select instanceof Side)
        countElements = ((Side) select).getElements().size();
    else
        countElements = ((Side) copy).getElements().size();
    System.out.println(
        "3Dpoints:"
            + piace3D.getPoints3D().size()
            + "; figures:"
            + piace3D.getFigures().size()
            + "; sides in figure:"
            + figure.getSwarms().size()
            + ";Points in figure:"
            + figure.getField().size()
            + ";select:"
            + select.getField().size()
            + "; elements in side:"
            + countElements);
}
/*   private void message()// расширенный метод
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
   public void mouseClicked(MouseEvent e) {
    int x = e.getX(), y = e.getY();
    if(regimeMove)    //режим перемещения.
      Move.mouseClicked(this, x, y);
   }
	public void mouseDragged(MouseEvent e)               
	{ 
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)                 //режим рисования
	    Pencil.mouseDrag(this, x, y);
	  else
	    Move.mouseDrag(this,x,y,e);   //режим перемещения.
	}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mouseMoved(MouseEvent e) {}
   public void mousePressed(MouseEvent e) 
	{
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)      //режим рисования
	    Pencil.mouseDown(this, x, y) ;
	  else
	    Move.mouseDown(this,x,y,e);  //режим перемещения.  
	}
   public void mouseReleased(MouseEvent e) 
	{ 
	  int x = e.getX(), y = e.getY();
	  if(!regimeMove)    //режим рисования.
	    Pencil.mouseUp(this, x, y);
	  else
	    Move.mouseUp(this,x,y,e);        //режим перемещения.
	}
///////////////////////////////////////////////////////Блок создания элементов:
public void newElement() //создание нового элемента стороны-select
{
    if (select instanceof Side) //сохранение стороны-select в "copy"
        copy = (Side) select;
    if (graph.SP.kind.getSelectedIndex() == 0)
        select = new Poly(piace3D, color, free, full);
    else if (graph.SP.kind.getSelectedIndex() == 1)
        select = new Line(piace3D, color, free, full);
    copy.getElements().add(select);
    // message();
}
public void newFigure() //создание новой фигуры(figure)
{
    piace3D.addFigure(figure = new Figure(piace3D, net, flag_backface));
    figure.addSwarm(select = new Side(piace3D, color, free, full, false));
    //message();
}
public void newSide() //создание новой стороны текущей фигуры(figure)
{
    figure.addSwarm(select = new Side(piace3D, color, free, full, false));
    //message();
}
   private void next(Vector vector) //изменение "tab" - номера элемента "vector"
	 {                             //в пределах его размера.
	   tab = Math.min(Math.max(-1, tab + tab_add), vector.size());
	   if(tab == vector.size())
		 tab = 0;  
	   else if(tab == -1)
		 tab = vector.size()-1; 
	 }
//////////////////////////////////////////////////Блок навигации по элементам:
public void next_offset() ////смена оси вращения 
{
    next(select.getField()); //смена значения "tab"
    zero = ((Integer) select.getField().elementAt(tab)).intValue();
    Point3D p = (Point3D) piace3D.getPoints3D().elementAt(zero);
    offset.translate(Math.round(p.x), Math.round(p.y));//переводит ось вращения в точку "p"
    matrix.identity();
    matrix.translate(-p.x, -p.y, -p.z); //изменение координат мира(points3D)
    piace3D.transform(matrix);
}
   public void nextElement()   ////следующий элемент стороны-select
	 {
	   if(select instanceof Side) 
		 copy = (Side)select;  //сохранение стороны(select-Side) в "copy"      
	   if(copy.getElements().size() == 0)
		 return;
	   next(copy.getElements());   //смена значения "tab"
	   select = (Element)copy.getElements().elementAt(tab);
	   graph.SP.setControl();  
	 }
   public void nextFigure()   ////следующая фигура.
	 {
	   next(piace3D.getFigures());               //смена значения "tab"
	   figure = (Figure)piace3D.getFigures().elementAt(tab);
	   if(!(figure.getSwarms().contains(select)))
		 select = (Side)figure.getSwarms().elementAt(0);
	   graph.SP.setControl();  
	 }
   public void nextSide()      ////следующая сторона фигуры
	 {
	   next(figure.getSwarms());  //смена значения "tab"
	   select = (Side)figure.getSwarms().elementAt(tab);
	   graph.SP.setControl();  
	 }
//возвращает номер точки стороны с координатами(x,y) //Swarm.getField.
public int numberPoint(Vector field, int x, int y) {
    rect.translate(x - rect.x - 4, y - rect.y - 4);
    for (int i = 0, t = 0; i < field.size(); i++) {
        t = ((Integer) field.elementAt(i)).intValue();
        Point3D point3D = (Point3D) piace3D.getPoints3D().elementAt(t);
        x = Math.round(point3D.x) + offset.x;
        y = Math.round(point3D.y) + offset.y;
        if (rect.contains(x, y))
            return t;
    }
    return piace3D.getPoints3D().size();
}
public void paint(Graphics g) {
    sort(piace3D.getFigures()); //сортировка  фигур.
    for (int i = 0; i < piace3D.getFigures().size(); i++) {
        Figure figure = (Figure) piace3D.getFigures().elementAt(i);
        sort(figure.getSwarms()); //сортировка сторон фигуры
        figure.draw(g, offset, "view Face"); //рисование фигуры(figure)
        //прорисовка пустыми овалами точек сторон фигуры
        if (figure.isNet()) {
            g.setColor(Color.black);
            figure.net(g, offset);
        }
    }
    //выделение заполненным овалом начала отсчета(offset)
    if (offset != null) {
        g.setColor(Color.black);
        g.fillOval(offset.x - 3, offset.y - 3, 6, 6);
    }
    //выделение стороны-select
    if (select instanceof Side) {
        g.setColor(Color.red);
        select.net(g, offset);
    }
    //выделение елемента-select
    else if (select instanceof Element) {
        g.setColor(Color.green);
        copy.net(g, offset);
    }
    if (select.getField().size() != 0) {
        g.setColor(Color.red);
        int t = ((Integer) select.getField().lastElement()).intValue();
        Point3D p = (Point3D) piace3D.getPoints3D().elementAt(t);
        g.drawOval(offset.x + Math.round(p.x) - 3, offset.y + Math.round(p.y) - 3, 6, 6);
        x0 = Math.round(p.x) + offset.x;
        y0 = Math.round(p.y) + offset.y; //коррекция x0, y0.
    }
}
//////////////////////////////////////////////////////Блок реконструкции элементов:
   public void reconstruct(Element elem, String axis)   //формирует фигуры вращения, axis-ось вращения.
	 { 
	   Point3D p;
	   int k = select.getField().size();  //кол-во точек элемента
	   matrix.identity();
	   matrix.rotate(turn, axis, true);
	   for (int i=0, t=0; i<k; i++) 
		 {        
		   t = ((Integer)select.getField().elementAt(i)).intValue();
		   select.add(piace3D.getPoints3D().size());
		   figure.add(piace3D.getPoints3D().size());
		   piace3D.getPoints3D().addElement(p = matrix.transform(Point3D.copy((Point3D)piace3D.getPoints3D().elementAt(t))));  
		 }
	   turnSum += turn;        //turnSum-контроль выхода за 360 градусов.          
	   turn *= 2.0f;           //turn-угол поворота фигуры.
	 }
//запись новой точки Элемента, "i" - номер точки
public void savePoint3D(int i, int x, int y) {
    if (i == piace3D.getPoints3D().size()) {
        piace3D.getPoints3D().addElement(new Point3D(x - offset.x, y - offset.y, 0));
        if (select instanceof Side)
            figure.add(i);
    }
    select.add(i);

    x0 = x;
    y0 = y; //сохранение координат последней записанной 3Dточки.
}
private void sort(Vector v) //сортировка "field"-a по удаленности его элементов 
{
    int j = v.size();
    while (--j > 0) {
        for (int i = 0; i < j; i++) {
            if (((Swarm) v.elementAt(i)).weight()
                < ((Swarm) v.elementAt(i + 1)).weight()) {
                Swarm temp = (Swarm) v.elementAt(i);
                v.setElementAt((Swarm) v.elementAt(i + 1), i);
                v.setElementAt(temp, i + 1);
            }
        }
    }
}
///////////////////////////////////////////////////////Блок для рисования элементов:
public void update(Graphics g) //с использованием двойной буферизации                    
{
    if (offScreen == null) {
        offScreenImage = createImage(getSize().width, getSize().height);
        offScreen = offScreenImage.getGraphics();
    }
    offScreen.setColor(getBackground());
    offScreen.fillRect(0, 0, getSize().width, getSize().height);
    paint(offScreen); //создание внеэкранного изображения.
    g.drawImage(offScreenImage, 0, 0, this); //и вывод его на экран.
    FP.repaint();
    //в анимации будет мешать приоритетам потоков    
    if (graph.anim == null)
        requestFocus();
}
}
