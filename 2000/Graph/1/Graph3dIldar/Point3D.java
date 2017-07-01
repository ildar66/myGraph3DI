public class Point3D  {      //трехмерная точка.
  
  float x, y, z;             //координаты трехмерной точки.
  public Point3D (float x0, float y0, float z0)
    {
       x = x0;
       y = y0;
       z = z0;
    }
  public Point3D (int x0, int y0, int z0)
    {
       x = x0;
       y = y0;
       z = z0;
    }
 
  public String toString()  //информация о трехмерной точке.
    {
      return (new String("(" + x + "," + y + "," + z + ")"));  
    }
 } 
/////////////////////////////////////////////////////////////