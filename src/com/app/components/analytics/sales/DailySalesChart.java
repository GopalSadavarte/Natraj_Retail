package com.app.components.analytics.sales;
import javax.swing.*;
import java.awt.*;

public class DailySalesChart extends JInternalFrame{
    public DailySalesChart(){
        super("Daily Sales Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
