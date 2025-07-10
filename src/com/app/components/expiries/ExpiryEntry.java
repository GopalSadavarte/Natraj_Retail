package com.app.components.expiries;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import com.app.components.abstracts.AbstractButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.HashMap;

public class ExpiryEntry extends AbstractButton {

    final JDialog dialog;

    public ExpiryEntry(JMenuItem currentMenuItem, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {
        super("Expiry Entry", currentMenuItem, frameTracker, frameKeyMap, dialog);
        this.dialog = dialog;
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
