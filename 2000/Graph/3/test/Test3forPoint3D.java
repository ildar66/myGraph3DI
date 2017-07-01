import java.io.*;

class Test3forPoint3D {

 public static void main(String args[]) {
  Point3D thePoint3D = new Point3D(1, 2, 3);

  try {
    FileOutputStream outputFile = new FileOutputStream("temp"); //new start
    ObjectOutputStream serializeStream = new ObjectOutputStream(outputFile);//new end
    serializeStream.writeObject(thePoint3D);
    serializeStream.flush();
    outputFile.close();
  } catch (Exception exc){System.out.println("Error");exc.printStackTrace();}

  thePoint3D = new Point3D(0, 0, 0);

  try {
    FileInputStream inputFile = new FileInputStream("temp");
    ObjectInputStream in = new ObjectInputStream(inputFile);
    thePoint3D = (Point3D)in.readObject();
    inputFile.close();
  } catch (Exception exc){System.out.println("Error");exc.printStackTrace();}

  System.out.println(thePoint3D);
 }
}