 import java.util.Vector;
 import java.awt.*;

 class Figure extends Side {    //������(Figure), ��������� �� ������(Side).
  boolean flag_backface;                 // ���� ������ �������� ������� ������ ������. 
  boolean net;                           // ����� �� ������ ������ ������.

  public Figure(Piace piace, int kind, Color color, boolean full, boolean net, boolean flag_backface) 
   {
     super(piace, kind, color, full);
     this.net = net;
     this.flag_backface = flag_backface;
   }  

  void draw(Graphics g, Point offset, String view) // ��������� ������.
   {
     for (int i=0; i<elements.size(); i++)
       {
         Side side = (Side)elements.elementAt(i);  // ���������� ������, ������������� ������.
         if (side.backface(flag_backface))           
           side.draw(g, offset, view);
       }
   }

  protected void setAnimAngle(float animX, float animY) {
    this.animX = animX;
    this.animY = animY;
  }

  public String toString()  {                //���������� � ����� ��������.
    return ("animX= " + animX + " ;animY= " + animY + " ;numberZero=" + numberZero);  
  }
 }
///////////////////////////////////////////////////////////////////////////////////////////////
