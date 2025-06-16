package com.app.components.analytics.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklyPurchaseChart extends JInternalFrame{
    public WeeklyPurchaseChart(){
        super("Weekly Purchase Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
