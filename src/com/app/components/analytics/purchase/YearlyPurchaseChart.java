package com.app.components.analytics.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlyPurchaseChart extends JInternalFrame{
    public YearlyPurchaseChart(){
        super("Yearly Purchase Analysis", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
