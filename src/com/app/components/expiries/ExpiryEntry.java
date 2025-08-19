package com.app.components.expiries;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.app.components.abstracts.AbstractButton;
import com.app.components.purchase.support.*;
import com.app.config.*;
import com.app.partials.event.*;
import com.app.partials.interfaces.*;
import com.toedter.calendar.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public final class ExpiryEntry extends AbstractButton implements CellEditorListener {

    JLabel dateLabel, counterNoLabel, dealerNameLabel, dealerMobileLabel, pIdLabel,
            pBarcodeLabel, pEXPDateLabel, tQtyLabel, entryNoLabel,
            dealerGstNoLabel,
            pQtyLabel, pMRPLabel, billAmtLabel, tTaxLabel,
            tEntriesLabel, pDiscLabel, pBillQtyLabel, lpBillQtyLabel, pBillNoLabel, lpBillNoLabel;
    JPanel mainPanel, subBottomPanel, billInfoPanel, dealerInfoPanel, billFormPanel,
            bottomPanel;
    JTextField counterNoField, dealerNameField, mobileField, pBarcodeField, pIdField, pNameField,
            pQtyField, entryNoField,
            dealerGstNoField,
            pMRPField, billAmtField, pBillNoField, lpBillNoField,
            tTaxField,
            tQtyField, tEntriesField, pDiscField, pBillQtyField, lpBillQtyField;
    JDateChooser dateChooser, expDateChooser;
    JTable expiryTable;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    JScrollPane scrollPane;
    double discountVal = 0, taxVal = 0;
    final Dimension innerPanelSize = new Dimension(1330, 35);

    public ExpiryEntry(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Expiry Entry", currentMenu, frameTracker, frameKeyMap, dialog);
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setSize(toolkit.getScreenSize());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        mainPanel.setBorder(border);
        mainPanel.setPreferredSize(new Dimension(1330, 650));
        mainPanel.setBackground(Color.white);

        allocateToTopPanel();

        designTable();

        allocateToBottomPanel();

        buttonPanel.setLayout(flowLayoutCenter);
        buttonPanel.setPreferredSize(new Dimension(1320, 50));

        mainPanel.add(billInfoPanel);
        mainPanel.add(dealerInfoPanel);
        mainPanel.add(billFormPanel);
        mainPanel.add(scrollPane);
        mainPanel.add(bottomPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        pBarcodeField.addKeyListener(new CustomKeyListener(pIdField));
        pIdField.addKeyListener(new CustomKeyListener(pQtyField));
        pQtyField.addKeyListener(new CustomKeyListener(rateField));
        rateField.addKeyListener(new CustomKeyListener(pMRPField));
        JComponent dateUiComponent = expDateChooser.getDateEditor().getUiComponent();
        pMRPField.addKeyListener(new CustomKeyListener(dateUiComponent));
        dateUiComponent.addKeyListener(new CustomKeyListener(pBarcodeField));
        dateUiComponent.addKeyListener(this);
        dateChooser.getDateEditor().getUiComponent().addKeyListener(new CustomKeyListener(entryNoField));

        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            pBarcodeField.requestFocus();
        });
    }

    private void allocateToTopPanel() {

        billInfoPanel = new JPanel(flowLayoutLeft);
        billInfoPanel.setBackground(Color.white);
        billInfoPanel.setPreferredSize(innerPanelSize);

        dateLabel = new JLabel("Date:");
        dateLabel.setFont(labelFont);

        dateChooser = new JDateChooser(new Date());
        dateChooser.setDateFormatString("dd-MM-yyyy");
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

        entryNoLabel = new JLabel("Entry No.:");
        entryNoLabel.setFont(labelFont);

        entryNoField = new JTextField(5);
        entryNoField.setFont(labelFont);
        entryNoField.setEnabled(false);
        entryNoField.setDisabledTextColor(Color.darkGray);
        entryNoField.setText(getLastId("expiries", "day_wise_entry_no"));
        entryNoField.addFocusListener(this);
        entryNoField.addKeyListener(this);
        entryNoField.setBackground(lemonYellow);

        billInfoPanel.add(entryNoLabel);
        billInfoPanel.add(entryNoField);

        dealerInfoPanel = new JPanel(flowLayoutLeft);
        dealerInfoPanel.setBackground(Color.white);
        dealerInfoPanel.setPreferredSize(innerPanelSize);

        dealerNameLabel = new JLabel("Dealer Name:");
        dealerNameLabel.setFont(labelFont);

        dealerNameField = new JTextField(25);
        dealerNameField.setFont(labelFont);
        dealerNameField.setForeground(Color.darkGray);
        dealerNameField.setBackground(lemonYellow);
        dealerNameField.addFocusListener(this);
        dealerNameField.addKeyListener(this);

        dealerInfoPanel.add(dealerNameLabel);
        dealerInfoPanel.add(dealerNameField);

        dealerMobileLabel = new JLabel("Mobile No.:");
        dealerMobileLabel.setFont(labelFont);

        mobileField = new JTextField(10);
        mobileField.setBackground(lemonYellow);
        mobileField.setFont(labelFont);
        mobileField.addFocusListener(this);

        dealerInfoPanel.add(dealerMobileLabel);
        dealerInfoPanel.add(mobileField);

        dealerGstNoLabel = new JLabel("GST No.:");
        dealerGstNoLabel.setFont(labelFont);

        dealerGstNoField = new JTextField(10);
        dealerGstNoField.setBackground(lemonYellow);
        dealerGstNoField.setFont(labelFont);
        dealerGstNoField.addFocusListener(this);

        dealerInfoPanel.add(dealerGstNoLabel);
        dealerInfoPanel.add(dealerGstNoField);

        billFormPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 15));
        billFormPanel.setPreferredSize(new Dimension(1300, 45));
        billFormPanel.setBackground(Color.white);

        pBarcodeLabel = new JLabel("Barcode :");
        pBarcodeLabel.setFont(labelFont);

        pBarcodeField = new JTextField(7);
        pBarcodeField.addFocusListener(this);
        pBarcodeField.addActionListener(this);
        pBarcodeField.addKeyListener(this);
        pBarcodeField.setBackground(lemonYellow);
        pBarcodeField.setFont(labelFont);
        pBarcodeField.setFocusable(true);

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

        pQtyField = new JTextField(2);
        pQtyField.setBackground(lemonYellow);
        pQtyField.addFocusListener(this);
        pQtyField.addKeyListener(this);
        pQtyField.setFont(labelFont);
        pQtyField.setText("1");

        billFormPanel.add(pQtyLabel);
        billFormPanel.add(pQtyField);

        rateLabel = new JLabel("Rate :");
        rateLabel.setFont(labelFont);

        rateField = new JTextField(4);
        rateField.setBackground(lemonYellow);
        rateField.addFocusListener(this);
        rateField.addKeyListener(this);
        rateField.setFont(labelFont);
        rateField.setText("1");

        billFormPanel.add(rateLabel);
        billFormPanel.add(rateField);

        pMRPLabel = new JLabel("MRP:");
        pMRPLabel.setFont(labelFont);

        pMRPField = new JTextField(4);
        pMRPField.setBackground(lemonYellow);
        pMRPField.setFont(labelFont);
        pMRPField.addKeyListener(this);
        pMRPField.addFocusListener(this);

        billFormPanel.add(pMRPLabel);
        billFormPanel.add(pMRPField);

        pEXPDateLabel = new JLabel("EXP.Date:");
        pEXPDateLabel.setFont(labelFont);

        expDateChooser = new JDateChooser(new Date());
        expDateChooser.setDateFormatString("dd-MM-yyyy");
        expDateChooser.setFont(labelFont);
        expDateChooser.setPreferredSize(labelSize);
        expDateChooser.setForeground(Color.darkGray);
        expDateChooser.addFocusListener(this);
        expDateChooser.setBackground(lemonYellow);

        billFormPanel.add(pEXPDateLabel);
        billFormPanel.add(expDateChooser);

    }

    JLabel rateLabel;
    JTextField rateField;

    private void allocateToBottomPanel() {
        bottomPanel = new JPanel(flowLayoutLeft);
        bottomPanel.setBackground(Color.white);
        bottomPanel.setPreferredSize(new Dimension(1310, 45));

        tEntriesLabel = new JLabel("Total Entries :");
        tEntriesLabel.setFont(labelFont);

        tEntriesField = new JTextField(9);
        tEntriesField.setEnabled(false);
        tEntriesField.setDisabledTextColor(Color.darkGray);
        tEntriesField.setText("0");
        tEntriesField.setBackground(lemonYellow);
        tEntriesField.setFont(labelFont);

        bottomPanel.add(tEntriesLabel);
        bottomPanel.add(tEntriesField);

        tQtyLabel = new JLabel("Total Quantity :");
        tQtyLabel.setFont(labelFont);

        tQtyField = new JTextField(9);
        tQtyField.setText("0.00");
        tQtyField.setFont(labelFont);
        tQtyField.setEnabled(false);
        tQtyField.setDisabledTextColor(Color.darkGray);
        tQtyField.setBackground(lemonYellow);

        bottomPanel.add(tQtyLabel);
        bottomPanel.add(tQtyField);

        tTaxLabel = new JLabel("Total Tax Amount :");
        tTaxLabel.setFont(labelFont);

        tTaxField = new JTextField(9);
        tTaxField.setText("0.00");
        tTaxField.setFont(labelFont);
        tTaxField.setEnabled(false);
        tTaxField.setDisabledTextColor(Color.darkGray);
        tTaxField.setBackground(lemonYellow);

        bottomPanel.add(tTaxLabel);
        bottomPanel.add(tTaxField);

        billAmtLabel = new JLabel("Total :");
        billAmtLabel.setFont(labelFont);

        billAmtField = new JTextField(10);
        billAmtField.setFont(new Font("Cambria", 25, 25));
        billAmtField.setHorizontalAlignment(SwingConstants.RIGHT);
        billAmtField.setText("0.00");
        billAmtField.setEnabled(false);
        billAmtField.setDisabledTextColor(Color.darkGray);
        billAmtField.setBackground(lemonYellow);

        bottomPanel.add(billAmtLabel);
        bottomPanel.add(billAmtField);
    }

    private void designTable() {

        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return (col == 4 || col == 5 || col == 6 || col == 7 || col == 8 || col == 10);
            }

            public Object getValueAt(int row, int col) {
                Object value = super.getValueAt(row, col);
                if (value instanceof Double) {
                    return String.format("%.2f", value);
                }
                return value;
            }

            public void setValueAt(Object value, int row, int col) {
                if (value instanceof Double) {
                    value = String.format("%.2f", value);
                }
                super.setValueAt(value, row, col);
            }
        };

        String columnNames[] = new String[] { "Sr.No.", "item code", "barcode", "name", "Qty.", "Sale Rate", "MRP",
                "Net Amt.",
                "Tax", "Tax Amt.", "Exp.Date" };

        for (String column : columnNames)
            tableModel.addColumn(column);

        sorter = new TableRowSorter<>(tableModel);
        expiryTable = new JTable(tableModel);
        expiryTable.setFont(labelFont);
        expiryTable.setSelectionBackground(skyBlue);
        expiryTable.setSelectionForeground(Color.white);
        expiryTable.setRowSorter(sorter);
        expiryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        expiryTable.setRowHeight(30);
        expiryTable.addFocusListener(this);
        expiryTable.addKeyListener(this);
        expiryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expiryTable.setCellSelectionEnabled(true);
        expiryTable.getDefaultEditor(Object.class).addCellEditorListener(this);
        expiryTable.putClientProperty("terminateEditOnFocusLost", true);

        JTableHeader header = expiryTable.getTableHeader();
        header.setFont(labelFont);

        TableColumnModel columnModel = expiryTable.getColumnModel();
        int[] values = new int[] { 70, 140, 170, 245, 100, 120, 120, 100, 90, 120, 150 };
        for (int i = 0; i < values.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(values[i]);
            column.setMinWidth(values[i]);
        }

        scrollPane = new JScrollPane(expiryTable);
        scrollPane.setPreferredSize(new Dimension(1310, 330));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);

    }

    public void editingCanceled(ChangeEvent e) {
        calculate();
    }

    public void editingStopped(ChangeEvent e) {
        calculate();
    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        if (!(source instanceof JTable)) {
            JTextField field = ((JTextField) source);
            field.setBackground(lightYellow);
            field.selectAll();
        }
    }

    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        if (source instanceof JTable && expiryTable.getRowCount() > 0 && !isForDeleteRow) {
            expiryTable.removeRowSelectionInterval(0, expiryTable.getRowCount() - 1);
        } else if (!(source instanceof JTable)) {
            ((JComponent) e.getSource()).setBackground(lemonYellow);
        }
    }

    private void showProductDetails(Object source) {
        try {
            isForShowProductDetails = source.equals(pIdField);
            if (source.equals(pBarcodeField) && pBarcodeField.getText().isBlank()) {
                pIdField.requestFocus();
                return;
            }
            if (source.equals(pIdField) && pIdField.getText().isBlank()) {
                pQtyField.requestFocus();
                return;
            }
            String barcode = "";
            if (!isForShowProductDetails) {
                barcode = pBarcodeField.getText().trim();
            }

            String query = isForShowProductDetails ? "select * from products where p_id = ? limit 1"
                    : "select * from products where barcode_no = ? or p_id = ? limit 1";

            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            if (isForShowProductDetails) {
                pst.setLong(1, Long.parseLong(pIdField.getText().trim()));
            } else {
                pst.setString(1, barcode);
                pst.setLong(2, Long.parseLong(barcode));
            }

            ResultSet result = pst.executeQuery();
            if (result.next()) {
                String mrp = result.getString("product_mrp");
                String pName = result.getString("product_name").trim();
                String rate = result.getString("sale_rate").trim();
                pBarcodeField.setText(result.getString("barcode_no"));
                pIdField.setText(result.getString("p_id"));
                pNameField.setText(pName);
                pQtyField.setText("1");
                pMRPField.setText(mrp);
                rateField.setText(rate);

                pQtyField.requestFocusInWindow();

            } else {
                JOptionPane.showMessageDialog(this, "In-correct barcode or item code...!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertIntoBillTable() {
        try {
            String barcode = pBarcodeField.getText().trim();
            String code = pIdField.getText().trim();
            String pName = pNameField.getText().trim();
            boolean bRes = isBarcodeValid(barcode);
            boolean idRes = isBarcodeValid(code);

            if (!bRes || !idRes) {
                JOptionPane.showMessageDialog(this, "Barcode OR Item code is invalid!!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double qty = Double.parseDouble(pQtyField.getText().trim());
            double mrp = Double.parseDouble(pMRPField.getText().trim());
            double rate = Double.parseDouble(rateField.getText().trim());
            String exp = dateFormat.format(expDateChooser.getDate());

            int row = checkRecordExist(code, rate, mrp, exp);
            if (row == -1) {
                tableModel.insertRow(0, new Object[] {
                        cnt++, code, barcode, pName, qty, rate, mrp, (qty * rate), taxVal,
                        (((qty * rate) * taxVal) / 100), exp
                });
            } else {
                tableModel.setValueAt(Double.parseDouble(tableModel.getValueAt(row, 4).toString()) + qty, row, 4);
                moveRow(tableModel, row, 0);
                cnt = updateSrNo(tableModel);
            }
            calculate();

            pIdField.setText("");
            pBarcodeField.setText("");
            pNameField.setText("");
            pQtyField.setText("");
            pMRPField.setText("");
            rateField.setText("");
            pBarcodeField.requestFocusInWindow();

            SwingUtilities.invokeLater(() -> {
                pBarcodeField.requestFocus();
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int checkRecordExist(String pId, double rate, double mrp, String exp) {
        int rowCount = expiryTable.getRowCount();
        int foundedRow = -1;
        for (int row = 0; row < rowCount; row++) {
            double m = Double.parseDouble(expiryTable.getValueAt(row, 6).toString());
            String id = expiryTable.getValueAt(row, 1).toString();
            String expDate = expiryTable.getValueAt(row, 10).toString();
            double r = Double.parseDouble(expiryTable.getValueAt(row, 5).toString());

            if (id.equals(pId) && mrp == m && expDate.equals(exp) && r == rate) {
                foundedRow = row;
                break;
            }
        }
        return foundedRow;
    }

    private void calculate() {
        try {

            int rows = expiryTable.getRowCount();
            double totalAmt = 0, totalTaxAmt = 0, totalEntries = rows, totalQty = 0;
            for (int row = 0; row < rows; row++) {
                Double qty = Double.parseDouble(expiryTable.getValueAt(row, 4).toString());
                Double rate = Double.parseDouble(expiryTable.getValueAt(row, 5).toString());
                Double tax = Double.parseDouble(expiryTable.getValueAt(row, 8).toString());

                Double taxVal = qty * rate * tax / 100;
                expiryTable.setValueAt(taxVal, row, 9);

                Double amt = qty * rate;

                expiryTable.setValueAt(amt, row, 7);

                totalAmt += amt;
                totalQty += qty;
                totalTaxAmt += taxVal;
            }

            tQtyField.setText(String.format("%.2f", totalQty));
            tTaxField.setText(String.format("%.2f", totalTaxAmt));
            billAmtField.setText(String.format("%.2f", totalAmt));
            tEntriesField.setText(String.format("%.2f", totalEntries));

            isAnyInvalidEntry = false;
        } catch (Exception e) {
            isAnyInvalidEntry = true;
            return;
        }

    }

    Object actionSource;
    TableExporter view;
    long dealerId;
    boolean isForShowProductDetails, isAnyInvalidEntry;

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(newBtn)) {
            reCreate();
        } else {
            if (source.equals(saveBtn)) {
                double billAmt = Double.parseDouble(billAmtField.getText());
                if (action.equals(SAVE_ACTION)) {
                    if (dealerId == 0) {
                        JOptionPane.showMessageDialog(this, "Must select a supplier...!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (billAmt > 0 && !isAnyInvalidEntry) {
                        try {
                            int rows = expiryTable.getRowCount();
                            String query = "insert into expiries (day_wise_entry_no,dealer_id)values(?,?)";
                            PreparedStatement pst = DBConnection.con.prepareStatement(query);
                            pst.setInt(1, Integer.parseInt(getLastId("expiries", "day_wise_entry_no")));
                            pst.setLong(2, dealerId);

                            int affectedRows = pst.executeUpdate();
                            boolean flag = false;
                            if (affectedRows > 0) {
                                query = "select id from expiries order by id desc limit 1";
                                ResultSet result = DBConnection.executeQuery(query);
                                long expiry_id = result.next() ? result.getLong("id") : 0;
                                for (int row = 0; row < rows; row++) {

                                    long pId = Long.parseLong(expiryTable.getValueAt(row, 1).toString());
                                    long product_id = getProductId(pId);
                                    double qty = Double.parseDouble(expiryTable.getValueAt(row, 4).toString().trim());
                                    double rate = Double.parseDouble(expiryTable.getValueAt(row, 5).toString().trim());
                                    double mrp = Double.parseDouble(expiryTable.getValueAt(row, 6).toString().trim());
                                    String exp = expiryTable.getValueAt(row, 10).toString();
                                    java.sql.Date expDate = new java.sql.Date(dateFormat.parse(exp).getTime());

                                    query = "select id,current_quantity from inventories where product_id = ? and sale_rate = ? and product_mrp = ? and product_exp_date = ?";
                                    pst = DBConnection.con.prepareStatement(query);
                                    pst.setLong(1, product_id);
                                    pst.setDouble(2, rate);
                                    pst.setDouble(3, mrp);
                                    pst.setDate(4, expDate);

                                    result = pst.executeQuery();

                                    double current_quantity = 0;
                                    long inventory_id = 0;

                                    if (result.next()) {
                                        current_quantity = result.getDouble("current_quantity");
                                        inventory_id = result.getLong("id");
                                        query = "update inventories set current_quantity = ? where id = ?";
                                        pst = DBConnection.con.prepareStatement(query);
                                        pst.setDouble(1, current_quantity - qty);
                                        pst.setLong(2, inventory_id);

                                        pst.executeUpdate();
                                    }

                                    query = "insert into expiry_entries(expiry_id,product_id,sale_rate,product_mrp,quantity,expiry_date,inventory_id)values(?,?,?,?,?,?,"
                                            + (inventory_id != 0 ? inventory_id : null) + ")";
                                    pst = DBConnection.con.prepareStatement(query);
                                    pst.setLong(1, expiry_id);
                                    pst.setLong(2, product_id);
                                    pst.setDouble(3, rate);
                                    pst.setDouble(4, mrp);
                                    pst.setDouble(5, qty);
                                    pst.setDate(6, expDate);

                                    affectedRows = pst.executeUpdate();
                                    if (affectedRows > 0) {
                                        flag = true;
                                    }
                                }
                            }

                            if (flag) {
                                reCreate();
                            } else {
                                JOptionPane.showMessageDialog(this, "Entry cannot saved...try again!!", "Warning",
                                        JOptionPane.WARNING_MESSAGE);
                            }

                        } catch (Exception exc) {
                            JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Bill cannot save with zero amount!!\nOR\nRecord may have invalid values..!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
                if (action.equals(DELETE_ACTION)) {
                    try {
                        if (billAmt > 0) {
                            boolean res = JOptionPane.showConfirmDialog(this,
                                    "Are you sure to remove this information?") == JOptionPane.OK_OPTION;
                            if (res) {
                                int entryNo = (int) previousRecords.getLast().get("entry_no");
                                java.sql.Date date = (java.sql.Date) previousRecords.getLast().get("entry_date");
                                boolean res1 = recoverStock();
                                boolean res2 = deleteFromDB(entryNo, date);

                                if (res1 && res2) {
                                    reCreate();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Entry cannot deleted...try again!!", "Warning",
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (action.equals(UPDATE_ACTION)) {
                    if (dealerId == 0) {
                        JOptionPane.showMessageDialog(this, "Must select a supplier...!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (billAmt > 0 && !isAnyInvalidEntry) {
                        try {
                            boolean res = JOptionPane.showConfirmDialog(this,
                                    "Are you sure to update this information?") == JOptionPane.OK_OPTION;
                            if (res) {
                                int entryNo = (int) previousRecords.getLast().get("entry_no");
                                java.sql.Date entryDate = (java.sql.Date) previousRecords.getLast().get("entry_date");
                                Timestamp timestamp = (Timestamp) previousRecords.getLast().get("created_at");
                                if (recoverStock() && deleteFromDB(entryNo, entryDate)) {
                                    int rows = expiryTable.getRowCount();
                                    String query = "insert into expiries (day_wise_entry_no,dealer_id,created_at)values(?,?,?)";
                                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                                    pst.setInt(1, entryNo);
                                    pst.setLong(2, dealerId);
                                    pst.setTimestamp(3, timestamp);

                                    int affectedRows = pst.executeUpdate();
                                    boolean flag = false;
                                    if (affectedRows > 0) {
                                        query = "select id from expiries order by id desc limit 1";
                                        ResultSet result = DBConnection.executeQuery(query);
                                        long expiry_id = result.next() ? result.getLong("id") : 0;
                                        for (int row = 0; row < rows; row++) {

                                            long pId = Long.parseLong(expiryTable.getValueAt(row, 1).toString());
                                            long product_id = getProductId(pId);
                                            double qty = Double
                                                    .parseDouble(expiryTable.getValueAt(row, 4).toString().trim());
                                            double rate = Double
                                                    .parseDouble(expiryTable.getValueAt(row, 5).toString().trim());
                                            double mrp = Double
                                                    .parseDouble(expiryTable.getValueAt(row, 6).toString().trim());
                                            String exp = expiryTable.getValueAt(row, 10).toString();
                                            java.sql.Date expDate = new java.sql.Date(dateFormat.parse(exp).getTime());

                                            query = "select id,current_quantity from inventories where product_id = ? and sale_rate = ? and product_mrp = ? and product_exp_date = ?";
                                            pst = DBConnection.con.prepareStatement(query);
                                            pst.setLong(1, product_id);
                                            pst.setDouble(2, rate);
                                            pst.setDouble(3, mrp);
                                            pst.setDate(4, expDate);

                                            result = pst.executeQuery();

                                            double current_quantity = 0;
                                            long inventory_id = 0;

                                            if (result.next()) {
                                                current_quantity = result.getDouble("current_quantity");
                                                inventory_id = result.getLong("id");
                                                query = "update inventories set current_quantity = ? where id = ?";
                                                pst = DBConnection.con.prepareStatement(query);
                                                pst.setDouble(1, current_quantity - qty);
                                                pst.setLong(2, inventory_id);

                                                pst.executeUpdate();
                                            }

                                            query = "insert into expiry_entries(expiry_id,product_id,sale_rate,product_mrp,quantity,expiry_date,inventory_id)values(?,?,?,?,?,?,"
                                                    + (inventory_id != 0 ? inventory_id : null) + ")";
                                            pst = DBConnection.con.prepareStatement(query);
                                            pst.setLong(1, expiry_id);
                                            pst.setLong(2, product_id);
                                            pst.setDouble(3, rate);
                                            pst.setDouble(4, mrp);
                                            pst.setDouble(5, qty);
                                            pst.setDate(6, expDate);

                                            affectedRows = pst.executeUpdate();
                                            if (affectedRows > 0) {
                                                flag = true;
                                            }
                                        }
                                    }

                                    if (flag) {
                                        reCreate();
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Entry cannot updated...try again!!",
                                                "Warning",
                                                JOptionPane.WARNING_MESSAGE);
                                    }
                                }
                            }
                        } catch (Exception exc) {
                            JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Bill cannot save with zero amount!!\nOR\nRecord may have invalid values..!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            if (source.equals(editBtn) || source.equals(deleteBtn)) {
                dateChooser.setEnabled(true);
                entryNoField.setEnabled(true);
                entryNoField.requestFocus();
                entryNoField.selectAll();
                actionSource = source.equals(editBtn) ? editBtn : deleteBtn;
            }
            if (source.equals(cancelBtn)) {
                editBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
            }
            if (source.equals(exitBtn)) {
                boolean res = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == 0;
                if (res) {
                    String key = frameKeyMap.get(this);
                    frameTracker.remove(key);
                    dispose();
                }
            }

            if (source.equals(selectBtn)) {
                if (view != null && view instanceof ProductView) {
                    int row = view.getTable().getSelectedRow();
                    if (row != -1) {
                        if (isForShowProductDetails) {
                            String pId = view.getTable().getValueAt(row, 1).toString();
                            pIdField.setText(pId);
                            showProductDetails(pIdField);
                            pQtyField.requestFocus();
                        } else {
                            String pId = view.getTable().getValueAt(row, 2).toString();
                            pBarcodeField.setText(pId);
                            showProductDetails(pBarcodeField);
                            insertIntoBillTable();

                            pBarcodeField.requestFocus();
                        }
                    }
                } else {
                    JTable table = view.getTable();
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String dealerName = table.getValueAt(row, 2).toString();
                        String contact = table.getValueAt(row, 3).toString();
                        String gstNo = table.getValueAt(row, 5).toString();

                        dealerNameField.setText(dealerName);
                        dealerGstNoField.setText(gstNo);
                        mobileField.setText(contact);
                        dealerId = Integer.parseInt(table.getValueAt(row, 1).toString());
                    } else {
                        dealerId = 0;
                    }
                    pBarcodeField.requestFocus();
                }
                dataViewer.setVisible(false);
            }

        }
    }

    private boolean recoverStock() {
        previousRecords.forEach(map -> {
            try {
                if (!map.containsKey("entry_no")) {
                    long inventory_id = (long) map.get("inventory_id");
                    int qty = (int) map.get("quantity");

                    if (inventory_id != 0) {
                        String query = "select current_quantity from inventories where id = ?";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setLong(1, inventory_id);

                        ResultSet result = pst.executeQuery();

                        double current_quantity = 0;

                        if (result.next()) {
                            current_quantity = result.getDouble("current_quantity");
                            query = "update inventories set current_quantity = ? where id = ?";
                            pst = DBConnection.con.prepareStatement(query);
                            pst.setDouble(1, current_quantity + qty);
                            pst.setLong(2, inventory_id);

                            pst.executeUpdate();
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println(e.getMessage() + " at ExpiryEntry.recoverStock()");
            }
        });
        return true;
    }

    private boolean deleteFromDB(int entryNo, java.sql.Date entryDate) {
        try {
            String query = "delete from expiries where day_wise_entry_no = ? and date(created_at) = ?";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, entryNo);
            pst.setDate(2, entryDate);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at ExpiryEntry.deleteFromDB()");
        }
        return true;
    }

    int cnt = 1;
    boolean isForDeleteRow;
    java.util.List<HashMap<String, Object>> previousRecords;

    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        Object source = e.getSource();
        String key = KeyEvent.getKeyText(e.getKeyCode());
        if ((source.equals(pBarcodeField) || source.equals(pIdField)) && key.equals("F1")) {
            isForShowProductDetails = source.equals(pIdField);
            view = new ProductView(this);
            view.getScrollPane().setPreferredSize(new Dimension(800, 350));
            createViewer((JPanel) view);
        }

        if (source.equals(dealerNameField) && key.equals("F1")) {
            view = new DealerView(this);
            view.getScrollPane().setPreferredSize(new Dimension(800, 350));
            createViewer((JPanel) view);
        }

        if (view != null && source.equals(view.getTable()) && key.equals("Enter")) {
            selectBtn.doClick();
        }

        if ((source.equals(pBarcodeField) || source.equals(pIdField)) && key.equals("Enter")) {
            showProductDetails(source);
            if (source.equals(pBarcodeField) && !pBarcodeField.getText().isBlank()) {
                insertIntoBillTable();
            }
        }

        if (source.equals(expDateChooser.getDateEditor().getUiComponent()) && key.equals("Enter")) {
            insertIntoBillTable();
        }

        String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
        if (source.equals(expiryTable) && key.equals("D") && modifier.equals("Ctrl")) {
            isForDeleteRow = true;
            int option = JOptionPane.showConfirmDialog(this, "Are you sure to remove this entry?", "Confirmation",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                int row = expiryTable.getSelectedRow();
                if (row >= 0) {
                    tableModel.removeRow(row);
                    cnt = updateSrNo(tableModel);
                    if (tableModel.getRowCount() == 0) {
                        pBarcodeField.requestFocus();
                    }
                }
            }
            calculate();
            isForDeleteRow = false;
        }

        if (source.equals(entryNoField) && key.equals("Enter")) {
            try {
                tableModel.setRowCount(0);
                int entryNo = Integer.parseInt(entryNoField.getText().trim());
                java.sql.Date date = new java.sql.Date(dateChooser.getDate().getTime());

                String query = "select *,expiries.created_at as expiry_created_at,name as dealer_name,expiry_entries.quantity as expired_qty,expiry_entries.product_mrp as expiry_mrp,expiry_entries.sale_rate as expiry_rate from dealers,expiries,expiry_entries,products where expiries.id = expiry_entries.expiry_id and products.id = expiry_entries.product_id and dealers.id = expiries.dealer_id and expiries.day_wise_entry_no = ? and date(expiries.created_at) = ? order by expiry_entries.id desc";
                PreparedStatement pst = DBConnection.con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                pst.setInt(1, entryNo);
                pst.setDate(2, date);

                ResultSet result = pst.executeQuery();

                result.first();
                dealerId = result.getLong("dealer_id");
                String dealerName = result.getString("dealer_name");
                String mobile = result.getString("contact_no");
                String gstNo = result.getString("gst_no");
                Timestamp timestamp = result.getTimestamp("expiry_created_at");
                result.beforeFirst();

                previousRecords = new ArrayList<HashMap<String, Object>>();

                while (result.next()) {

                    long inventory_id = result.getLong("inventory_id");
                    long id = result.getLong("product_id");
                    String barcode = result.getString("barcode_no");
                    String pId = result.getString("p_id");
                    String name = result.getString("product_name");
                    Double qty = result.getDouble("expired_qty");
                    Double rate = result.getDouble("expiry_rate");
                    Double mrp = result.getDouble("expiry_mrp");
                    Date expDate = new Date(result.getDate("expiry_date").getTime());

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("product_original_id", id);
                    map.put("inventory_id", inventory_id);
                    map.put("quantity", qty);
                    map.put("rate", rate);
                    map.put("mrp", mrp);
                    map.put("exp_date", expDate);

                    previousRecords.add(map);

                    pBarcodeField.setText(barcode);
                    pIdField.setText(pId);
                    pNameField.setText(name);
                    pQtyField.setText("" + qty);
                    pMRPField.setText("" + mrp);
                    rateField.setText("" + rate);
                    expDateChooser.setDate(expDate);

                    insertIntoBillTable();
                }

                HashMap<String, Object> map = new HashMap<>();
                map.put("entry_no", entryNo);
                map.put("entry_date", date);
                map.put("created_at", timestamp);
                previousRecords.add(map);

                dealerNameField.setText(dealerName);
                dealerGstNoField.setText(gstNo);
                mobileField.setText(mobile);
                cnt = updateSrNo(tableModel);
                calculate();
                action = actionSource.equals(deleteBtn) ? DELETE_ACTION : UPDATE_ACTION;
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Invalid entry number or date..!" + exc.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
