public class Vector3D  {  //трехмерный вектор.
  float x, y, z;              //координаты вектора.

  public Vector3D (float x0, float y0, float z0)
	{
	   x = x0;
	   y = y0;
	   z = z0;
	}

  float dot (Vector3D A)     //скл€рное произведение с вектором ј.
	{
	  return(A.x * x + A.y * y + A.z * z);
	}

  Vector3D cross (Vector3D A)//векторное произведение на вектор ј.
	{
	  float cx = (y*A.z) - (z*A.y);
	  float cy = (z*A.x) - (x*A.z);
	  float cz = (x*A.y) - (y*A.x);
	  return (new Vector3D(cx, cy, cz));
	}     

  float mag()                //вычисление модул€ вектора.
	{
	  return((float)Math.sqrt(x*x + y*y + z*z));
	}

  public String toString()          //информаци€ о векторе.
	{
	  return (new String("(" + x + "," + y + "," + z + ")"));  
	}
 }