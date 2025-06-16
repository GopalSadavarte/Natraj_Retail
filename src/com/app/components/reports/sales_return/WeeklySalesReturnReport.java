package com.app.components.reports.sales_return;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklySalesReturnReport extends JInternalFrame{
    public WeeklySalesReturnReport(){
        super("Weekly Sales Return Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
