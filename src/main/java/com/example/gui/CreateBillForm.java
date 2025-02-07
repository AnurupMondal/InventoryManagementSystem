package com.example.gui;

import com.example.billing.Bill;
import com.example.billing.BillItem;
import com.example.inventory.Inventory;
import com.example.inventory.Product;
import com.example.inventory.Electronics;
import com.example.inventory.GroceryItem;
import com.example.inventory.Clothing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreateBillForm extends JDialog {
    private Inventory inventory;
    private Bill createdBill;
    private List<BillItem> billItems;

    private JTextField customerNameField;

    // Left: available inventory table
    private JTable availableTable;
    private DefaultTableModel availableTableModel;
    private TableRowSorter<DefaultTableModel> availableSorter;
    private JTextField availableFilterField;

    // Right: selected items table
    private JTable selectedTable;
    private DefaultTableModel selectedTableModel;

    public CreateBillForm(Frame owner, Inventory inventory) {
        super(owner, "Create Bill", true);
        this.inventory = inventory;
        createdBill = new Bill();
        billItems = new ArrayList<>();
        initComponents();
        setSize(900, 600);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        topPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField(20);
        topPanel.add(customerNameField);
        add(topPanel, BorderLayout.NORTH);

        // Center: Split pane with available and selected items
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        // Left panel: Available inventory with filter
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Inventory"));
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        availableFilterField = new JTextField(15);
        filterPanel.add(availableFilterField);
        leftPanel.add(filterPanel, BorderLayout.NORTH);

        String[] availableColumns = {"Product ID", "Name", "Price", "Available", "Details"};
        availableTableModel = new DefaultTableModel(availableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        availableTable = new JTable(availableTableModel);
        availableTable.setAutoCreateRowSorter(true);
        availableSorter = new TableRowSorter<>(availableTableModel);
        availableTable.setRowSorter(availableSorter);
        refreshAvailableTable();
        JScrollPane availableScrollPane = new JScrollPane(availableTable);
        leftPanel.add(availableScrollPane, BorderLayout.CENTER);

        // Panel below available table: quantity spinner and "Add to Bill" button
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.add(new JLabel("Quantity:"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        addPanel.add(quantitySpinner);
        JButton addToBillBtn = new JButton("Add to Bill");
        addPanel.add(addToBillBtn);
        leftPanel.add(addPanel, BorderLayout.SOUTH);

        addToBillBtn.addActionListener(e -> {
            int selectedRow = availableTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select an item from available inventory.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int modelRow = availableTable.convertRowIndexToModel(selectedRow);
            String prodId = (String) availableTableModel.getValueAt(modelRow, 0);
            String name = (String) availableTableModel.getValueAt(modelRow, 1);
            double price = (double) availableTableModel.getValueAt(modelRow, 2);
            int availableQty = (int) availableTableModel.getValueAt(modelRow, 3);
            int qtyToAdd = (Integer) quantitySpinner.getValue();
            if (qtyToAdd > availableQty) {
                JOptionPane.showMessageDialog(this, "Selected quantity exceeds available stock.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Add item to bill list
            BillItem item = new BillItem(prodId, name, price, qtyToAdd);
            billItems.add(item);
            Object[] rowData = {prodId, name, price, qtyToAdd, item.getTotalPrice()};
            selectedTableModel.addRow(rowData);
            // Update available quantity in the left table (without updating underlying inventory yet)
            availableTableModel.setValueAt(availableQty - qtyToAdd, modelRow, 3);
        });

        // Right panel: Selected items for the bill
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Items in Bill"));
        String[] selectedColumns = {"Product ID", "Name", "Unit Price", "Quantity", "Total"};
        selectedTableModel = new DefaultTableModel(selectedColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        selectedTable = new JTable(selectedTableModel);
        selectedTable.setAutoCreateRowSorter(true);
        JScrollPane selectedScrollPane = new JScrollPane(selectedTable);
        rightPanel.add(selectedScrollPane, BorderLayout.CENTER);
        JButton removeSelectedBtn = new JButton("Remove Selected Item");
        removeSelectedBtn.addActionListener(e -> {
            int selectedRow = selectedTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select an item to remove.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int modelRow = selectedTable.convertRowIndexToModel(selectedRow);
            String prodId = (String) selectedTableModel.getValueAt(modelRow, 0);
            int qtyToRemove = (int) selectedTableModel.getValueAt(modelRow, 3);
            selectedTableModel.removeRow(modelRow);
            billItems.removeIf(item -> item.getProductId().equals(prodId) && item.getQuantity() == qtyToRemove);
            // Update available table: add back the quantity
            for (int i = 0; i < availableTableModel.getRowCount(); i++) {
                if (((String) availableTableModel.getValueAt(i, 0)).equals(prodId)) {
                    int currentAvail = (int) availableTableModel.getValueAt(i, 3);
                    availableTableModel.setValueAt(currentAvail + qtyToRemove, i, 3);
                    break;
                }
            }
        });
        rightPanel.add(removeSelectedBtn, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);

        // Bottom panel: Finish and Cancel buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton finishBtn = new JButton("Finish Bill");
        JButton cancelBtn = new JButton("Cancel");
        bottomPanel.add(finishBtn);
        bottomPanel.add(cancelBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        finishBtn.addActionListener(e -> finishBill());
        cancelBtn.addActionListener(e -> {
            createdBill = null; // Cancel the bill
            dispose();
        });

        // Add filtering for available inventory
        availableFilterField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterAvailableTable(); }
            public void removeUpdate(DocumentEvent e) { filterAvailableTable(); }
            public void changedUpdate(DocumentEvent e) { filterAvailableTable(); }
        });
    }

    private void filterAvailableTable() {
        String text = availableFilterField.getText();
        if (text.trim().isEmpty()) {
            availableSorter.setRowFilter(null);
        } else {
            availableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void refreshAvailableTable() {
        availableTableModel.setRowCount(0);
        for (Product p : inventory.getProducts()) {
            String details = "";
            if (p instanceof Electronics) {
                details = "Warranty: " + ((Electronics) p).getWarrantyMonths() + " months";
            } else if (p instanceof GroceryItem) {
                details = "Expiry: " + ((GroceryItem) p).getExpiryDate();
            } else if (p instanceof Clothing) {
                Clothing c = (Clothing) p;
                details = "Size: " + c.getSize() + ", Material: " + c.getMaterial();
            }
            Object[] rowData = {p.getProductId(), p.getName(), p.getPrice(), p.getQuantity(), details};
            availableTableModel.addRow(rowData);
        }
    }

    private void finishBill() {
        String customerName = customerNameField.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the customer's name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (billItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items have been added to the bill.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        createdBill.setCustomerName(customerName);
        for (BillItem item : billItems) {
            createdBill.addItem(item);
            // Now update the inventory quantities (if necessary)
            try {
                Product prod = inventory.searchProductById(item.getProductId());
                inventory.updateProductQuantity(prod.getProductId(), prod.getQuantity() - item.getQuantity());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating inventory: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        createdBill.calculateTotal();
        // Display the modern, printable invoice view
        displayModernBill(createdBill);
        dispose();
    }

    // Displays a modern, invoice-style bill view in a new modal dialog.
    private void displayModernBill(Bill bill) {
        // Create a new modal dialog for the invoice
        JDialog invoiceDialog = new JDialog(this, "Invoice", true);
        invoiceDialog.setLayout(new BorderLayout(10, 10));
        invoiceDialog.setSize(700, 500);
        invoiceDialog.setLocationRelativeTo(this);

        // ----- Header Panel -----
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(45, 45, 45)); // Dark background
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("INVOICE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel customerLabel = new JLabel("Customer: " + bill.getCustomerName(), SwingConstants.CENTER);
        customerLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        customerLabel.setForeground(Color.LIGHT_GRAY);
        headerPanel.add(customerLabel, BorderLayout.SOUTH);

        invoiceDialog.add(headerPanel, BorderLayout.NORTH);

        // ----- Center Panel: Bill Items Table -----
        String[] columns = {"Product ID", "Name", "Unit Price", "Quantity", "Total"};
        DefaultTableModel billTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable billTable = new JTable(billTableModel);
        billTable.setFillsViewportHeight(true);
        billTable.setRowHeight(25);
        billTable.getTableHeader().setReorderingAllowed(false);
        // Populate table with bill items
        for (BillItem item : bill.getItems()) {
            Object[] row = {item.getProductId(), item.getProductName(), item.getUnitPrice(), item.getQuantity(), item.getTotalPrice()};
            billTableModel.addRow(row);
        }
        JScrollPane tableScrollPane = new JScrollPane(billTable);
        invoiceDialog.add(tableScrollPane, BorderLayout.CENTER);

        // ----- Summary Panel -----
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        summaryPanel.setBackground(new Color(45, 45, 45));
        JLabel totalLabel = new JLabel("Total: $" + String.format("%.2f", bill.getTotal()));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalLabel.setForeground(Color.WHITE);
        summaryPanel.add(totalLabel);
        invoiceDialog.add(summaryPanel, BorderLayout.SOUTH);

        // ----- Button Panel -----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(45, 45, 45));
        JButton printBtn = new JButton("Print");
        JButton closeBtn = new JButton("Close");
        buttonPanel.add(printBtn);
        buttonPanel.add(closeBtn);
        invoiceDialog.add(buttonPanel, BorderLayout.PAGE_END);

        // ----- Button Actions -----
        printBtn.addActionListener(e -> {
            try {
                // Print the bill table
                boolean complete = billTable.print();
                if (complete) {
                    JOptionPane.showMessageDialog(invoiceDialog, "Printing Complete", "Printer", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(invoiceDialog, "Printing Cancelled", "Printer", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(invoiceDialog, "Printing Failed: " + ex.getMessage(), "Printer", JOptionPane.ERROR_MESSAGE);
            }
        });
        closeBtn.addActionListener(e -> invoiceDialog.dispose());

        invoiceDialog.setVisible(true);
    }

    // Displays a printable view of the bill in a new modal dialog.
    private void displayPrintableBill(Bill bill) {
        JDialog printDialog = new JDialog(this, "Printable Bill", true);
        printDialog.setLayout(new BorderLayout(10, 10));
        printDialog.setSize(600, 600);
        printDialog.setLocationRelativeTo(this);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setText(generateBillText(bill));

        JScrollPane scrollPane = new JScrollPane(textArea);
        printDialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton printBtn = new JButton("Print");
        printBtn.addActionListener(e -> {
            try {
                boolean complete = textArea.print();
                if (complete) {
                    JOptionPane.showMessageDialog(printDialog, "Printing Complete", "Printer", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(printDialog, "Printing Cancelled", "Printer", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(printDialog, "Printing Failed: " + ex.getMessage(), "Printer", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> printDialog.dispose());
        buttonPanel.add(printBtn);
        buttonPanel.add(closeBtn);

        printDialog.add(buttonPanel, BorderLayout.SOUTH);
        printDialog.setVisible(true);
    }

    // Generates a formatted text view of the bill for printing.
    private String generateBillText(Bill bill) {
        StringBuilder sb = new StringBuilder();
        sb.append("----- Customer Bill -----\n");
        sb.append("Customer: ").append(bill.getCustomerName()).append("\n\n");
        sb.append(String.format("%-10s %-20s %-10s %-10s %-10s\n", "ProdID", "Name", "UnitPrice", "Qty", "Total"));
        sb.append("-------------------------------------------------------------\n");
        for (BillItem item : bill.getItems()) {
            sb.append(String.format("%-10s %-20s %-10.2f %-10d %-10.2f\n",
                    item.getProductId(), item.getProductName(), item.getUnitPrice(), item.getQuantity(), item.getTotalPrice()));
        }
        sb.append("\nTotal Amount: $").append(String.format("%.2f", bill.getTotal())).append("\n");
        sb.append("-------------------------------\n");
        return sb.toString();
    }

    public Bill getCreatedBill() {
        return createdBill;
    }
}
