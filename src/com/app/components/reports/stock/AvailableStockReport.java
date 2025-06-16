package com.app.components.reports.stock;

import javax.swing.JInternalFrame;
import java.awt.*;

public class AvailableStockReport extends JInternalFrame{
    public AvailableStockReport(){
        super("Available Stock Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
