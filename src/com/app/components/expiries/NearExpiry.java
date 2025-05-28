package com.app.components.expiries;

import javax.swing.JInternalFrame;
import java.awt.*;

public class NearExpiry extends JInternalFrame{
    public NearExpiry(){
        super("Near Expiry", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
