import java.io.*;
import java.awt.*;
import java.awt.event.*;

class Test2 extends Frame {
 private Graph3DI theGraph3DI;

 public Test2(String name) {
  super(name);
  addWindowListener(new ExitGraph3DI_WindowAdapter());
    try {
    FileInputStream inputFile = new FileInputStream("temp"); //new start
    ObjectInputStream serializeStream = new ObjectInputStream(inputFile);//new end
    theGraph3DI = (Graph3DI)serializeStream.readObject();
    inputFile.close();
   } catch (Exception exc){System.out.println("Error");exc.printStackTrace();}
 // theGraph3DI.init();
 // theGraph3DI.start();
  add(theGraph3DI);
 }

 class ExitGraph3DI_WindowAdapter extends WindowAdapter{
  public void windowClosing(WindowEvent e) {
//   theGraph3DI.stop();  theGraph3DI.destroy();
   System.exit(0);
  }
 } 

 public static void main(String args[]) {
  Test2 theTest2 = new Test2("Test2 Graph3DI");
  theTest2.setSize(620, 600);
  theTest2.show();
 }

}