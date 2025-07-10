package com.app.components.purchase;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import java.awt.*;
import java.util.HashMap;

public final class PurchaseReturn extends JInternalFrame {
    public PurchaseReturn(JMenuItem currentMenuItem,HashMap<String,JInternalFrame> frameTracker,HashMap<JInternalFrame,String>frameKeyMap,final JDialog dialog) {
        super("Purchase Return Entry", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
