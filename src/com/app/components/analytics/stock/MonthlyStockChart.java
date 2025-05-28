package com.app.components.analytics.stock;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlyStockChart extends JInternalFrame{
    public MonthlyStockChart(){
        super("Monthly Stock Analysis", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
