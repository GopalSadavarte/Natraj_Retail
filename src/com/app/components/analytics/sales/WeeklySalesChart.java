package com.app.components.analytics.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklySalesChart extends JInternalFrame{
    public WeeklySalesChart(){
        super("Weekly Sales Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
