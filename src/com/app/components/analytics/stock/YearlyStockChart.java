package com.app.components.analytics.stock;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlyStockChart extends JInternalFrame{
    public YearlyStockChart(){
        super("Yearly Stock Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
