package com.app.components.analytics.stock;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklyStockChart extends JInternalFrame{
    public WeeklyStockChart(){
        super("Weekly Stock Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
