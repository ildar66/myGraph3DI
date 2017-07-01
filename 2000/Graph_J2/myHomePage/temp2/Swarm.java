//package graph$anim3di;

/**
 * Insert the type's description here.
 * Creation date: (08.10.2001 12:34:06)
 * @author: Shafigullin Ildar
 */
 import java.util.*;
 import java.awt.*;   
  
 public class Swarm {
   protected ArrayList field = new ArrayList();      //набор номеров(number) трехмерных точек.
   protected Piace piace;                      //ссылка на панель 3D
 
   protected boolean isRevolving   = true ,    // будет ли вращаться в анимации.
		             isTranslation = false,    // будет ли поступательно перемещаться в анимации.
		             isScale       = false;    // будет ли масштабироваться в анимации.
   protected float animX, animY,               // углы анимации 
				   animXmax=360f ,             // maximume вращательного движения.
		           animYmax=360f ,             // maximume вращательного движения.
		           animXcur, animYcur;         // текущее положение вращательного движения.
   protected int numberZero = -1,             // номер осевой точки вращения
				 a, b,                        // номера точек оси вращения(a, b)
				 stepX, stepY, stepZ,         // шаги поступательного перемещения.
				 maxX,  maxY,  maxZ;          // max-ы поступательного перемещения.
 
/**
 * Swarm constructor comment.
 */
public Swarm(Piace piace) {
  this.piace = piace;
}
/**
 * Insert the method's description here.
 * Creation date: (08.10.2001 12:40:55)
 * @param number int
 */
 protected void add(int number)  {   //добавляет к полю(field) ссылку на трехмерную точку с номером(number).                           
   field.add(new Integer(number));
 }        
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 10:24:07)
 */
   protected void net(Graphics g) //сеть точек роя.
	{ 
/*	 for (int i=0, t=0; i<field.size(); i++) 
	  {
		t = ((Integer)field.get(i)).intValue();
		Point3D p = (Point3D)piace.points3D.get(t);
		g.drawOval(piace.offset.x + Math.round(p.x) - 5, piace.offset.y + Math.round(p.y) - 5, 10, 10); 
	  } */
	 ListIterator litr = field.listIterator();
	 while(litr.hasNext()) {
		int t = ((Integer)litr.next()).intValue();
		Point3D p = (Point3D)piace.points3D.get(t);
		g.drawOval(piace.offset.x + Math.round(p.x) - 5, piace.offset.y + Math.round(p.y) - 5, 10, 10);        	 
	 } 
	}
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 11:24:45)
 */
  protected Vector3D normal(boolean normal_Left, int n1, int n2, int n3)      //вычисление вектора нормали к трем точкам
	{                                                                         //по показателю нормали "normal_Left".
	  int a = ((Integer)field.get(n1)).intValue();    //номер n1 точки в piace.
	  int b = ((Integer)field.get(n2)).intValue();    //№ n2 точки в piace.
	  int c = ((Integer)field.get(n3)).intValue();    //№ n3 точки в piace.
		
	  Point3D pA = (Point3D)piace.points3D.get(a);
	  Point3D pB = (Point3D)piace.points3D.get(b);
	  Point3D pC = (Point3D)piace.points3D.get(c);

	  Vector3D A = new Vector3D(pB.x-pA.x, pB.y-pA.y, pB.z-pA.z);  //вектор проведенный из середины эл-та в начало.
	  Vector3D B = new Vector3D(pC.x-pA.x, pC.y-pA.y, pC.z-pA.z);  //вектор проведенный из конца эл-та в начало.

	  if (normal_Left)           //если показатель левосторонний.
		return(A.cross(B));
	  else                       //если показатель правосторонний.
		return(B.cross(A));
	}
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:41:05)
 */
   synchronized protected void rotate(Matrix3D matrix)     //new : вращение Element-a "e" 
	 {                                                           
	  matrix.identity();
	  if(numberZero != -1)
	   {
		Point3D p = (Point3D)piace.points3D.get(numberZero);
		matrix.translate(-p.x, -p.y, -p.z);                      //изменение координат мира(points3D)
		matrix.rotate(animX, "X", false);               
		matrix.rotate(animY, "Y", false);               
		matrix.translate(p.x, p.y, p.z);                         //изменение координат мира(points3D)
	   }
	  else
	   {
		matrix.rotate(animX, "X", false);               
		matrix.rotate(animY, "Y", false);
	   }               
	  transform(matrix);
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:56:15)
 */
   synchronized protected void rotate(Matrix3D matrix, int a, int b, float rotate)  //new : вращение Element-a "e" вокруг оси(a,b)
	 {                                                                 //на угол=rotate
	  if ((a!=b) && (Math.max(a,b)< piace.points3D.size())) 
	   {
		float animX, animY, animZ;
		matrix.identity();
		Point3D pA = (Point3D)piace.points3D.get(a);
		Point3D pB = (Point3D)piace.points3D.get(b);
		Vector3D axis = new Vector3D(pB.x-pA.x, pB.y-pA.y, pB.z-pA.z); // вектор оси вращения
		animX = axis.x* rotate/axis.mag() ; 
		animY = axis.y* rotate/axis.mag() ; 
		animZ = axis.z* rotate/axis.mag() ; 
		matrix.translate(-pA.x, -pA.y, -pA.z);                      //изменение координат мира(points3D)
		matrix.rotate(animX, "X", false);               
		matrix.rotate(animY, "Y", false);               
		matrix.rotate(animZ, "Z", false);               
		matrix.translate(pA.x, pA.y, pA.z);                      //изменение координат мира(points3D)
		transform(matrix);
	   }
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:49:06)
 */
///////////////////////////////////////////////////////Блок вращения элементов:
   synchronized protected void rotate(Matrix3D matrix, String axis, float rotate) //вращение Element-a "e" вокруг оси координат"axis"
	 {                                                      //на угол "rotate"
	   matrix.identity();
	   matrix.rotate(rotate, axis, false);               
	   transform(matrix);
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:08:18)
 */
///////////////////////////////////////////////////////Блок масштабирования элементов:
   synchronized protected void scale( Matrix3D matrix, float x, float y, float z) //масштабирование "е" по осям X, Y, Z
	 {
	   matrix.identity();
	   matrix.scale(x, y, z);
	   transform(matrix);
	 }
/**
 * Insert the method's description here.
 * Creation date: (09.10.2001 11:36:05)
 */
 protected void setA(int a) {this.a = a;}            
/**
 * Insert the method's description here.
 * Creation date: (09.10.2001 11:40:31)
 */
  protected void setAnimAngle(float animX, float animY) { //углы анимации
	this.animX = animX;
	this.animY = animY;
  }                  
/**
 * Insert the method's description here.
 * Creation date: (09.10.2001 11:36:39)
 */
 protected void setB(int b) {this.b = b;}      
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:17:50)
 */
///////////////////////////////////////////////////////общий Блок:
  protected synchronized void transform(Matrix3D matrix) //метод, вызываемый после изменения СК
	 {
/*	   for (int i=0, t=0; i<field.size(); i++)      
		{
		  t = ((Integer)field.get(i)).intValue();
		  matrix.transform((Point3D)piace.points3D.get(t));
		}*/
	   ListIterator litr = field.listIterator();
	   while (litr.hasNext()) {
		  int t = ((Integer)litr.next()).intValue();
		  matrix.transform((Point3D)piace.points3D.get(t));
	   }	
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:29:58)
 */
///////////////////////////////////////////////////////Блок перемещения элементов:
   synchronized protected void translate(Matrix3D matrix, int dx, int dy, int dz)  //перемещение "e" на расстояние "dx", "dy", "dz"
	 {
	   matrix.identity();
	   matrix.translate(dx, dy, dz);  
	   transform(matrix);
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 12:26:27)
 */
  protected float weight()                //используется для сортировки очереди рисования сторон.
	{
	  if(field.size() == 0)  
		return(0f);    
	  float sumZ = 0f;
	  for (int i=0, t=0; i<field.size(); i++) 
		{
		  t = ((Integer)field.get(i)).intValue();
		  sumZ += ((Point3D)piace.points3D.get(t)).z; //подумать над sum**2;
		}
	  return(sumZ / field.size());     
	}
}  
