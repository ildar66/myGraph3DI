 import java.util.Vector;
 import java.awt.*;
 class Side extends Element {                 //������� ������(figure).
  boolean normal_Left;                     //���������� �������(normal_Left).

  public Side(Piace piace, int kind, Color color, boolean full) 
    {
      super(piace, kind, color, full);
    }  
 
  public Vector3D normal()    //���������� ������� ������� � �������(side).
    { 
      int a = ((Integer)field.elementAt(0)).intValue();             //����� ������� ����� ��������.
      int b = ((Integer)field.elementAt(field.size()/2)).intValue();//� ������� ����� ��������.
      int c = ((Integer)field.elementAt(field.size()-1)).intValue();//� ��������� ����� ��������.

      float x = ((Point3D)piace.points3D.elementAt(b)).x - 
                ((Point3D)piace.points3D.elementAt(a)).x;
      float y = ((Point3D)piace.points3D.elementAt(b)).y -
                ((Point3D)piace.points3D.elementAt(a)).y;
      float z = ((Point3D)piace.points3D.elementAt(b)).z - 
                ((Point3D)piace.points3D.elementAt(a)).z;
      Vector3D A = new Vector3D(x, y, z); //������ ����������� �� �������� ��-�� � ������.

      x = ((Point3D)piace.points3D.elementAt(c)).x - 
                ((Point3D)piace.points3D.elementAt(a)).x;
      y = ((Point3D)piace.points3D.elementAt(c)).y -
                ((Point3D)piace.points3D.elementAt(a)).y;
      z = ((Point3D)piace.points3D.elementAt(c)).z - 
                ((Point3D)piace.points3D.elementAt(a)).z;
      Vector3D B = new Vector3D(x, y, z); //������ ����������� �� ����� ��-�� � ������.
      if (normal_Left)           //���� ���������� �������������.
        return(A.cross(B));
      else                       //���� ���������� ��������������.
        return(B.cross(A));
    }

  public boolean backface(boolean flag)//����������� ������� ����������� ��������.
    {
      if ((field.size() < 3)||(!flag)) //���� ���� ����������(flag) ����.
        return true;                   
      if (normal().z < 0.0f)           //���� ������� � ����������� ���������� �� ���.
        return true;  
      else                             //���� ������� � ����������� ���������� �� ���.
        return false;  
    }

  public float weight()                //������������ ��� ���������� ������� ��������� ������.
    {
      if(field.size() == 0)  
        return(0f);    
      int t;
      float sumZ = 0f;
      for (int i=0; i<field.size(); i++) 
        {
          t = ((Integer)field.elementAt(i)).intValue();
          sumZ += ((Point3D)piace.points3D.elementAt(t)).z; //�������� ��� sum**2;
        }
      return(sumZ / field.size());     
    } 

  public void draw(Graphics g, Point offset, String view) //��������� �������.
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
