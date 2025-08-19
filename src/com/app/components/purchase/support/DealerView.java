package com.app.components.purchase.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.app.config.DBConnection;
import com.app.partials.interfaces.AppConstants;
import com.app.partials.interfaces.TableExporter;

public class DealerView extends JPanel implements AppConstants, TableExporter {
    final JTable table;
    final DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    final JScrollPane scrollPane;

    public DealerView(KeyListener listener) {

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
        tableModel.addColumn("Email");
        tableModel.addColumn("GST No.");

        setTableData(null);

        table = new JTable(tableModel);
        sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);
        table.setSelectionBackground(skyBlue);
        table.setSelectionForeground(Color.white);
        table.setFont(labelFont);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addKeyListener(listener);
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");
        table.setPreferredScrollableViewportSize(new Dimension(900, 350));
        table.setRowHeight(30);
        if (table.getRowCount() > 0)
            table.setRowSelectionInterval(0, 0);

        TableColumnModel columnModel = table.getColumnModel();
        int[] values = new int[] { 70, 70, 200, 140, 200, 200 };
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
            tableModel.setRowCount(0);
            if (searchValue == null || searchValue.isBlank()) {
                query = "select * from dealers";
                result = DBConnection.executeQuery(query);
            } else {
                query = "select * from dealers where name like ?";
                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                pst.setString(1, '%' + searchValue.trim() + '%');
                result = pst.executeQuery();
            }

            int srNo = 1;
            while (result.next()) {
                tableModel.addRow(new Object[] {
                        srNo++,
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("contact_no"),
                        result.getString("email"),
                        result.getString("gst_no"),
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
