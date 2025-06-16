package com.app.components.sales;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import java.awt.*;

public class SalesReturnEntry extends JInternalFrame{
    final JDialog dialog;
    public SalesReturnEntry(final JDialog dialog){
        super("Sales Return Entry", false, true, true);
        setBackground(Color.white);
        this.dialog = dialog;
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
