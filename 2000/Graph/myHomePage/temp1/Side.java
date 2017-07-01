 import java.util.Vector;
 import java.awt.*;

    class Side extends Element {      //сторона фигуры(figure).
   boolean normal_Left;                   //показатель нормали(normal_Left).


//  protected int numberEnd = -1;          // номер конечной точки оси вращения

  public Side(Piace piace, int kind, Color color, boolean full, boolean normal_Left) 
	{
	  super(piace, kind, color, full);
	  this.normal_Left = normal_Left;
	}  
 
  Vector3D normal()        //вычисление вектора нормали к стороне(side).
	{ 
	  int n1 = 0;                 //номер нулевой точки роя.
	  int n2 = field.size()/2;    //№ средней точки роя.
	  int n3 = 3*field.size()/4;  //№ 3/4 точки роя.

	  return super.normal(normal_Left, n1, n2, n3);
	}

  boolean backface(boolean flag)//определение лицевой поверхности элемента.
	{
	  if ((field.size() < 3)||(!flag)) //если флаг разрешения(flag) выкл.или нельзя вычислить normal().
		return true;                   
	  if (normal().z < 0.0f)           //если нормаль к поверхности направлена на нас.
		return true;  
	  else                             //если нормаль к поверхности направлена от нас.
		return false;  
	}

 

  void draw(Graphics g, Point offset, String view)         //рисование стороны.
	{
	  super.draw(g, offset, view);
	  for (int i=0; i<elements.size(); i++)
		 {
		   Element element = (Element)elements.elementAt(i);        
		   element.draw(g, offset, view);
		 }      
	} 

         Vector elements = new Vector();        //вектор элементов(Elements) из которых состоит этот Элемент.
  }