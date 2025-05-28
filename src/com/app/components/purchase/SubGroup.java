package com.app.components.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class SubGroup extends JInternalFrame{
    public SubGroup(){
        super("Sub Group", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
