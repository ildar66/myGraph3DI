import java.awt.*;
import java.applet.*;
import java.util.*;

public class testOrthApplet extends Applet{
  Cube cube = new Cube(50);
  Point xaxis = null;  
  Point yaxis = null;  
  Point zaxis = null; 
 
  public void init()
   {
     Matrix3D matrix = new Matrix3D();
     cube.matrix.translate(0f, 0f, 0f);
     matrix.orthogonal(30f, 80f, 0f);
     cube.transform(matrix);
      
     Point3D x = new Point3D(150f, 0f, 0f);
     Point3D y = new Point3D(0f, 150f, 0f);
     Point3D z = new Point3D(0f, 0f, 150f);
     xaxis = matrix.transform2D(x);  
     yaxis = matrix.transform2D(y);   
     zaxis = matrix.transform2D(z);  
   }

  public void paint(Graphics g)
   {
     Point offset = new Point(size().width/2, size().height/2);
     cube.drawSolid(g, offset); 

     g.setColor(Color.red);
     g.drawLine(offset.x, offset.y, offset.x + xaxis.x, offset.y + xaxis.y);
     g.setColor(Color.green);
     g.drawLine(offset.x, offset.y, offset.x + yaxis.x, offset.y + yaxis.y);
     g.setColor(Color.blue);
     g.drawLine(offset.x, offset.y, offset.x + zaxis.x, offset.y + zaxis.y);
   }
}