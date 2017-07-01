 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;
 import javax.swing.*;  

 public class Graph3DI extends JApplet implements ItemListener{
   JComboBox kind = new JComboBox();
   public void init()  {
     getRootPane().setJMenuBar(new JMenuBar());
     javax.swing.JMenuBar menubar = getRootPane().getJMenuBar();
     javax.swing.JMenu menu = new javax.swing.JMenu("menu");
     menu.add(new JMenuItem("Hi"));
     menubar.add(menu);
     Container cp = getContentPane();  
     cp.setLayout(new BorderLayout());
     JPanel southPane = new  JPanel();
     southPane.add(kind); 
     cp.add("South", southPane);
     kind.addItem("Polygon"); kind.addItem("free Polygon"); kind.addItem("Line"); kind.addItem("free Line");
     kind.addItemListener(this); 
   }                                             
	 public void itemStateChanged(ItemEvent evt)   {  
	   if (evt.getSource() == kind)
		System.out.println("kind.getSelectedIndex()" + kind.getSelectedIndex());

	 }
   public String getAppletInfo() {
	 return "Graph3DI.2001(avtor Ildar Chafigouline)";
   }                  

}