package com.app.components.reports.expiry;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlyExpiryReport extends JInternalFrame{
    public YearlyExpiryReport(){
        super("Yearly Expiry Report", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
