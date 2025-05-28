package com.app.components.reports.sales_return;

import javax.swing.JInternalFrame;
import java.awt.*;

public class YearlySalesReturnReport extends JInternalFrame{
    public YearlySalesReturnReport(){
        super("Yearly Sales Return Report", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
