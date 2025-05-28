package com.app.components.utilities;

import javax.swing.JInternalFrame;
import java.awt.*;

public class Quotation extends JInternalFrame{
    public Quotation(){
        super("Quotation", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
