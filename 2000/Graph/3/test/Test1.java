import java.io.*;
import java.awt.*;
import java.awt.event.*;

class Test1 extends Frame {
 private static Graph3DI theGraph3DI;

 public Test1(String name) {
  super(name);
  addWindowListener(new ExitGraph3DI_WindowAdapter());

  theGraph3DI = new Graph3DI();
  theGraph3DI.init();
  theGraph3DI.start();
  add(theGraph3DI);
 }

 class ExitGraph3DI_WindowAdapter extends WindowAdapter{
  public void windowClosing(WindowEvent e) {
//   theGraph3DI.stop();  theGraph3DI.destroy();
   System.exit(0);
  }
 } 

 public static void main(String args[]) {
  Test1 theTest1 = new Test1("Test Graph3DI");
  theTest1.setSize(620, 600);
  theTest1.show();

  try {
    Thread.sleep(30000);

    FileOutputStream outputFile = new FileOutputStream("temp"); //new start
    ObjectOutputStream serializeStream = new ObjectOutputStream(outputFile);//new end
    serializeStream.writeObject(theGraph3DI);
    serializeStream.flush();
    outputFile.close();
  } catch (Exception exc){System.out.println("Error"); exc.printStackTrace();}


 }
}
