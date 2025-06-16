package com.app.components.reports.purchase_return;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlyPurchaseReturnReport extends JInternalFrame{
    public MonthlyPurchaseReturnReport(){
        super("Monthly Purchase Return Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
