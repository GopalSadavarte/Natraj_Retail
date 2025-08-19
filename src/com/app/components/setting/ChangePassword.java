/**
 * 
 * 
 * This file contains the class which have a form to change the password for login the 
 * system.
 * 
 * 
 */

package com.app.components.setting;

import javax.swing.*;
import com.app.config.*;
import com.app.partials.event.*;
import com.app.partials.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

/**
 * The ChangePassword class have form to change the password of the system which
 * are use to login the system.
 */
public final class ChangePassword extends JInternalFrame
        implements ActionListener, AppConstants, Validation, FocusListener {

    JPanel mainPanel, headingPanel, contentPanel, buttonPanel;
    JButton save, exit;
    JLabel headingLabel, oldPassLabel, newPassLabel, confirmPassLabel;
    JTextField oldPassField;
    JPasswordField newPassField, confirmPassField;
    HashMap<String, JInternalFrame> frameTracker;
    HashMap<JInternalFrame, String> frameKeyMap;

    public ChangePassword( HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap) {

            super("Change Password",false,true,true);
        // set the menus.
        this.frameKeyMap = frameKeyMap;
        this.frameTracker = frameTracker;
        // set the flow layout to panel.
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
        setBackground(Color.white);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());

        // set the flow layout to panel.
        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(400, 410));
        mainPanel.setBorder(border);

        // create heading panel which hold the heading of the form.
        headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(390, 60));

        // create heading label.
        headingLabel = new JLabel("Change Password");
        headingLabel.setForeground(Color.darkGray);
        headingLabel.setFont(headingFont);

        // add label to heading panel.
        headingPanel.add(headingLabel);

        // add heading panel to the main panel.
        mainPanel.add(headingPanel);

        // create the content panel which holds the labels and fields for changing the
        // password.
        contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        contentPanel.setBackground(Color.white);
        contentPanel.setPreferredSize(new Dimension(390, 300));

        // size for label.
        Dimension labelSize = new Dimension(315, 20);

        // create label and field.
        oldPassLabel = new JLabel("Enter Old Password :");
        oldPassLabel.setFont(labelFont);
        oldPassLabel.setPreferredSize(labelSize);
        oldPassLabel.setForeground(Color.darkGray);

        oldPassField = new JTextField(18);
        oldPassField.setFont(labelFont);
        oldPassField.addFocusListener(this);
        oldPassField.setBackground(lemonYellow);

        // add label and field to content panel.
        contentPanel.add(oldPassLabel);
        contentPanel.add(oldPassField);

        // create label and field.
        newPassLabel = new JLabel("Enter New Password :");
        newPassLabel.setFont(labelFont);
        newPassLabel.setForeground(Color.darkGray);
        newPassLabel.setPreferredSize(labelSize);

        newPassField = new JPasswordField(18);
        newPassField.setFont(labelFont);
        newPassField.addFocusListener(this);
        newPassField.setEchoChar('*');
        newPassField.setBackground(lemonYellow);

        // add label and field to content panel.
        contentPanel.add(newPassLabel);
        contentPanel.add(newPassField);

        // create label and field.
        confirmPassLabel = new JLabel("Repeat Password :");
        confirmPassLabel.setFont(labelFont);
        confirmPassLabel.setPreferredSize(labelSize);
        confirmPassLabel.setForeground(Color.darkGray);

        confirmPassField = new JPasswordField(18);
        confirmPassField.setFont(labelFont);
        confirmPassField.addFocusListener(this);
        confirmPassField.setEchoChar('*');
        confirmPassField.setBackground(lemonYellow);

        // add label and field to content panel.
        contentPanel.add(confirmPassLabel);
        contentPanel.add(confirmPassField);

        // button panel which hold the buttons.
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(390, 40));
        buttonPanel.setBackground(Color.white);

        // save button to update the info.
        save = new JButton("Save");
        save.setBackground(lightBlue);
        save.setForeground(Color.white);
        save.setPreferredSize(new Dimension(100, 30));
        save.setFont(labelFont);
        save.addActionListener(this);

        // exit button to leave from change password panel.
        exit = new JButton("Exit");
        exit.setBackground(orange);
        exit.setForeground(Color.white);
        exit.setPreferredSize(new Dimension(100, 30));
        exit.setFont(labelFont);
        exit.addActionListener(this);

        // add buttons to button panel.
        buttonPanel.add(save);
        buttonPanel.add(exit);

        // add button panel to content panel.
        contentPanel.add(buttonPanel);

        // add content panel to main panel.
        mainPanel.add(contentPanel);

        // add main panel to frame.
        add(mainPanel);

        // add key listener to fields,which allows user press enter key then the focus
        // switch to other component.
        oldPassField.addKeyListener(new CustomKeyListener(newPassField));
        newPassField.addKeyListener(new CustomKeyListener(confirmPassField));
        confirmPassField.addKeyListener(new CustomKeyListener(save));

        // set the panel visible.
        setVisible(true);
    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        JTextField textField = ((JTextField) source);
        // change background color when focus gained.
        textField.setBackground(lightYellow);
    }

    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        JTextField textField = ((JTextField) source);
        // change background color when focus lost.
        textField.setBackground(lemonYellow);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check the source is save or not.
        if (source.equals(save)) {
            // Get the values from fields.
            String oldPassVal = oldPassField.getText().trim();
            String newPassVal = new String(newPassField.getPassword()).trim();
            String conPassVal = new String(confirmPassField.getPassword()).trim();

            // validate the new password.
            boolean passResult = isPasswordValid(newPassVal);
            try {
                // check old pass field value is empty or null or not.
                if (!oldPassVal.isBlank()) {
                    // query that checks the password is already exist or not.
                    String query = "SELECT * FROM users WHERE password = ?";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setString(1, oldPassVal);
                    ResultSet resultSet = pst.executeQuery();
                    // password is exist then user which are requested to update the password is
                    // Authorized or not.
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        // check the new password is valid or not.
                        if (passResult) {
                            // check the both password are not equal.
                            if (!newPassVal.equals(oldPassVal)) {
                                // checks the confirm password and new password is equal or not.
                                if (conPassVal.equals(newPassVal)) {
                                    // both password are equal then this will updated by new password.
                                    query = "UPDATE users SET password = ? WHERE id = ?";
                                    pst = DBConnection.con.prepareStatement(query);
                                    pst.setString(1, newPassVal);
                                    pst.setInt(2, id);

                                    boolean res = (pst.executeUpdate() > 0);
                                    // check password updated successfully or not.
                                    if (res) {
                                        // show the message.
                                        JOptionPane.showMessageDialog(this, "Password Updated Successfully!");
                                        oldPassField.setText("");
                                        newPassField.setText("");
                                        confirmPassField.setText("");
                                        // set focus to old password field.
                                        oldPassField.requestFocus();
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Password cannot updated,try later!");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this,
                                            "The Repeat password doesn't match with new password!", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "The New password is similar to Old password!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Invalid New Password! \n Password must contains exact 8 characters with digits or lowercase alphabets or uppercase alphabets!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "The old password is wrong, enter correct password!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please Enter old password!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Connection error,try again!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // checks the source is exit button or not.
        if (source.equals(exit)) {
           frameTracker.remove(frameKeyMap.get(this));
           dispose();
        }
    }
}
/**
 * 
 * This component are ends here...
 */