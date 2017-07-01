public class testRotate  {  
 
  public static void main(String[] arg) 
    {
      Matrix3D matrixA = new Matrix3D();
      Point3D ptA = new Point3D(100.0f, 0.0f, 0.0f);

      matrixA.rotateY(180.0f);
      System.out.println("Befor pointA:" + ptA);
      ptA = matrixA.transform(ptA);
      System.out.println("After pointA" + ptA );  
    } 
 } 

  