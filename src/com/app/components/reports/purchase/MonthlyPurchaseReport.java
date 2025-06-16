package com.app.components.reports.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;
public class MonthlyPurchaseReport extends JInternalFrame {
    public MonthlyPurchaseReport(){
        super("Monthly Purchase Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
