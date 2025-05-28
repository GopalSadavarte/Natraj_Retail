package com.app.components.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class Group extends JInternalFrame{
    public Group(){
        super("Group", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
