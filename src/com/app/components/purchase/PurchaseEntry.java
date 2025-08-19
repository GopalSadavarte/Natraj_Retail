package com.app.components.purchase;

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

public final class PurchaseEntry extends AbstractButton implements CellEditorListener {

    JLabel dateLabel, billNoLabel, counterNoLabel, dealerNameLabel, dealerMobileLabel, pIdLabel,
            pBarcodeLabel, pFreeQtyLabel, purchaseRateLabel, pMFDDateLabel, pEXPDateLabel, lpBillAmtLabel, entryNoLabel,
            dealerGstNoLabel, purchaseDiscLabel,
            pQtyLabel, pRateLabel, pMRPLabel, billAmtLabel, pBillAmtLabel,
            totalQtyLabel, pDiscLabel, pBillQtyLabel, lpBillQtyLabel, pBillNoLabel, lpBillNoLabel;
    JPanel mainPanel, subBottomPanel, lastScannedItemPanel, billInfoPanel, dealerInfoPanel, billFormPanel,
            bottomPanel;
    JTextField billNoField, counterNoField, dealerNameField, mobileField, pBarcodeField, pIdField, pNameField,
            pQtyField, entryNoField, purchaseDiscField,
            pRateField, dealerGstNoField, pFreeQtyField, purchaseRateField,
            pMRPField, billAmtField, pBillNoField, lpBillNoField,
            pBillAmtField,
            lpBillAmtField, totalQtyField, pDiscField, pBillQtyField, lpBillQtyField;
    JDateChooser dateChooser, expDateChooser, mfdDateChooser;
    JTable purchaseTable;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    JScrollPane scrollPane;
    double taxVal = 0;
    JButton addBtn;
    final Dimension innerPanelSize = new Dimension(1330, 35);

    private int checkRecordExist(long pId, double saleRate, double purRate, double mrp, String mfd, String exp) {
        int rows = tableModel.getRowCount();
        int row = -1;
        if (rows > 0) {
            for (int index = 0; index < rows; index++) {
                long rowId = Long.parseLong(tableModel.getValueAt(index, 1).toString());
                String rowRate = tableModel.getValueAt(index, 7).toString();
                String rowMrp = tableModel.getValueAt(index, 8).toString();
                String rowPurRate = tableModel.getValueAt(index, 6).toString();
                String mfdDate = tableModel.getValueAt(index, 16).toString();
                String expDate = tableModel.getValueAt(index, 17).toString();

                double r = Double.parseDouble(rowRate);
                double m = Double.parseDouble(rowMrp);
                double pr = Double.parseDouble(rowPurRate);

                if (pId == rowId && saleRate == r && mrp == m && purRate == pr && mfd.equals(mfdDate)
                        && exp.equals(expDate)) {
                    row = index;
                    break;
                }
            }
        }
        return row;
    }

    void insertIntoBillTable() {
        try {
            String b = pBarcodeField.getText().trim();
            long id = Long.parseLong(pIdField.getText().trim());
            String name = pNameField.getText().trim();
            String qty = pQtyField.getText().trim();
            String rate = pRateField.getText().trim();
            String mrp = pMRPField.getText().trim();
            String freeQty = pFreeQtyField.getText().trim();
            String purRate = purchaseRateField.getText().trim();
            String purDisc = purchaseDiscField.getText().trim();
            String expDate = dateFormat.format(expDateChooser.getDate());
            String mfdDate = dateFormat.format(mfdDateChooser.getDate());

            boolean bRes = isBarcodeValid(b);
            boolean idRes = isBarcodeValid("" + id);
            double rateVal = Double.parseDouble(rate);
            double qtyVal = Double.parseDouble(qty);

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

            int availableStock = getStock(id);
            double purRateVal = Double.parseDouble(purRate);
            double dAmt = (purRateVal * Double.parseDouble(purDisc)) / 100;
            int row = checkRecordExist(id, rateVal, purRateVal, mrpVal, mfdDate, expDate);
            if (row == -1) {
                tableModel.insertRow(0, new Object[] {
                        cnt++, id,
                        b, name, qtyVal, Double.parseDouble(freeQty), purRateVal,
                        rateVal,
                        mrpVal, availableStock,
                        purDisc,
                        dAmt,
                        purRateVal - dAmt,
                        (purRateVal - dAmt) * qtyVal,
                        taxVal, 0, mfdDate, expDate,
                });
            } else {
                tableModel.setValueAt(Double.parseDouble(tableModel.getValueAt(row, 4).toString()) + qtyVal,
                        row,
                        4);
                moveRow(tableModel, row, 0);
                cnt = updateSrNo(tableModel);
            }

            calculate();
            pBarcodeField.setText("");
            pIdField.setText("");
            pNameField.setText("");
            pQtyField.setText("1");
            pRateField.setText("");
            pMRPField.setText("");
            purchaseDiscField.setText("0");
            purchaseRateField.setText("0");
            pBarcodeField.requestFocusInWindow();

        } catch (NullPointerException exc) {
            System.out.println(exc.getMessage() + " at PurchaseEntry.insertIntoBillTable()");
            JOptionPane.showMessageDialog(this, "The values are not should be empty..." + exc.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException exc) {
            System.out.println(exc.getMessage() + " at PurchaseEntry.insertIntoBillTable()");
            JOptionPane.showMessageDialog(this, "Please, enter valid numeric values..." + exc.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            if (pBarcodeField.getText().isBlank())
                pBarcodeField.requestFocus();
            else
                pQtyField.requestFocus();
        } catch (Exception exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid details..!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    boolean isForShowProductDetails;

    void showProductDetails(Object source) {
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
                String rate = result.getString("sale_rate");
                String mrp = result.getString("product_mrp");
                String pName = result.getString("product_name").trim();
                pBarcodeField.setText(result.getString("barcode_no"));
                pIdField.setText(result.getString("p_id"));
                pNameField.setText(pName);
                pQtyField.setText("1");
                pRateField.setText(rate);
                pMRPField.setText(mrp);
                purchaseRateField.setText(rate);
                purchaseDiscField.setText(result.getString("discount"));

                pQtyField.requestFocusInWindow();

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

    private void calculate() {
        try {
            int rows = purchaseTable.getRowCount();
            double totalDiscount = 0, totalQty = 0, totalGST = 0, totalStock = 0, totalAmtAsPerPurRate = 0;
            for (int row = 0; row < rows; row++) {
                Double qty = Double.parseDouble(purchaseTable.getValueAt(row, 4).toString());
                // double saleRate = Double.parseDouble(purchaseTable.getValueAt(row,
                // 7).toString());
                double purRate = Double.parseDouble(purchaseTable.getValueAt(row, 6).toString());
                // double freeQty = Double.parseDouble(purchaseTable.getValueAt(row,
                // 5).toString());
                // double mrp = Double.parseDouble(purchaseTable.getValueAt(row, 8).toString());
                double discount = Double.parseDouble(purchaseTable.getValueAt(row, 10).toString());
                double stock = Double.parseDouble(purchaseTable.getValueAt(row, 9).toString());
                double tax = Double.parseDouble(purchaseTable.getValueAt(row, 14).toString());

                double perPieceDiscAmt = (purRate * discount) / 100;

                purchaseTable.setValueAt(String.format("%.2f", perPieceDiscAmt), row, 11);

                double amt = purRate - perPieceDiscAmt;

                purchaseTable.setValueAt(String.format("%.2f", amt), row, 12);

                double netAmt = amt * qty;

                purchaseTable.setValueAt(String.format("%.2f", netAmt), row, 13);

                double taxAmt = ((purRate * tax) / 100) * qty;

                purchaseTable.setValueAt(String.format("%.2f", taxAmt), row, 15);

                totalDiscount += (perPieceDiscAmt * qty);
                totalQty += qty;
                totalStock += stock;
                totalAmtAsPerPurRate += netAmt;
                totalGST += taxAmt;
            }

            tGSTField.setText(String.format("%.2f", totalGST));
            totalStockField.setText(String.format("%.2f", totalStock));
            totalQtyField.setText(String.format("%.2f", totalQty));
            pDiscField.setText(String.format("%.2f", totalDiscount));
            billAmtField.setText(String.format("%.2f", totalAmtAsPerPurRate));
            tCGSTField.setText(String.format("%.2f", (totalGST / 2)));
            tSGSTField.setText(String.format("%.2f", (totalGST / 2)));
            isAnyInvalidEntry = false;
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at PurchaseEntry.calculate() 277");
            JOptionPane.showMessageDialog(this, "Invalid values of entry.." + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            isAnyInvalidEntry = true;
            return;
        }

    }

    Boolean isAnyInvalidEntry = true;

    public PurchaseEntry(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Purchase Entry", currentMenu, frameTracker, frameKeyMap, dialog);
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
        pQtyField.addKeyListener(new CustomKeyListener(pFreeQtyField));
        pFreeQtyField.addKeyListener(new CustomKeyListener(purchaseRateField));
        purchaseRateField.addKeyListener(new CustomKeyListener(pRateField));
        pRateField.addKeyListener(new CustomKeyListener(pMRPField));
        pMRPField.addKeyListener(new CustomKeyListener(purchaseDiscField));
        purchaseDiscField.addKeyListener(new CustomKeyListener(mfdDateChooser.getDateEditor().getUiComponent()));
        mfdDateChooser.getDateEditor().getUiComponent()
                .addKeyListener(new CustomKeyListener(expDateChooser.getDateEditor().getUiComponent()));
        expDateChooser.getDateEditor().getUiComponent().addKeyListener(new CustomKeyListener(addBtn));
        dateChooser.getDateEditor().getUiComponent().addKeyListener(new CustomKeyListener(entryNoField));

        setVisible(true);
        entryNoField.setText(getLastId("purchases", "day_wise_entry_no"));
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
        entryNoField.setText("1");
        entryNoField.addFocusListener(this);
        entryNoField.addKeyListener(this);
        entryNoField.setBackground(lemonYellow);

        billInfoPanel.add(entryNoLabel);
        billInfoPanel.add(entryNoField);

        billNoLabel = new JLabel("Bill No.:");
        billNoLabel.setFont(labelFont);

        billNoField = new JTextField(5);
        billNoField.setFont(labelFont);
        billNoField.setText("1");
        billNoField.addFocusListener(this);
        billNoField.setBackground(lemonYellow);

        billInfoPanel.add(billNoLabel);
        billInfoPanel.add(billNoField);

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

        billFormPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        billFormPanel.setPreferredSize(new Dimension(1300, 90));
        billFormPanel.setBackground(Color.white);

        pBarcodeLabel = new JLabel("Barcode :");
        pBarcodeLabel.setFont(labelFont);

        pBarcodeField = new JTextField(7);
        pBarcodeField.addFocusListener(this);
        pBarcodeField.addActionListener(this);
        pBarcodeField.addKeyListener(this);
        pBarcodeField.addActionListener(this);
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

        pQtyField = new JTextField(5);
        pQtyField.setBackground(lemonYellow);
        pQtyField.addFocusListener(this);
        pQtyField.addKeyListener(this);
        pQtyField.setFont(labelFont);
        pQtyField.setText("1");

        billFormPanel.add(pQtyLabel);
        billFormPanel.add(pQtyField);

        pFreeQtyLabel = new JLabel("Free Qty.:");
        pFreeQtyLabel.setFont(labelFont);

        pFreeQtyField = new JTextField(5);
        pFreeQtyField.setBackground(lemonYellow);
        pFreeQtyField.addFocusListener(this);
        pFreeQtyField.addKeyListener(this);
        pFreeQtyField.setFont(labelFont);
        pFreeQtyField.setText("1");

        billFormPanel.add(pFreeQtyLabel);
        billFormPanel.add(pFreeQtyField);

        purchaseRateLabel = new JLabel("Pur.Rate:");
        purchaseRateLabel.setFont(labelFont);

        purchaseRateField = new JTextField(5);
        purchaseRateField.addKeyListener(this);
        purchaseRateField.addFocusListener(this);
        purchaseRateField.setFont(labelFont);
        purchaseRateField.setBackground(lemonYellow);

        billFormPanel.add(purchaseRateLabel);
        billFormPanel.add(purchaseRateField);

        pRateLabel = new JLabel("Sale Rate:");
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

        purchaseDiscLabel = new JLabel("Pur.Discount :");
        purchaseDiscLabel.setFont(labelFont);

        purchaseDiscField = new JTextField(5);
        purchaseDiscField.setBackground(lemonYellow);
        purchaseDiscField.setFont(labelFont);
        purchaseDiscField.addKeyListener(this);
        purchaseDiscField.addFocusListener(this);

        billFormPanel.add(purchaseDiscLabel);
        billFormPanel.add(purchaseDiscField);

        pMFDDateLabel = new JLabel("MFD.Date:");
        pMFDDateLabel.setFont(labelFont);

        mfdDateChooser = new JDateChooser(new Date());
        mfdDateChooser.setDateFormatString("dd-MM-yyyy");
        mfdDateChooser.setPreferredSize(labelSize);
        mfdDateChooser.setFont(labelFont);
        mfdDateChooser.setForeground(Color.darkGray);
        mfdDateChooser.addFocusListener(this);
        mfdDateChooser.setBackground(lemonYellow);
        mfdDateChooser.setMaxSelectableDate(new Date());

        billFormPanel.add(pMFDDateLabel);
        billFormPanel.add(mfdDateChooser);

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

        addBtn = new JButton("Add");
        addBtn.setBackground(skyBlue);
        addBtn.setForeground(Color.white);
        addBtn.setFont(labelFont);
        addBtn.addActionListener(this);

        billFormPanel.add(addBtn);

    }

    private void allocateToBottomPanel() {
        bottomPanel = new JPanel(flowLayoutLeft);
        bottomPanel.setBackground(Color.white);
        bottomPanel.setPreferredSize(new Dimension(1310, 40));

        pDiscLabel = new JLabel("Disc.Amt.:");
        pDiscLabel.setFont(labelFont);

        pDiscField = new JTextField(4);
        pDiscField.setFont(labelFont);
        pDiscField.setEnabled(false);
        pDiscField.setDisabledTextColor(Color.darkGray);
        pDiscField.setBackground(lemonYellow);
        pDiscField.setText("0.00");

        bottomPanel.add(pDiscLabel);
        bottomPanel.add(pDiscField);

        totalQtyLabel = new JLabel("T.Qty.:");
        totalQtyLabel.setFont(labelFont);

        totalQtyField = new JTextField(4);
        totalQtyField.setEnabled(false);
        totalQtyField.setDisabledTextColor(Color.darkGray);
        totalQtyField.setText("0");
        totalQtyField.setBackground(lemonYellow);
        totalQtyField.setFont(labelFont);

        bottomPanel.add(totalQtyLabel);
        bottomPanel.add(totalQtyField);

        totalStockLabel = new JLabel("T.Stock:");
        totalStockLabel.setFont(labelFont);

        totalStockField = new JTextField(4);
        totalStockField.setEnabled(false);
        totalStockField.setDisabledTextColor(Color.darkGray);
        totalStockField.setText("0");
        totalStockField.setBackground(lemonYellow);
        totalStockField.setFont(labelFont);

        bottomPanel.add(totalStockLabel);
        bottomPanel.add(totalStockField);

        tGSTLabel = new JLabel("T.GST:");
        tGSTLabel.setFont(labelFont);

        tGSTField = new JTextField(4);
        tGSTField.setEnabled(false);
        tGSTField.setDisabledTextColor(Color.darkGray);
        tGSTField.setText("0");
        tGSTField.setBackground(lemonYellow);
        tGSTField.setFont(labelFont);

        bottomPanel.add(tGSTLabel);
        bottomPanel.add(tGSTField);

        tCGSTLabel = new JLabel("T.CGST:");
        tCGSTLabel.setFont(labelFont);

        tCGSTField = new JTextField(4);
        tCGSTField.setEnabled(false);
        tCGSTField.setDisabledTextColor(Color.darkGray);
        tCGSTField.setText("0");
        tCGSTField.setBackground(lemonYellow);
        tCGSTField.setFont(labelFont);

        bottomPanel.add(tCGSTLabel);
        bottomPanel.add(tCGSTField);

        tSGSTLabel = new JLabel("T.SGST:");
        tSGSTLabel.setFont(labelFont);

        tSGSTField = new JTextField(4);
        tSGSTField.setEnabled(false);
        tSGSTField.setDisabledTextColor(Color.darkGray);
        tSGSTField.setText("0");
        tSGSTField.setBackground(lemonYellow);
        tSGSTField.setFont(labelFont);

        bottomPanel.add(tSGSTLabel);
        bottomPanel.add(tSGSTField);

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

    JLabel totalStockLabel, tCGSTLabel, tSGSTLabel, tGSTLabel;
    JTextField totalStockField, tCGSTField, tSGSTField, tGSTField;

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

        String columnNames[] = new String[] { "Sr.No.", "item code", "barcode", "name", "Qty.", "Free Qty.",
                "Pur.Rate", "Sale Rate",
                "MRP", "Closing Stock",
                "disc.%", "disc.amt.", "Amt.", "Net Amt.", "Tax", "Tax Amt.", "Mfd.Date", "Exp.Date" };

        for (String column : columnNames)
            tableModel.addColumn(column);

        sorter = new TableRowSorter<>(tableModel);
        purchaseTable = new JTable(tableModel);
        purchaseTable.setFont(labelFont);
        purchaseTable.setSelectionBackground(skyBlue);
        purchaseTable.setSelectionForeground(Color.white);
        purchaseTable.setRowSorter(sorter);
        purchaseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        purchaseTable.setRowHeight(30);
        purchaseTable.addFocusListener(this);
        purchaseTable.addKeyListener(this);
        purchaseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        purchaseTable.setCellSelectionEnabled(true);
        purchaseTable.getDefaultEditor(Object.class).addCellEditorListener(this);
        purchaseTable.putClientProperty("terminateEditOnFocusLost", true);
        purchaseTable.getTableHeader().setFont(labelFont);

        TableColumnModel columnModel = purchaseTable.getColumnModel();
        int[] values = new int[] { 70, 140, 170, 220, 70, 100, 100, 90, 120, 120, 120, 120, 120, 120, 70, 120, 120,
                120 };
        for (int i = 0; i < values.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(values[i]);
            column.setMinWidth(values[i]);
        }

        scrollPane = new JScrollPane(purchaseTable);
        scrollPane.setPreferredSize(new Dimension(1310, 300));
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
        if (source instanceof JTable && purchaseTable.getRowCount() > 0 && !isForDeleteRow) {
            purchaseTable.removeRowSelectionInterval(0, purchaseTable.getRowCount() - 1);
        } else if (!(source instanceof JTable)) {
            ((JComponent) e.getSource()).setBackground(lemonYellow);
        }
    }

    Object actionSource;
    long dealerId;
    boolean tempRecoveryFlag;

    private boolean recoverStock() {
        previousRecords.forEach(entry -> {
            try {
                if (entry.containsKey("product_original_id")) {
                    long productId = (long) entry.get("product_original_id");
                    java.sql.Date expDate = new java.sql.Date(((Date) entry.get("exp")).getTime());
                    double mrp = Double.parseDouble(entry.get("mrp").toString());
                    double saleRate = Double.parseDouble(entry.get("sale_rate").toString());
                    double quantity = Double.parseDouble(entry.get("quantity").toString());
                    double freeQty = Double.parseDouble(entry.get("freeQty").toString());

                    String query = "select * from inventories where product_id = ? and product_exp_date = ? and product_mrp = ? and sale_rate = ?";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setLong(1, productId);
                    pst.setDate(2, expDate);
                    pst.setDouble(3, mrp);
                    pst.setDouble(4, saleRate);

                    ResultSet result = pst.executeQuery();
                    long inventory_id = 0;
                    if (result.next()) {
                        double oldQuantity = result.getDouble("current_quantity");
                        inventory_id = result.getLong("id");
                        query = "update inventories set current_quantity = ? where id = ?";
                        pst = DBConnection.con.prepareStatement(query);
                        pst.setDouble(1, (oldQuantity - (quantity + freeQty)));
                        pst.setLong(2, inventory_id);

                        tempRecoveryFlag = pst.executeUpdate() > 0;
                    }
                }
            } catch (Exception e) {
                // System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                // e.printStackTrace();
                tempRecoveryFlag = false;
            }
        });
        return tempRecoveryFlag;
    }

    private int deleteFromDB() throws Exception {
        String query = "delete from purchases where day_wise_entry_no = ? and date(created_at) = ?";
        PreparedStatement pst = DBConnection.con.prepareStatement(query);
        pst.setInt(1, (int) previousRecords.getLast().get("entry_no"));
        pst.setDate(2, (java.sql.Date) previousRecords.getLast().get("entry_date"));
        return pst.executeUpdate();
    }

    private void saveIntoDB(Boolean isNewEntry) throws Exception {
        String query = "";
        int affectedRows = 0;
        PreparedStatement pst;
        if (!isNewEntry) {
            recoverStock();
            affectedRows = deleteFromDB();
            if (affectedRows > 0) {
                query = "insert into purchases(day_wise_entry_no,dealer_id,created_at)values(?,"
                        + (dealerId == 0 ? null : dealerId) + ",?)";
                pst = DBConnection.con.prepareStatement(query);
                pst.setInt(1, (int) previousRecords.getLast().get("entry_no"));
                pst.setTimestamp(2, (Timestamp) previousRecords.getLast().get("timestamp"));
                affectedRows = pst.executeUpdate();
            } else {
                affectedRows = 0;
            }

        } else {
            query = "insert into purchases(day_wise_entry_no,dealer_id)values(?,"
                    + (dealerId == 0 ? null : dealerId) + ")";
            pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(getLastId("purchases", "day_wise_entry_no")));
            affectedRows = pst.executeUpdate();
        }

        if (affectedRows > 0) {
            query = "select id from purchases order by id desc limit 1";
            ResultSet result = DBConnection.executeQuery(query);
            long purchaseId = result.next() ? result.getLong("id") : 0;
            int rows = purchaseTable.getRowCount();
            for (int row = 0; row < rows; row++) {

                double purRate = Double.parseDouble(purchaseTable.getValueAt(row, 6).toString());
                double saleRate = Double.parseDouble(purchaseTable.getValueAt(row, 7).toString());
                double mrp = Double.parseDouble(purchaseTable.getValueAt(row, 8).toString());
                java.sql.Date mfdDate = new java.sql.Date(
                        dateFormat.parse(purchaseTable.getValueAt(row, 16).toString()).getTime());
                java.sql.Date expDate = new java.sql.Date(
                        dateFormat.parse(purchaseTable.getValueAt(row, 17).toString()).getTime());
                String gst = purchaseTable.getValueAt(row, 14).toString();
                double quantity = Double.parseDouble(purchaseTable.getValueAt(row, 4).toString());
                double freeQty = Double.parseDouble(purchaseTable.getValueAt(row, 5).toString());
                long itemCode = Long.parseLong(purchaseTable.getValueAt(row, 1).toString());
                double purDisc = Double.parseDouble(purchaseTable.getValueAt(row, 10).toString());

                long productId = getProductId(itemCode);
                if (productId != 0) {

                    query = "select * from inventories where product_id = ? and product_exp_date = ? and product_mrp = ? and sale_rate = ?";
                    pst = DBConnection.con.prepareStatement(query);
                    pst.setLong(1, productId);
                    pst.setDate(2, expDate);
                    pst.setDouble(3, mrp);
                    pst.setDouble(4, saleRate);

                    result = pst.executeQuery();
                    boolean flagForInventory = false;
                    long inventory_id = 0;
                    if (result.next()) {
                        double oldQuantity = result.getDouble("current_quantity");
                        inventory_id = result.getLong("id");
                        query = "update inventories set current_quantity = ? where id = ?";
                        pst = DBConnection.con.prepareStatement(query);
                        pst.setDouble(1, (oldQuantity + quantity + freeQty));
                        pst.setLong(2, inventory_id);

                        flagForInventory = pst.executeUpdate() > 0;

                    } else {
                        query = "insert into inventories(product_id,current_quantity,sale_rate,product_mrp,product_mfd_date,product_exp_date)values(?,?,?,?,?,?)";
                        pst = DBConnection.con.prepareStatement(query);
                        pst.setLong(1, productId);
                        pst.setDouble(2, quantity + freeQty);
                        pst.setDouble(3, saleRate);
                        pst.setDouble(4, mrp);
                        pst.setDate(5, mfdDate);
                        pst.setDate(6, expDate);

                        flagForInventory = pst.executeUpdate() > 0;

                        if (flagForInventory) {
                            query = "select id from inventories order by id desc limit 1";
                            result = DBConnection.executeQuery(query);
                            inventory_id = result.next() ? result.getLong("id") : 0;
                        }

                    }
                    if (flagForInventory && inventory_id != 0) {
                        query = "insert into purchase_entries(purchase_id,product_id,purchase_rate,sale_rate,product_mrp,quantity,product_gst,product_mfd_date,product_exp_date,inventory_id,free_quantity,purchase_discount)values(?,?,?,?,?,?,?,?,?,?,?,?)";
                        pst = DBConnection.con.prepareStatement(query);
                        pst.setLong(1, purchaseId);
                        pst.setLong(2, productId);
                        pst.setDouble(3, purRate);
                        pst.setDouble(4, saleRate);
                        pst.setDouble(5, mrp);
                        pst.setDouble(6, quantity);
                        pst.setString(7, gst);
                        pst.setDate(8, mfdDate);
                        pst.setDate(9, expDate);
                        pst.setLong(10, inventory_id);
                        pst.setDouble(11, freeQty);
                        pst.setDouble(12, purDisc);

                        affectedRows = pst.executeUpdate();
                        if (affectedRows > 0)
                            reCreate();
                        else
                            JOptionPane.showMessageDialog(this,
                                    "Purchase bill cannot saved..try again!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(newBtn)) {
            reCreate();
        } else {
            if (source.equals(saveBtn)) {
                if (action.equals(SAVE_ACTION) || action.equals(UPDATE_ACTION)) {
                    try {
                        double totalAmt = Double.parseDouble(billAmtField.getText());
                        if (totalAmt > 0 && !isAnyInvalidEntry) {
                            saveIntoDB(action.equals(SAVE_ACTION));
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Bill cannot save with zero amount..!\nOR\nAny Invalid entry value..!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage());
                    }
                }
                if (action.equals(DELETE_ACTION)) {
                    try {
                        recoverStock();
                        if (deleteFromDB() > 0) {
                            reCreate();
                        } else {
                            JOptionPane.showMessageDialog(this, "Bill cannot removed..!");
                        }
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage());
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
            if (source.equals(addBtn)) {
                insertIntoBillTable();
                SwingUtilities.invokeLater(() -> {
                    pBarcodeField.requestFocus();
                });
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

    Integer cnt = 1;
    boolean isForDeleteRow;
    TableExporter view;
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

        String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
        if (source.equals(purchaseTable) && key.equals("D") && modifier.equals("Ctrl")) {
            isForDeleteRow = true;
            int option = JOptionPane.showConfirmDialog(this, "Are you sure to remove this entry?", "Confirmation",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                int row = purchaseTable.getSelectedRow();
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

        if ((source.equals(pBarcodeField) || source.equals(pIdField)) && key.equals("Enter")) {
            showProductDetails(source);
            if (source.equals(pBarcodeField) && !pBarcodeField.getText().isBlank()) {
                actionPerformed(new ActionEvent(addBtn, 1, ""));
            }
        }

        if (source.equals(entryNoField) && key.equals("Enter")) {
            try {
                java.sql.Date entryDate = new java.sql.Date(dateChooser.getDate().getTime());
                int entryNumber = Integer.parseInt(entryNoField.getText().trim());

                String query = "select *,purchases.created_at as timestamp,products.id as prod_id,products.p_id as products_p_id,purchase_entries.purchase_discount as purchase_entries_purchase_discount,purchase_entries.quantity as purchase_entries_quantity,products.barcode_no as products_barcode_no,products.product_name as products_product_name,purchase_entries.sale_rate as purchase_entries_sale_rate,purchase_entries.product_mrp as purchase_entries_product_mrp from purchases,purchase_entries,inventories,products where products.id = purchase_entries.product_id and purchases.id = purchase_entries.purchase_id and inventories.id = purchase_entries.inventory_id and purchases.day_wise_entry_no = ? and date(purchases.created_at) = ? order by purchase_entries.id desc";
                PreparedStatement pst = DBConnection.con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                pst.setInt(1, entryNumber);
                pst.setDate(2, entryDate);

                previousRecords = new ArrayList<HashMap<String, Object>>();

                ResultSet result = pst.executeQuery();
                if (result.next()) {
                    tableModel.setRowCount(0);
                    cnt = 1;
                    dealerId = result.getLong("dealer_id");
                    Timestamp timestamp = result.getTimestamp("timestamp");
                    result.beforeFirst();
                    while (result.next()) {

                        long productId = result.getLong("prod_id");
                        String pId = result.getString("products_p_id");
                        String pBarcode = result.getString("products_barcode_no");
                        String pName = result.getString("products_product_name");
                        String qty = result.getString("purchase_entries_quantity");
                        String freeQty = result.getString("free_quantity");
                        String purRate = result.getString("purchase_rate");
                        String saleRate = result.getString("purchase_entries_sale_rate");
                        String mrp = result.getString("purchase_entries_product_mrp");
                        String disc = result.getString("purchase_entries_purchase_discount");
                        Date mfd = new Date(result.getDate("product_mfd_date").getTime());
                        Date exp = new Date(result.getDate("product_exp_date").getTime());

                        pBarcodeField.setText(pBarcode);
                        pIdField.setText(pId);
                        pNameField.setText(pName);
                        pQtyField.setText(qty);
                        pFreeQtyField.setText(freeQty);
                        purchaseRateField.setText(purRate);
                        pRateField.setText(saleRate);
                        purchaseDiscField.setText(disc);
                        pMRPField.setText(mrp);
                        mfdDateChooser.setDate(mfd);
                        expDateChooser.setDate(exp);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("product_original_id", productId);
                        map.put("p_id", pId);
                        map.put("barcode_no", pBarcode);
                        map.put("p_name", pName);
                        map.put("quantity", qty);
                        map.put("freeQty", freeQty);
                        map.put("purchase_rate", purRate);
                        map.put("sale_rate", saleRate);
                        map.put("discount", disc);
                        map.put("mrp", mrp);
                        map.put("mfd", mfd);
                        map.put("exp", exp);

                        previousRecords.add(map);

                        insertIntoBillTable();
                    }

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("timestamp", timestamp);
                    map.put("entry_no", entryNumber);
                    map.put("entry_date", entryDate);
                    previousRecords.add(map);

                    query = "select * from dealers where id = ?";
                    pst = DBConnection.con.prepareStatement(query);
                    pst.setLong(1, dealerId);
                    result = pst.executeQuery();
                    if (result.next()) {
                        String dealerName = result.getString("name");
                        String gstNo = result.getString("gst_no");
                        String contact = result.getString("contact_no");

                        dealerNameField.setText(dealerName);
                        dealerGstNoField.setText(gstNo);
                        mobileField.setText(contact);
                    } else {
                        dealerNameField.setText("");
                        dealerGstNoField.setText("");
                        mobileField.setText("");
                    }

                    action = actionSource.equals(deleteBtn) ? DELETE_ACTION : UPDATE_ACTION;
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid entry no..try again!");
                    action = SAVE_ACTION;
                }

            } catch (Exception exc) {
                System.out.println(exc.getMessage() + " at PurchaseEntry.keyPressed 1026");
                JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
