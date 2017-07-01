 import java.util.*;
 import java.awt.*;   
  
 public class Swarm {
   Vector field = new Vector(); //набор номеров(number) трехмерных точек.
   Piace piace;
   int a, b;                              // номера точек оси вращения
   protected float animX, animY;          // углы анимации 
   protected int numberZero = -1;         // номер осевой точки вращения
 
public Swarm(Piace piace) {
  this.piace = piace;
}

 protected void add(int number)  {   //добавляет к полю(field) ссылку на трехмерную точку с номером(number).                           
   field.addElement(new Integer(number));
 }
        
  protected void setA(int a)
   {this.a = a;}
      
  protected void setAnimAngle(float animX, float animY) { //углы анимации
	this.animX = animX;
	this.animY = animY;
  }          
 void setB(int b) {this.b = b;}   
}  
