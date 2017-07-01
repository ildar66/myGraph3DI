 import java.awt.*;
 import java.awt.event.*;

    public class floatPanel extends Frame implements ActionListener, ItemListener {
   MenuBar menubar = new MenuBar();
   Menu view = new Menu("View", true);     //определяет направление взгляда на текущую фигуру.                        
   Menu rotate = new Menu("Rotate", true); //скорость вращения фигуры по осям.
   Menu step = new Menu("Step", true);     //скорость перемещения фигуры по осям.
   Choice turn = new Choice();             //для фигур вращения
   Button formA = new Button("Form('A')");
   Button formB = new Button("Form('B')");
   Button formD = new Button("Form('D')");
   String viewer = "view Right";           //установка направления взгляда
   Piace piace;
   Point zero;                             //центр вращения.
   String menuI[][] = {{"view Right", "view Up",  "view Face"},
					   {"rotate=1",   "rotate=3", "rotate=5" }, 
					   {"step=1",     "step=3",   "step=5"   }};
   MenuItem mI[][];   

   floatPanel(Piace piace, String title)               
	{ 
	  super(title);  
	  this.piace = piace;
	  addWindowListener(new ClosePanel(this)); 
	   
	  setMenuBar(menubar);
	  menubar.add(view); menubar.add(rotate); menubar.add(step); 
	  mI = new MenuItem[menuI.length][];
	  for (int i=0; i < menuI.length; i++)
		{
		 mI[i] = new MenuItem[menuI[i].length];
		 for (int k=0; k < menuI[i].length; k++)
		   { mI[i][k] = new MenuItem(menuI[i][k]); mI[i][k].addActionListener(this); }
		}
	  view.add(mI[0][0]);   view.add(mI[0][1]);   view.add(mI[0][2]);
	  rotate.add(mI[1][0]); rotate.add(mI[1][1]); rotate.add(mI[1][2]);
	  step.add(mI[2][0]);   step.add(mI[2][1]);   step.add(mI[2][2]); 
	  
	  Panel north_panel = new Panel(new FlowLayout(FlowLayout.CENTER, 2, 2));
	  turn.add("Form0"); turn.add("Form1"); turn.add("Form2"); turn.add("Form3"); 
	  turn.add("Form4"); turn.add("Form5"); turn.add("Form6"); turn.add("Form7"); 
	  turn.add("Form8"); turn.add("Form9"); turn.add("Form10");       
	  north_panel.add(turn);  turn.addItemListener(this);         
	  north_panel.add(formA); formA.addActionListener(this);         
	  north_panel.add(formB); formB.addActionListener(this);         
	  north_panel.add(formD); formD.addActionListener(this);         
	  add("South", north_panel); 
 
	  setSize(300, 300); 
	  show();
	}
  public void paint(Graphics g)   //прорисовка скелета выбранной фигуры.
	{
	  zero = new Point(getSize().width/2, getSize().height/2);   
	  if(piace.select.field.size() != 0)
	   {
		g.setColor(piace.select.color);     //прорисовка заполненной стороны-select.
		g.fillPolygon(piace.select.getShade(zero, viewer));
		for (int i=0; i<piace.figure.elements.size(); i++)
		 {
		  Side side = (Side)piace.figure.elements.elementAt(i);
		  g.setColor(side.color);
		  g.drawPolygon(side.getShade(zero, viewer));  
		 }
	  g.setColor(Color.black);                  //прорисовка центра вращения
	  g.fillOval(zero.x - 4, zero.y - 4, 8, 8);
	   }
	} 

  public void actionPerformed(ActionEvent evt)
	{
	  String obj = evt.getActionCommand();
	  if (evt.getSource() instanceof MenuItem)
		{ 
		 if (obj.equals("step=1"))             //установка шага перемещения.
		   piace.step = 1;        
		 else if (obj.equals("step=3"))
		   piace.step = 3;        
		 else if (obj.equals("step=5"))
		   piace.step = 5;     
		 else if (obj.equals("rotate=1"))      //установка скорости вращения.
		   piace.rotate = 1; 
		 else if (obj.equals("rotate=3"))
		   piace.rotate = 3;
		 else if (obj.equals("rotate=5"))
		   piace.rotate = 5; 
		 else if (obj.equals("view Up")||obj.equals("view Face")||obj.equals("view Right"))
		   {viewer  = obj; setTitle(viewer + " :Shell Figure and full selected Side");}  //направление взгляда.
		}
	  else if (evt.getSource() instanceof Button)  //направление вращения формирователя фигуры вращения.
		{ 
		 if ((obj.equals("Form('A')"))&&(piace.turnSum < (float)(1.5*Math.PI))&&(piace.select.field.size() != 0))
		   {piace.reconstruct(piace.select, "Z");}
		 if ((obj.equals("Form('B')"))&&(piace.turnSum < (float)(1.5*Math.PI))&&(piace.select.field.size() != 0))
		   {piace.reconstruct(piace.select, "Y");}
		 if ((obj.equals("Form('D')"))&&(piace.turnSum < (float)(1.5*Math.PI))&&(piace.select.field.size() != 0))
		   {piace.reconstruct(piace.select, "X");}
		}
	  else return;
	  piace.repaint(); 
	}                      

  public void itemStateChanged(ItemEvent evt)
	{  
	  if (evt.getSource() == turn)               //установка угла поворота формирователя фигур вращения.
	   {
		 String obj = turn.getSelectedItem();
		 if (obj.equals("Form0"))  
		   {piace.turn = (float)(Math.PI/32f); piace.turnSum = 0;}
		 else if (obj.equals("Form1"))  
		   {piace.turn = (float)(Math.PI/16f); piace.turnSum = 0;}
		 else if (obj.equals("Form2")) 
		   {piace.turn = (float)(Math.PI/8f); piace.turnSum = 0;}
		 else if (obj.equals("Form10"))
		   {piace.turn = (float)(Math.PI/5f); piace.turnSum = 0;} 
		 else if (obj.equals("Form9"))
		   {piace.turn = (float)(2f*Math.PI/9f); piace.turnSum = 0;} 
		 else if (obj.equals("Form8"))
		   {piace.turn = (float)(Math.PI/4f); piace.turnSum = 0;} 
		 else if (obj.equals("Form7"))
		   {piace.turn = (float)(2f*Math.PI/7f); piace.turnSum = 0;} 
		 else if (obj.equals("Form6"))
		   {piace.turn = (float)(Math.PI/3f); piace.turnSum = 0;}
		 else if (obj.equals("Form5"))
		   {piace.turn = (float)(2f*Math.PI/5f); piace.turnSum = 0;}
		 else if (obj.equals("Form4"))
		   {piace.turn = (float)(Math.PI/2f); piace.turnSum = 0;}
		 else if (obj.equals("Form3"))
		   {piace.turn = (float)(2f*Math.PI/3f); piace.turnSum = 0;}
	   }
	  else return;
	  piace.repaint(); 
	} 
 }