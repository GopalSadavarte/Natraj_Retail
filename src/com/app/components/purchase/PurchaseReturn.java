package com.app.components.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class PurchaseReturn extends JInternalFrame{
    public PurchaseReturn(){
        super("Purchase Return Entry", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
