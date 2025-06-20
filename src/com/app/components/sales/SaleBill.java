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

    JLabel lastScannedItemLabel, dateLabel, billNoLabel, counterNoLabel, custNameLabel, custMobileLabel, pIdLabel,
            pBarcodeLabel, printLabel,
            pQtyLabel, pRateLabel, pMRPLabel, billAmtLabel, returnAmtLabel, paidAmtLabel, pBillAmtLabel,
            lpBillAmtLabel, pBillNoLabel, lpBillNoLabel, paymentTypeLabel, totalQtyLabel, pDiscLabel;
    JPanel mainPanel, sidePanel, lastScannedItemPanel, billInfoPanel, custInfoPanel, billFormPanel, bottomPanel;
    JTextField billNoField, counterNoField, custNameField, mobileField, pBarcodeField, pIdField, pNameField, pQtyField,
            pRateField, lastScannedItemField,
            pMRPField, billAmtField, returnAmtField, paidAmtField, pBillNoField, lpBillNoField, pBillAmtField,
            lpBillAmtField, totalQtyField, pDiscField;
    JDateChooser dateChooser;
    JComboBox<String> paymentTypes, printOptions;
    JTable billTable;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    JScrollPane scrollPane;
    final Dimension innerPanelSize = new Dimension(1330 - 160, 45);

    public SaleBill(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Bill Information", currentMenu, frameTracker, frameKeyMap, dialog);
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

        lastScannedItemPanel = new JPanel(flowLayoutLeft);
        lastScannedItemPanel.setBackground(Color.white);
        lastScannedItemPanel.setPreferredSize(innerPanelSize);
        lastScannedItemPanel.setBorder(border);

        lastScannedItemLabel = new JLabel("Last Scanned Item :");
        lastScannedItemLabel.setForeground(Color.darkGray);
        lastScannedItemLabel.setFont(labelFont);

        lastScannedItemField = new JTextField(50);
        lastScannedItemField.setFont(labelFont);
        lastScannedItemField.setEnabled(false);
        lastScannedItemField.setText("product");
        lastScannedItemField.setBackground(lemonYellow);

        lastScannedItemPanel.add(lastScannedItemLabel);
        lastScannedItemPanel.add(lastScannedItemField);

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

        billFormPanel = new JPanel(flowLayoutLeft);
        billFormPanel.setPreferredSize(innerPanelSize);
        billFormPanel.setBackground(Color.white);
        billFormPanel.setBorder(border);

        pBarcodeLabel = new JLabel("Barcode :");
        pBarcodeLabel.setFont(labelFont);

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

        pIdLabel = new JLabel("Code:");
        pIdLabel.setFont(labelFont);

        pIdField = new JTextField(4);
        pIdField.setBackground(lemonYellow);
        pIdField.addFocusListener(this);
        pIdField.addKeyListener(this);
        pIdField.setFont(labelFont);

        billFormPanel.add(pIdLabel);
        billFormPanel.add(pIdField);

        pNameField = new JTextField(18);
        pNameField.setBackground(lemonYellow);
        pNameField.setEnabled(false);
        pNameField.setFont(labelFont);
        pNameField.setDisabledTextColor(Color.darkGray);

        billFormPanel.add(pNameField);

        pQtyLabel = new JLabel("Qty.:");
        pQtyLabel.setFont(labelFont);

        pQtyField = new JTextField(5);
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

        pDiscLabel = new JLabel("Disc.Amt.:");
        pDiscLabel.setFont(labelFont);

        pDiscField = new JTextField(5);
        pDiscField.setFont(labelFont);
        pDiscField.setEnabled(false);
        pDiscField.setDisabledTextColor(Color.darkGray);
        pDiscField.setBackground(lemonYellow);
        pDiscField.setText("343.43");

        bottomPanel.add(pDiscLabel);
        bottomPanel.add(pDiscField);

        totalQtyLabel = new JLabel("T.Qty.:");
        totalQtyLabel.setFont(labelFont);

        totalQtyField = new JTextField(5);
        totalQtyField.setEnabled(false);
        totalQtyField.setDisabledTextColor(Color.darkGray);
        totalQtyField.setText("12");
        totalQtyField.setBackground(lemonYellow);
        totalQtyField.setFont(labelFont);

        bottomPanel.add(totalQtyLabel);
        bottomPanel.add(totalQtyField);

        lpBillNoLabel = new JLabel("L.P.No.");
        lpBillNoLabel.setFont(labelFont);

        lpBillNoField = new JTextField(2);
        lpBillNoField.setText("1");
        lpBillNoField.setEnabled(false);
        lpBillNoField.setFont(labelFont);
        lpBillNoField.setDisabledTextColor(Color.darkGray);
        lpBillNoField.setBackground(lemonYellow);

        bottomPanel.add(lpBillNoLabel);
        bottomPanel.add(lpBillNoField);

        pBillNoLabel = new JLabel("P.No.");
        pBillNoLabel.setFont(labelFont);

        pBillNoField = new JTextField(2);
        pBillNoField.setText("1");
        pBillNoField.setFont(labelFont);
        pBillNoField.setEnabled(false);
        pBillNoField.setDisabledTextColor(Color.darkGray);
        pBillNoField.setBackground(lemonYellow);

        bottomPanel.add(pBillNoLabel);
        bottomPanel.add(pBillNoField);

        lpBillAmtLabel = new JLabel("L.P.Amt.");
        lpBillAmtLabel.setFont(labelFont);

        lpBillAmtField = new JTextField(3);
        lpBillAmtField.setText("1");
        lpBillAmtField.setFont(labelFont);
        lpBillAmtField.setEnabled(false);
        lpBillAmtField.setDisabledTextColor(Color.darkGray);
        lpBillAmtField.setBackground(lemonYellow);

        bottomPanel.add(lpBillAmtLabel);
        bottomPanel.add(lpBillAmtField);

        pBillAmtLabel = new JLabel("P.Amt.");
        pBillAmtLabel.setFont(labelFont);

        pBillAmtField = new JTextField(3);
        pBillAmtField.setText("1");
        pBillAmtField.setFont(labelFont);
        pBillAmtField.setEnabled(false);
        pBillAmtField.setDisabledTextColor(Color.darkGray);
        pBillAmtField.setBackground(lemonYellow);

        bottomPanel.add(pBillAmtLabel);
        bottomPanel.add(pBillAmtField);

        billAmtLabel = new JLabel("Total :");
        billAmtLabel.setFont(labelFont);

        billAmtField = new JTextField(12);
        billAmtField.setFont(labelFont);
        billAmtField.setHorizontalAlignment(SwingConstants.RIGHT);
        billAmtField.setText("656523.32");
        billAmtField.setEnabled(false);
        billAmtField.setDisabledTextColor(Color.darkGray);
        billAmtField.setBackground(lemonYellow);

        bottomPanel.add(billAmtLabel);
        bottomPanel.add(billAmtField);

        buttonPanel.setPreferredSize(new Dimension(120, 600));
        printLabel = new JLabel("Print :");
        printLabel.setFont(labelFont);

        printOptions = new JComboBox<>();
        printOptions.setFont(labelFont);
        printOptions.setPreferredSize(new Dimension(90, 30));
        printOptions.addFocusListener(this);
        printOptions.addItem("Yes");
        printOptions.addItem("No");

        buttonPanel.add(printLabel);
        buttonPanel.add(printOptions);

        paidAmtLabel = new JLabel("Paid Amt.");
        paidAmtLabel.setFont(labelFont);

        paidAmtField = new JTextField(5);
        paidAmtField.setBackground(lemonYellow);
        paidAmtField.addKeyListener(this);
        paidAmtField.addFocusListener(this);
        paidAmtField.setFont(labelFont);

        buttonPanel.add(paidAmtLabel);
        buttonPanel.add(paidAmtField);

        returnAmtLabel = new JLabel("Return Amt.");
        returnAmtLabel.setFont(labelFont);

        returnAmtField = new JTextField(5);
        returnAmtField.setBackground(lemonYellow);
        returnAmtField.setEnabled(false);
        returnAmtField.setDisabledTextColor(Color.darkGray);
        returnAmtField.setFont(labelFont);

        buttonPanel.add(returnAmtLabel);
        buttonPanel.add(returnAmtField);

        sidePanel.add(billInfoPanel);
        sidePanel.add(custInfoPanel);
        sidePanel.add(billFormPanel);
        sidePanel.add(lastScannedItemPanel);
        sidePanel.add(scrollPane);
        sidePanel.add(bottomPanel);

        mainPanel.add(sidePanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            pBarcodeField.requestFocus();
        });
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public JComboBox<String> getPrintOption() {
        return printOptions;
    }

    private void designTable() {

        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return (col == 4 || col == 5 || col == 6 || col == 7);
            }
        };

        String columnNames[] = new String[] { "Sr.No.", "item code", "barcode", "name", "Qty.", "MRP", "Sale Rate",
                "disc.%", "disc.amt.", "Amt.", "Net Amt.", "Tax", "Tax Amt." };

        for (String column : columnNames)
            tableModel.addColumn(column);

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
        int[] values = new int[] { 70, 140, 170, 220, 70, 100, 100, 90, 120, 120, 120, 70, 120 };
        for (int i = 0; i < values.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(values[i]);
            column.setMinWidth(values[i]);
        }

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
                JOptionPane.showConfirmDialog(null, "Are you sure to save this information?",
                        "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION);
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
