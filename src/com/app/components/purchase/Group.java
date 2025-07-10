package com.app.components.purchase;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import com.app.components.abstracts.AbstractButton;
import com.app.components.purchase.support.GroupView;
import com.app.config.*;

public final class Group extends AbstractButton {

    JPanel mainPanel, headingPanel, fieldPanel;
    JLabel heading, groupIdLabel, groupNameLabel;
    JTextField groupIdField, groupNameField;
    final Integer SAVE_ACTION = 1, UPDATE_ACTION = 0, DELETE_ACTION = -1;
    Integer action = SAVE_ACTION;
    int groupIdForUpdateOrDelete = 0;
    GroupView view;

    public Group(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Group", currentMenu, frameTracker, frameKeyMap, dialog);
        setBackground(Color.white);
        setSize(toolkit.getScreenSize());
        setLayout(flowLayoutCenter);
        setView("Group");

        mainPanel = new JPanel(flowLayoutCenter);
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        mainPanel.setPreferredSize(new Dimension(700, 300));

        heading = new JLabel("Item Group");
        heading.setPreferredSize(new Dimension(600, 70));
        heading.setFont(headingFont);
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(heading);

        fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setPreferredSize(new Dimension(650, 50));
        fieldPanel.setBackground(Color.white);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        groupIdLabel = new JLabel("Group ID :");
        groupIdLabel.setFont(labelFont);
        groupIdLabel.setForeground(Color.darkGray);
        groupIdLabel.setPreferredSize(labelSize);
        groupIdLabel.setHorizontalAlignment(SwingConstants.LEFT);

        groupIdField = new JTextField(10);
        groupIdField.setFont(labelFont);
        groupIdField.setEnabled(false);
        groupIdField.setBackground(lemonYellow);
        groupIdField.setDisabledTextColor(Color.darkGray);
        groupIdField.addFocusListener(this);
        groupIdField.addKeyListener(this);

        fieldPanel.add(groupIdLabel);
        fieldPanel.add(groupIdField);

        mainPanel.add(fieldPanel);

        fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setPreferredSize(new Dimension(650, 50));
        fieldPanel.setBackground(Color.white);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        groupNameLabel = new JLabel("Group Name :");
        groupNameLabel.setForeground(Color.darkGray);
        groupNameLabel.setPreferredSize(labelSize);
        groupNameLabel.setFont(labelFont);

        groupNameField = new JTextField(20);
        groupNameField.setBackground(lemonYellow);
        groupNameField.setFont(labelFont);
        groupNameField.addFocusListener(this);

        fieldPanel.add(groupNameLabel);
        fieldPanel.add(groupNameField);

        mainPanel.add(fieldPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setLastId("groups", groupIdField);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(newBtn)) {
            reCreate();
        }

        if (source.equals(exitBtn)) {
            frameTracker.remove(frameKeyMap.get(this));
            dispose();
        }

        if (source.equals(cancelBtn)) {
            editBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
        }

        if (source.equals(editBtn) || source.equals(deleteBtn)) {
            groupIdField.setEnabled(true);
            groupIdField.requestFocus();
            groupIdField.setText("");

            if (source.equals(editBtn)) {
                action = UPDATE_ACTION;
            } else {
                action = DELETE_ACTION;
            }
        }

        if (source.equals(selectBtn)) {
            JTable table = view.getTable();
            int row = table.getSelectedRow();
            String value = table.getValueAt(row, 2).toString();
            String id = table.getValueAt(row, 1).toString();
            groupNameField.setText(value);
            groupIdForUpdateOrDelete = Integer.valueOf(id);
            groupIdField.setText(id);
            dataViewer.setVisible(false);
        }

        if (source.equals(saveBtn)) {
            if (action.equals(SAVE_ACTION)) {
                String groupName = groupNameField.getText().trim().toUpperCase();
                if (isNameValid(groupName)) {
                    try {
                        String query = "insert into groups (g_name)values(?)";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setString(1, groupName);
                        int affectedRows = pst.executeUpdate();
                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(this, "group added!");
                            reCreate();
                        } else
                            JOptionPane.showMessageDialog(this, "Group does not added,try again!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage() + " at Group.java 141");
                        JOptionPane.showMessageDialog(this, exc.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid group name!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (action.equals(UPDATE_ACTION)) {
                String groupName = groupNameField.getText().trim().toUpperCase();
                if (isNameValid(groupName)) {
                    if (!isGroupAlreadyExist(groupName)) {
                        try {
                            String query = "update groups set g_name = ? where id = ?";
                            PreparedStatement pst = DBConnection.con.prepareStatement(query);
                            pst.setString(1, groupName);
                            pst.setInt(2, groupIdForUpdateOrDelete);

                            int affectedRows = pst.executeUpdate();
                            if (affectedRows > 0) {
                                JOptionPane.showMessageDialog(this, "group updated!");
                                reCreate();
                            } else
                                JOptionPane.showMessageDialog(this, "Group does not update,try again!", "Error",
                                        JOptionPane.ERROR_MESSAGE);

                        } catch (Exception exc) {
                            System.out.println(exc.getMessage() + " at Group.java 164");
                            JOptionPane.showMessageDialog(this, exc.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);

                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "group already exist!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {

                    JOptionPane.showMessageDialog(this, "Please enter a valid group name!", "Error",
                            JOptionPane.ERROR_MESSAGE);

                }
            }
            if (action.equals(DELETE_ACTION)) {
                if (groupIdForUpdateOrDelete != 0) {
                    try {
                        String query = "delete from groups where id = ?";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setInt(1, groupIdForUpdateOrDelete);
                        int affectedRows = pst.executeUpdate();
                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(this, "group deleted!");
                            reCreate();
                        } else
                            JOptionPane.showMessageDialog(this, "Group does not delete,try again!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(this, exc.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.out.println(exc.getMessage() + " at Group.java 209");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter group id!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            groupIdForUpdateOrDelete = 0;
        }
    }

    private boolean isGroupAlreadyExist(String group) {
        try {
            String query = "select * from groups where g_name = ? limit 1";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setString(1, group.toUpperCase());
            ResultSet result = pst.executeQuery();
            return result.next();
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at Group.java 202");
        }
        return true;
    }

    public void focusGained(FocusEvent e) {
        JTextField field = (JTextField) e.getSource();
        field.setBackground(lightYellow);
    }

    public void focusLost(FocusEvent e) {
        JTextField field = (JTextField) e.getSource();
        field.setBackground(lemonYellow);
    }

    public void keyReleased(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        Object source = e.getSource();
        if (source.equals(groupIdField)) {
            if (key.equals("Enter")) {
                try {
                    int id = Integer.parseInt(groupIdField.getText());
                    String query = "select * from groups where id = ? limit 1";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, id);
                    ResultSet res = pst.executeQuery();
                    if (res.next()) {
                        groupIdField.setText(res.getString("id"));
                        groupNameField.setText(res.getString("g_name"));
                        groupNameField.requestFocus();
                        groupIdForUpdateOrDelete = id;
                    } else {
                        JOptionPane.showMessageDialog(this, "Group not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exc) {
                    System.out.println(exc.getMessage() + " at Group.java 177");
                    JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (key.equals("F1")) {
                view = new GroupView(this);
                view.getScrollPane().setPreferredSize(new Dimension(800, 350));
                createViewer(view);
            }
        }
    }
}
