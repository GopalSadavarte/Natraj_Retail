package com.app.components.purchase.support;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.app.config.DBConnection;
import com.app.partials.interfaces.AppConstants;
import com.app.partials.interfaces.TableExporter;

public class StockView extends JPanel implements AppConstants, KeyListener, TableExporter {
    final JTable table;
    final DefaultTableModel tableModel;
    final TableRowSorter<TableModel> sorter;
    final JScrollPane scrollPane;
    final String productId;
    final JButton select;
    final ActionListener listener;

    public StockView(String pId, ActionListener listener) {
        productId = pId.trim();
        this.listener = listener;
        setLayout(flowLayoutCenter);
        setBackground(Color.white);
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableModel.addColumn("ID");
        tableModel.addColumn("Qty.");
        tableModel.addColumn("MRP");
        tableModel.addColumn("Rate");
        tableModel.addColumn("Exp.Date");

        sorter = new TableRowSorter<>(tableModel);

        table = new JTable(tableModel);
        table.setRowSorter(sorter);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowSelectionAllowed(true);
        table.setSelectionBackground(skyBlue);
        table.setSelectionForeground(Color.white);
        table.setRowHeight(20);
        table.setFont(labelFont);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(labelFont);
        table.addKeyListener(this);
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");

        TableColumnModel columnModel = table.getColumnModel();
        int[] widths = new int[] { 65, 100, 100, 100, 150 };

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
        }

        setTableData(null);

        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);

        add(scrollPane);

        select = new JButton("select");
        select.setForeground(Color.white);
        select.setBackground(orange);
        select.addActionListener(listener);

        add(select);

        setVisible(true);
    }

    public JTable getTable() {
        return table;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JButton getSelectBtn() {
        return select;
    }

    public void keyPressed(KeyEvent e) {

        String key = KeyEvent.getKeyText(e.getKeyCode());
        Object source = e.getSource();

        if (source.equals(table) && key.equals("Enter")) {
            select.doClick();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void setTableData(String searchValue) {
        try {
            String query = "select * from inventories where product_id = ? and current_quantity != 0 order by sale_rate asc";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(productId));

            ResultSet result = pst.executeQuery();
            boolean flag = false;
            while (result.next()) {
                String exp = result.getString("product_exp_date");
                exp = exp != null ? exp : "NA";
                tableModel.addRow(new Object[] {
                        result.getLong("id"), result.getInt("current_quantity"),
                        result.getDouble("product_mrp"),
                        result.getDouble("sale_rate"),
                        exp
                });
                flag = true;
            }
            if (flag)
                table.setRowSelectionInterval(0, 0);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at StockView.java");
        }
    }
}
