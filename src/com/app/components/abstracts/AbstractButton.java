package com.app.components.abstracts;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import com.app.config.*;
import com.app.partials.interfaces.*;

public abstract class AbstractButton extends JInternalFrame
        implements AppConstants, ActionListener, FocusListener, KeyListener, Validation {

    protected JButton newBtn, cancelBtn, deleteBtn, exitBtn, editBtn, saveBtn, selectBtn;
    protected JPanel buttonPanel;
    protected JMenuItem currentMenuItem;
    protected HashMap<String, JInternalFrame> frameTracker;
    protected HashMap<JInternalFrame, String> frameKeyMap;
    protected final JDialog dataViewer;
    JLabel searchLabel;
    JPanel dataViewerHeadingPanel, selectBtnPanel;
    protected JTextField searchField;

    public AbstractButton(String title, JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {
        super(title, false, true, true);
        currentMenuItem = currentMenu;
        this.frameKeyMap = frameKeyMap;
        this.frameTracker = frameTracker;
        this.dataViewer = dialog;

        double width = toolkit.getScreenSize().getWidth();
        double height = toolkit.getScreenSize().getHeight();
        dataViewer.setSize(new Dimension((int) (width / 1.5), 500));
        dataViewer.setBackground(Color.white);
        dataViewer.setLayout(new FlowLayout());
        dataViewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        int x = (int) (width - (width / 1.5)) / 2;
        int y = (int) (height - 500) / 2;

        dataViewer.setLocation(new Point(x, y));

        createButtons();
    }

    private void createButtons() {
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.white);

        newBtn = new JButton("New");
        newBtn.setForeground(Color.white);
        newBtn.setBackground(skyBlue);
        newBtn.setPreferredSize(buttonSize);
        newBtn.setFont(labelFont);
        newBtn.addActionListener(this);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setForeground(Color.white);
        cancelBtn.setBackground(orange);
        cancelBtn.setPreferredSize(buttonSize);
        cancelBtn.setFont(labelFont);
        cancelBtn.addActionListener(this);

        editBtn = new JButton("Edit");
        editBtn.setForeground(Color.white);
        editBtn.setBackground(lightBlue);
        editBtn.setPreferredSize(buttonSize);
        editBtn.setFont(labelFont);
        editBtn.setEnabled(false);
        editBtn.addActionListener(this);

        deleteBtn = new JButton("Delete");
        deleteBtn.setForeground(Color.white);
        deleteBtn.setBackground(darkRed);
        deleteBtn.setPreferredSize(buttonSize);
        deleteBtn.setFont(labelFont);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(this);

        exitBtn = new JButton("Exit");
        exitBtn.setForeground(Color.white);
        exitBtn.setBackground(red);
        exitBtn.setPreferredSize(buttonSize);
        exitBtn.setFont(labelFont);
        exitBtn.addActionListener(this);

        saveBtn = new JButton("Save");
        saveBtn.setForeground(Color.white);
        saveBtn.setBackground(parrotGreen);
        saveBtn.setPreferredSize(buttonSize);
        saveBtn.setFont(labelFont);
        saveBtn.addActionListener(this);

        buttonPanel.add(newBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(exitBtn);
    }

    protected void setLastId(String tableName, JTextField textField) {
        try {
            String query = "select * from " + tableName + " order by id desc limit 1";
            Statement st = DBConnection.con.createStatement();
            ResultSet result = st.executeQuery(query);
            if (result.next()) {
                int id = result.getInt("id");
                textField.setText("" + (id + 1));
            } else {
                textField.setText("1");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at AbstractButton.java 93");
        }
    }

    protected void reCreate() {
        frameTracker.remove(frameKeyMap.get(this));
        dispose();
        currentMenuItem.doClick();
    }

    protected void createViewer(JPanel view) {
        dataViewer.setResizable(false);
        Container container = dataViewer.getContentPane();
        container.removeAll();

        dataViewerHeadingPanel = new JPanel(flowLayoutCenter);
        dataViewerHeadingPanel.setPreferredSize(new Dimension(dataViewer.getWidth(), 40));
        dataViewerHeadingPanel.setBackground(Color.white);

        searchLabel = new JLabel("Search Value :");
        searchLabel.setFont(labelFont);
        searchLabel.setForeground(Color.darkGray);

        dataViewerHeadingPanel.add(searchLabel);

        searchField = new JTextField(40);
        searchField.setFont(labelFont);
        searchField.addFocusListener(this);
        searchField.addKeyListener(this);
        searchField.setBackground(lemonYellow);

        dataViewerHeadingPanel.add(searchField);

        container.add(dataViewerHeadingPanel);

        dataViewer.add(view);

        selectBtnPanel = new JPanel(flowLayoutCenter);
        selectBtnPanel.setBackground(Color.white);
        selectBtnPanel.setPreferredSize(new Dimension(dataViewer.getWidth(), 40));

        selectBtn = new JButton("Select");
        selectBtn.setFont(labelFont);
        selectBtn.setForeground(Color.white);
        selectBtn.setBackground(new Color(253, 32, 25));
        selectBtn.addActionListener(this);

        selectBtnPanel.add(selectBtn);

        container.add(selectBtnPanel);

        container.revalidate();
        container.repaint();
        dataViewer.setModal(true);
        dataViewer.setVisible(true);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }
}
