import java.io.*;

class Test5forVector3D {

 public static void main(String args[]) {
  Vector3D theVector3D = new Vector3D(1, 2, 3);
  System.out.println("До записи: " + theVector3D);

  try {
    FileOutputStream outputFile = new FileOutputStream("temp"); //new start
    ObjectOutputStream serializeStream = new ObjectOutputStream(outputFile);//new end
    serializeStream.writeObject(theVector3D);
    serializeStream.flush();
    outputFile.close();
  } catch (Exception exc){System.out.println("Error"); exc.printStackTrace();}

  System.out.println("После записи: " + theVector3D);
  theVector3D=null;
  System.out.println("После очистки: " + theVector3D);

  try {
    FileInputStream inputFile = new FileInputStream("temp");
    ObjectInputStream in = new ObjectInputStream(inputFile);
    theVector3D = (Vector3D)in.readObject();
    inputFile.close();
  } catch (Exception exc){System.out.println("Error"); exc.printStackTrace();}

  System.out.println("После чтения: " + theVector3D);
 }
}