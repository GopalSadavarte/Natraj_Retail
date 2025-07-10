package com.app.components.purchase.support;

import javax.swing.*;
import javax.swing.table.*;
import com.app.config.*;
import com.app.partials.interfaces.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProductView extends JPanel implements AppConstants, Validation {

  final JTable table;
  final DefaultTableModel tableModel;
  TableRowSorter<TableModel> sorter;
  final JScrollPane scrollPane;

  public ProductView(KeyListener listener) {

    setLayout(new FlowLayout());
    setBackground(Color.white);

    tableModel = new DefaultTableModel() {
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };

    tableModel.addColumn("Sr.No.");
    tableModel.addColumn("ID");
    tableModel.addColumn("Barcode");
    tableModel.addColumn("Name");
    tableModel.addColumn("Sale Rate");
    tableModel.addColumn("MRP");
    tableModel.addColumn("Disc.%");

    setTableData(null);

    table = new JTable(tableModel);
    sorter = new TableRowSorter<TableModel>(tableModel);
    table.setRowSorter(sorter);
    table.setSelectionBackground(skyBlue);
    table.setSelectionForeground(Color.white);
    table.setFont(labelFont);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.addKeyListener(listener);
    table.setRowHeight(30);
    table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
        "none");
    if (table.getRowCount() > 0)
      table.setRowSelectionInterval(0, 0);

    TableColumnModel columnModel = table.getColumnModel();
    int[] values = new int[] { 70, 70, 140, 200, 90, 90, 70 };
    for (int i = 0; i < values.length; i++) {
      TableColumn column = columnModel.getColumn(i);
      column.setPreferredWidth(values[i]);
      column.setMinWidth(values[i]);
    }

    JTableHeader header = table.getTableHeader();
    header.setFont(labelFont);

    scrollPane = new JScrollPane(table);
    scrollPane.getViewport().setBackground(Color.white);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    add(scrollPane);

    setVisible(true);
  }

  public void setTableData(String searchValue) {
    try {
      String query = "";
      ResultSet result;
      if (searchValue == null || searchValue.isBlank()) {
        query = "select * from products";
        result = DBConnection.executeQuery(query);
      } else {
        query = "select * from products where product_name like ?";
        PreparedStatement pst = DBConnection.con.prepareStatement(query);
        pst.setString(1, '%' + searchValue.trim() + '%');
        result = pst.executeQuery();
      }

      int srNo = 1;
      while (result.next()) {
        tableModel.addRow(new Object[] {
            srNo++,
            result.getInt("p_id"),
            result.getString("barcode_no"),
            result.getString("product_name"),
            result.getString("sale_rate"),
            result.getString("product_mrp"),
            result.getString("discount"),
        });
      }
    } catch (Exception e) {
      System.out.println(e.getMessage() + " at GroupView.java 60");
    }
  }

  public JScrollPane getScrollPane() {
    return scrollPane;
  }

  public JTable getTable() {
    return table;
  }

  public DefaultTableModel getTableModel() {
    return tableModel;
  }
}
