package com.app.components.analytics.expiry;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlyExpiryChart extends JInternalFrame{
    public MonthlyExpiryChart(){
        super("Monthly Expiry Analysis", false, true, true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
