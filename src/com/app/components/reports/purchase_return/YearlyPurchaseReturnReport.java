package com.app.components.reports.purchase_return;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlyPurchaseReturnReport extends JInternalFrame{
    public YearlyPurchaseReturnReport(){
        super("Yearly Purchase Return Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
