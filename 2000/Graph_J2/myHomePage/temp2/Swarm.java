//package graph$anim3di;

/**
 * Insert the type's description here.
 * Creation date: (08.10.2001 12:34:06)
 * @author: Shafigullin Ildar
 */
 import java.util.*;
 import java.awt.*;   
  
 public class Swarm {
   protected ArrayList field = new ArrayList();      //����� �������(number) ���������� �����.
   protected Piace piace;                      //������ �� ������ 3D
 
   protected boolean isRevolving   = true ,    // ����� �� ��������� � ��������.
		             isTranslation = false,    // ����� �� ������������� ������������ � ��������.
		             isScale       = false;    // ����� �� ���������������� � ��������.
   protected float animX, animY,               // ���� �������� 
				   animXmax=360f ,             // maximume ������������� ��������.
		           animYmax=360f ,             // maximume ������������� ��������.
		           animXcur, animYcur;         // ������� ��������� ������������� ��������.
   protected int numberZero = -1,             // ����� ������ ����� ��������
				 a, b,                        // ������ ����� ��� ��������(a, b)
				 stepX, stepY, stepZ,         // ���� ��������������� �����������.
				 maxX,  maxY,  maxZ;          // max-� ��������������� �����������.
 
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
 protected void add(int number)  {   //��������� � ����(field) ������ �� ���������� ����� � �������(number).                           
   field.add(new Integer(number));
 }        
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 10:24:07)
 */
   protected void net(Graphics g) //���� ����� ���.
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
  protected Vector3D normal(boolean normal_Left, int n1, int n2, int n3)      //���������� ������� ������� � ���� ������
	{                                                                         //�� ���������� ������� "normal_Left".
	  int a = ((Integer)field.get(n1)).intValue();    //����� n1 ����� � piace.
	  int b = ((Integer)field.get(n2)).intValue();    //� n2 ����� � piace.
	  int c = ((Integer)field.get(n3)).intValue();    //� n3 ����� � piace.
		
	  Point3D pA = (Point3D)piace.points3D.get(a);
	  Point3D pB = (Point3D)piace.points3D.get(b);
	  Point3D pC = (Point3D)piace.points3D.get(c);

	  Vector3D A = new Vector3D(pB.x-pA.x, pB.y-pA.y, pB.z-pA.z);  //������ ����������� �� �������� ��-�� � ������.
	  Vector3D B = new Vector3D(pC.x-pA.x, pC.y-pA.y, pC.z-pA.z);  //������ ����������� �� ����� ��-�� � ������.

	  if (normal_Left)           //���� ���������� �������������.
		return(A.cross(B));
	  else                       //���� ���������� ��������������.
		return(B.cross(A));
	}
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:41:05)
 */
   synchronized protected void rotate(Matrix3D matrix)     //new : �������� Element-a "e" 
	 {                                                           
	  matrix.identity();
	  if(numberZero != -1)
	   {
		Point3D p = (Point3D)piace.points3D.get(numberZero);
		matrix.translate(-p.x, -p.y, -p.z);                      //��������� ��������� ����(points3D)
		matrix.rotate(animX, "X", false);               
		matrix.rotate(animY, "Y", false);               
		matrix.translate(p.x, p.y, p.z);                         //��������� ��������� ����(points3D)
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
   synchronized protected void rotate(Matrix3D matrix, int a, int b, float rotate)  //new : �������� Element-a "e" ������ ���(a,b)
	 {                                                                 //�� ����=rotate
	  if ((a!=b) && (Math.max(a,b)< piace.points3D.size())) 
	   {
		float animX, animY, animZ;
		matrix.identity();
		Point3D pA = (Point3D)piace.points3D.get(a);
		Point3D pB = (Point3D)piace.points3D.get(b);
		Vector3D axis = new Vector3D(pB.x-pA.x, pB.y-pA.y, pB.z-pA.z); // ������ ��� ��������
		animX = axis.x* rotate/axis.mag() ; 
		animY = axis.y* rotate/axis.mag() ; 
		animZ = axis.z* rotate/axis.mag() ; 
		matrix.translate(-pA.x, -pA.y, -pA.z);                      //��������� ��������� ����(points3D)
		matrix.rotate(animX, "X", false);               
		matrix.rotate(animY, "Y", false);               
		matrix.rotate(animZ, "Z", false);               
		matrix.translate(pA.x, pA.y, pA.z);                      //��������� ��������� ����(points3D)
		transform(matrix);
	   }
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:49:06)
 */
///////////////////////////////////////////////////////���� �������� ���������:
   synchronized protected void rotate(Matrix3D matrix, String axis, float rotate) //�������� Element-a "e" ������ ��� ���������"axis"
	 {                                                      //�� ���� "rotate"
	   matrix.identity();
	   matrix.rotate(rotate, axis, false);               
	   transform(matrix);
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 15:08:18)
 */
///////////////////////////////////////////////////////���� ��������������� ���������:
   synchronized protected void scale( Matrix3D matrix, float x, float y, float z) //��������������� "�" �� ���� X, Y, Z
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
  protected void setAnimAngle(float animX, float animY) { //���� ��������
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
///////////////////////////////////////////////////////����� ����:
  protected synchronized void transform(Matrix3D matrix) //�����, ���������� ����� ��������� ��
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
///////////////////////////////////////////////////////���� ����������� ���������:
   synchronized protected void translate(Matrix3D matrix, int dx, int dy, int dz)  //����������� "e" �� ���������� "dx", "dy", "dz"
	 {
	   matrix.identity();
	   matrix.translate(dx, dy, dz);  
	   transform(matrix);
	 }
/**
 * Insert the method's description here.
 * Creation date: (11.10.2001 12:26:27)
 */
  protected float weight()                //������������ ��� ���������� ������� ��������� ������.
	{
	  if(field.size() == 0)  
		return(0f);    
	  float sumZ = 0f;
	  for (int i=0, t=0; i<field.size(); i++) 
		{
		  t = ((Integer)field.get(i)).intValue();
		  sumZ += ((Point3D)piace.points3D.get(t)).z; //�������� ��� sum**2;
		}
	  return(sumZ / field.size());     
	}
}  
