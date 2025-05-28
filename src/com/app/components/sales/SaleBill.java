package com.app.components.sales;

import javax.swing.JInternalFrame;
import java.awt.*;

public class SaleBill extends JInternalFrame{
    public SaleBill(){
        super("Sale Bill", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
