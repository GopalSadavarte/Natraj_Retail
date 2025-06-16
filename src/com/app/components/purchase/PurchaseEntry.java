package com.app.components.purchase;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import java.awt.*;

public final class PurchaseEntry extends JInternalFrame{
    public PurchaseEntry(final JDialog dialog){
        super("Purchase Entry", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
