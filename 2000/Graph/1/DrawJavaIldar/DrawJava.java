 import java.awt.*;
 import java.applet.*;
 import java.util.*; 

 public class DrawJava extends Applet {   

  String labels[] ={"Rect", "Oval", "Line", "Point", 
                       "Move", "Copy", "Rub"}; 
  String files[][] ={ {"up.gif", "down.gif"},
                      {"up.gif", "down.gif"},  
                      {"T2.gif", "T1.gif"},
                      {"T2.gif", "T1.gif"},
                      {"msover.gif", "msout.gif"},
                      {"msover.gif", "msout.gif"},                      
                      {"msover.gif", "msout.gif"} }; 

  Image imgs[][] = new Image[7][2];    
  Vector rects = new Vector();
  floatToolBar f;
  gridArea grid;
  public static final int RECT  = 0;
  public static final int OVAL  = 1;
  public static final int LINE  = 2;
  public static final int POINT = 3;
  public static final int MOVE  = 4;
  public static final int COPY  = 5;
  public static final int RUB   = 6;
  colorMode selection;
  colorMode Undo;    
  static boolean full;
  static int mode;
  int index;
  int transA = 0;
  int transB = 100;
  int xL, yL;
  int rub_size = 10; 
  boolean net = true;
  int tab;              

  public DrawJava()
   { 
    setLayout(new BorderLayout());
    Panel batton_panel = new Panel();
    batton_panel.add(new Button("floatToolBar"));      
    add("North",batton_panel);
   } 

  public void init()
   { 
     for (int i = 0; i < imgs.length; i++)
      {
       imgs[i][0] = getImage(getCodeBase(),files[i][0]);
       imgs[i][1] = getImage(getCodeBase(),files[i][1]);
      }
     MediaTracker tracker = new MediaTracker(this);
     for (int i = 0,id = 0; i < imgs.length; i++) 
      {
       tracker.addImage(imgs[i][0], id++);     
       tracker.addImage(imgs[i][1], id++);
      }
     try {
        tracker.waitForAll();
        }
     catch (InterruptedException e) 
      {
        System.out.println("Interrupted: " + e);     
      }
   }

  public boolean action(Event evt, Object arg)
   {
    if(evt.target instanceof Button)
     {      
      f = new floatToolBar(this,"Angela", labels, imgs);                 
      return true;
     }
    return false;    
   }

  public boolean mouseMove(Event evt, int x, int y)
   {
     if (mode == RUB)
      {
       rubRect.mouseMove(this, x, y, rub_size);
      }
     return(true);
   }

  public boolean mouseDown(Event evt, int x, int y)
   { 
     switch(mode)
      {
       case  MOVE: 
       case  COPY:          
         select(x, y);        
         setSituation(selection.x, selection.y);
         if(mode == MOVE)
           {
             f.setColor(selection.color);
             setForeground(selection.color); 
           }     
         if(mode == COPY)
           {
           colorMode cm = selection;           
           selection = new colorMode(cm.x, cm.y, cm.width, cm.height,    
              cm.transA, cm.transB, cm.mode, getForeground(), cm.full);  
           rects.addElement(selection);            
           }
         if(selection != null)
         moveMode.mouseDown(this, x, y, selection);
         break;
       case RUB:
         rubRect.mouseDown(this, x, y, rub_size);
         repaint(x, y, rub_size, rub_size);         //?
         break;
       case POINT:
         xL = x;
         yL = y;
         break;
       default:  
         x = grid.snap(x);
         y = grid.snap(y);
         shadeMode.mouseDown(this, x, y, mode, full, transA, transB);
         setSituation(x, y);   
         break;
      }
     return (true);
   }

  public boolean mouseDrag(Event evt, int x, int y)
   {
     switch (mode)
      {
       case MOVE:
       case COPY:
         if(selection != null)                
           moveMode.mouseDrag(this, x, y, selection); 
           setSituation(selection.x, selection.y);   
         break;
       case RUB:         
         rubRect.mouseDrag(this, rects, x, y, rub_size);
         break;
       case POINT:
         colorMode cm = new colorMode(x, y, xL, yL, transA, transB,
                                      LINE, getForeground(), full);
         Graphics g = getGraphics();
         cm.draw(g);
         g.dispose();
         rects.addElement(cm);         
         xL = x;
         yL = y;       
         break;
       default:
         x = grid.snap(x);
         y = grid.snap(y);   
         shadeMode.mouseDrag(this, x, y, mode, full, transA, transB);
         break;
      }       
     return (true);
   }

  public boolean mouseUp(Event evt, int x, int y)
   {
     switch (mode)
      {
       case MOVE:
       case COPY: 
         if(selection != null)           
           moveMode.mouseUp(this, x, y, selection);
           setSituation(selection.x, selection.y);
         repaint();             
         break; 
       case RUB:
       case POINT:
         repaint();
         break;
       default:
         x = grid.snap(x);
         y = grid.snap(y);     
         Rectangle r = shadeMode.mouseUp(this, x, y, mode, full, transA, transB);
         if (r != null)
          {
           selection = new colorMode(r.x, r.y, r.width, r.height, transA, transB,
                                     mode, getForeground(), full);
           rects.addElement(selection);       
           if (mode == LINE)
           repaint();
           else      
           repaint(r.x, r.y, r.width+1, r.height+1);          
          }
         break;
      }
     return (true);
   }

  public void paint(Graphics g)
   {
    if (grid == null)
      {
       grid = new gridArea(this, size().width, size().height, 5);
      }
    if (net)
      grid.draw(g); 
    for (int i = 0; i < rects.size(); i++)
      {
        colorMode c = (colorMode)rects.elementAt(i);
        c.draw(g);
      }
    System.out.println(rects.size()); 
   }

  public void select(int x, int y)
   {        
     if ((selection != null)&&(selection.inside(x, y)))
       {
         if (mode == MOVE)
           repaint(selection.x,selection.y,selection.width+1,selection.height+1);  
         return;
       }
     int k, i;   
     for (k = 0, i = rects.size()-1; i >= 0; i--)
       {
         colorMode m = (colorMode) rects.elementAt(i);        
         if ((m.inside(x, y))&&(m.mode != LINE))
           {
             selection = m;
             transA = selection.transA;  
             transB = selection.transB;        
             f.textA.setText(String.valueOf(transA));  
             f.textB.setText(String.valueOf(transB)); 
             if (mode == MOVE)
               repaint(m.x, m.y, m.width+1, m.height+1);                   
             if (k++ == tab) 
               return;
           }                
       }
     tab = -1;             
   }

  public boolean keyDown(Event evt, int key)
   { 
    if (selection != null)
     {
      int N = rects.indexOf(selection); 
      colorMode cM = selection;    

      switch(key)
       {
        case 9:  // code<TAB>==9;
           selection = null;
           tab++;
           return(true);
        case 'd':     
           rects.removeElement(selection);
           index = N;
           Undo = cM;
           if (rects.size() != 0) 
           selection = (colorMode)rects.lastElement();
           if (selection.mode == LINE)
             repaint();
           else    
             repaint(cM.x, cM.y, cM.width+1, cM.height+1);               
           return(true);
        case 'u':
           if (!rects.contains(Undo))
             {                     
               rects.insertElementAt(Undo, index);          
               if (selection.mode == LINE)
                 repaint();
               else    
                 repaint(Undo.x, Undo.y, Undo.width+1, Undo.height+1);
             }
           return(true);
         case Event.LEFT:
            if (selection.transA < 100)
              {
                transA = cM.transA+5;
                selection = new colorMode(cM.x, cM.y, cM.width, cM.height,
                    transA, cM.transB, cM.mode, cM.color, cM.full );
                f.textA.setText(String.valueOf(transA));
              }
            break;  
         case Event.RIGHT:
            if (selection.transA > 0)
              {
                transA = cM.transA-5;
                selection = new colorMode(cM.x, cM.y, cM.width, cM.height,
                          transA, cM.transB, cM.mode, cM.color, cM.full );   
                f.textA.setText(String.valueOf(transA));
              }
            break;
         case Event.UP:
            if (selection.transB < 100)
              {
                transB = cM.transB+5;
                selection = new colorMode(cM.x, cM.y, cM.width, cM.height,
                    cM.transA, transB, cM.mode, cM.color, cM.full ); 
                f.textB.setText(String.valueOf(transB));
              }
            break;
         case Event.DOWN:
            if (selection.transB > 0)
              {
                transB = cM.transB-5;
                selection = new colorMode(cM.x, cM.y, cM.width, cM.height,
                    cM.transA, transB, cM.mode, cM.color, cM.full );
                f.textB.setText(String.valueOf(transB));
              }
            break;  
       }
      rects.removeElementAt(N);
      rects.insertElementAt(selection, N);
      repaint(selection.x, selection.y, selection.width+1, selection.height+1);                   
     }
    return(true); 
   } 
 public void setSituation(int x, int y)
   {
    f.textX.setText(String.valueOf(x));  
    f.textY.setText(String.valueOf(y)); 
   }             
 } 