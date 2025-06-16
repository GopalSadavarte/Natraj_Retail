package com.app.components.utilities;

import javax.swing.*;
import java.awt.*;

public class Quotation extends JInternalFrame {
    public Quotation(final JDialog dialog) {
        super("Quotation", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
