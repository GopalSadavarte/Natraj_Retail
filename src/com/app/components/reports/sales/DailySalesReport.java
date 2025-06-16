package com.app.components.reports.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class DailySalesReport extends JInternalFrame{
    public DailySalesReport(){
        super("Daily Sales Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
