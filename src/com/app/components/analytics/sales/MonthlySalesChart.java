package com.app.components.analytics.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlySalesChart extends JInternalFrame{
    public MonthlySalesChart(){
        super("Monthly Sales Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
