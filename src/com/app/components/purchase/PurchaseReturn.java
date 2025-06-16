package com.app.components.purchase;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import java.awt.*;

public final class PurchaseReturn extends JInternalFrame {
    public PurchaseReturn(final JDialog dialog) {
        super("Purchase Return Entry", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
