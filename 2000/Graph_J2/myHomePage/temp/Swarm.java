 import java.util.*;
 import java.awt.*;   
  
 public class Swarm {
   Vector field = new Vector(); //����� �������(number) ���������� �����.
   Piace piace;
   int a, b;                              // ������ ����� ��� ��������
   protected float animX, animY;          // ���� �������� 
   protected int numberZero = -1;         // ����� ������ ����� ��������
 
public Swarm(Piace piace) {
  this.piace = piace;
}

 protected void add(int number)  {   //��������� � ����(field) ������ �� ���������� ����� � �������(number).                           
   field.addElement(new Integer(number));
 }
        
  protected void setA(int a)
   {this.a = a;}
      
  protected void setAnimAngle(float animX, float animY) { //���� ��������
	this.animX = animX;
	this.animY = animY;
  }          
 void setB(int b) {this.b = b;}   
}  
