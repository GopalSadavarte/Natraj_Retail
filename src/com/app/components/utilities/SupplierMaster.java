package com.app.components.utilities;

import javax.swing.JInternalFrame;

import com.app.partials.interfaces.AppConstants;

import java.awt.*;

public class SupplierMaster extends JInternalFrame implements AppConstants {
    public SupplierMaster() {
        super("Supplier Master", false, true, true);
        setBackground(Color.white);
        setSize(toolkit.getScreenSize());
        setVisible(true);
    }
}
