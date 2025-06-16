package com.app.components.analytics.expiry;

import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JInternalFrame;

public class DailyExpiryChart extends JInternalFrame{
    public DailyExpiryChart(){
        super("Daily Expiry Analysis",false,true,true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(Color.white);
        setVisible(true);
    }
}
