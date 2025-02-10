package com.example.gui;

import com.example.transactions.Transaction;
import com.example.transactions.TransactionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TransactionLogForm extends JDialog {
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public TransactionLogForm(Frame owner) {
        super(owner, "Transaction Log", true);
        initComponents();
        setSize(700, 400);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Create a table model with columns for transaction details.
        String[] columns = {"Timestamp", "Employee ID", "Operation", "Details"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setFillsViewportHeight(true);

        // Add mouse listener to expand a log entry on double-click.
        transactionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedRow = transactionTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = transactionTable.convertRowIndexToModel(selectedRow);
                        // Retrieve log details from the model.
                        String timestamp = (String) tableModel.getValueAt(modelRow, 0);
                        String employeeId = (String) tableModel.getValueAt(modelRow, 1);
                        String operation = (String) tableModel.getValueAt(modelRow, 2);
                        String details = (String) tableModel.getValueAt(modelRow, 3);

                        // Prepare a text area to show full details.
                        JTextArea textArea = new JTextArea();
                        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        textArea.setText("Timestamp: " + timestamp + "\n"
                                + "Employee ID: " + employeeId + "\n"
                                + "Operation: " + operation + "\n"
                                + "Details: " + details);
                        textArea.setEditable(false);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(500, 300));

                        JOptionPane.showMessageDialog(TransactionLogForm.this, scrollPane, "Transaction Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        refreshTransactions();

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with a Close button.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshTransactions() {
        tableModel.setRowCount(0);
        List<Transaction> transactions = TransactionManager.getTransactions();
        for (Transaction t : transactions) {
            Object[] rowData = {
                    t.getTimestamp().toString(),
                    t.getEmployeeId(),
                    t.getOperation(),
                    t.getDetails()
            };
            tableModel.addRow(rowData);
        }
    }
}