package com.app.components.analytics.stock;

import javax.swing.JInternalFrame;
import java.awt.*;

public class DailyStockChart extends JInternalFrame{
    public DailyStockChart(){
        super("Daily Stock Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
