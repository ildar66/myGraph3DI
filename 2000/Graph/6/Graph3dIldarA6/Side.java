 import java.util.Vector;
 import java.awt.*;
 class Side extends Element {                 //сторона фигуры(figure).
  boolean normal_Left;                     //показатель нормали(normal_Left).

  public Side(Piace piace, int kind, Color color, boolean full) 
    {
      super(piace, kind, color, full);
    }  
 
  public Vector3D normal()    //вычисление вектора нормали к стороне(side).
    { 
      int a = ((Integer)field.elementAt(0)).intValue();             //номер нулевой точки элемента.
      int b = ((Integer)field.elementAt(field.size()/2)).intValue();//№ средней точки элемента.
      int c = ((Integer)field.elementAt(field.size()-1)).intValue();//№ последней точки элемента.

      float x = ((Point3D)piace.points3D.elementAt(b)).x - 
                ((Point3D)piace.points3D.elementAt(a)).x;
      float y = ((Point3D)piace.points3D.elementAt(b)).y -
                ((Point3D)piace.points3D.elementAt(a)).y;
      float z = ((Point3D)piace.points3D.elementAt(b)).z - 
                ((Point3D)piace.points3D.elementAt(a)).z;
      Vector3D A = new Vector3D(x, y, z); //вектор проведенный из середины эл-та в начало.

      x = ((Point3D)piace.points3D.elementAt(c)).x - 
                ((Point3D)piace.points3D.elementAt(a)).x;
      y = ((Point3D)piace.points3D.elementAt(c)).y -
                ((Point3D)piace.points3D.elementAt(a)).y;
      z = ((Point3D)piace.points3D.elementAt(c)).z - 
                ((Point3D)piace.points3D.elementAt(a)).z;
      Vector3D B = new Vector3D(x, y, z); //вектор проведенный из конца эл-та в начало.
      if (normal_Left)           //если показатель левосторонний.
        return(A.cross(B));
      else                       //если показатель правосторонний.
        return(B.cross(A));
    }

  public boolean backface(boolean flag)//определение лицевой поверхности элемента.
    {
      if ((field.size() < 3)||(!flag)) //если флаг разрешения(flag) выкл.
        return true;                   
      if (normal().z < 0.0f)           //если нормаль к поверхности направлена на нас.
        return true;  
      else                             //если нормаль к поверхности направлена от нас.
        return false;  
    }

  public float weight()                //используется для сортировки очереди рисования сторон.
    {
      if(field.size() == 0)  
        return(0f);    
      int t;
      float sumZ = 0f;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          sumZ += ((Point3D)piace.points3D.elementAt(t)).z; //подумать над sum**2;
        }
      return(sumZ / field.size());     
    } 

  public void draw(Graphics g, Point offset, String view) //рисование стороны.
    {
      super.draw(g, offset, view);
      for (int i=0; i<elements.size(); i++)
         {
           Element element = (Element)elements.elementAt(i);        
           element.draw(g, offset, view);
         }      
    } 
 }
/////////////////////////////////////////////////////////////////////////////////////////
