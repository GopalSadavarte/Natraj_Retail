package com.app.components.reports.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class DailyPurchaseReport extends JInternalFrame{
    public DailyPurchaseReport(){
        super("Daily Purchase Report", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
