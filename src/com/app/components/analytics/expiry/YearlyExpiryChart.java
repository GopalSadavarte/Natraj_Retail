package com.app.components.analytics.expiry;

import java.awt.*;
import javax.swing.JInternalFrame;

public class YearlyExpiryChart extends JInternalFrame{
    public YearlyExpiryChart(){
        super("Yearly Expiry Analysis", false, true, true);
        setBackground(Color.white);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
