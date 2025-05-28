package com.app.components.reports.purchase_return;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklyPurchaseReturnReport extends JInternalFrame{
    public WeeklyPurchaseReturnReport(){
        super("Weekly Purchase Return Report", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
