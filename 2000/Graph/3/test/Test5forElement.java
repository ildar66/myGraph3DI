import java.io.*;
import java.awt.*;

class Test5forElement {

 public static void main(String args[]) {
  Graph3DI theGraph3DI = new Graph3DI();
  Element theElement = new Element(theGraph3DI.piace, Element.Line, Color.red, true);
  System.out.println("До записи: " + theElement.color);

  try {
    FileOutputStream outputFile = new FileOutputStream("temp");    //new start
    ObjectOutputStream serializeStream = new ObjectOutputStream(outputFile);//new end
    serializeStream.writeObject(theElement);
    serializeStream.flush();
    outputFile.close();
  } catch (Exception exc){System.out.println("Error"); exc.printStackTrace();}

  System.out.println("После записи: " + theElement.color);
  theElement = new Element(theGraph3DI.piace, Element.Line, Color.blue, true);
  System.out.println("После смены: " + theElement.color);

  try {
    FileInputStream inputFile = new FileInputStream("temp");
    ObjectInputStream in = new ObjectInputStream(inputFile);
    theElement = (Element)in.readObject();
    inputFile.close();
  } catch (Exception exc){System.out.println("Error"); exc.printStackTrace();}

  System.out.println("После чтения: " + theElement.color);
 }
}