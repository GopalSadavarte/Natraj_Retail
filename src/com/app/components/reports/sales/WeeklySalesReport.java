package com.app.components.reports.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklySalesReport extends JInternalFrame{
    public WeeklySalesReport(){
        super("Weekly Sales Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
