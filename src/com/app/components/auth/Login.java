/**
 * 
 * This is a login component, the part of authentication .
 * In this components ask to user there authentication details 
 * like there username and password to prove it is a valid user 
 * or not. 
 * 
 */
package com.app.components.auth;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import com.app.partials.event.*;
import com.app.partials.interfaces.*;
import com.app.config.*;
import com.app.components.*;

/**
 * The Login class creates an component to handle application authentication.
 */
public class Login extends JFrame implements ActionListener, KeyListener, AppConstants {

    // instance variables/references/identifiers.
    JButton login, exit;
    JLabel heading, nameLabel, passwordLabel;
    JTextField nameTextField;
    JPasswordField passwordTextField;
    JPanel headingPanel, btnPanel, contentPanel;
    Toolkit toolkit;
    // ImageIcon icon = new ImageIcon(Login.class.getResource("logo.png"));

    /**
     * Constructor to create a Login component.
     */
    public Login() {
        // this function are use to hide all default frame functionality.
        setUndecorated(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        setSize(new Dimension(400, 300));
        setBackground(Color.white);
        // setIconImage(icon.getImage());

        // gets an default screen (microsoft window) layout
        toolkit = Toolkit.getDefaultToolkit();

        int width = (toolkit.getScreenSize().width - getWidth()) / 2;

        int height = (toolkit.getScreenSize().height - getHeight()) / 2;

        // sets the frame to the center of the screen
        setLocation(new Point(width, height));

        // functions to allocate memory to the frame components
        allocateToPanels();
        allocateToFields();
        allocateToLabels();

        // adds the components within different panels
        headingPanel.add(heading);
        contentPanel.add(nameLabel);
        contentPanel.add(nameTextField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordTextField);
        btnPanel.add(login);
        btnPanel.add(exit);

        // add an panels to the frame.
        add(headingPanel);
        add(contentPanel);
        add(btnPanel);

        // the KeyListener for password field .when user type Escape key of keyboard in
        // this field then it will call and execute the frame close operation.
        passwordTextField.addKeyListener(this);

        // the KeyListener for name field .when user type Escape key of keyboard in this
        // field then it will call and execute the frame close operation.
        nameTextField.addKeyListener(this);

        // this is same event of above but it applies on the frame
        addKeyListener(this);
        setVisible(true);
    }

    public void keyPressed(KeyEvent e) {
        //
    }

    public void keyReleased(KeyEvent e) {
        // it is use to get typed key code/number
        int code = e.getKeyCode();
        // gets an name of the key with the help of key code
        String keyboardBtn = KeyEvent.getKeyText(code);
        if (keyboardBtn.equals("Escape")) {
            dispose();
        }
    }

    public void keyTyped(KeyEvent e) {
        //
    }

    /**
     * this function is allocates an memory to all JPanels with there styles.
     */
    private void allocateToPanels() {

        headingPanel = new JPanel();
        headingPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
        // headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(300, 50));

        contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        // contentPanel.setBackground(Color.white);
        contentPanel.setPreferredSize(new Dimension(300, 150));

        btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnPanel.setPreferredSize(new Dimension(300, 40));
        // btnPanel.setBackground(Color.white);
    }

    /**
     * This function allocates an memory to JLabels with there styles.
     */
    private void allocateToLabels() {

        Dimension labelSize = new Dimension(270, 20);
        heading = new JLabel("Login");
        heading.setForeground(Color.blue);
        heading.setFont(new Font("Arial", 10, 25));

        nameLabel = new JLabel("User Name: ");
        nameLabel.setLabelFor(nameTextField);
        nameLabel.setPreferredSize(labelSize);
        nameLabel.setFont(new Font("Arial", 10, 20));

        passwordLabel = new JLabel("Password:  ");
        passwordLabel.setLabelFor(passwordTextField);
        passwordLabel.setPreferredSize(labelSize);
        passwordLabel.setFont(new Font("Arial", 10, 20));

    }

    /**
     * This function allocates an memory to JTextFields with there styles.
     */
    private void allocateToFields() {
        nameTextField = new JTextField();
        nameTextField.setColumns(20);
        nameTextField.setFont(new Font("Arial", 10, 18));
        nameTextField.setFocusable(true);

        passwordTextField = new JPasswordField();
        passwordTextField.setEchoChar('*');
        passwordTextField.setColumns(20);
        passwordTextField.setFocusable(true);
        passwordTextField.setFont(new Font("Arial", 10, 18));
        // adds an Custom event listener for going to next field when user pressed an
        // enter key.
        nameTextField.addKeyListener(new CustomKeyListener(passwordTextField));

        login = new JButton("Login");
        login.setVisible(true);
        login.setBackground(skyBlue);
        login.setForeground(Color.white);
        login.setFont(labelFont);
        login.setFocusable(true);
        login.addActionListener(this);
        // adds an Custom event listener for going to next field when user pressed an
        // enter key.
        passwordTextField.addKeyListener(new CustomKeyListener(login));

        exit = new JButton("Exit");
        exit.setVisible(true);
        exit.setBackground(Color.red);
        exit.setForeground(Color.white);
        exit.setFont(labelFont);
        exit.setFocusable(true);
        exit.addActionListener(this);

    }

    /**
     * This function will triggered when the action event are occur.
     */
    public void actionPerformed(ActionEvent e) {
        // checks the event source component.
        if (e.getSource() == login) {
            // gets the values from components which are entered by the user.
            String username = nameTextField.getText().trim();
         
            // string conversion.
            String pass = passwordTextField.getPassword().toString();

            // The Validation class is custom class to validate the field value given by
            // user.
            boolean res = true;
            boolean nameRes = true;

            // checks the result given by Validation and perform operation accordingly.
            if (res && nameRes) {
                try {
                    String query = "SELECT * FROM users WHERE username = ? AND password = ? LIMIT 1";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setString(1, username);
                    pst.setString(2, pass);
                    ResultSet resultSet = pst.executeQuery();
                    /**
                     * The next() are move the pointer to next row of the result set data .
                     * If the data are found then it will move to next otherwise returns false,if no
                     * record found.
                     */
                    if (resultSet.next()) {
                        // open an Home screen if validated.
                        new MDI();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid credentials!!");
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Connection error,try again!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "The username or password invalid!");
            }
        }

        if (e.getSource() == exit) {
            try {
                DBConnection.con.close();
                dispose();
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Connection error,try again!");
            }
        }
    }
}
/*
 * This component end here...
 */