package com.app.components.reports.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlySalesReport extends JInternalFrame{
    public YearlySalesReport(){
        super("Yearly Sales Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
