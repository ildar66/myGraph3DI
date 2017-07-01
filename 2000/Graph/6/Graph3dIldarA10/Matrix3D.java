import java.awt.*;
public class Matrix3D {

  float e00, e01, e02, e03;
  float e10, e11, e12, e13;
  float e20, e21, e22, e23;
  static final double toRad = Math.PI/180.0;
  static int REPLACE = 0;
  static int PRECONCAT = 1;
  static int POSTCONCAT =2;

  public Matrix3D()
   {
     identity();
   }
  
  public void identity()     //инициализация матрицы.
   {
     e00 = e11 = e22 = 1.0f;
     e01 = e02 = e03 = 0.0f;      
     e10 = e12 = e13 = 0.0f;   
     e20 = e21 = e23 = 0.0f;  
   }

  public Point3D transform(Point3D p) //координаты точки3D в этой системе координат.
   {
     float x = p.x * e00 + p.y * e01 + p.z * e02 + e03; 
     float y = p.x * e10 + p.y * e11 + p.z * e12 + e13; 
     float z = p.x * e20 + p.y * e21 + p.z * e22 + e23;
     p.x = x; p.y = y; p.z = z;
     return(p); 
   }

/*  public Vector3D transform(Vector3D p)//координаты vector3D-a в этой системе координат.
   {
     float x = p.x * e00 + p.y * e01 + p.z * e02 + e03; 
     float y = p.x * e10 + p.y * e11 + p.z * e12 + e13; 
     float z = p.x * e20 + p.y * e21 + p.z * e22 + e23;
     return(new Vector3D(x, y, z)); 
   }  */

/*  public Point transform2D(Point3D p)//экранные координаты.
   {
     Point3D pt = transform(p);
     return(new Point(Math.round(pt.x), Math.round(pt.y)));
   }  */

  public void translate (float x, float y, float z)//перемещение системы координат.
   {
     e03 += x;
     e13 += y;
     e23 += z;
   }

  public void scale(float a, float b, float c) //масштабирование системы(CK) координат.
   {
     e00 *= a;
     e11 *= b;
     e22 *= c;
   }
  
  public void rotate(double theta, String axis)//вращение СК на угол theta 
   {                                           //вокруг оси axist
     theta *= toRad;
     double theta_cos = Math.cos(theta);
     double theta_sin = Math.sin(theta);

     Matrix3D m = new Matrix3D();
     if(axis.equals("X"))
      { 
       m.e11 = (float)theta_cos;
       m.e12 = (float)-theta_sin;
       m.e21 = (float)theta_sin;
       m.e22 = (float)theta_cos;
      }
     else if(axis.equals("Y")) 
      { 
       m.e00 = (float)theta_cos;
       m.e20 = (float)-theta_sin;
       m.e02 = (float)theta_sin;
       m.e22 = (float)theta_cos;
      }
     else if(axis.equals("Z")) 
      { 
       m.e00 = (float)theta_cos;
       m.e01 = (float)-theta_sin;
       m.e10 = (float)theta_sin;
       m.e11 = (float)theta_cos;
      }
     this.multiply(m, Matrix3D.POSTCONCAT);
   }

  void multiply(Matrix3D matrix, int concat) //умножение матриц:
   {
     Matrix3D A = null;
     Matrix3D B = null;
     if (concat == Matrix3D.PRECONCAT)      //матрицу matrix на эту матрицу.
       {	
         A = matrix;
         B = this;
       }
     else if (concat == Matrix3D.POSTCONCAT) //эту матрицу на matrix.
       {
         A = this;
         B = matrix;
       }
     else if (concat == Matrix3D.REPLACE)    //замена.
       {
         A = this;
         A.identity();
         B = matrix;
       }

     float n00 = A.e00*B.e00 + A.e10*B.e01 + A.e20*B.e02;    
     float n01 = A.e01*B.e00 + A.e11*B.e01 + A.e21*B.e02;    
     float n02 = A.e02*B.e00 + A.e12*B.e01 + A.e22*B.e02;    
     float n03 = A.e03*B.e00 + A.e13*B.e01 + A.e23*B.e02 + B.e03;    

     float n10 = A.e00*B.e10 + A.e10*B.e11 + A.e20*B.e12;    
     float n11 = A.e01*B.e10 + A.e11*B.e11 + A.e21*B.e12;    
     float n12 = A.e02*B.e10 + A.e12*B.e11 + A.e22*B.e12;    
     float n13 = A.e03*B.e10 + A.e13*B.e11 + A.e23*B.e12 + B.e13; 
   
     float n20 = A.e00*B.e20 + A.e10*B.e21 + A.e20*B.e22;     
     float n21 = A.e01*B.e20 + A.e11*B.e21 + A.e21*B.e22;    
     float n22 = A.e02*B.e20 + A.e12*B.e21 + A.e22*B.e22;    
     float n23 = A.e03*B.e20 + A.e13*B.e21 + A.e23*B.e22 + B.e23; 

     e00 = n00;   
     e01 = n01;   
     e02 = n02;   
     e03 = n03;

     e10 = n10;   
     e11 = n11;   
     e12 = n12;   
     e13 = n13; 

     e20 = n20;   
     e21 = n21;   
     e22 = n22;   
     e23 = n23;     
   } 
}          