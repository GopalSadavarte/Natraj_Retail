package com.app.components.purchase;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import com.app.components.abstracts.AbstractButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.HashMap;

public final class PurchaseEntry extends AbstractButton{
    public PurchaseEntry(JMenuItem currentMenuItem,HashMap<String,JInternalFrame> frameTracker,HashMap<JInternalFrame,String>frameKeyMap,final JDialog dialog){
        super("Purchase Entry", currentMenuItem, frameTracker, frameKeyMap, dialog);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }

    public void focusGained(FocusEvent e){}
    public void focusLost(FocusEvent e){}
    public void actionPerformed(ActionEvent e){}
}
