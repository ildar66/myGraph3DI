 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;

 public class Graph3DI extends Applet implements Runnable {   
   Piace piace;
   SouthPanel SP;
   Thread anim;

   public void init()
    {
      setLayout(new BorderLayout());
      piace = new Piace(this);
      piace.regimeMove = true;
      SP = new SouthPanel(piace);
      add("Center", piace);
      add("South",SP);
      scenarioPaint();
    }

   public void scenarioPaint() {
    int theHeight = getSize().height;
    int theWidth = getSize().width;
    piace.offset = new Point(theWidth/2, theHeight/2);

      //создание куба
    piace.figure.net = false; 
    piace.select.full = true; piace.select.color = Color.cyan; 
    piace.figure.setAnimAngle(-4.0f, -8.0f); piace.figure.numberZero = 0; 
    piace.SavePoint3D(piace.points3D.size() , theWidth/8*3, theHeight/2);
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
    piace.SavePoint3D(piace.points3D.size() , theWidth/4, theHeight/2);
    piace.turn = (float)(Math.PI/16f); // piace.turnSum = 0;
    for(int i=0; i<5; i++)
      piace.reconstruct(piace.select, "Z");
    piace.SavePoint3D(piace.points3D.size() , theWidth/4, theHeight/2);
    piace.turn = (float)(Math.PI/16f);  piace.turnSum = 0;   
    for(int i=0; i<4; i++)
      piace.reconstruct(piace.select, "Y");

      //создание тетраедра
    piace.newFigure();   
    piace.select.full = true; piace.select.color = Color.green; 
    piace.figure.net = false; piace.figure.setAnimAngle(9.0f, -11.0f);  
    piace.SavePoint3D(piace.points3D.size() , theWidth/8*3, theHeight/2);
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
    piace.SavePoint3D(piace.points3D.size() , -175, 196);
    piace.SavePoint3D(piace.points3D.size() , -135, 71);
    piace.SavePoint3D(piace.points3D.size() , -83, 205);
    piace.SavePoint3D(piace.points3D.size() , -194, 120);
    piace.SavePoint3D(piace.points3D.size() , -61, 136);
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
    piace.SavePoint3D(piace.points3D.size() , theWidth/2, theHeight/8*5);
    piace.turnSum = 0; piace.turn = (float)(Math.PI/8f); 
    for(int i=0; i<3; i++)
      piace.reconstruct(piace.select, "Z");
    piace.SavePoint3D(piace.points3D.size() , theWidth*9/20, theHeight/8*2);
    piace.SavePoint3D(piace.points3D.size() , theWidth*8/20, theHeight/8);
    piace.turnSum = 0; piace.turn = (float)(Math.PI/8f);    
    for(int i=0; i<4; i++)
      piace.reconstruct(piace.select, "Y");
    piace.translate(piace.select, 0, theHeight/3, -theWidth/8*5);

      //создание надписи "JAVA"
     piace.newFigure();
     piace.figure.net = false; piace.figure.setAnimAngle(0, 2.0f);
        //"J"
     piace.select.full = true; piace.select.color = Color.red; piace.select.kind = Element.Line;
     piace.SavePoint3D( piace.points3D.size(), 230 , 34);
     piace.SavePoint3D( piace.points3D.size(), 256 , 33);
     piace.SavePoint3D( piace.points3D.size(), 255, 87);
     piace.SavePoint3D( piace.points3D.size(), 244, 98);
     piace.SavePoint3D( piace.points3D.size(), 234, 99);
     piace.SavePoint3D( piace.points3D.size(), 225, 86);
       piace.copy(piace.select);piace.select.color = Color.green;
       piace.translate(piace.select, 5, 5, 10);
        //"A"
     piace.newSide(); piace.select.full = true;
     piace.select.color = Color.red; piace.select.kind = Element.Line;
     piace.SavePoint3D( piace.points3D.size(), 275, 101);
     piace.SavePoint3D( piace.points3D.size(), 295, 31);
     piace.SavePoint3D( piace.points3D.size(), 312, 104);
       piace.copy(piace.select);piace.select.color = Color.green;
       piace.translate(piace.select, 5, 5, 10);
     piace.newSide(); piace.select.full = true;
     piace.select.color = Color.red; piace.select.kind = Element.Line;
     piace.SavePoint3D( piace.points3D.size(), 282, 72);
     piace.SavePoint3D( piace.points3D.size(), 305, 72);
       piace.copy(piace.select);piace.select.color = Color.green;
       piace.translate(piace.select, 5, 5, 10);
       //"V"
     piace.newSide(); piace.select.full = true;
     piace.select.color = Color.red; piace.select.kind = Element.Line;
     piace.SavePoint3D( piace.points3D.size(), 326, 30);
     piace.SavePoint3D( piace.points3D.size(), 345, 100);
     piace.SavePoint3D( piace.points3D.size(), 367, 31);
       piace.copy(piace.select);piace.select.color = Color.green;
       piace.translate(piace.select, 5, 5, 10);
        //"A"
     piace.newSide(); piace.select.full = true;
     piace.select.color = Color.red; piace.select.kind = Element.Line;
     piace.SavePoint3D( piace.points3D.size(), 377, 102);
     piace.SavePoint3D( piace.points3D.size(), 400, 30);
     piace.SavePoint3D( piace.points3D.size(), 413, 103);
       piace.copy(piace.select);piace.select.color = Color.green;
       piace.translate(piace.select, 5, 5, 10);
     piace.newSide(); piace.select.full = true;
     piace.select.color = Color.red; piace.select.kind = Element.Line;
     piace.SavePoint3D( piace.points3D.size(), 386, 71);
     piace.SavePoint3D( piace.points3D.size(), 409, 71);
       piace.copy(piace.select);piace.select.color = Color.green;
       piace.translate(piace.select, 5, 5, 10);
     piace.translate(piace.figure, 0, -25, -theWidth*1/6);   

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

   public void run() {     //метод run потока анимации
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    while(Thread.currentThread() == anim) {
      if(piace.regimeMove) {
        for(int i=0; i<piace.figures.size(); i++) 
         {
          Figure figure = (Figure)piace.figures.elementAt(i);
          piace.rotate(figure); 
/*
          for(int k=0; k<figure.elements.size(); k++) 
           {
            Side theSide = (Side)figure.elements.elementAt(k);
            if(theSide.numberZero != -1) piace.rotate(theSide);
           } 
*/
         }
        piace.repaint(); 
      }
      try { anim.sleep(100); } catch (InterruptedException e) {}
    }
   }

   public void destroy()
    {
     removeAll();
    }
 
   public String getAppletInfo() 
    {
     return "Graph3DI.2000(avtor Ildar Shafigullin)";
    }
 }
///////////////////////////////////////////////////////////////////////////////Южная панель
   class SouthPanel extends Panel implements ActionListener, ItemListener{   
     Piace piace;
     Checkbox checkbox[];
     Button Figure = new Button("Figure");  
     Button Side = new Button("Side");  
     Button Zero = new Button("Zero");
     Button plus = new Button("+");
     Button less = new Button("-");
     Button start = new Button("start");
     Button stop = new Button("stop");
     Button copy = new Button("copy");

     public SouthPanel(Piace piace)    {
      setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
      this.piace = piace;
      add(new Label("next:"));
      add(Zero); Zero.addActionListener(this);
      add(Figure); Figure.addActionListener(this);
      add(Side); Side.addActionListener(this);
      CheckboxGroup group = new CheckboxGroup();
      Color CL[] = {Color.white, Color.orange, Color.pink, Color.green,
         Color.red, Color.cyan, Color.yellow, Color.blue,Color.magenta,
              Color.lightGray, Color.gray,Color.darkGray, Color.black}; 
      checkbox = new Checkbox[CL.length];     
      for (int i=0; i<checkbox.length; i++)
       {         
         add(checkbox[i] = new Checkbox(null, group, true));
         checkbox[i].setBackground(CL[i]);
         checkbox[i].addItemListener(this);       
       } 
      add(copy); copy.addActionListener(this);
      add(plus); plus.addActionListener(this);
      add(less); less.addActionListener(this);
      add(start); start.addActionListener(this);
      add(stop); stop.addActionListener(this);
     }  

     public void actionPerformed(ActionEvent ev)  {
       String obj = ev.getActionCommand();

       if (obj.equals("+"))
         {piace.scale(piace.figure, 1.1f, 1.1f, 1.1f);}    
       else if (obj.equals("-"))
         {piace.scale(piace.figure, 1f/1.1f, 1f/1.1f, 1f/1.1f);}
       else if (obj.equals("Figure"))
         {piace.nextFigure();}
       else if (obj.equals("Side"))
         {piace.nextSide();}
       else if (obj.equals("Zero")) 
         {piace.next_offset();}
       else if (obj.equals("start")) 
         {if(piace.graph.anim == null) piace.graph.start();}
       else if (obj.equals("stop")) 
         {if(piace.graph.anim != null) piace.graph.stop();}
       else if (obj.equals("copy")) 
         {piace.copy(piace.figure);}

       else return;

       piace.repaint();  
     }

     public void itemStateChanged(ItemEvent evt)   {  
       if (evt.getSource() instanceof Checkbox)
         {          
          piace.color = ((Component)evt.getSource()).getBackground();
          if(piace.select != null)
           piace.select.color = ((Component)evt.getSource()).getBackground();
         }

       else return;  

       piace.repaint(); 
     } 

     void setControl()
      {
        for(int i=0; i<checkbox.length; i++)
         {
          if(checkbox[i].getBackground()== piace.select.color)          
              checkbox[i].setState(true);
         }
      }
   }     
 /////////////////////////////////////////////////////////////////////////////////Северная панель
   