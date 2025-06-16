package com.app.partials.event;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class CustomKeyListener extends KeyAdapter {
    JComponent target;

    public CustomKeyListener(JComponent target) {
        this.target = target;
    }

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        if (key.equals("Enter")) {
            if (target instanceof JButton) {
                ((JButton) target).doClick();
            } else {
                target.requestFocus();
                if (target instanceof JTextField)
                    ((JTextField) target).selectAll();
            }
        }
    }
}
