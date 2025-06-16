package com.app.components.reports.stock;

import javax.swing.JInternalFrame;
import java.awt.*;
public class LessSoldStockReport extends JInternalFrame{
    public LessSoldStockReport(){
        super("Less Sold Stock Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
