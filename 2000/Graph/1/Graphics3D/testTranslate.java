public class testTranslate  {  
 
  public static void main(String[] arg) 
    {
      Matrix3D matrix = new Matrix3D();
      Point3D pointA = new Point3D(5.0f, 5.0f, 5.0f);

      System.out.println("Befor pointA" + pointA);      
      matrix.translate(3.0f, 4.0f, 5.0f);
      pointA = matrix.transform(pointA);
      System.out.println("After pointA" + pointA);
    } 
 } 

  