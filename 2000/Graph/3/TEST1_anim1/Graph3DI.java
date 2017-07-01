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
      scenarioPaint();
      piace.regimeMove = true;
      SP = new SouthPanel(piace);
      add("Center", piace);
      add("South",SP);
    }

   public void scenarioPaint() {
    int theHeight = getSize().height;
    int theWidth = getSize().width;

    //создание шара
    piace.offset = new Point(theWidth/2, theHeight/2);
    piace.select.full = true; piace.select.color = Color.blue; 
    piace.figure.setAnimAngle(-15.0f, -21.0f); 
    piace.figure.net = false; 
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
    piace.figure.net = false; 
    piace.figure.setAnimAngle(9.0f, -11.0f);  
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
    piace.figure.net = false; 
    piace.figure.setAnimAngle(5, -5.0f);  
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
    ((Side)piace.select).normal_Left = true; //ошибка в конструкторе Side.
    piace.figure.setAnimAngle(0, 7.0f);  

    //создание вазы
    piace.newFigure();   
    piace.select.full = true; piace.select.color = Color.magenta; 
    piace.figure.setAnimAngle(0, 5.0f);  
    piace.SavePoint3D(piace.points3D.size() , theWidth/2, theHeight/8*5);
    piace.turnSum = 0; piace.turn = (float)(Math.PI/8f); 
    for(int i=0; i<3; i++)
      piace.reconstruct(piace.select, "Z");
    piace.SavePoint3D(piace.points3D.size() , theWidth*9/20, theHeight/8*2);
    piace.SavePoint3D(piace.points3D.size() , theWidth*8/20, theHeight/8);
    piace.turnSum = 0; piace.turn = (float)(Math.PI/8f);    
    for(int i=0; i<4; i++)
      piace.reconstruct(piace.select, "Y");
    piace.translate(piace.select, 0, 0, -theWidth);
   }

   public void start() {
    anim = new Thread(this);
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
          piace.rotate("X", figure, figure.animX); 
          piace.rotate("Y", figure, figure.animY); 
         }
        piace.repaint(); 
      }
      try { anim.sleep(150); } catch (InterruptedException e) {}
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
     Choice kind = new Choice();
     Button net = new Button("Net OFF");  
     Button full = new Button(" Full ");
     Button backface = new Button("Backface ON");

     public SouthPanel(Piace piace)    {
      setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
      this.piace = piace;
      kind.add("Polygon"); kind.add("free Polygon"); kind.add("Line"); kind.add("free Line");
      add(kind); kind.addItemListener(this);  
      add(full); full.addActionListener(this);
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
      add(net); net.addActionListener(this);
      add(backface); backface.addActionListener(this);
     }  

     public void actionPerformed(ActionEvent ev)  {
       String obj = ev.getActionCommand();

       if (obj.equals(" Full "))
         {full.setLabel("Empty"); piace.full = piace.select.full = true;}    
       else if (obj.equals("Empty"))
         {full.setLabel(" Full "); piace.full = piace.select.full = false;}
       else if (obj.equals("Net ON"))
         {net.setLabel("Net OFF"); piace.net = piace.figure.net = true;}
       else if (obj.equals("Net OFF")) 
         {net.setLabel("Net ON"); piace.net = piace.figure.net = false;}
       else if (obj.equals("Backface ON"))
         {backface.setLabel("Backface OFF"); piace.flag_backface = piace.figure.flag_backface = true;}
       else if (obj.equals("Backface OFF")) 
         {backface.setLabel("Backface ON"); piace.flag_backface = piace.figure.flag_backface = false;}

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
       else if (evt.getSource() == kind)
         { piace.kind = piace.select.kind = kind.getSelectedIndex();}

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
        piace.color = piace.select.color;
        piace.kind = piace.select.kind;
        piace.full = piace.select.full;
        kind.select(piace.select.kind);
        if (piace.select.full)
          full.setLabel("Empty");    
        else
          full.setLabel(" Full ");   
        if (piace.figure.net)
          net.setLabel("Net OFF");  
        else
          net.setLabel("Net ON");   
        if (piace.figure.flag_backface)
          backface.setLabel("Backface OFF");  
        else
          backface.setLabel("Backface ON");   
      }
   }     
 /////////////////////////////////////////////////////////////////////////////////Северная панель
   