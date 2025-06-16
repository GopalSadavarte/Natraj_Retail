package com.app.components.analytics.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlySalesChart extends JInternalFrame{
    public YearlySalesChart(){
        super("Yearly Sales Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
