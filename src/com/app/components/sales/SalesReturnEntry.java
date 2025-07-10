package com.app.components.sales;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.app.components.abstracts.AbstractButton;
import com.app.components.purchase.support.*;
import com.app.config.*;
import com.app.partials.event.*;
import com.toedter.calendar.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public final class SalesReturnEntry extends AbstractButton implements PropertyChangeListener, CellEditorListener {

    JLabel lastScannedItemLabel, dateLabel, billNoLabel, counterNoLabel, custNameLabel, custMobileLabel, pIdLabel,
            pBarcodeLabel, printLabel,
            pQtyLabel, pRateLabel, pMRPLabel, billAmtLabel, returnAmtLabel, paidAmtLabel, pBillAmtLabel,
            lpBillAmtLabel, pBillNoLabel, creditNoteIdLabel, creditNoteAmtLabel, lpBillNoLabel, paymentTypeLabel,
            totalQtyLabel, pDiscLabel, pBillQtyLabel, lpBillQtyLabel;
    JPanel mainPanel, sidePanel, subBottomPanel, lastScannedItemPanel, billInfoPanel, custInfoPanel, billFormPanel,
            bottomPanel;
    JTextField billNoField, counterNoField, custNameField, mobileField, pBarcodeField, pIdField, pNameField, pQtyField,
            pRateField, lastScannedItemField, creditNoteIdField, creditNoteAmtField,
            pMRPField, billAmtField, returnAmtField, paidAmtField, pBillNoField, lpBillNoField, pBillAmtField,
            lpBillAmtField, totalQtyField, pDiscField, pBillQtyField, lpBillQtyField;
    JDateChooser dateChooser;
    JComboBox<String> paymentTypes, printOptions;
    JTable billTable, stockTable;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    JScrollPane scrollPane;
    ProductView productView;
    JButton stockSelectBtn;
    int rowId = 1;
    final Integer UPDATE_ACTION = -1, SAVE_ACTION = 1, DELETE_ACTION = 0;
    Integer action = SAVE_ACTION;

    private void insertIntoBillTable() {
        try {
            String b = pBarcodeField.getText().trim();
            String id = pIdField.getText().trim();
            String name = pNameField.getText().trim();
            String qty = pQtyField.getText().trim();
            String rate = pRateField.getText().trim();
            String mrp = pMRPField.getText().trim();

            boolean bRes = isBarcodeValid(b);
            boolean idRes = isBarcodeValid(id);
            double rateVal = Double.parseDouble(rate);
            int qtyVal = Integer.parseInt(qty);

            if (!bRes || !idRes) {
                JOptionPane.showMessageDialog(this, "Barcode OR Item code is invalid!!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            double mrpVal = rateVal;
            if (!mrp.isBlank()) {
                mrpVal = Double.parseDouble(mrp);
            }

            if (mrpVal < rateVal) {
                JOptionPane.showMessageDialog(this, "Rate not should be greater then MRP..!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                pMRPField.requestFocus();
                return;
            }

            double dAmt = (rateVal * discountVal) / 100;

            int row = checkRecordExist(id, rateVal, mrpVal);
            if (row == -1) {
                tableModel.insertRow(0, new Object[] {
                        cnt++, id, b, name, qtyVal,
                        rateVal,
                        mrpVal,
                        discountVal,
                        dAmt,
                        rateVal - dAmt,
                        (rateVal - dAmt) * qtyVal,
                        taxVal, 0, stockId == null ? "-" : stockId
                });
            } else {
                tableModel.setValueAt(Integer.parseInt(tableModel.getValueAt(row, 4).toString()) + qtyVal,
                        row,
                        4);
            }

            calculate();
            stockId = null;
            pBarcodeField.setText("");
            pIdField.setText("");
            pNameField.setText("");
            pQtyField.setText("1");
            pRateField.setText("");
            pMRPField.setText("");
            pBarcodeField.requestFocus();
            discountVal = 0;

        } catch (NullPointerException exc) {
            System.out.println(exc.getMessage() + " at SaleBill.insertIntoBillTable()");
            JOptionPane.showMessageDialog(this, "The values are not should be empty..!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException exc) {
            System.out.println(exc.getMessage() + " at SaleBill.insertIntoBillTable()");
            JOptionPane.showMessageDialog(this, "Please, enter valid numeric values..!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            if (pBarcodeField.getText().isBlank())
                pBarcodeField.requestFocus();
            else
                pQtyField.requestFocus();
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(this, "Invalid details..!", "Error", JOptionPane.ERROR_MESSAGE);
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

            int barcode = Integer.parseInt(
                    isForShowProductDetails ? pIdField.getText().trim()
                            : pBarcodeField.getText().trim());

            String query = isForShowProductDetails ? "select * from products where p_id = ? limit 1"
                    : "select * from products where barcode_no = ? limit 1";

            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            if (isForShowProductDetails) {
                pst.setInt(1, barcode);
            } else {
                pst.setString(1, "00" + barcode);
            }

            ResultSet result = pst.executeQuery();
            if (result.next()) {
                String rate = result.getString("sale_rate");
                String mrp = result.getString("product_mrp");
                String pName = result.getString("product_name").trim();
                pBarcodeField.setText(result.getString("barcode_no"));
                pIdField.setText(result.getString("p_id"));
                pNameField.setText(pName);
                pQtyField.setText("1");
                pRateField.setText(rate);
                pMRPField.setText(mrp);
                discountVal = result.getDouble("discount");

                int availableStock = getStock(Integer.parseInt(pIdField.getText()));
                lastScannedItemField.setText(
                        pBarcodeField.getText().trim() + " Item Name:" + pName + " Available Qty.:" + availableStock);
                showStockList(pIdField.getText());
            } else {
                JOptionPane.showMessageDialog(this, "In-correct barcode or item code...!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException exc) {
            // exc.printStackTrace();
            System.out.println(exc.getMessage() + " at SaleBill.java 106");
            JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException exc) {
            // exc.printStackTrace();
            System.out.println(exc.getMessage() + " at SaleBill.java 106");
            JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception exc) {
            System.out.println(exc.getLocalizedMessage() + " at SaleBill.showProductDetails()");
        }
    }

    private int getStock(int pId) throws Exception {
        PreparedStatement pst = DBConnection.con.prepareStatement(
                "select sum(current_quantity) as qty from inventories where product_id = ?");
        pst.setInt(1, pId);
        ResultSet result = pst.executeQuery();
        if (result.next()) {
            return result.getInt("qty");
        }
        return 0;
    }

    private int checkRecordExist(String pId, double rate, double mrp) {
        int rows = tableModel.getRowCount();
        int row = -1;
        if (rows > 0) {
            for (int index = 0; index < rows; index++) {
                String rowId = tableModel.getValueAt(index, 1).toString();
                String rowRate = tableModel.getValueAt(index, 5).toString();
                String rowMrp = tableModel.getValueAt(index, 6).toString();

                double r = Double.parseDouble(rowRate);
                double m = Double.parseDouble(rowMrp);

                if (pId.equals(rowId) && rate == r && mrp == m) {
                    row = index;
                    break;
                }
            }
        }
        return row;
    }

    final KeyListener keyListenerToAddTableRecords = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            String key = KeyEvent.getKeyText(e.getKeyCode());
            Object source = e.getSource();
            if (source.equals(pMRPField) && key.equals("Enter")) {
                insertIntoBillTable();
            }

            if ((source.equals(pBarcodeField) || source.equals(pIdField)) && key.equals("Enter")) {
                showProductDetails(source);
            }
        }
    };

    private void showStockList(String pId) {

        StockView view = new StockView(pId, this);
        stockSelectBtn = view.getSelectBtn();
        stockTable = view.getTable();
        if (stockTable.getRowCount() > 1) {
            Container container = dataViewer.getContentPane();
            container.removeAll();
            view.setPreferredSize(new Dimension(530, 390));

            view.getScrollPane().setPreferredSize(new Dimension(520, 320));
            container.add(view);

            container.revalidate();
            container.repaint();

            dataViewer.setSize(new Dimension(560, 420));
            double w = toolkit.getScreenSize().getWidth();
            double h = toolkit.getScreenSize().getHeight();
            int x = ((int) (w - dataViewer.getWidth()) / 2);
            int y = ((int) (h - dataViewer.getHeight()) / 2);
            dataViewer.setLocation(new Point(x, y));
            stockTable.requestFocus();
            dataViewer.setVisible(true);
        } else {
            dataViewer.setVisible(false);
            stockSelectBtn.doClick();
        }
    }

    private void calculate() {
        int rows = tableModel.getRowCount();

        double total = 0;
        double tDisc = 0;
        long tQty = 0;

        for (int i = 0; i < rows; i++) {
            try {
                long qty = Long.parseLong(tableModel.getValueAt(i, 4).toString().trim());
                double rate = Double.parseDouble(tableModel.getValueAt(i, 5).toString().trim());
                String d = tableModel.getValueAt(i, 7).toString().trim();
                if (d.contains("%"))
                    d = d.replace('%', '\s').trim();
                double discount = Double.parseDouble(d);

                double dAmt = (rate * discount) / 100;
                double amt = rate - dAmt;
                double nAmt = amt * qty;

                total += nAmt;
                tDisc += dAmt * qty;
                tQty += qty;

                tableModel.setValueAt(dAmt, i, 8);
                tableModel.setValueAt(amt, i, 9);
                tableModel.setValueAt(nAmt, i, 10);
            } catch (Exception e) {
                System.out.println(e.getMessage() + " at SaleBill.calculate()");
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        billAmtField.setText(String.format("%.2f", total));
        totalQtyField.setText("" + tQty);
        pDiscField.setText(String.format("%.2f", tDisc));
    }

    double discountVal = 0, taxVal = 0;
    final Dimension innerPanelSize = new Dimension(1330 - 160, 40);

    public SalesReturnEntry(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
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

        allocateToTopPanel();

        designTable();

        allocateToBottomPanel();

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

        creditNoteIdLabel = new JLabel("Crd.Note ID");
        creditNoteIdLabel.setFont(labelFont);

        creditNoteIdField = new JTextField(4);
        creditNoteIdField.setFont(labelFont);
        creditNoteIdField.addFocusListener(this);
        creditNoteIdField.addKeyListener(this);
        creditNoteIdField.setText("0");

        buttonPanel.add(creditNoteIdLabel);
        buttonPanel.add(creditNoteIdField);

        creditNoteAmtLabel = new JLabel("Crd.Amt.");
        creditNoteAmtLabel.setFont(labelFont);

        creditNoteAmtField = new JTextField(4);
        creditNoteAmtField.setFont(labelFont);
        creditNoteAmtField.setEnabled(false);
        creditNoteAmtField.setDisabledTextColor(Color.darkGray);
        creditNoteAmtField.setText("0");

        buttonPanel.add(creditNoteAmtLabel);
        buttonPanel.add(creditNoteAmtField);

        sidePanel.add(billInfoPanel);
        sidePanel.add(custInfoPanel);
        sidePanel.add(billFormPanel);
        sidePanel.add(lastScannedItemPanel);
        sidePanel.add(scrollPane);
        sidePanel.add(bottomPanel);

        mainPanel.add(sidePanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        pQtyField.addKeyListener(new CustomKeyListener(pRateField));
        pRateField.addKeyListener(new CustomKeyListener(pMRPField));

        String billNo = getLastId();
        billNoField.setText(billNo);
        setBottomFields();
        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            pBarcodeField.requestFocus();
        });
    }

    private void setBottomFields() {
        try {
            String query = "select total_amount,day_wise_bill_no,sum(quantity) as qty from bills,sales where date(bills.created_at) = ? and bills.id = sales.bill_id group by bill_id,total_amount,day_wise_bill_no order by bill_id desc limit 2";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setDate(1, new java.sql.Date(new Date().getTime()));
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                String qty = result.getString("qty");
                String billNo = result.getString("day_wise_bill_no");
                String amount = result.getString("total_amount");
                if (result.getRow() == 1) {
                    pBillNoField.setText(billNo);
                    pBillAmtField.setText(amount);
                    pBillQtyField.setText(qty);
                } else {
                    lpBillNoField.setText(billNo);
                    lpBillAmtField.setText(amount);
                    lpBillQtyField.setText(qty);
                }
            }
            result.close();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getLastId() {
        String id = "1";
        try {
            String query = "select day_wise_bill_no from bills where date(created_at) = ? order by day_wise_bill_no desc limit 1";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setDate(1, new java.sql.Date(new Date().getTime()));
            ResultSet result = pst.executeQuery();
            id = result.next() ? "" + (result.getInt("day_wise_bill_no") + 1) : "1";
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at SaleBill.getLastId()");
        }
        return id;
    }

    private void allocateToTopPanel() {
        lastScannedItemPanel = new JPanel(flowLayoutLeft);
        lastScannedItemPanel.setBackground(Color.white);
        lastScannedItemPanel.setPreferredSize(innerPanelSize);

        lastScannedItemLabel = new JLabel("Last Scanned Item :");
        lastScannedItemLabel.setForeground(Color.darkGray);
        lastScannedItemLabel.setFont(labelFont);

        lastScannedItemField = new JTextField(50);
        lastScannedItemField.setFont(labelFont);
        lastScannedItemField.setEnabled(false);
        lastScannedItemField.setText("Item name: NA \tStock : 0");
        lastScannedItemField.setBackground(lemonYellow);

        lastScannedItemPanel.add(lastScannedItemLabel);
        lastScannedItemPanel.add(lastScannedItemField);

        billInfoPanel = new JPanel(flowLayoutLeft);
        billInfoPanel.setBackground(Color.white);
        billInfoPanel.setPreferredSize(innerPanelSize);

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

        custNameLabel = new JLabel("Customer Name:");
        custNameLabel.setFont(labelFont);

        custNameField = new JTextField(25);
        custNameField.setFont(labelFont);
        custNameField.setForeground(Color.darkGray);
        custNameField.setBackground(lemonYellow);
        custNameField.addFocusListener(this);
        custNameField.addKeyListener(this);

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

        pBarcodeLabel = new JLabel("Barcode :");
        pBarcodeLabel.setFont(labelFont);

        pBarcodeField = new JTextField(7);
        pBarcodeField.addFocusListener(this);
        pBarcodeField.addActionListener(this);
        pBarcodeField.addKeyListener(keyListenerToAddTableRecords);
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
        pIdField.addKeyListener(keyListenerToAddTableRecords);
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
        pMRPField.addKeyListener(keyListenerToAddTableRecords);
        pMRPField.addKeyListener(this);
        pMRPField.addFocusListener(this);

        billFormPanel.add(pMRPLabel);
        billFormPanel.add(pMRPField);
    }

    private void allocateToBottomPanel() {
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        bottomPanel.setBackground(Color.white);
        bottomPanel.setPreferredSize(new Dimension(1330 - 160, 80));

        lpBillNoLabel = new JLabel("L.P.No.");
        lpBillNoLabel.setFont(labelFont);

        lpBillNoField = new JTextField(4);
        lpBillNoField.setText("0");
        lpBillNoField.setEnabled(false);
        lpBillNoField.setFont(smallFont);
        lpBillNoField.setDisabledTextColor(Color.darkGray);
        lpBillNoField.setBackground(lemonYellow);

        bottomPanel.add(lpBillNoLabel);
        bottomPanel.add(lpBillNoField);

        pBillNoLabel = new JLabel("P.No.");
        pBillNoLabel.setFont(labelFont);

        pBillNoField = new JTextField(4);
        pBillNoField.setText("0");
        pBillNoField.setFont(smallFont);
        pBillNoField.setEnabled(false);
        pBillNoField.setDisabledTextColor(Color.darkGray);
        pBillNoField.setBackground(lemonYellow);

        bottomPanel.add(pBillNoLabel);
        bottomPanel.add(pBillNoField);

        lpBillQtyLabel = new JLabel("L.P.Qty.");
        lpBillQtyLabel.setFont(labelFont);

        lpBillQtyField = new JTextField(4);
        lpBillQtyField.setText("0");
        lpBillQtyField.setEnabled(false);
        lpBillQtyField.setFont(smallFont);
        lpBillQtyField.setDisabledTextColor(Color.darkGray);
        lpBillQtyField.setBackground(lemonYellow);

        bottomPanel.add(lpBillQtyLabel);
        bottomPanel.add(lpBillQtyField);

        pBillQtyLabel = new JLabel("P.Qty.");
        pBillQtyLabel.setFont(labelFont);

        pBillQtyField = new JTextField(4);
        pBillQtyField.setText("0");
        pBillQtyField.setFont(smallFont);
        pBillQtyField.setEnabled(false);
        pBillQtyField.setDisabledTextColor(Color.darkGray);
        pBillQtyField.setBackground(lemonYellow);

        bottomPanel.add(pBillQtyLabel);
        bottomPanel.add(pBillQtyField);

        lpBillAmtLabel = new JLabel("L.P.Amt.");
        lpBillAmtLabel.setFont(labelFont);

        lpBillAmtField = new JTextField(7);
        lpBillAmtField.setText("0.00");
        lpBillAmtField.setFont(smallFont);
        lpBillAmtField.setEnabled(false);
        lpBillAmtField.setDisabledTextColor(Color.darkGray);
        lpBillAmtField.setBackground(lemonYellow);

        bottomPanel.add(lpBillAmtLabel);
        bottomPanel.add(lpBillAmtField);

        pBillAmtLabel = new JLabel("P.Amt.");
        pBillAmtLabel.setFont(labelFont);

        pBillAmtField = new JTextField(7);
        pBillAmtField.setText("0.00");
        pBillAmtField.setFont(smallFont);
        pBillAmtField.setEnabled(false);
        pBillAmtField.setDisabledTextColor(Color.darkGray);
        pBillAmtField.setBackground(lemonYellow);

        bottomPanel.add(pBillAmtLabel);
        bottomPanel.add(pBillAmtField);

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

        pDiscLabel = new JLabel("Disc.Amt.:");
        pDiscLabel.setFont(labelFont);

        pDiscField = new JTextField(5);
        pDiscField.setFont(labelFont);
        pDiscField.setEnabled(false);
        pDiscField.setDisabledTextColor(Color.darkGray);
        pDiscField.setBackground(lemonYellow);
        pDiscField.setText("0.00");

        bottomPanel.add(pDiscLabel);
        bottomPanel.add(pDiscField);

        totalQtyLabel = new JLabel("T.Qty.:");
        totalQtyLabel.setFont(labelFont);

        totalQtyField = new JTextField(5);
        totalQtyField.setEnabled(false);
        totalQtyField.setDisabledTextColor(Color.darkGray);
        totalQtyField.setText("0");
        totalQtyField.setBackground(lemonYellow);
        totalQtyField.setFont(labelFont);

        bottomPanel.add(totalQtyLabel);
        bottomPanel.add(totalQtyField);

        paidAmtLabel = new JLabel("Paid Amt.");
        paidAmtLabel.setFont(labelFont);

        paidAmtField = new JTextField(12);
        paidAmtField.setBackground(lemonYellow);
        paidAmtField.addKeyListener(this);
        paidAmtField.addFocusListener(this);
        paidAmtField.setFont(labelFont);

        bottomPanel.add(paidAmtLabel);
        bottomPanel.add(paidAmtField);

        returnAmtLabel = new JLabel("Return Amt.");
        returnAmtLabel.setFont(labelFont);

        returnAmtField = new JTextField(12);
        returnAmtField.setBackground(lemonYellow);
        returnAmtField.setEnabled(false);
        returnAmtField.setDisabledTextColor(Color.darkGray);
        returnAmtField.setFont(labelFont);
        returnAmtField.setHorizontalAlignment(SwingConstants.RIGHT);

        bottomPanel.add(returnAmtLabel);
        bottomPanel.add(returnAmtField);
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
                "disc.%", "disc.amt.", "Amt.", "Net Amt.", "Tax", "Tax Amt.", "ID" };

        for (String column : columnNames)
            tableModel.addColumn(column);

        sorter = new TableRowSorter<>(tableModel);
        billTable = new JTable(tableModel);
        billTable.setFont(labelFont);
        billTable.setSelectionBackground(skyBlue);
        billTable.setSelectionForeground(Color.white);
        billTable.setRowSorter(sorter);
        billTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        billTable.setRowHeight(30);
        billTable.addFocusListener(this);
        billTable.addKeyListener(this);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        billTable.setCellSelectionEnabled(true);
        billTable.getDefaultEditor(Object.class).addCellEditorListener(this);

        JTableHeader header = billTable.getTableHeader();
        header.setFont(labelFont);

        TableColumnModel columnModel = billTable.getColumnModel();
        int[] values = new int[] { 70, 140, 170, 220, 70, 100, 100, 90, 120, 120, 120, 70, 120, 70 };
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

    public void editingCanceled(ChangeEvent e) {
        calculate();
    }

    public void editingStopped(ChangeEvent e) {
        calculate();
    }

    public void propertyChange(PropertyChangeEvent e) {
        //
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
        if (source instanceof JTable && billTable.getRowCount() > 0 && !isForDeleteRow) {
            billTable.removeRowSelectionInterval(0, billTable.getRowCount() - 1);
        } else if (!(source instanceof JTable)) {
            ((JComponent) e.getSource()).setBackground(lemonYellow);
        }
    }

    String stockId = null;
    int customerId = 0;

    private int insertIntoCustomerTable(String name, String contact) {
        int id = 0;
        try {
            if (!name.matches(NAME_PATTERN))
                return id;
            if (!contact.isBlank() && contact.matches(MOBILE_NO_PATTERN))
                return id;

            String query = "insert into customers (name,contact_no) values(?,?)";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, contact);

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                query = "select id from customers order by id desc limit 1";
                java.sql.Statement st = DBConnection.con.createStatement();
                ResultSet result = st.executeQuery(query);
                if (result.next()) {
                    id = result.getInt("id");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + " at SaleBill.insertIntoCustomerTable()");
        }
        return id;
    }

    private long getProductId(int itemCode) {
        long id = 0;
        try {
            String query = "select id from products where p_id = ?";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, itemCode);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                id = result.getLong("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return id;

    }

    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        if (source.equals(newBtn)) {
            reCreate();
        } else {
            if (source.equals(saveBtn)) {
                if (action.equals(SAVE_ACTION)) {
                    int choseOption = JOptionPane.showConfirmDialog(this, "Are you sure to save this information?",
                            "Confirmation",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (choseOption == JOptionPane.OK_OPTION) {
                        double billAmt = Double.parseDouble(billAmtField.getText());
                        boolean flag = false;
                        if (billAmt != 0) {
                            try {
                                int rows = billTable.getRowCount();

                                int billNo = Integer.parseInt(getLastId());
                                String name = custNameField.getText().trim().toUpperCase();
                                String contact = mobileField.getText().trim().toUpperCase();

                                String salesReturnId = creditNoteIdField.getText().trim();

                                salesReturnId = salesReturnId.isEmpty() || salesReturnId.equals("0") ? "0"
                                        : salesReturnId;

                                String paymentType = paymentTypes.getSelectedItem().toString();
                                int custId = customerId == 0
                                        ? insertIntoCustomerTable(name, contact)
                                        : customerId;
                                long salesRId = Long.parseLong(salesReturnId);

                                String query = "insert into bills(day_wise_bill_no,customer_id,payment_type,sales_return_id,total_amount)values(?,"
                                        + (custId == 0 ? null : custId) + ",?," + (salesRId == 0 ? null : salesRId)
                                        + ",?)";
                                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                                pst.setInt(1, billNo);
                                pst.setString(2, paymentType);
                                pst.setDouble(3, billAmt);

                                customerId = 0;

                                int affectedRows = pst.executeUpdate();
                                pst.close();
                                if (affectedRows > 0) {
                                    query = "select id from bills where day_wise_bill_no = ? and date(created_at) = ?";
                                    pst = DBConnection.con.prepareStatement(query);
                                    pst.setInt(1, billNo);
                                    pst.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));

                                    ResultSet result = pst.executeQuery();
                                    long billId = 0;
                                    if (result.next()) {
                                        billId = result.getLong("id");
                                    }
                                    pst.close();
                                    result.close();
                                    for (int i = 0; i < rows; i++) {
                                        int itemCode = Integer.parseInt(billTable.getValueAt(i, 1).toString());
                                        long productId = getProductId(itemCode);
                                        int qty = Integer.parseInt(billTable.getValueAt(i, 4).toString());
                                        double rate = Double.parseDouble(billTable.getValueAt(i, 5).toString());
                                        double mrp = Double.parseDouble(billTable.getValueAt(i, 6).toString());
                                        double discount = Double.parseDouble(billTable.getValueAt(i, 7).toString());
                                        String stockId = billTable.getValueAt(i, 13).toString();
                                        // System.out.println(stockId);

                                        query = "insert into sales(product_id,bill_id,new_rate,new_mrp,quantity,discount,inventory_id)values(?,?,?,?,?,?,?)";
                                        pst = DBConnection.con.prepareStatement(query);
                                        pst.setLong(1, productId);
                                        pst.setLong(2, billId);
                                        pst.setDouble(3, rate);
                                        pst.setDouble(4, mrp);
                                        pst.setInt(5, qty);
                                        pst.setDouble(6, discount);
                                        pst.setLong(7, stockId.contains("-") ? null
                                                : Long.parseLong(stockId));
                                        affectedRows = pst.executeUpdate();
                                        pst.close();
                                        if (affectedRows > 0 && !stockId.contains("-")) {
                                            // System.out.println(Long.parseLong(stockId));
                                            query = "select current_quantity from inventories where id = ? limit 1";
                                            pst = DBConnection.con.prepareStatement(query);
                                            pst.setLong(1, Long.parseLong(stockId));
                                            result = pst.executeQuery();

                                            if (result.next()) {
                                                int availableQty = result.getInt("current_quantity");
                                                query = "update inventories set current_quantity = ? where id = ?";
                                                pst = DBConnection.con.prepareStatement(query);
                                                pst.setInt(1, (availableQty - qty));
                                                pst.setLong(2, Long.parseLong(stockId));
                                                affectedRows = pst.executeUpdate();
                                                flag = affectedRows > 0;
                                            }
                                        }
                                    }
                                }
                                if (flag) {
                                    newBtn.doClick();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Something wrong..try again!", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            } catch (Exception exc) {
                                System.out.println(exc.getMessage() + " at SaleBill.actionPerformed().save");
                                JOptionPane.showMessageDialog(this, exc.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Bill cannot save with zero amount..!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                if (action.equals(UPDATE_ACTION)) {
                    int choseOption = JOptionPane.showConfirmDialog(this, "Are you sure to update this information?",
                            "Confirmation",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (choseOption == JOptionPane.OK_OPTION) {

                    }
                }

                if (action.equals(DELETE_ACTION)) {
                    int choseOption = JOptionPane.showConfirmDialog(this, "Are you sure to delete this information?",
                            "Confirmation",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (choseOption == JOptionPane.OK_OPTION) {

                    }
                }

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
                action = source.equals(editBtn) ? UPDATE_ACTION : DELETE_ACTION;
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
                try {
                    if (selectBtn.getActionCommand().equals("other views")) {
                        JTable table = productView.getTable();
                        int row = table.getSelectedRow();
                        if (row != -1) {
                            String barcode = table.getValueAt(row, 2).toString();
                            String id = table.getValueAt(row, 1).toString();
                            String rate = table.getValueAt(row, 4).toString();
                            String mrp = table.getValueAt(row, 5).toString();
                            String pName = table.getValueAt(row, 3).toString();
                            pIdField.setText(id);
                            pBarcodeField.setText(barcode);
                            pNameField.setText(pName);
                            pRateField.setText(rate);
                            pMRPField.setText(mrp);
                            int availableStock = getStock(Integer.parseInt(pIdField.getText()));
                            lastScannedItemField.setText(pBarcodeField.getText().trim() + " Item Name:" + pName
                                    + " Available Qty.:" + availableStock);
                            showStockList(id);
                        } else {
                            JOptionPane.showMessageDialog(this, "Item not selected..!");
                        }
                    } else {
                        JTable table = customerView.getTable();
                        int row = table.getSelectedRow();
                        if (row != -1) {
                            String name = table.getValueAt(row, 2).toString();
                            String contactNo = table.getValueAt(row, 3).toString();
                            customerId = Integer.parseInt(table.getValueAt(row, 1).toString());

                            custNameField.setText(name);
                            mobileField.setText(contactNo);

                            dataViewer.setVisible(false);
                        } else {
                            customerId = 0;
                            dataViewer.setVisible(false);
                        }
                        pBarcodeField.requestFocus();
                    }
                } catch (Exception exc) {
                    System.out.println(exc.getMessage() + " at SaleBill.actionPerformed() 1114");
                    JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (source.equals(stockSelectBtn)) {
                int row = stockTable.getSelectedRow();
                if (row != -1) {
                    stockId = stockTable.getValueAt(row, 0).toString();
                    pRateField.setText(stockTable.getValueAt(row, 3).toString());
                    pMRPField.setText(stockTable.getValueAt(row, 2).toString());
                    dataViewer.setVisible(false);
                } else {
                    stockId = null;
                }
                if (isForShowProductDetails)
                    pQtyField.requestFocus();
                else
                    insertIntoBillTable();
            }
        }

    }

    private void updateSrNo() {
        int rows = tableModel.getRowCount();
        if (rows > 0) {
            cnt = 1;
            for (int row = rows - 1; row >= 0; tableModel.setValueAt(cnt++, row, 0), row--)
                ;
        } else {
            cnt = 1;
        }
    }

    int cnt = 1;
    boolean isForShowProductDetails, isForDeleteRow;
    HashMap<Integer, String> rowStockIdMap = new HashMap<>();
    CustomerView customerView;

    public void keyReleased(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        Object source = e.getSource();

        if ((source.equals(pBarcodeField) || source.equals(pIdField)) && key.equals("F1")) {
            isForShowProductDetails = source.equals(pIdField);
            productView = new ProductView(this);
            productView.getScrollPane().setPreferredSize(new Dimension(800, 350));
            createViewer(productView);
        }

        if (source.equals(custNameField) && key.equals("F1")) {
            customerView = new CustomerView(this);
            customerView.getScrollPane().setPreferredSize(new Dimension(800, 350));
            createViewer(customerView);
        }

        if (source.equals(billTable)) {
            String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
            if (modifier.equals("Ctrl") && key.equals("D")) {
                isForDeleteRow = true;
                int option = JOptionPane.showConfirmDialog(this, "Are you sure to remove this entry?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    int row = billTable.getSelectedRow();
                    if (row >= 0) {
                        rowStockIdMap.remove(tableModel.getValueAt(row, 13));
                        tableModel.removeRow(row);
                        calculate();
                        updateSrNo();
                        if (tableModel.getRowCount() == 0) {
                            pBarcodeField.requestFocus();
                            rowId = 1;
                        }
                    }
                }
                isForDeleteRow = false;
            }
        }

        if (source.equals(paidAmtField) && key.equals("Enter")) {
            double amt = Double.parseDouble(paidAmtField.getText().trim());
            double returnAmt = amt - Double.parseDouble(billAmtField.getText().trim());
            returnAmt = Math.round(returnAmt);
            returnAmtField.setText(String.format("%.2f", returnAmt));
        }

        if (source.equals(billNoField) && key.equals("Enter")) {
            try {
                tableModel.setRowCount(0);
                String query = "select * from bills,sales,products where bills.id = sales.bill_id and products.id = sales.product_id and day_wise_bill_no = ? and date(bills.created_at) = ? order by sales.id desc";
                int billNo = Integer.parseInt(billNoField.getText());
                java.sql.Date date = new java.sql.Date(dateChooser.getDate().getTime());

                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                pst.setInt(1, billNo);
                pst.setDate(2, date);

                ResultSet result = pst.executeQuery();
                int cnt = 1, flag = 0;
                while (result.next()) {
                    flag = 1;
                    long itemCode = result.getLong("p_id");
                    String barcode = result.getString("barcode_no");
                    String name = result.getString("product_name");
                    long qty = result.getLong("quantity");
                    double mrp = result.getDouble("new_mrp");
                    double rate = result.getDouble("new_rate");
                    double discount = result.getDouble("discount");
                    long stockId = result.getLong("inventory_id");

                    double dAmt = (rate * discount) / 100;
                    tableModel.insertRow(0, new Object[] {
                            cnt++, itemCode, barcode, name, qty,
                            rate,
                            mrp,
                            discount,
                            dAmt,
                            rate - dAmt,
                            (rate - dAmt) * qty,
                            taxVal, 0, stockId == 0 ? "-" : stockId
                    });
                }

                if (flag == 0)
                    JOptionPane.showMessageDialog(this, "Bill not found..try again!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                calculate();
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Invalid bill no..try again!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
