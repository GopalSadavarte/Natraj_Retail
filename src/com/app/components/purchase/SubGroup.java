package com.app.components.purchase;

import com.app.components.abstracts.AbstractButton;
import com.app.components.purchase.support.SubGroupView;
import com.app.config.DBConnection;
import com.app.partials.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import javax.swing.*;

public final class SubGroup extends AbstractButton {

    JPanel mainPanel, headingPanel, fieldPanel;
    JLabel heading, subGroupIdLabel, subGroupNameLabel, groupNameLabel;
    JTextField subGroupIdField, subGroupNameField;
    JComboBox<String> groupNames;
    final Integer SAVE_ACTION = 1, UPDATE_ACTION = 0, DELETE_ACTION = -1;
    Integer action = SAVE_ACTION;
    int subGroupIdForUpdateOrDelete = 0;
    Dimension labelSize = new Dimension(200, 30);
    final HashMap<String, Integer> groupNameIdMap = new HashMap<>();
    SubGroupView view;

    public SubGroup(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Sub Group", currentMenu, frameTracker, frameKeyMap, dialog);

        setBackground(Color.white);
        setSize(toolkit.getScreenSize());
        setLayout(flowLayoutCenter);

        mainPanel = new JPanel(flowLayoutCenter);
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        mainPanel.setPreferredSize(new Dimension(700, 400));

        heading = new JLabel("Item Sub Group");
        heading.setPreferredSize(new Dimension(600, 70));
        heading.setFont(headingFont);
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(heading);

        fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setPreferredSize(new Dimension(650, 50));
        fieldPanel.setBackground(Color.white);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        subGroupIdLabel = new JLabel("Sub Group ID :");
        subGroupIdLabel.setFont(labelFont);
        subGroupIdLabel.setForeground(Color.darkGray);
        subGroupIdLabel.setPreferredSize(labelSize);
        subGroupIdLabel.setHorizontalAlignment(SwingConstants.LEFT);

        subGroupIdField = new JTextField(10);
        subGroupIdField.setFont(labelFont);
        subGroupIdField.setEnabled(false);
        subGroupIdField.setBackground(lemonYellow);
        subGroupIdField.setDisabledTextColor(Color.darkGray);
        subGroupIdField.addFocusListener(this);
        subGroupIdField.addKeyListener(this);

        fieldPanel.add(subGroupIdLabel);
        fieldPanel.add(subGroupIdField);

        mainPanel.add(fieldPanel);

        fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setPreferredSize(new Dimension(650, 50));
        fieldPanel.setBackground(Color.white);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        subGroupNameLabel = new JLabel("Sub Group Name :");
        subGroupNameLabel.setForeground(Color.darkGray);
        subGroupNameLabel.setPreferredSize(labelSize);
        subGroupNameLabel.setFont(labelFont);

        subGroupNameField = new JTextField(20);
        subGroupNameField.setBackground(lemonYellow);
        subGroupNameField.setFont(labelFont);
        subGroupNameField.addFocusListener(this);

        fieldPanel.add(subGroupNameLabel);
        fieldPanel.add(subGroupNameField);

        mainPanel.add(fieldPanel);

        fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setPreferredSize(new Dimension(650, 50));
        fieldPanel.setBackground(Color.white);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        groupNameLabel = new JLabel("Group Name :");
        groupNameLabel.setForeground(Color.darkGray);
        groupNameLabel.setPreferredSize(labelSize);
        groupNameLabel.setFont(labelFont);

        groupNames = new JComboBox<String>();
        groupNames.setBackground(lemonYellow);
        groupNames.setFont(labelFont);
        groupNames.addFocusListener(this);
        groupNames.setPreferredSize(new Dimension(350, 30));

        fieldPanel.add(groupNameLabel);
        fieldPanel.add(groupNames);

        mainPanel.add(fieldPanel);
        mainPanel.add(buttonPanel);

        subGroupNameField.addKeyListener(new CustomKeyListener(groupNames));

        add(mainPanel);
        setLastId("sub_groups", subGroupIdField);
        setGroups();
        setVisible(true);
    }

    private void setGroups() {
        try {
            String query = "select * from groups";
            ResultSet result = DBConnection.executeQuery(query);
            while (result.next()) {
                String name = result.getString("g_name");
                int id = result.getInt("id");
                groupNames.addItem(name);
                groupNameIdMap.put(name, id);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at SubGroup.java 120");
        }
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
            subGroupIdField.setEnabled(true);
            subGroupIdField.requestFocus();
            subGroupIdField.setText("");

            if (source.equals(editBtn)) {
                action = UPDATE_ACTION;
            } else {
                action = DELETE_ACTION;
            }
        }

        if (source.equals(saveBtn)) {
            if (action.equals(SAVE_ACTION) || action.equals(UPDATE_ACTION)) {
                String subGroupName = subGroupNameField.getText().trim().toUpperCase();
                if (isNameValid(subGroupName)) {
                    try {
                        int groupId = groupNameIdMap.get(groupNames.getSelectedItem());

                        String query = "";
                        if (action.equals(SAVE_ACTION))
                            query = "insert into sub_groups (sub_group_name,group_id)values(?,?)";
                        else
                            query = "update sub_groups set sub_group_name = ?, group_id = ? where id = ?";

                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setString(1, subGroupName);
                        pst.setInt(2, groupId);

                        if (action.equals(UPDATE_ACTION))
                            pst.setInt(3, subGroupIdForUpdateOrDelete);

                        int affectedRows = pst.executeUpdate();
                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(this,
                                    action.equals(SAVE_ACTION) ? "Sub Group added" : "Information updated!!");
                            reCreate();
                        } else {
                            JOptionPane.showMessageDialog(this, "Process failed,try again!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage() + " at SubGroup.java 168");
                        JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
            if (action.equals(DELETE_ACTION)) {
                try {
                    String query = "delete from sub_groups where id = ?";
                    if (subGroupIdForUpdateOrDelete != 0) {
                        boolean con = JOptionPane.showConfirmDialog(this,
                                "Are you sure to remove this information?") == 0;
                        if (con) {
                            PreparedStatement pst = DBConnection.con.prepareStatement(query);
                            pst.setInt(1, subGroupIdForUpdateOrDelete);
                            int affectedRows = pst.executeUpdate();
                            if (affectedRows > 0) {
                                JOptionPane.showMessageDialog(this, "Information removed!");
                                reCreate();
                            } else
                                JOptionPane.showMessageDialog(this, "Data cannot removed,try again!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Sub group not selected!");
                    }
                } catch (Exception exc) {
                    System.out.println(exc.getMessage() + " at SubGroup.java 218");
                    JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            subGroupIdForUpdateOrDelete = 0;
        }

        if (source.equals(selectBtn)) {
            JTable table = view.getTable();
            int row = table.getSelectedRow();
            String id = table.getValueAt(row, 1).toString();
            String subGroup = table.getValueAt(row, 2).toString();
            String group = table.getValueAt(row, 3).toString();

            subGroupIdField.setText(id);
            groupNames.setSelectedItem(group);
            subGroupNameField.setText(subGroup);
            subGroupIdForUpdateOrDelete = Integer.valueOf(id);

            dataViewer.setVisible(false);

        }
    }

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        Object source = e.getSource();
        if (source.equals(subGroupIdField)) {
            if (key.toLowerCase().equals("enter")) {
                try {
                    int id = Integer.parseInt(subGroupIdField.getText());
                    String query = "select * from groups,sub_groups where groups.id = sub_groups.group_id and sub_groups.id = ? limit 1";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, id);
                    ResultSet result = pst.executeQuery();
                    if (result.next()) {
                        subGroupIdForUpdateOrDelete = id;
                        subGroupNameField.setText(result.getString("sub_group_name"));
                        groupNames.setSelectedItem(result.getString("g_name"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid sub group id!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exc) {
                    System.out.println(exc.getMessage() + " at SubGroup.java 211");
                    JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (key.equals("F1")) {
                view = new SubGroupView();
                view.scrollPane.setPreferredSize(new Dimension(800, 350));
                createViewer(view);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        String key = KeyEvent.getKeyText(e.getKeyCode());
        if (source.equals(searchField)) {
            if (key.equals("Enter")) {
                selectBtn.doClick();
            } else {
                String value = searchField.getText().toUpperCase().trim();
                JTable table = view.getTable();
                int rows = table.getRowCount();
                for (int i = 0; i < rows; i++) {
                    String rowVal = table.getValueAt(i, 2).toString();
                    table.setRowHeight(i, rowVal.contains(value) ? table.getRowHeight() : 1);
                    if (rowVal.contains(value))
                        table.setRowSelectionInterval(i, i);
                }
            }
        }
    }

    public void focusGained(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        component.setBackground(lightYellow);
    }

    public void focusLost(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        component.setBackground(lemonYellow);
    }
}
