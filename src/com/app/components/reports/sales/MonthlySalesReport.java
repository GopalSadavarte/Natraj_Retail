package com.app.components.reports.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlySalesReport extends JInternalFrame{
    public MonthlySalesReport(){
        super("Monthly Sales Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
