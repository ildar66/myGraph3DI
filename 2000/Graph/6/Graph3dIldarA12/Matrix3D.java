 import java.awt.*;
 public class Matrix3D {
  float e00, e01, e02, e03;
  float e10, e11, e12, e13;
  float e20, e21, e22, e23;
  static final double toRad = Math.PI/180.0;

  public Matrix3D()
   {
     identity();
   }

  public void identity()     //инициализация системы координат(СК).
   {
     e00 = e11 = e22 = 1.0f;
     e01 = e02 = e03 = 0.0f;      
     e10 = e12 = e13 = 0.0f;   
     e20 = e21 = e23 = 0.0f;  
   }

  public Point3D transform(Point3D p)     //координаты точки3D в этой системе координат.
   {
     float x = p.x * e00 + p.y * e01 + p.z * e02 + e03; 
     float y = p.x * e10 + p.y * e11 + p.z * e12 + e13; 
     float z = p.x * e20 + p.y * e21 + p.z * e22 + e23;
     p.x = x; p.y = y; p.z = z;
     return(p); 
   }

  public void translate (float dx, float dy, float dz)//смещение СК по оси X, Y, Z
   {
     e03 += dx;
     e13 += dy;
     e23 += dz;
   }

  public void scale(float a, float b, float c)        //изменение масштаба по оси x(a), y(b), z(c)
   {
     e00 *= a;
     e11 *= b;
     e22 *= c;
   }

  public void rotate(double theta, String axis)       //вращение СК на угол theta 
   {                                                  //вокруг оси axist
     theta *= toRad;
     double theta_cos = Math.cos(theta);
     double theta_sin = Math.sin(theta);

     if(axis.equals("X"))
      { 
       e11 = (float)theta_cos;
       e12 = (float)-theta_sin;
       e21 = (float)theta_sin;
       e22 = (float)theta_cos;
      }
     else if(axis.equals("Y")) 
      { 
       e00 = (float)theta_cos;
       e20 = (float)-theta_sin;
       e02 = (float)theta_sin;
       e22 = (float)theta_cos;
      }
     else if(axis.equals("Z")) 
      { 
       e00 = (float)theta_cos;
       e01 = (float)-theta_sin;
       e10 = (float)theta_sin;
       e11 = (float)theta_cos;
      }
   }
 }          