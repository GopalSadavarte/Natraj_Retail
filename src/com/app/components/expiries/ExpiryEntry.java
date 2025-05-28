package com.app.components.expiries;

import javax.swing.JInternalFrame;
import java.awt.*;

public class ExpiryEntry extends JInternalFrame{
    public ExpiryEntry(){
        super("Expiry Entry", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
