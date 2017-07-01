 import java.util.Vector;
 import java.awt.*;

 class Figure extends Side implements java.io.Serializable{  //������(Figure), ��������� �� ������(Side).
  boolean flag_backface;        // ���� ������ �������� ������� ������ ������. 
  boolean net;                  // ����� �� ������ ������ ������.
  protected float animX, animY; // ���� ��������

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

  private void readObject(java.io.ObjectInputStream in)  throws java.io.IOException, java.lang.ClassNotFoundException {
   in.defaultReadObject();
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
   out.defaultWriteObject();
  }
 }
///////////////////////////////////////////////////////////////////////////////////////////////
