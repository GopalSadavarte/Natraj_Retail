package com.app.components.purchase.support;

import javax.swing.*;
import javax.swing.table.*;
import com.app.config.*;
import com.app.partials.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GroupView extends JPanel implements AppConstants, Validation {

  final JTable table;
  final DefaultTableModel tableModel;
  TableRowSorter<TableModel> sorter;
  final JScrollPane scrollPane;

  public GroupView(JInternalFrame frame) {
    setLayout(new FlowLayout());
    setBackground(Color.white);

    tableModel = new DefaultTableModel() {
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };

    tableModel.addColumn("Sr.No.");
    tableModel.addColumn("ID");
    tableModel.addColumn("Name");

    setTableData(null);

    table = new JTable(tableModel);
    sorter = new TableRowSorter<TableModel>(tableModel);
    table.setRowSorter(sorter);
    table.setFont(labelFont);
    table.setSelectionBackground(skyBlue);
    table.setSelectionForeground(Color.white);
    table.setRowHeight(30);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.addKeyListener((KeyListener) frame);
    if (table.getRowCount() > 0)
      table.setRowSelectionInterval(0, 0);

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
        query = "select * from groups";
        result = DBConnection.executeQuery(query);
      } else {
        query = "select * from groups where g_name like ?";
        PreparedStatement pst = DBConnection.con.prepareStatement(query);
        pst.setString(1, '%' + searchValue.trim() + '%');
        result = pst.executeQuery();
      }
      int srNo = 1;
      while (result.next()) {
        tableModel.addRow(new Object[] {
            srNo++,
            result.getInt("id"),
            result.getString("g_name")
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
