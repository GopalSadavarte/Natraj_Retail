package com.app.components.purchase;

import javax.swing.JInternalFrame;
import java.awt.*;

public class Product extends JInternalFrame{
    public Product(){
        super("Product Manager", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());setVisible(true);
    }
}
