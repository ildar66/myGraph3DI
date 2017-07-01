 import java.awt.*;
 import java.awt.event.*;

  //////////////////////////////////////////////////////////////////////////////////////////////////// 
  class ClosePanel extends WindowAdapter {
   floatPanel FP;
   public ClosePanel(floatPanel FP)
	 {this.FP = FP;}
   public void windowClosing(WindowEvent e)
	 { FP.setVisible (false); } 
 }