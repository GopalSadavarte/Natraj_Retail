package com.app.components.expiries;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import java.awt.*;

public class ExpiryEntry extends JInternalFrame {

    final JDialog dialog;

    public ExpiryEntry(final JDialog dialog) {
        super("Expiry Entry", false, true, true);
        this.dialog = dialog;
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
