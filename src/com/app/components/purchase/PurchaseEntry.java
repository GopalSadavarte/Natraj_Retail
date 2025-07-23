package com.app.components.purchase;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.app.components.abstracts.AbstractButton;
import com.app.components.purchase.support.DealerView;
import com.app.components.purchase.support.ProductView;
import com.app.partials.event.*;
import com.toedter.calendar.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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
    double discountVal = 0, taxVal = 0;
    JButton addBtn;
    final Dimension innerPanelSize = new Dimension(1330, 35);

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
        purchaseDiscField.addKeyListener(new CustomKeyListener(mfdDateChooser));
        mfdDateChooser.getDateEditor().getUiComponent().addKeyListener(new CustomKeyListener(expDateChooser));
        expDateChooser.getDateEditor().getUiComponent().addKeyListener(new CustomKeyListener(addBtn));

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
        bottomPanel.setPreferredSize(new Dimension(1310, 75));

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

        JTableHeader header = purchaseTable.getTableHeader();
        header.setFont(labelFont);

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
        //
    }

    public void editingStopped(ChangeEvent e) {
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
        if (source instanceof JTable && purchaseTable.getRowCount() > 0 && !isForDeleteRow) {
            purchaseTable.removeRowSelectionInterval(0, purchaseTable.getRowCount() - 1);
        } else if (!(source instanceof JTable)) {
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
            if (source.equals(editBtn)) {

            }
            if (source.equals(deleteBtn)) {
            }
            if (source.equals(cancelBtn)) {
            }
            if (source.equals(exitBtn)) {
            }
        }
    }

    int cnt = 1;
    boolean isForDeleteRow;

    public void keyPressed(KeyEvent e) {
        Object source = e.getSource();
        String key = KeyEvent.getKeyText(e.getKeyCode());
        if ((source.equals(pBarcodeField)|| source.equals(pIdField)) && key.equals("F1")) {
            ProductView view = new ProductView(this);
            view.getScrollPane().setPreferredSize(new Dimension(800, 350));
            createViewer(view);
        }

        if(source.equals(dealerNameField) && key.equals("F1")){
            DealerView view = new DealerView(this);
            view.getScrollPane().setPreferredSize(new Dimension(800, 350));
            createViewer(view);
        }

        if(source.equals(searchField)){
            super.keyPressed(e);
        }

        if(source.equals(selectBtn)){

        }
    }
}
