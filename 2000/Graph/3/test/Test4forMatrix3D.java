import java.io.*;

class Test4forMatrix3D {

 public static void main(String args[]) {
  Matrix3D theMatrix3D = new Matrix3D();
  theMatrix3D.translate(1, 2, 3);
  System.out.println("До записи " +","+ theMatrix3D.e03 +","+ theMatrix3D.e13 +","+ theMatrix3D.e23);

  try {
    FileOutputStream outputFile = new FileOutputStream("temp"); //new start
    ObjectOutputStream serializeStream = new ObjectOutputStream(outputFile);//new end
    serializeStream.writeObject(theMatrix3D);
    serializeStream.flush();
    outputFile.close();
  } catch (Exception exc){System.out.println("Error"); exc.printStackTrace();}

  System.out.println("После записи " +","+ theMatrix3D.e03 +","+ theMatrix3D.e13 +","+ theMatrix3D.e23);
  theMatrix3D.identity();
  System.out.println("После очистки " +","+ theMatrix3D.e03 +","+ theMatrix3D.e13 +","+ theMatrix3D.e23);

  try {
    FileInputStream inputFile = new FileInputStream("temp");
    ObjectInputStream in = new ObjectInputStream(inputFile);
    theMatrix3D = (Matrix3D)in.readObject();
    inputFile.close();
  } catch (Exception exc){System.out.println("Error"); exc.printStackTrace();}

  System.out.println("После чтения " +","+ theMatrix3D.e03 +","+ theMatrix3D.e13 +","+ theMatrix3D.e23);
 }
}