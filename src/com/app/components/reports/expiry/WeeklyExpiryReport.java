package com.app.components.reports.expiry;

import javax.swing.JInternalFrame;
import java.awt.*;

public class WeeklyExpiryReport extends JInternalFrame{
    public WeeklyExpiryReport(){
        super("Weekly Expiry Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
