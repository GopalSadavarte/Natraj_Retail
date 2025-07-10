package com.app.components.purchase.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.app.config.DBConnection;
import com.app.partials.interfaces.AppConstants;
import com.app.partials.interfaces.Validation;

public class CustomerView extends JPanel implements AppConstants, Validation {
    final JTable table;
    final DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    final JScrollPane scrollPane;

    public CustomerView(KeyListener listener) {

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
        tableModel.addColumn("Contact No.");

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
        if (table.getRowCount() > 0)
            table.setRowSelectionInterval(0, 0);

        TableColumnModel columnModel = table.getColumnModel();
        int[] values = new int[] { 70, 70, 200, 140 };
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
                query = "select * from customers";
                result = DBConnection.executeQuery(query);
            } else {
                query = "select * from customers where name like ?";
                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                pst.setString(1, '%' + searchValue.trim() + '%');
                result = pst.executeQuery();
            }

            int srNo = 1;
            while (result.next()) {
                tableModel.addRow(new Object[] {
                        srNo++,
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("contact_no"),
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at CustomerView.setTableData() 60");
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
