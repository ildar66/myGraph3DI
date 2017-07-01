public class testScale  {  
 
  public static void main(String[] arg) 
    {
      Matrix3D matrixA = new Matrix3D();
      Matrix3D matrixB = new Matrix3D();

      Point3D ptA = new Point3D(100.0f, 100.0f, 100.0f);
      Point3D ptB = new Point3D(100.0f, 100.0f, 100.0f);

      System.out.println("Before pointA" + ptA + ", B" + ptB);  
      matrixA.scale(0.30f, 0.40f, 0.50f);
      matrixB.scale(0.50f);

      ptA = matrixA.transform(ptA);
      ptB = matrixB.transform(ptB);
      System.out.println("After pointA" + ptA + ", B" + ptB);  
    } 
 } 

  