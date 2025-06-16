package com.app.components.purchase.support;

import javax.swing.*;
import javax.swing.table.*;
import com.app.config.*;
import com.app.partials.interfaces.AppConstants;
import com.app.partials.interfaces.Validation;

import java.awt.*;
import java.sql.*;

public class SubGroupView extends JPanel implements AppConstants, Validation {

  final JTable table;
  public DefaultTableModel tableModel;
  TableRowSorter<TableModel> sorter;
  public final JScrollPane scrollPane;

  public SubGroupView() {
    setLayout(new FlowLayout());
    setBackground(Color.white);

    tableModel = new DefaultTableModel() {
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };

    tableModel.addColumn("Sr.No.");
    tableModel.addColumn("ID");
    tableModel.addColumn("Sub group name");
    tableModel.addColumn("Group name");

    setTableData();

    table = new JTable(tableModel);
    sorter = new TableRowSorter<TableModel>(tableModel);
    table.setRowSorter(sorter);
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
      String query = "select * from groups,sub_groups where groups.id = sub_groups.group_id";
      ResultSet result = DBConnection.executeQuery(query);
      int srNo = 1;
      while (result.next()) {
        tableModel.addRow(new Object[] {
            srNo++,
            result.getInt("id"),
            result.getString("sub_group_name"),
            result.getString("g_name")
        });
      }
    } catch (Exception e) {
      System.out.println(e.getMessage() + " at GroupView.java 60");
    }
  }

  public JTable getTable(){
    return table;
  }
}
