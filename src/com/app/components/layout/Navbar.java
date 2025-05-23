package com.app.components.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.app.partials.interfaces.*;

public abstract class Navbar extends JFrame implements ActionListener,AppConstants {

    final Toolkit toolkit = Toolkit.getDefaultToolkit();
    final String[] menuStrings = new String[] { "Home", "Purchase", "Sale", "Stock", "Expiry", "Report", "Utilities",
            "Analytics", "Setting",
            "BackUp And Exit" };
    final String[][] menuItemStrings = new String[][] { { "Home" },
            { "Purchase Entry Ctrl+P", "Purchase Return Ctrl+Alt+P", "Product", "Group", "Sub Group" },
            { "Sale", "Sales Return" }, { "Stock Entry" }, { "Expiry Entry Ctrl+E", "Near Expiry" },
            { "Sale", "Stock", "Expiry", "Sales Return", "Purchase Return" },
            { "Quotation", "Debit Note", "Credit Note" },
            { "Sale Analysis", "Purchase Analysis", "Stock Analysis", "Expiry Analysis" }, {
                    "Change Password"
            }, {} };
    final JMenuBar menubar;

    public Navbar() {
        setSize(toolkit.getScreenSize());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        menubar = new JMenuBar();

        for (int i = 0; i < menuStrings.length; i++) {
            JMenu menu = new JMenu(menuStrings[i]);
            menu.setFont(menuFont);
            for (int j = 0; j < menuItemStrings[i].length; j++) {
                JMenuItem menuItem = new JMenuItem(menuItemStrings[i][j]);
                menu.add(menuItem);
            }
            menubar.add(menu);
        }
        setJMenuBar(menubar);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                boolean res = JOptionPane.showConfirmDialog(Navbar.this, "Are you sure to exit?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == 0;
                if (res) {
                    dispose();
                }
            }
        });
    }
}
