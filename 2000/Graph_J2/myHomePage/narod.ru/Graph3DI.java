 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

  public class Graph3DI extends Applet implements Runnable {   
   Piace piace;
   SouthPanel SP;
   Thread anim = null;

   public void init()  {
     setLayout(new BorderLayout());
     piace = new Piace(this);
     //piace.regimeMove = true;
     SP = new SouthPanel(piace);
     add("Center", piace);
     add("South",SP);
     add("West", new MovePanel(piace));
     add("East", new EastPanel(piace));
     add("North", new NorthPanel(piace));
     scenarioPaint();
     SP.setControl(); 
   }

   public void start() {
     if(anim == null)
       anim = new Thread(this);
     if(!anim.isAlive()) 
       anim.start();
   }         

   public void stop() {
     anim = null;
   }

   public void destroy() {
     removeAll();
   }

   public String getAppletInfo() {
     return "Graph3DI.2000(avtor Ildar Shaphigullin)";
   }   
      
   public void run() {     //метод run потока анимации
     Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
     while(Thread.currentThread() == anim) {
     	for(int i=0; i<piace.figures.size(); i++) 
     	 {
     	  Figure figure = (Figure)piace.figures.elementAt(i);
     	  piace.rotate(figure); 
     	 }
     	piace.repaint(); 
       try { anim.sleep(100); } catch (InterruptedException e) {}
     }
   } 
   
   public void scenarioPaint() {
     int theHeight = getSize().height;
     int theWidth = getSize().width;
     piace.offset = new Point(theWidth/2, theHeight/2);

       //создание куба
     piace.figure.net = false; 
     piace.select.full = true; piace.select.color = Color.cyan; 
     piace.figure.setAnimAngle(-4.0f, -8.0f); piace.figure.numberZero = 0; 
     piace.savePoint3D(piace.points3D.size() , theWidth/8*3, theHeight/2);
     piace.turn = (float)(Math.PI/2f); // piace.turnSum = 0;
     for(int i=0; i<2; i++)
       piace.reconstruct(piace.select, "Z");
     piace.copy(piace.select); piace.select.color = Color.blue;
     piace.translate(piace.select, 0, 0, theWidth/4);
     piace.newSide(); piace.select.full = true; piace.select.color = Color.magenta; 
     piace.select.add(0); piace.select.add(1);
     piace.select.add(5); piace.select.add(4);
     piace.newSide(); piace.select.full = true; piace.select.color = Color.red; 
     piace.select.add(2); piace.select.add(3);
     piace.select.add(7); piace.select.add(6);
     piace.newSide(); piace.select.full = true; piace.select.color = Color.gray; 
     piace.select.add(0); piace.select.add(3);
     piace.select.add(7); piace.select.add(4);
     piace.translate(piace.figure, -theWidth*2/6, -theHeight*2/6, 0);   

       //создание шара
     piace.newFigure();   
     piace.select.full = true; piace.select.color = Color.blue; 
     piace.figure.net = false; piace.figure.setAnimAngle(-8.0f, -10.0f); 
     piace.savePoint3D(piace.points3D.size() , theWidth/4, theHeight/2);
     piace.turn = (float)(Math.PI/16f); // piace.turnSum = 0;
     for(int i=0; i<5; i++)
       piace.reconstruct(piace.select, "Z");
     piace.savePoint3D(piace.points3D.size() , theWidth/4, theHeight/2);
     piace.turn = (float)(Math.PI/16f);  piace.turnSum = 0;   
     for(int i=0; i<4; i++)
       piace.reconstruct(piace.select, "Y");

       //создание тетраедра
     piace.newFigure();   
     piace.select.full = true; piace.select.color = Color.green; 
     piace.figure.net = false; piace.figure.setAnimAngle(9.0f, -11.0f);  
     piace.savePoint3D(piace.points3D.size() , theWidth/8*3, theHeight/2);
     piace.turnSum = 0; piace.turn = (float)(2f*Math.PI/3f); 
     for(int i=0; i<2; i++)
       piace.reconstruct(piace.select, "Z");
     piace.turnSum = 0; piace.turn = (float)(Math.PI/2f);    
     for(int i=0; i<2; i++)
       piace.reconstruct(piace.select, "Y");
     piace.translate(piace.select, -theWidth*2/6, -theHeight*2/6, 0);   

       //создание звезды
     piace.newFigure();   
     piace.select.full = true; piace.select.color = Color.red; 
     piace.figure.net = false; piace.figure.setAnimAngle(5, -5.0f);  
     piace.savePoint3D(piace.points3D.size() , -175, 196);
     piace.savePoint3D(piace.points3D.size() , -135, 71);
     piace.savePoint3D(piace.points3D.size() , -83, 205);
     piace.savePoint3D(piace.points3D.size() , -194, 120);
     piace.savePoint3D(piace.points3D.size() , -61, 136);
     piace.copy(piace.select);
     ((Side)piace.select).normal_Left = true; 
     piace.select.color = Color.yellow;
     piace.figure.flag_backface = true;  
       //создание копии звезды
     piace.copy(piace.figure);
     piace.figure.setAnimAngle(0, 7.0f);  

       //создание вазы
     piace.newFigure();   
     piace.select.full = true; piace.select.color = Color.magenta; 
     piace.figure.net = false; piace.figure.setAnimAngle(0, 3.0f);  
     piace.savePoint3D(piace.points3D.size() , theWidth/2, theHeight/8*5);
     piace.turnSum = 0; piace.turn = (float)(Math.PI/8f); 
     for(int i=0; i<3; i++)
       piace.reconstruct(piace.select, "Z");
     piace.savePoint3D(piace.points3D.size() , theWidth*9/20, theHeight/8*2);
     piace.savePoint3D(piace.points3D.size() , theWidth*8/20, theHeight/8);
     piace.turnSum = 0; piace.turn = (float)(Math.PI/8f);    
     for(int i=0; i<4; i++)
       piace.reconstruct(piace.select, "Y");
     piace.translate(piace.select, 0, theHeight/3, -theWidth/8*5);
   }  

}