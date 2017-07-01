 
 import java.awt.*;
 import java.util.*; 
    class Side extends Poly {      //������� ������(figure).
   boolean normal_Left;                         //���������� �������(normal_Left).


  
 
  Vector3D normal()        //���������� ������� ������� � �������(side).
	{ 
	  int n1 = 0;                 //����� ������� ����� ���.
	  int n2 = field.size()/2;    //� ������� ����� ���.
	  int n3 = 3*field.size()/4;  //� 3/4 ����� ���.

	  return super.normal(normal_Left, n1, n2, n3);
	}

  boolean backface(boolean flag)//����������� ������� ����������� ��������.
	{
	  if ((field.size() < 3)||(!flag)) //���� ���� ����������(flag) ����.��� ������ ��������� normal().
		return true;                   
	  if (normal().z < 0.0f)           //���� ������� � ����������� ���������� �� ���.
		return true;  
	  else                             //���� ������� � ����������� ���������� �� ���.
		return false;  
	}

 

  void draw(Graphics g, Point offset, String view)         //��������� �������.
	{
	  super.draw(g, offset, view);
	  ListIterator litr = elements.listIterator();
	  while (litr.hasNext()) {
		Element elem = (Element)litr.next();        
		elem.draw(g, offset, view);	  	
	  } 
	} 

   ArrayList elements = new ArrayList();        //������ ���������(Elements) �� ������� ������� ���� �������.	 

  public Side(Piace piace, Color color, boolean free, boolean full, boolean normal_Left) 
	{
	  super(piace, color, free, full);
	  this.normal_Left = normal_Left;
	}}