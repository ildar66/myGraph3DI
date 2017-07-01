 import java.awt.*;

 class Square  { 

  int index[] = new int[4];
  Cube cube = null; 
  Vector3D normal;
  boolean visible;

  public Square(Cube c, int v0, int v1, int v2, int v3) 
    {
      cube = c;
      index[0] = v0;
      index[1] = v1;
      index[2] = v2;
      index[3] = v3;

      float x = cube.w[v1].x - cube.w[v0].x;
      float y = cube.w[v1].y - cube.w[v0].y;
      float z = cube.w[v1].z - cube.w[v0].z;
      Vector3D A = new Vector3D(x, y, z); 

      x = cube.w[v3].x - cube.w[v0].x;
      y = cube.w[v3].y - cube.w[v0].y;
      z = cube.w[v3].z - cube.w[v0].z;
      Vector3D B = new Vector3D(x, y, z); 

      normal = A.cross(B);
    }

  public void backface(Matrix3D m)
    {    
      Vector3D n = m.transform(normal);
      Vector3D o = new Vector3D(0.0f, 0.0f, -1.0f);
      if (n.dot(o) < 0.0f)
        visible = false;
      else
        visible = true;        
    }

  public void draw(Graphics g, Point offset)
    {
      Polygon poly = new Polygon();
      for (int i=0; i<index.length; i++)      
         poly.addPoint(offset.x + cube.v[index[i]].x,
                      offset.y + cube.v[index[i]].y); 

         poly.addPoint(offset.x + cube.v[index[0]].x,
                      offset.y + cube.v[index[0]].y); 
         g.drawPolygon(poly); 
    }
  public void drawSolid(Graphics g, Point offset)
    {
      if (!visible)
        return;

      Polygon poly = new Polygon();
      for (int i=0; i<index.length; i++)      
         poly.addPoint(offset.x + cube.v[index[i]].x,
                      offset.y + cube.v[index[i]].y); 

         poly.addPoint(offset.x + cube.v[index[0]].x,
                      offset.y + cube.v[index[0]].y); 
         g.drawPolygon(poly); 
    }
 }

 public class Cube {
   
   Point3D w[] = new Point3D[8];
   Point v[] = new Point[8];
   Square sides[] = new Square[6];
   Matrix3D matrix = new Matrix3D();

   public Cube (float size)
     {
        w[0] = new Point3D (-size, -size, -size);
        w[1] = new Point3D (-size, size, -size);
        w[2] = new Point3D (size, size, -size);
        w[3] = new Point3D (size, -size, -size);
        w[4] = new Point3D (-size, -size, size);
        w[5] = new Point3D (-size, size, size);
        w[6] = new Point3D (size, size, size);
        w[7] = new Point3D (size, -size, size);
        sides[0] = new Square(this,0,1,2,3);// bottom
        sides[1] = new Square(this,7,6,5,4);// top
        sides[2] = new Square(this,0,4,5,1);// rear  
        sides[3] = new Square(this,1,5,6,2);// right 
        sides[4] = new Square(this,2,6,7,3);// front
        sides[5] = new Square(this,3,7,4,0);// left 
     }
   
   public void draw(Graphics g, Point offset)
     {
       g.setColor(Color.black); 
       for (int i=0; i<sides.length; i++)   
         sides[i].draw(g, offset);
     }
   public void drawSolid(Graphics g, Point offset)
     {
       g.setColor(Color.black); 
       for (int i=0; i<sides.length; i++)   
         sides[i].drawSolid(g, offset);
     }

   public void transform(Matrix3D tooscreen)
     {
       Matrix3D m = new Matrix3D();
       m.multiply(tooscreen, Matrix3D.REPLACE);
       m.multiply(matrix, Matrix3D.PRECONCAT);

       for (int i=0; i<v.length; i++)
         v[i] = m.transform2D(w[i]);

       for (int i=0; i<sides.length; i++)
         sides[i].backface(m);
     } 
   public void transformPer(Matrix3D tooscreen, float d)
     {
       Matrix3D m = new Matrix3D();
       m.multiply(tooscreen, Matrix3D.REPLACE);
       m.multiply(matrix, Matrix3D.PRECONCAT);

       for (int i=0; i<v.length; i++)
         v[i] = m.transform2DPer(w[i], d);

       for (int i=0; i<sides.length; i++)
         sides[i].backface(m);
     } 
 }