public class testMultiply  {  
 
  public static void main(String[] arg) 
    {
      Matrix3D matrixA = new Matrix3D();
      Matrix3D matrixB = new Matrix3D();
      Point3D ptA = new Point3D(100.0f, 100.0f, 100.0f);    

      matrixA.scale(0.30f, 0.40f, 0.50f);
      matrixB.scale(0.50f);
      matrixA.multiply(matrixB, Matrix3D.POSTCONCAT);
      System.out.println("Before pointA" + ptA );  
      ptA = matrixA.transform(ptA);
      System.out.println("After pointA" + ptA);  
    } 
 } 

  