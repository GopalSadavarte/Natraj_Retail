package com.app.components.reports.expiry;

import javax.swing.JInternalFrame;
import java.awt.*;

public class DailyExpiryReport extends JInternalFrame{
    public DailyExpiryReport(){
        super("Daily Expiry Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
