package com.app.components.reports.sales_return;

import javax.swing.JInternalFrame;
import java.awt.*;

public class MonthlySalesReturnReport extends JInternalFrame{
    public MonthlySalesReturnReport(){
        super("Monthly Sales Return Report", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
