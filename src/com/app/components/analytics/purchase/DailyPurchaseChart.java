package com.app.components.analytics.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class DailyPurchaseChart extends JInternalFrame{
    public DailyPurchaseChart(){
        super("Daily Purchase Analysis", false, true, true);
        setBackground(Color.white);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
