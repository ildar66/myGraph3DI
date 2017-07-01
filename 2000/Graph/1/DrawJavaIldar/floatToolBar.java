 import java.awt.*;
 import java.awt.image.*; 

 public class floatToolBar extends Frame {

   graphicalButton buttons[];
   Checkbox checkbox[];
   String labels[];
   TextField textA;
   TextField textB;
   TextField textX;
   TextField textY;
   TextField choose;
   DrawJava DJ;

   floatToolBar(DrawJava D, String title, String labels[],
                Image imgs[][])
    { 
      super(title);  
      DJ = D;      
      this.labels = labels;
      buttons = new graphicalButton[labels.length]; 
      setLayout(new BorderLayout());

      Panel GraphicalButtonPanel = new Panel();
      for (int i = 0; i < labels.length; i++) 
        {
          buttons[i] = new graphicalButton(labels[i],imgs[i][0],
                                                    imgs[i][1]);
          GraphicalButtonPanel.add(buttons[i]);
        }
      buttons[0].setButton(graphicalButton.DOWN);
      GraphicalButtonPanel.add(choose = new TextField("Rect",3));
      choose.setEditable(false);      
      add("North",GraphicalButtonPanel);   

      Panel text_panel = new Panel();  
      text_panel.setLayout(new GridLayout(2, 4)); 
      text_panel.add(new Label("A,%",Label.RIGHT));
      text_panel.add(textA = new TextField("0", 1));
      text_panel.add(new Label("<X>",Label.RIGHT));
      text_panel.add(textX = new TextField(1));
      text_panel.add(new Label("B,%",Label.RIGHT));                    
      text_panel.add(textB = new TextField("100", 1));
      text_panel.add(new Label("<Y>",Label.RIGHT));
      text_panel.add(textY = new TextField(1)); 
      textX.setEditable(false);  
      textY.setEditable(false);  
      add("Center",text_panel);

      Panel buttonA_panel = new Panel();
      buttonA_panel.add(new Button(" full "));
      add("West", buttonA_panel);

      Panel buttonB_panel = new Panel();
      buttonB_panel.add(new Button("net"));
      add("East", buttonB_panel);      

      Panel color_panel = new Panel();
      CheckboxGroup group = new CheckboxGroup();
      Color CL[] = {Color.white, Color.orange, Color.pink, Color.green,
         Color.red, Color.cyan, Color.yellow, Color.blue,Color.magenta,
              Color.lightGray, Color.gray,Color.darkGray, Color.black}; 
      checkbox = new Checkbox[CL.length];     
      for (int i=0; i<checkbox.length; i++)
       {         
         color_panel.add(checkbox[i] = new Checkbox(null,group,true));
         checkbox[i].setBackground(CL[i]);          
       } 
      add("South", color_panel); 

      pack();
      show();
    } 

   public boolean action(Event evt, Object obj)
    {
      if (evt.target instanceof graphicalButton)
        {
          String label = (String) obj;
          for (int i = 0; i < labels.length; i++)
            {
             if ((label.equals(labels[i]))&&(DJ.mode != i))
               {             
                 buttons[DJ.mode].setButton(graphicalButton.UP);      
                 DJ.mode = i;
                 choose.setText(labels[i]);                              
                 break;
               }
            }
        }
      else if (evt.target instanceof Button)
        {
          if (obj.equals(" full "))
            {
              ((Button)evt.target).setLabel("empty");
              DJ.full = true;
            }
          else if (obj.equals("empty")) 
            {
              ((Button)evt.target).setLabel(" full ");
              DJ.full = false;
            }
          else if (obj.equals("net"))
            {
              ((Button)evt.target).setLabel("NET");
              DJ.net = false;
              DJ.repaint();
            }
          else if (obj.equals("NET")) 
            {
              ((Button)evt.target).setLabel("net");
              DJ.net = true;
              DJ.repaint();
            }
        }      
      else if (evt.target instanceof Checkbox)
        {               
          DJ.setForeground(((Component)evt.target).getBackground());
        }
      else if (evt.target instanceof TextField)
        {
          if(evt.target == textA)
           {
             float A = Float.valueOf(textA.getText()).floatValue(); 
             DJ.transA = Math.round(Math.max(0, Math.min(A, 100)));
             textA.setText(String.valueOf(DJ.transA));
           }
          else if(evt.target == textB)
           {
             float B = Float.valueOf(textB.getText()).floatValue(); 
             DJ.transB = Math.round(Math.max(0, Math.min(B, 100)));
             textB.setText(String.valueOf(DJ.transB)); 
           }
        }            
      return true;
    } 

   public void setColor(Color color)
    {
      for(int i=0; i<checkbox.length; i++)
       {
        if(checkbox[i].getBackground()== color)
           checkbox[i].setState(true);
       }
    }

   public boolean handleEvent(Event evt)
    {
      if(evt.id == Event.WINDOW_DESTROY)
        dispose();
       return super.handleEvent(evt);
    }
 }  
