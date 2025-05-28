package com.app.components.reports.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklyPurchaseReport extends JInternalFrame{
    public WeeklyPurchaseReport(){
        super("Weekly Purchase Report", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
