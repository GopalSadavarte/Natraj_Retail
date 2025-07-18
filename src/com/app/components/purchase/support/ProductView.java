package com.app.components.purchase.support;

import javax.swing.*;
import javax.swing.table.*;
import com.app.config.*;
import com.app.partials.interfaces.AppConstants;
import com.app.partials.interfaces.Validation;

import java.awt.*;
import java.sql.*;

public class ProductView extends JPanel implements AppConstants, Validation {

  final JTable table;
  final DefaultTableModel tableModel;
  TableRowSorter<TableModel> sorter;
  final JScrollPane scrollPane;

  public ProductView() {
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
    tableModel.addColumn("Discount");

    setTableData();

    table = new JTable(tableModel);
    sorter = new TableRowSorter<TableModel>(tableModel);
    table.setRowSorter(sorter);
    table.setSelectionBackground(skyBlue);
    table.setSelectionForeground(Color.white);
    table.setFont(labelFont);
    table.setRowHeight(30);

    JTableHeader header = table.getTableHeader();
    header.setFont(labelFont);

    scrollPane = new JScrollPane(table);
    scrollPane.getViewport().setBackground(Color.white);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    add(scrollPane);

    setVisible(true);
  }

  private void setTableData() {
    try {
      String query = "select * from products";
      ResultSet result = DBConnection.executeQuery(query);
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
