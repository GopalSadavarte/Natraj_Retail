package com.app.components.sales;

import javax.swing.*;
import javax.swing.table.*;
import com.app.components.abstracts.AbstractButton;
import com.toedter.calendar.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

public final class SaleBill extends AbstractButton implements PropertyChangeListener {

    JLabel headingLabel, dateLabel, billNoLabel, counterNoLabel, custNameLabel, custMobileLabel, pIdLabel,
            pBarcodeLabel,
            pNameLabel, pQtyLabel, pRateLabel, pMRPLabel, billAmtLabel, returnAmtLabel, paidAmtLabel, pBillAmtLabel,
            lpBillAmtLabel, pBillNoLabel, lpBillNoLabel, paymentTypeLabel, totalQtyLabel, pDiscLabel;
    JPanel mainPanel, sidePanel, headingPanel, billInfoPanel, custInfoPanel, billFormPanel, bottomPanel;
    JTextField billNoField, counterNoField, custNameField, mobileField, pBarcodeField, pIdField, pNameField, pQtyField,
            pRateField,
            pMRPField, billAmtField, returnAmtField, paidAmtField, pBillNoField, lpBillNoField, pBillAmtField,
            lpBillAmtField, totalQtyField, pDiscField;
    JDateChooser dateChooser;
    JComboBox<String> paymentTypes;
    JTable billTable;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    JScrollPane scrollPane;
    final Dimension innerPanelSize = new Dimension(1330 - 160, 45);

    public SaleBill(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Sale Bill", currentMenu, frameTracker, frameKeyMap, dialog);
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setSize(toolkit.getScreenSize());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        mainPanel.setBorder(border);
        mainPanel.setPreferredSize(new Dimension(1330, 600));
        mainPanel.setBackground(Color.white);

        sidePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sidePanel.setBackground(Color.white);
        sidePanel.setPreferredSize(new Dimension(1330 - 150, 595));
        sidePanel.setBorder(border);

        headingPanel = new JPanel(flowLayoutLeft);
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(innerPanelSize);

        headingLabel = new JLabel("Bill Information");
        headingLabel.setForeground(Color.darkGray);
        headingLabel.setFont(headingFont2);
        headingLabel.setBorder(border);
        headingLabel.setPreferredSize(new Dimension(250, 35));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headingPanel.add(headingLabel);

        billInfoPanel = new JPanel(flowLayoutLeft);
        billInfoPanel.setBackground(Color.white);
        billInfoPanel.setPreferredSize(innerPanelSize);
        billInfoPanel.setBorder(border);

        dateLabel = new JLabel("Date:");
        dateLabel.setFont(labelFont);

        dateChooser = new JDateChooser(new Date());
        dateChooser.setDateFormatString("dd-MM-yyyy");
        dateChooser.addPropertyChangeListener("date", this);
        dateChooser.setEnabled(false);
        dateChooser.setPreferredSize(labelSize);
        dateChooser.setFont(labelFont);
        dateChooser.setForeground(Color.darkGray);
        dateChooser.addFocusListener(this);
        dateChooser.setBackground(lemonYellow);

        billInfoPanel.add(dateLabel);
        billInfoPanel.add(dateChooser);

        counterNoLabel = new JLabel("Counter No.:");
        counterNoLabel.setForeground(Color.darkGray);
        counterNoLabel.setFont(labelFont);

        counterNoField = new JTextField(2);
        counterNoField.setFont(labelFont);
        counterNoField.setEnabled(false);
        counterNoField.setText("1");
        counterNoField.setDisabledTextColor(Color.darkGray);
        counterNoField.addFocusListener(this);
        counterNoField.setBackground(lemonYellow);

        billInfoPanel.add(counterNoLabel);
        billInfoPanel.add(counterNoField);

        billNoLabel = new JLabel("Bill No.:");
        billNoLabel.setFont(labelFont);

        billNoField = new JTextField(5);
        billNoField.setFont(labelFont);
        billNoField.setEnabled(false);
        billNoField.setDisabledTextColor(Color.darkGray);
        billNoField.setText("1");
        billNoField.addFocusListener(this);
        billNoField.addKeyListener(this);
        billNoField.setBackground(lemonYellow);

        billInfoPanel.add(billNoLabel);
        billInfoPanel.add(billNoField);

        paymentTypeLabel = new JLabel("Payment Type:");
        paymentTypeLabel.setFont(labelFont);

        paymentTypes = new JComboBox<String>();
        paymentTypes.setFont(labelFont);
        paymentTypes.addItem("Cash");
        paymentTypes.addItem("Debit/Credit Card");
        paymentTypes.addItem("UPI/Phone Pay");

        billInfoPanel.add(paymentTypeLabel);
        billInfoPanel.add(paymentTypes);

        custInfoPanel = new JPanel(flowLayoutLeft);
        custInfoPanel.setBackground(Color.white);
        custInfoPanel.setPreferredSize(innerPanelSize);
        custInfoPanel.setBorder(border);

        custNameLabel = new JLabel("Customer Name:");
        custNameLabel.setFont(labelFont);

        custNameField = new JTextField(25);
        custNameField.setFont(labelFont);
        custNameField.setForeground(Color.darkGray);
        custNameField.setBackground(lemonYellow);
        custNameField.addFocusListener(this);

        custInfoPanel.add(custNameLabel);
        custInfoPanel.add(custNameField);

        custMobileLabel = new JLabel("Mobile No.:");
        custMobileLabel.setFont(labelFont);

        mobileField = new JTextField(10);
        mobileField.setBackground(lemonYellow);
        mobileField.setFont(labelFont);
        mobileField.addFocusListener(this);

        custInfoPanel.add(custMobileLabel);
        custInfoPanel.add(mobileField);

        billFormPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        billFormPanel.setPreferredSize(innerPanelSize);
        billFormPanel.setBackground(Color.white);
        billFormPanel.setBorder(border);

        pBarcodeLabel = new JLabel("Barcode :");
        pBarcodeLabel.setFont(labelFont);
        pBarcodeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        pBarcodeField = new JTextField(7);
        pBarcodeField.addFocusListener(this);
        pBarcodeField.addActionListener(this);
        pBarcodeField.addKeyListener(this);
        pBarcodeField.setBackground(lemonYellow);
        pBarcodeField.setFont(labelFont);
        pBarcodeField.setFocusable(true);
        pBarcodeField.requestFocus();

        billFormPanel.add(pBarcodeLabel);
        billFormPanel.add(pBarcodeField);

        pIdLabel = new JLabel("Item Code:");
        pIdLabel.setFont(labelFont);

        pIdField = new JTextField(4);
        pIdField.setBackground(lemonYellow);
        pIdField.addFocusListener(this);
        pIdField.addKeyListener(this);
        pIdField.setFont(labelFont);

        billFormPanel.add(pIdLabel);
        billFormPanel.add(pIdField);

        pNameLabel = new JLabel("Item Name:");
        pNameLabel.setFont(labelFont);

        pNameField = new JTextField(15);
        pNameField.setBackground(lemonYellow);
        pNameField.setEnabled(false);
        pNameField.setFont(labelFont);
        pNameField.setDisabledTextColor(Color.darkGray);

        billFormPanel.add(pNameLabel);
        billFormPanel.add(pNameField);

        pQtyLabel = new JLabel("Qty.:");
        pQtyLabel.setFont(labelFont);

        pQtyField = new JTextField(3);
        pQtyField.setBackground(lemonYellow);
        pQtyField.addFocusListener(this);
        pQtyField.addKeyListener(this);
        pQtyField.setFont(labelFont);
        pQtyField.setText("1");

        billFormPanel.add(pQtyLabel);
        billFormPanel.add(pQtyField);

        pRateLabel = new JLabel("Rate:");
        pRateLabel.setFont(labelFont);

        pRateField = new JTextField(5);
        pRateField.addKeyListener(this);
        pRateField.addFocusListener(this);
        pRateField.setFont(labelFont);
        pRateField.setBackground(lemonYellow);

        billFormPanel.add(pRateLabel);
        billFormPanel.add(pRateField);

        pMRPLabel = new JLabel("MRP:");
        pMRPLabel.setFont(labelFont);

        pMRPField = new JTextField(5);
        pMRPField.setBackground(lemonYellow);
        pMRPField.setFont(labelFont);
        pMRPField.addKeyListener(this);
        pMRPField.addFocusListener(this);

        billFormPanel.add(pMRPLabel);
        billFormPanel.add(pMRPField);

        designTable();

        bottomPanel = new JPanel(flowLayoutLeft);
        bottomPanel.setBackground(Color.white);
        bottomPanel.setBorder(border);
        bottomPanel.setPreferredSize(innerPanelSize);

        

        buttonPanel.setPreferredSize(new Dimension(120, 600));

        sidePanel.add(headingPanel);
        sidePanel.add(billInfoPanel);
        sidePanel.add(custInfoPanel);
        sidePanel.add(billFormPanel);
        sidePanel.add(scrollPane);

        mainPanel.add(sidePanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            pBarcodeField.requestFocus();
        });
    }

    private void designTable() {

        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("item code");
        tableModel.addColumn("barcode");
        tableModel.addColumn("name");
        tableModel.addColumn("Qty.");
        tableModel.addColumn("MRP");
        tableModel.addColumn("Sale Rate");
        tableModel.addColumn("disc.%");
        tableModel.addColumn("disc.amt.");
        tableModel.addColumn("Amt.After Disc.");
        tableModel.addColumn("Net Amt.");
        tableModel.addColumn("Tax");
        tableModel.addColumn("Tax Amt.");
        tableModel.addColumn("Amt.");

        billTable = new JTable(tableModel);
        billTable.setFont(labelFont);
        billTable.setSelectionBackground(skyBlue);
        billTable.setSelectionForeground(Color.white);
        sorter = new TableRowSorter<>(tableModel);
        billTable.setRowSorter(sorter);
        billTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        billTable.setRowHeight(30);
        billTable.addFocusListener(this);

        JTableHeader header = billTable.getTableHeader();
        header.setFont(labelFont);

        TableColumnModel columnModel = billTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(140);
        columnModel.getColumn(2).setPreferredWidth(170);
        columnModel.getColumn(3).setPreferredWidth(220);
        columnModel.getColumn(4).setPreferredWidth(70);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(70);
        columnModel.getColumn(8).setPreferredWidth(120);
        columnModel.getColumn(9).setPreferredWidth(120);
        columnModel.getColumn(10).setPreferredWidth(120);
        columnModel.getColumn(11).setPreferredWidth(70);
        columnModel.getColumn(12).setPreferredWidth(120);
        columnModel.getColumn(13).setPreferredWidth(150);

        scrollPane = new JScrollPane(billTable);
        scrollPane.setPreferredSize(new Dimension(1170, 300));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);

    }

    public void propertyChange(PropertyChangeEvent e) {
        //
    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        if (!(source instanceof JTable)) {
            ((JComponent) e.getSource()).setBackground(lightYellow);
        }
    }

    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        if (source instanceof JTable) {
            billTable.removeRowSelectionInterval(0, billTable.getRowCount() - 1);
        } else {
            ((JComponent) e.getSource()).setBackground(lemonYellow);
        }
    }

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        if (source.equals(newBtn)) {
            reCreate();
        } else {

            if (source.equals(saveBtn)) {
                //
            }
            if (source.equals(cancelBtn)) {
                deleteBtn.setEnabled(true);
                editBtn.setEnabled(true);
            }

            if (source.equals(editBtn) || source.equals(deleteBtn)) {
                billNoField.setEnabled(true);
                dateChooser.setEnabled(true);

                billNoField.requestFocus();
                billNoField.selectAll();
            }
            if (source.equals(exitBtn)) {
                boolean res = JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == 0;
                if (res) {
                    String key = frameKeyMap.get(this);
                    frameTracker.remove(key);
                    dispose();
                }
            }
        }

    }

    public void keyPressed(KeyEvent e) {
        if (e.getSource().equals(pMRPField)) {
            String b = pBarcodeField.getText();
            String id = pIdField.getText();
            String name = pNameField.getText();
            String qty = pQtyField.getText();
            String rate = pRateField.getText();
            String mrp = pMRPField.getText();

            tableModel.addRow(new Object[] {
                    1, id, b, name, qty, mrp, rate, 10, 10, 90, 90 * Integer.parseInt(qty), 18, 36,
                    90 * Integer.parseInt(qty)
            });
        }
    }
}
