package com.app.components.purchase;

import javax.swing.*;
import javax.swing.filechooser.*;
import com.app.components.abstracts.AbstractButton;
import com.app.components.purchase.support.*;
import com.app.config.*;
import com.app.partials.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public final class Product extends AbstractButton {

    JPanel mainPanel, headingPanel, fieldHolder, subPanel;
    JLabel headingLabel, itemCodeLabel, itemBarcodeLabel, itemNameLabel, itemGroupLabel, itemSubGroupLabel,
            itemRateLabel, defaultStockLabel, itemImageLabel,
            itemMRPLabel, itemWholeRateLabel, itemWholeQtyLabel, itemDiscountLabel, itemDiscountOnLabel;
    JTextField itemCodeField, itemBarcodeField, itemNameField, itemRateField, itemMRPField, itemWholeRateField,
            itemWholeQtyField, itemDiscountField, defaultStockField;
    JButton itemImagePickerBt;
    JComboBox<String> itemGroups, itemSubGroups, discountOns;
    final Integer DELETE_ACTION = -1, UPDATE_ACTION = 0, SAVE_ACTION = 1;
    Integer action = SAVE_ACTION, productIdForUpdate = 0;
    boolean isUpdateClicked = false;
    JFileChooser fileChooser;
    ProductView view;
    File file;
    InputStream fis, previousUploadedImage;
    HashMap<String, Integer> groupNameIdMap = new HashMap<>(), subGroupNameIdMap = new HashMap<>();

    public Product(JMenuItem currentMenu, HashMap<String, JInternalFrame> frameTracker,
            HashMap<JInternalFrame, String> frameKeyMap, final JDialog dialog) {

        super("Product Manager", currentMenu, frameTracker, frameKeyMap, dialog);
        setBackground(Color.white);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLayout(flowLayoutCenter);

        mainPanel = new JPanel(flowLayoutCenter);
        mainPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        mainPanel.setPreferredSize(new Dimension(900, 600));
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(flowLayoutCenter);
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(700, 50));

        headingLabel = new JLabel("  Item Master");
        headingLabel.setFont(headingFont);
        headingLabel.setForeground(Color.darkGray);
        headingPanel.add(headingLabel);

        mainPanel.add(headingPanel);

        fieldHolder = new JPanel(new FlowLayout());
        fieldHolder.setPreferredSize(new Dimension(850, 480));
        fieldHolder.setBackground(Color.white);
        fieldHolder.setBorder(BorderFactory.createLineBorder(borderColor));

        subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setPreferredSize(new Dimension(800, 60));
        subPanel.setBackground(Color.white);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        itemCodeLabel = new JLabel("Item Code :");
        itemCodeLabel.setFont(labelFont);
        itemCodeLabel.setPreferredSize(labelSize);
        itemCodeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemCodeField = new JTextField(7);
        itemCodeField.setEnabled(false);
        itemCodeField.setFont(labelFont);
        itemCodeField.setDisabledTextColor(Color.darkGray);
        itemCodeField.setBackground(lemonYellow);
        itemCodeField.setFocusable(true);
        itemCodeField.addKeyListener(this);
        itemCodeField.addFocusListener(this);

        subPanel.add(itemCodeLabel);
        subPanel.add(itemCodeField);

        itemBarcodeLabel = new JLabel("Item Barcode :");
        itemBarcodeLabel.setFont(labelFont);
        itemBarcodeLabel.setPreferredSize(labelSize);
        itemBarcodeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemBarcodeField = new JTextField(10);
        itemBarcodeField.setBackground(lemonYellow);
        itemBarcodeField.addFocusListener(this);
        itemBarcodeField.setFont(labelFont);
        itemBarcodeField.addActionListener(this);

        subPanel.add(itemBarcodeLabel);
        subPanel.add(itemBarcodeField);

        fieldHolder.add(subPanel);

        subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setPreferredSize(new Dimension(800, 60));
        subPanel.setBackground(Color.white);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        itemNameLabel = new JLabel("Item Name :");
        itemNameLabel.setFont(labelFont);
        itemNameLabel.setPreferredSize(labelSize);
        itemNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemNameField = new JTextField(30);
        itemNameField.setBackground(lemonYellow);
        itemNameField.addFocusListener(this);
        itemNameField.setFont(labelFont);

        subPanel.add(itemNameLabel);
        subPanel.add(itemNameField);

        fieldHolder.add(subPanel);

        subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setPreferredSize(new Dimension(800, 60));
        subPanel.setBackground(Color.white);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        itemGroupLabel = new JLabel("Item Group :");
        itemGroupLabel.setFont(labelFont);
        itemGroupLabel.setPreferredSize(labelSize);
        itemGroupLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemGroups = new JComboBox<String>();
        itemGroups.setBackground(lemonYellow);
        itemGroups.addFocusListener(this);
        itemGroups.setFont(labelFont);

        subPanel.add(itemGroupLabel);
        subPanel.add(itemGroups);

        itemSubGroupLabel = new JLabel("Item Sub Group :");
        itemSubGroupLabel.setFont(labelFont);
        itemSubGroupLabel.setPreferredSize(labelSize);
        itemSubGroupLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemSubGroups = new JComboBox<String>();
        itemSubGroups.setBackground(lemonYellow);
        itemSubGroups.addFocusListener(this);
        itemSubGroups.setFont(labelFont);

        subPanel.add(itemSubGroupLabel);
        subPanel.add(itemSubGroups);

        fieldHolder.add(subPanel);

        subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setPreferredSize(new Dimension(800, 60));
        subPanel.setBackground(Color.white);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        itemRateLabel = new JLabel("Sale Rate :");
        itemRateLabel.setFont(labelFont);
        itemRateLabel.setPreferredSize(labelSize);
        itemRateLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemRateField = new JTextField(10);
        itemRateField.setBackground(lemonYellow);
        itemRateField.addFocusListener(this);
        itemRateField.setFont(labelFont);

        subPanel.add(itemRateLabel);
        subPanel.add(itemRateField);

        itemMRPLabel = new JLabel("MRP :");
        itemMRPLabel.setFont(labelFont);
        itemMRPLabel.setPreferredSize(labelSize);
        itemMRPLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemMRPField = new JTextField(10);
        itemMRPField.setBackground(lemonYellow);
        itemMRPField.addFocusListener(this);
        itemMRPField.setFont(labelFont);

        subPanel.add(itemMRPLabel);
        subPanel.add(itemMRPField);

        fieldHolder.add(subPanel);

        subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setPreferredSize(new Dimension(800, 60));
        subPanel.setBackground(Color.white);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        itemWholeRateLabel = new JLabel("Wholesale Rate :");
        itemWholeRateLabel.setFont(labelFont);
        itemWholeRateLabel.setPreferredSize(labelSize);
        itemWholeRateLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemWholeRateField = new JTextField(10);
        itemWholeRateField.setBackground(lemonYellow);
        itemWholeRateField.addFocusListener(this);
        itemWholeRateField.setFont(labelFont);

        subPanel.add(itemWholeRateLabel);
        subPanel.add(itemWholeRateField);

        itemWholeQtyLabel = new JLabel("Wholesale Qty :");
        itemWholeQtyLabel.setFont(labelFont);
        itemWholeQtyLabel.setPreferredSize(labelSize);
        itemWholeQtyLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemWholeQtyField = new JTextField(10);
        itemWholeQtyField.setBackground(lemonYellow);
        itemWholeQtyField.addFocusListener(this);
        itemWholeQtyField.setFont(labelFont);

        subPanel.add(itemWholeQtyLabel);
        subPanel.add(itemWholeQtyField);

        fieldHolder.add(subPanel);

        subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setPreferredSize(new Dimension(800, 60));
        subPanel.setBackground(Color.white);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        itemDiscountLabel = new JLabel("Discount (%):");
        itemDiscountLabel.setFont(labelFont);
        itemDiscountLabel.setPreferredSize(labelSize);
        itemDiscountLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemDiscountField = new JTextField(10);
        itemDiscountField.setBackground(lemonYellow);
        itemDiscountField.addFocusListener(this);
        itemDiscountField.setFont(labelFont);

        subPanel.add(itemDiscountLabel);
        subPanel.add(itemDiscountField);

        itemDiscountOnLabel = new JLabel("Discount On :");
        itemDiscountOnLabel.setFont(labelFont);
        itemDiscountOnLabel.setPreferredSize(labelSize);
        itemDiscountOnLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        discountOns = new JComboBox<String>();
        discountOns.setBackground(lemonYellow);
        discountOns.addFocusListener(this);
        discountOns.setFont(labelFont);

        discountOns.addItem("Sale rate");
        discountOns.addItem("MRP");

        subPanel.add(itemDiscountOnLabel);
        subPanel.add(discountOns);

        fieldHolder.add(subPanel);

        subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setPreferredSize(new Dimension(800, 60));
        subPanel.setBackground(Color.white);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        defaultStockLabel = new JLabel("Fixed Stock :");
        defaultStockLabel.setFont(labelFont);
        defaultStockLabel.setPreferredSize(labelSize);
        defaultStockLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        defaultStockField = new JTextField(10);
        defaultStockField.setBackground(lemonYellow);
        defaultStockField.addFocusListener(this);
        defaultStockField.setFont(labelFont);
        defaultStockField.setDisabledTextColor(Color.darkGray);

        subPanel.add(defaultStockLabel);
        subPanel.add(defaultStockField);

        itemDiscountOnLabel = new JLabel("Item Picture :");
        itemDiscountOnLabel.setFont(labelFont);
        itemDiscountOnLabel.setPreferredSize(labelSize);
        itemDiscountOnLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        itemImagePickerBt = new JButton("Pick an image");
        itemImagePickerBt.addActionListener(this);
        itemImagePickerBt.setFont(labelFont);

        subPanel.add(itemDiscountOnLabel);
        subPanel.add(itemImagePickerBt);

        fieldHolder.add(subPanel);

        mainPanel.add(fieldHolder);

        mainPanel.add(buttonPanel);
        add(mainPanel);

        itemCodeField.addKeyListener(new CustomKeyListener(itemBarcodeField));
        itemBarcodeField.addKeyListener(new CustomKeyListener(itemNameField));
        itemNameField.addKeyListener(new CustomKeyListener(itemGroups));
        itemGroups.addKeyListener(new CustomKeyListener(itemSubGroups));
        itemSubGroups.addKeyListener(new CustomKeyListener(itemRateField));
        itemRateField.addKeyListener(new CustomKeyListener(itemMRPField));
        itemMRPField.addKeyListener(new CustomKeyListener(itemWholeRateField));
        itemWholeRateField.addKeyListener(new CustomKeyListener(itemWholeQtyField));
        itemWholeQtyField.addKeyListener(new CustomKeyListener(itemDiscountField));
        itemDiscountField.addKeyListener(new CustomKeyListener(discountOns));
        discountOns.addKeyListener(new CustomKeyListener(defaultStockField));
        defaultStockField.addKeyListener(new CustomKeyListener(itemImagePickerBt));

        setGroupsAndSubGroups();

        setVisible(true);
    }

    private void setGroupsAndSubGroups() {
        try {
            String query = "select * from groups";
            String name;
            int id;
            ResultSet result = DBConnection.executeQuery(query);
            while (result.next()) {
                name = result.getString("g_name");
                id = result.getInt("id");
                itemGroups.addItem(name);
                groupNameIdMap.put(name, id);
            }
            query = "select * from sub_groups";
            result = DBConnection.executeQuery(query);
            while (result.next()) {
                name = result.getString("sub_group_name");
                id = result.getInt("id");
                itemSubGroups.addItem(name);
                subGroupNameIdMap.put(name, id);
            }
            query = "select p_id from products order by p_id desc limit 1";
            result = DBConnection.executeQuery(query);
            if (result.next())
                itemCodeField.setText("" + (result.getInt("p_id") + 1));
            else
                itemCodeField.setText("1");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage() + " at Product.java ");
        }
    }

    public void focusGained(FocusEvent e) {
        ((JComponent) e.getSource()).setBackground(lightYellow);
    }

    public void focusLost(FocusEvent e) {
        ((JComponent) e.getSource()).setBackground(lemonYellow);
    }

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        Object source = e.getSource();
        if (source.equals(itemCodeField)) {
            if (key.equals("F1")) {
                view = new ProductView();
                view.getScrollPane().setPreferredSize(new Dimension(800, 350));
                createViewer(view);
            }

            if (key.equals("Enter")) {
                int id;
                try {
                    id = Integer.parseInt(itemCodeField.getText().trim());
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "please! put a valid id..!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                showProductDetails(id);
            }
        }
    }

    private void showProductDetails(int id) {
        try {
            String query = "select * from products,groups,sub_groups where groups.id = products.group_id and sub_groups.id = products.sub_group_id and p_id = ? limit 1";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                String itemBarcode = result.getString("barcode_no");
                itemBarcodeField.setText(itemBarcode);
                itemNameField.setText(result.getString("product_name"));
                itemGroups.setSelectedItem(result.getString("g_name"));
                itemSubGroups.setSelectedItem(result.getString("sub_group_name"));
                itemRateField.setText(result.getString("sale_rate"));
                itemMRPField.setText(result.getString("product_mrp"));
                itemWholeRateField.setText(result.getString("wholesale_rate"));
                itemWholeQtyField.setText(result.getString("wholesale_quantity"));
                itemDiscountField.setText(result.getString("discount"));
                discountOns.setSelectedItem(result.getString("discount_on"));

                previousUploadedImage = result.getBinaryStream("product_image");
                productIdForUpdate = result.getInt("id");

                query = "select sum(current_quantity) as qty from inventories where product_id = ? group by product_id";
                pst = DBConnection.con.prepareStatement(query);
                pst.setInt(1, id);
                result = pst.executeQuery();
                defaultStockField.setText(result.next() ? result.getString("qty") : "0");

                query = "select * from inventories where product_id = ? and product_exp_date is not null and product_mfd_date is not null limit 1";
                pst = DBConnection.con.prepareStatement(query);
                pst.setInt(1, id);
                result = pst.executeQuery();
                defaultStockField.setEnabled(!result.next());
                action = isUpdateClicked ? UPDATE_ACTION : DELETE_ACTION;
            } else {
                JOptionPane.showMessageDialog(null, "Product not found,please check item code!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception exc) {
            System.out.println(exc.getMessage() + " at Product.java 397");
            JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        String key = KeyEvent.getKeyText(e.getKeyCode());
        if (source.equals(searchField)) {
            if (key.equals("Enter")) {
                selectBtn.doClick();
            } else {
                String value = searchField.getText().toUpperCase().trim();
                JTable table = view.getTable();
                int rows = table.getRowCount();
                for (int i = 0; i < rows; i++) {
                    String rowVal = table.getValueAt(i, 3).toString();
                    table.setRowHeight(i, rowVal.contains(value) ? table.getRowHeight() : 1);
                    if (rowVal.contains(value))
                        table.setRowSelectionInterval(i, i);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(cancelBtn)) {
            editBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
        }

        if (source.equals(newBtn)) {
            reCreate();
        }

        if (source.equals(deleteBtn) || source.equals(editBtn)) {
            itemCodeField.setEnabled(true);
            itemCodeField.requestFocus();
            itemCodeField.selectAll();
            isUpdateClicked = source.equals(editBtn);
        }

        if (source.equals(exitBtn)) {
            frameTracker.remove(frameKeyMap.get(this));
            dispose();
        }

        if (source.equals(selectBtn)) {
            JTable table = view.getTable();
            int row = table.getSelectedRow();
            int id = Integer.parseInt(table.getValueAt(row, 1).toString());
            showProductDetails(id);
            dataViewer.setVisible(false);
        }

        if (source.equals(saveBtn)) {
            if (action.equals(SAVE_ACTION) || action.equals(UPDATE_ACTION)) {
                try {
                    int groupId = groupNameIdMap.get(itemGroups.getSelectedItem());
                    int subGroupId = subGroupNameIdMap.get(itemSubGroups.getSelectedItem());
                    String barcode = itemBarcodeField.getText().trim();
                    double saleRate = Double.parseDouble(itemRateField.getText().trim());
                    double mrp = Double.parseDouble(itemMRPField.getText().trim());
                    double discount = Double.parseDouble(itemDiscountField.getText().trim());
                    String discountOn = discountOns.getSelectedItem().toString();
                    double wholeRate = Double.parseDouble(itemWholeRateField.getText().trim());
                    double wholeQty = Double.parseDouble(itemWholeQtyField.getText().trim());
                    String productName = itemNameField.getText().trim().toUpperCase();
                    int stock;
                    try {
                        stock = Integer.parseInt(defaultStockField.getText().trim());
                    } catch (Exception exc) {
                        stock = 0;
                    }
                    /**
                     * --------- way 1 ----------
                     * if (action.equals(SAVE_ACTION)) {
                     * fis = file != null ? new FileInputStream(file)
                     * : Product.class.getResourceAsStream("defaultProductImage.png");
                     * } else {
                     * fis = file != null ? new FileInputStream(file) : previousUploadedImage;
                     * }
                     */

                    /**
                     * ---------way 2 ------------
                     * fis = action.equals(SAVE_ACTION) ? (file != null ? new FileInputStream(file)
                     * : Product.class.getResourceAsStream("defaultProductImage.png"))
                     * : (file != null ? new FileInputStream(file) : previousUploadedImage);
                     */

                    /** ----------way 3 ----------- */
                    fis = file != null ? new FileInputStream(file)
                            : (action.equals(SAVE_ACTION) ? Product.class.getResourceAsStream("defaultProductImage.png")
                                    : previousUploadedImage);

                    if (action.equals(SAVE_ACTION)) {
                        String query = "select p_id from products order by p_id desc limit 1";
                        ResultSet resultSet = DBConnection.executeQuery(query);
                        int itemCode = 1;
                        if (resultSet.next())
                            itemCode = resultSet.getInt("p_id") + 1;

                        short flag = 0;
                        if (barcode.isBlank()) {
                            barcode = "00" + itemCode;
                            flag = 1;
                        }

                        if (flag == 0 && !barcode.matches(INTEGER_VAL_PATTERN)) {
                            JOptionPane.showMessageDialog(null, "put a valid barcode!!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        query = "insert into products (p_id,barcode_no,product_name,group_id,sub_group_id,sale_rate,product_mrp,discount,discount_on,wholesale_rate,wholesale_quantity,product_image)values(?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setInt(1, itemCode);
                        pst.setString(2, barcode);
                        pst.setString(3, productName);
                        pst.setInt(4, groupId);
                        pst.setInt(5, subGroupId);
                        pst.setDouble(6, saleRate);
                        pst.setDouble(7, mrp);
                        pst.setDouble(8, discount);
                        pst.setString(9, discountOn);
                        pst.setDouble(10, wholeRate);
                        pst.setDouble(11, wholeQty);
                        pst.setBinaryStream(12, fis);

                        int affectedRows = pst.executeUpdate();
                        if (affectedRows > 0) {
                            if (stock != 0) {
                                query = "insert into inventories(product_id,current_quantity,sale_rate,product_mrp)values(?,?,?,?)";
                                pst = DBConnection.con.prepareStatement(query);
                                ResultSet result = DBConnection
                                        .executeQuery("select id from products order by id desc limit 1");
                                if (result.next()) {
                                    int id = result.getInt("id");
                                    pst.setInt(1, id);
                                    pst.setInt(2, stock);
                                    pst.setDouble(3, saleRate);
                                    pst.setDouble(4, mrp);

                                    affectedRows = pst.executeUpdate();
                                }
                            }
                            reCreate();
                        } else {
                            JOptionPane.showMessageDialog(null, "failed to add! check details");
                        }
                    } else {
                        boolean res = JOptionPane.showConfirmDialog(null, "Are you sure to update this information?",
                                "Confirmation", JOptionPane.OK_CANCEL_OPTION) == 0;
                        if (res) {
                            String query = "update products set product_name = ?,group_id = ?,sub_group_id = ?,sale_rate = ?,product_mrp = ?,discount = ?,discount_on = ?,wholesale_rate = ?,wholesale_quantity = ?,product_image = ? where id = ?";
                            PreparedStatement pst = DBConnection.con.prepareStatement(query);
                            pst.setString(1, productName);
                            pst.setInt(2, groupId);
                            pst.setInt(3, subGroupId);
                            pst.setDouble(4, saleRate);
                            pst.setDouble(5, mrp);
                            pst.setDouble(6, discount);
                            pst.setString(7, discountOn);
                            pst.setDouble(8, wholeRate);
                            pst.setDouble(9, wholeQty);
                            pst.setBinaryStream(10, fis);
                            pst.setInt(11, productIdForUpdate);

                            int affectedRows = pst.executeUpdate();
                            if (affectedRows > 0) {
                                if (defaultStockField.isEnabled()) {
                                    query = "select * from inventories where product_id = ? limit 1";
                                    pst = DBConnection.con.prepareStatement(query);
                                    pst.setInt(1, productIdForUpdate);
                                    ResultSet result = pst.executeQuery();
                                    if (result.next()) {
                                        query = "update inventories set current_quantity = ?,sale_rate = ?,product_mrp = ? where product_id = ?";
                                        pst = DBConnection.con.prepareStatement(query);
                                        pst.setInt(1, stock);
                                        pst.setDouble(2, saleRate);
                                        pst.setDouble(3, mrp);
                                        pst.setInt(4, productIdForUpdate);
                                        affectedRows = pst.executeUpdate();
                                    } else {
                                        query = "insert into inventories(product_id,current_quantity,sale_rate,product_mrp)values(?,?,?,?)";
                                        pst = DBConnection.con.prepareStatement(query);
                                        result = DBConnection
                                                .executeQuery("select id from products order by id desc limit 1");
                                        if (result.next()) {
                                            int id = result.getInt("id");
                                            pst.setInt(1, id);
                                            pst.setInt(2, stock);
                                            pst.setDouble(3, saleRate);
                                            pst.setDouble(4, mrp);
                                            affectedRows = pst.executeUpdate();
                                        }
                                    }
                                }
                                JOptionPane.showMessageDialog(null, "Record Updated!!");
                                reCreate();
                            } else {
                                JOptionPane.showMessageDialog(null, "Update Failed!!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            reCreate();
                        }
                    }
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (action.equals(DELETE_ACTION)) {
                try {
                    boolean res = JOptionPane.showConfirmDialog(null, "Are you sure to remove this record?") == 0;
                    if (res) {
                        String query = "delete from products where id = ?";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setInt(1, productIdForUpdate);
                        int affectedRows = pst.executeUpdate();
                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(null, "Record removed successfully!!");
                            reCreate();
                        } else {
                            JOptionPane.showMessageDialog(null, "Record cannot removed,try again!!");
                        }
                    }
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        if (source.equals(itemImagePickerBt)) {
            fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(
                    new FileNameExtensionFilter("jpg,jpeg,png,gif", "jpg", "jpeg", "gif", "png"));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            } else {
                file = null;
            }
        }
    }
}
