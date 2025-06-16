package com.app.components.reports.expiry;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlyExpiryReport extends JInternalFrame{
    public MonthlyExpiryReport(){
        super("Monthly Expiry Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
