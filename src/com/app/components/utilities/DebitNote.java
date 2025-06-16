package com.app.components.utilities;

import javax.swing.JInternalFrame;
import java.awt.*;

public class DebitNote extends JInternalFrame{
    public DebitNote(){
        super("Debit Note", false, true, true);
        setBackground(Color.white);

         setSize(Toolkit.getDefaultToolkit().getScreenSize());
         setVisible(true);
    }
}
