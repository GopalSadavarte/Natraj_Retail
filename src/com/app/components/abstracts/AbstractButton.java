package com.app.components.abstracts;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.app.components.purchase.support.*;
import com.app.config.*;
import com.app.partials.event.*;
import com.app.partials.interfaces.*;

public abstract class AbstractButton extends JInternalFrame
        implements AppConstants, ActionListener, DocumentListener, FocusListener, KeyListener, Validation {

    protected JButton newBtn, cancelBtn, deleteBtn, exitBtn, editBtn, saveBtn, selectBtn;
    protected JPanel buttonPanel;
    protected final JMenuItem currentMenuItem;
    protected final HashMap<String, JInternalFrame> frameTracker;
    protected final HashMap<JInternalFrame, String> frameKeyMap;
    protected final JDialog dataViewer;
    JLabel searchLabel;
    JPanel dataViewerHeadingPanel, selectBtnPanel;
    protected JTextField searchField;
    JPanel view;
    protected final Integer UPDATE_ACTION = -1, SAVE_ACTION = 1, DELETE_ACTION = 0;
    protected Integer action = SAVE_ACTION;

    public AbstractButton(String title, JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {
        super(title, false, true, true);
        currentMenuItem = currentMenu;
        this.frameKeyMap = frameKeyMap;
        this.frameTracker = frameTracker;
        this.dataViewer = dialog;

        configureDialog();

        createButtons();
    }

    private void configureDialog() {
        double width = toolkit.getScreenSize().getWidth();
        double height = toolkit.getScreenSize().getHeight();
        dataViewer.setSize(new Dimension((int) (width / 1.5), 500));
        dataViewer.setBackground(Color.white);
        dataViewer.setLayout(new FlowLayout());
        dataViewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dataViewer.setModal(true);
        dataViewer.setResizable(false);

        int x = (int) (width - (width / 1.5)) / 2;
        int y = (int) (height - 500) / 2;

        dataViewer.setLocation(new Point(x, y));
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

        newBtn.addKeyListener(new KeyListenerButtonSwitch(exitBtn, saveBtn));
        saveBtn.addKeyListener(new KeyListenerButtonSwitch(newBtn, editBtn));
        editBtn.addKeyListener(new KeyListenerButtonSwitch(saveBtn, deleteBtn));
        deleteBtn.addKeyListener(new KeyListenerButtonSwitch(editBtn, cancelBtn));
        cancelBtn.addKeyListener(new KeyListenerButtonSwitch(deleteBtn, exitBtn));
        exitBtn.addKeyListener(new KeyListenerButtonSwitch(cancelBtn, newBtn));

        buttonPanel.add(newBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(exitBtn);
    }

    protected void reCreate() {
        frameTracker.remove(frameKeyMap.get(this));
        dispose();
        currentMenuItem.doClick();
    }

    protected void createViewer(JPanel view) {
        this.view = view;
        configureDialog();
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
        searchField.getDocument().addDocumentListener(this);
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
        selectBtn.setActionCommand(view instanceof CustomerView ? "customer view" : "other views");
        selectBtnPanel.add(selectBtn);

        container.add(selectBtnPanel);

        container.revalidate();
        container.repaint();

        dataViewer.setVisible(true);
    }

    protected void moveRow(DefaultTableModel model, int from, int to) {
        Object[] values = new Object[model.getColumnCount()];
        for (int i = 0; i < values.length; values[i] = model.getValueAt(from, i), i++)
            ;
        model.removeRow(from);
        model.insertRow(to, values);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    protected int getStock(long pId) throws Exception {
        PreparedStatement pst = DBConnection.con.prepareStatement(
                "select sum(current_quantity) as qty from inventories where product_id = ?");
        pst.setLong(1, pId);
        ResultSet result = pst.executeQuery();
        if (result.next()) {
            return result.getInt("qty");
        }
        return 0;
    }

    private void performRowSwitch(String key) {
        int row = table.getSelectedRow();
        if (key.equals("Enter")) {
            selectBtn.doClick();
        } else if (key.equals("Up")) {
            if (row > 0) {
                table.setRowSelectionInterval(row - 1, row - 1);
                table.scrollRectToVisible(table.getCellRect(row, 0, true));
            }
        } else if (key.equals("Down")) {
            if ((row + 1) < table.getRowCount()) {
                table.setRowSelectionInterval(row + 1, row + 1);
                table.scrollRectToVisible(table.getCellRect(row, 0, true));
            }
        }
    }

    JTable table = null;

    public void keyPressed(KeyEvent e) {
        Object source = e.getSource();
        String key = KeyEvent.getKeyText(e.getKeyCode());
        if (source.equals(searchField) && view != null) {
            TableExporter tableExporter = (TableExporter) view;
            table = tableExporter.getTable();
            performRowSwitch(key);
        }
    }

    private void filter() {
        String value = searchField.getText().toUpperCase().trim();
        TableExporter tableExporter = (TableExporter) view;
        table = tableExporter.getTable();
        tableExporter.setTableData(value);
        if (table.getRowCount() > 0)
            table.setRowSelectionInterval(0, 0);
    }

    protected long getProductId(long itemCode) {
        long id = 0;
        try {
            String query = "select id from products where p_id = ?";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setLong(1, itemCode);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                id = result.getLong("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return id;

    }

    protected Integer updateSrNo(DefaultTableModel tableModel) {
        int rows = tableModel.getRowCount(), cnt = 1;
        if (rows > 0) {
            for (int row = rows - 1; row >= 0; tableModel.setValueAt(cnt++, row, 0), row--)
                ;
        }
        return cnt;
    }

    protected String getLastId(String sqlTable, String sqlColumn) {
        String id = "1";
        try {
            String query = "select " + sqlColumn + " from " + sqlTable + " where date(created_at) = ? order by "
                    + sqlColumn + " desc limit 1";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setDate(1, new java.sql.Date(new Date().getTime()));
            ResultSet result = pst.executeQuery();
            id = result.next() ? "" + (result.getInt(sqlColumn) + 1) : "1";
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at AbstractButton.getLastId()");
        }
        return id;
    }

    public void insertUpdate(DocumentEvent e) {
        filter();
    }

    public void removeUpdate(DocumentEvent e) {
        filter();
    }

    public void changedUpdate(DocumentEvent e) {
        System.out.println("changed update docs list");
    }
}
