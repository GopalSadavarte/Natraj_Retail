package com.app.partials.event;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;

public class KeyListenerButtonSwitch extends KeyAdapter {
    JButton prevTarget, nextTarget;

    public KeyListenerButtonSwitch(JButton prevTarget, JButton nextTarget) {
        this.prevTarget = prevTarget;
        this.nextTarget = nextTarget;
    }

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        JButton btn = (JButton) e.getSource();
        if (key.equals("Up") || key.equals("Left")) {
            prevTarget.requestFocus();
        }
        if (key.equals("Down") || key.equals("Right")) {
            nextTarget.requestFocus();
        }

        if (key.equals("Enter")) {
            btn.doClick();
        }
    }
}
