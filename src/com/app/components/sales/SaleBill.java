package com.app.components.sales;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import com.app.components.abstracts.AbstractButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.HashMap;

public final class SaleBill extends AbstractButton {

    public SaleBill(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Sale Bill", currentMenu, frameTracker, frameKeyMap, dialog);
        setBackground(Color.white);
        setLayout(new FlowLayout());
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        buttonPanel.setPreferredSize(new Dimension(120, 400));
        add(buttonPanel);
        setVisible(true);
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
