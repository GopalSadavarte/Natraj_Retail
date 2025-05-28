package com.app.components.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class PurchaseEntry extends JInternalFrame{
    public PurchaseEntry(){
        super("Purchase Entry", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
