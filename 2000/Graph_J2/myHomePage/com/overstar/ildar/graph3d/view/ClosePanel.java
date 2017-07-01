package com.overstar.ildar.graph3d.view;

 import java.awt.*;
 import java.awt.event.*;
  //////////////////////////////////////////////////////////////////////////////////////////////////// 
  public class ClosePanel extends WindowAdapter {
   FloatPanel FP;
 
   public ClosePanel(FloatPanel FP)
	 {this.FP = FP;}
   public void windowClosing(WindowEvent e)
	 { FP.setVisible (false); }
}
