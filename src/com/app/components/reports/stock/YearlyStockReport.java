package com.app.components.reports.stock;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlyStockReport extends JInternalFrame{
    public YearlyStockReport(){
        super("Yearly Stock Report", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
