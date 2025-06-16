package com.app.components.reports.purchase_return;

import javax.swing.JInternalFrame;
import java.awt.*;

public class DailyPurchaseReturnReport extends JInternalFrame{
    public DailyPurchaseReturnReport(){
        super("Daily Purchase Return Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
