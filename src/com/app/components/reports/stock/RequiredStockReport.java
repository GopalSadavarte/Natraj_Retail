package com.app.components.reports.stock;

import javax.swing.JInternalFrame;
import java.awt.*;

public class RequiredStockReport extends JInternalFrame{
    public RequiredStockReport(){
        super("Required Stock Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
