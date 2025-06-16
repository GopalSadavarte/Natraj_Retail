package com.app.components.reports.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlyPurchaseReport extends JInternalFrame{
    public YearlyPurchaseReport(){
        super("Yearly Purchase Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
