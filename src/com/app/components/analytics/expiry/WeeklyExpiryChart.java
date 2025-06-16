package com.app.components.analytics.expiry;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklyExpiryChart extends JInternalFrame{
    public WeeklyExpiryChart(){
        super("Weekly Expiry Analysis", false, true, true);
        setBackground(Color.white);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
