public class Vector3D  {
  
  float x, y, z;
  public Vector3D (float x0, float y0, float z0)
    {
       x = x0;
       y = y0;
       z = z0;
    }

  public float dot (Vector3D A)
    {
      return(A.x * x + A.y * y + A.z * z);
    }

  public Vector3D cross (Vector3D A)
    {
      float cx = (y*A.z) - (z*A.y);
      float cy = (z*A.x) - (x*A.z);
      float cz = (x*A.y) - (y*A.x);
      return (new Vector3D(cx, cy, cz));
    }     

  public float mag()
    {
      return((float)Math.sqrt(x*x + y*y + z*z));
    }

  public String toString()
    {
      return (new String("(" + x + "," + y + "," + z + ")"));  
    }
 } 

  