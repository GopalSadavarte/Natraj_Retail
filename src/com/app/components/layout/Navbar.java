package com.app.components.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.app.partials.interfaces.*;

public abstract class Navbar extends JFrame implements ActionListener, AppConstants {

    final String[] menuStrings = new String[] { "Purchase", "Sale", "Expiry", "Report", "Utilities",
            "Analytics", "Setting",
            "BackUp And Exit" };
    final String[][] menuItemStrings = new String[][] {
            { "Purchase Entry Ctrl+P", "Purchase Return Ctrl+Alt+P", "Product Alt+I", "Group", "Sub Group" },
            { "Sale Ctrl+S", "Sales Return Ctrl+Alt+S" }, { "Expiry Entry Ctrl+E", "Near Expiry" },
            { "Purchase", "Sale", "Stock", "Expiry", "Sales Return", "Purchase Return" },
            { "Quotation", "Debit Note", "Credit Note" },
            { "Sale Analysis", "Purchase Analysis", "Stock Analysis", "Expiry Analysis" }, {
                    "Change Password"
            } };
    final String[][] reportItemStrings = new String[][] {
            { "Daily", "Weekly", "Monthly", "Yearly" }, { "Daily", "Weekly", "Monthly", "Yearly" },
            { "available", "required", "less sold", "Yearly" }, { "Daily", "Weekly", "Monthly", "Yearly" },
            { "Daily", "Weekly", "Monthly", "Yearly" }, { "Daily", "Weekly", "Monthly", "Yearly" }
    };
    final JMenuBar menubar = new JMenuBar();
    protected final HashMap<String, JMenuItem> sources = new HashMap<>();
    protected final static JDialog dialogBox = new JDialog();

    public Navbar() {
        setSize(toolkit.getScreenSize());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        dialogBox.getContentPane().setBackground(Color.white);
        menubar.setLayout(new FlowLayout());
        menubar.setBackground(Color.white);
        int k = 0;
        for (int i = 0; i < menuStrings.length; i++) {

            if (i == (menuStrings.length - 1)) {
                JMenuItem menuItem = new JMenuItem(menuStrings[i]);
                menuItem.addActionListener(this);
                menuItem.setFont(menuFont);
                menuItem.setBackground(Color.white);
                menuItem.setForeground(Color.red);
                sources.put(menuItem.getText(), menuItem);
                menubar.add(menuItem);
                continue;
            }

            JMenu menu = new JMenu(menuStrings[i]);
            menu.setFont(menuFont);
            if (i == 3 || i == 5) {
                for (int j = 0; j < menuItemStrings[k].length; j++) {
                    JMenu subMenu = new JMenu(menuItemStrings[k][j]);
                    subMenu.setFont(menuFont);
                    for (int m = 0; m < reportItemStrings[j].length; m++) {
                        JMenuItem item = new JMenuItem(reportItemStrings[j][m]);
                        item.addActionListener(this);
                        item.setActionCommand(menuItemStrings[k][j] + " " + reportItemStrings[j][m]);
                        item.setFont(menuFont);
                        sources.put(item.getText(), item);
                        subMenu.add(item);
                    }
                    menu.add(subMenu);
                }
                k++;
                menubar.add(menu);
                continue;
            }
            for (int j = 0; j < menuItemStrings[k].length; j++) {
                JMenuItem menuItem = new JMenuItem(menuItemStrings[k][j]);
                menuItem.addActionListener(this);
                menuItem.setFont(menuFont);
                sources.put(menuItem.getText(), menuItem);
                menu.add(menuItem);
            }
            k++;
            menubar.add(menu);
        }
        setJMenuBar(menubar);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                boolean res = JOptionPane.showConfirmDialog(Navbar.this, "Are you sure to exit?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == 0;
                if (res) {
                    dialogBox.dispose();
                    dispose();
                }
            }
        });
    }
}
