 import java.util.Vector;
 import java.awt.*;   

	  abstract class Element extends Swarm {             //������� �������(Side) ������(Figure). 

  boolean full,                                     //����������(full) ������������� ��������.
		  free;                                     //free
  Color color;                                     //����(color) ��������.





                                          

                
						  
                
                

  abstract Polygon getShade(Point offset, String view);  //���� �������� ������������ view(�������) 
		                                            

  abstract void draw(Graphics g, Point offset, String view); //��������� ���� �������� ������������ (view)�������
	                                                        //� ������ ������� (offset). 
   public Element(Piace piace, Color color, boolean free, boolean full)  {
	super(piace);    
	this.color = color;
	this.free = free;
	this.full = full;
  }        //  int kind;                                //���(kind) ��������:{Poly, freePoly, Line, freeLine}.
//  public static final int Poly = 0, freePoly = 1,  //���������:�������������(Poly),������(freePoly).
//						  Line = 2, freeLine = 3;  //����������� :�����(Line), ������(freeLine). 
 }