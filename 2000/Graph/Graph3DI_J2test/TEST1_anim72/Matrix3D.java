 public class Matrix3D {
  float e00, e01, e02, e03;
  float e10, e11, e12, e13;
  float e20, e21, e22, e23;
  static final double toRad = Math.PI/180.0;

  public Matrix3D()
   {
     identity();
   }

  void identity()     //инициализация системы координат(СК).
   {
     e00 = e11 = e22 = 1.0f;
     e01 = e02 = e03 = 0.0f;      
     e10 = e12 = e13 = 0.0f;   
     e20 = e21 = e23 = 0.0f;  
   }

  Point3D transform(Point3D p)     //координаты точки3D в этой системе координат.
   {
     float x = p.x * e00 + p.y * e01 + p.z * e02 + e03; 
     float y = p.x * e10 + p.y * e11 + p.z * e12 + e13; 
     float z = p.x * e20 + p.y * e21 + p.z * e22 + e23;
     p.x = x; p.y = y; p.z = z;
     return(p); 
   }

  void translate (float dx, float dy, float dz)//смещение СК по оси X, Y, Z
   {
     e03 += dx;
     e13 += dy;
     e23 += dz;
   }

  void scale(float a, float b, float c)        //изменение масштаба по оси x(a), y(b), z(c)
   {
     e00 *= a;
     e11 *= b;
     e22 *= c;
   }

  void rotate(double theta, String axis, boolean isRad)       //вращение СК на угол theta 
   {                                                                 //вокруг оси axist.
     if (!isRad)                                                                
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
     float n00 = e00*m.e00 + e10*m.e01 + e20*m.e02;    
     float n01 = e01*m.e00 + e11*m.e01 + e21*m.e02;    
     float n02 = e02*m.e00 + e12*m.e01 + e22*m.e02;    
     float n03 = e03*m.e00 + e13*m.e01 + e23*m.e02 + m.e03;    

     float n10 = e00*m.e10 + e10*m.e11 + e20*m.e12;    
     float n11 = e01*m.e10 + e11*m.e11 + e21*m.e12;    
     float n12 = e02*m.e10 + e12*m.e11 + e22*m.e12;    
     float n13 = e03*m.e10 + e13*m.e11 + e23*m.e12 + m.e13; 
                 
     float n20 = e00*m.e20 + e10*m.e21 + e20*m.e22;     
     float n21 = e01*m.e20 + e11*m.e21 + e21*m.e22;    
     float n22 = e02*m.e20 + e12*m.e21 + e22*m.e22;    
     float n23 = e03*m.e20 + e13*m.e21 + e23*m.e22 + m.e23; 

     e00 = n00; e01 = n01; e02 = n02; e03 = n03;
     e10 = n10; e11 = n11; e12 = n12; e13 = n13; 
     e20 = n20; e21 = n21; e22 = n22; e23 = n23;     
   }
 }          