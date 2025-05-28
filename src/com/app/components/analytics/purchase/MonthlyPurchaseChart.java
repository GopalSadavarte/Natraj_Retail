package com.app.components.analytics.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlyPurchaseChart extends JInternalFrame{
    public MonthlyPurchaseChart(){
        super("Monthly Purchase Analysis", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
