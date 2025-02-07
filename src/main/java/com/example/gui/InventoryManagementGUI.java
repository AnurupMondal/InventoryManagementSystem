package com.example.gui;

import com.example.inventory.Inventory;
import com.example.inventory.Product;
import com.example.inventory.Electronics;
import com.example.inventory.GroceryItem;
import com.example.inventory.Clothing;
import com.example.inventory.SampleDataPopulator;
import com.example.billing.Bill;
import com.example.billing.BillingService;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class InventoryManagementGUI extends JFrame {
    private Inventory inventory;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField filterField;
    private boolean sampleDataLoaded = false;

    public InventoryManagementGUI() {
        inventory = new Inventory();
        initComponents();
        setTitle("Inventory Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left panel: buttons with equal spacing using GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addProductBtn = new JButton("Add Product");
        // Instead of prompting via dialogs, open our dedicated form.
        addProductBtn.addActionListener(e -> addProduct());
        buttonPanel.add(addProductBtn);

        JButton removeProductBtn = new JButton("Remove Product");
        removeProductBtn.addActionListener(e -> removeProduct());
        buttonPanel.add(removeProductBtn);

        JButton updateProductBtn = new JButton("Update Product");
        updateProductBtn.addActionListener(e -> updateProduct());
        buttonPanel.add(updateProductBtn);

        JButton searchProductBtn = new JButton("Search Product");
        searchProductBtn.addActionListener(e -> searchProduct());
        buttonPanel.add(searchProductBtn);

        if (!sampleDataLoaded) {
            JButton populateSampleBtn = new JButton("Populate Sample Data");
            populateSampleBtn.addActionListener(e -> {
                SampleDataPopulator.populateSampleProducts(inventory);
                sampleDataLoaded = true;
                refreshInventoryTable();
                JOptionPane.showMessageDialog(this, "Sample data loaded successfully.");
            });
            buttonPanel.add(populateSampleBtn);
        }

        JButton createBillBtn = new JButton("Create Bill");
        createBillBtn.addActionListener(e -> createBill());
        buttonPanel.add(createBillBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitBtn);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Center panel: a filter panel at top and the inventory table below
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterField = new JTextField(20);
        filterPanel.add(filterField);
        centerPanel.add(filterPanel, BorderLayout.NORTH);

        // Inventory table setup
        String[] columnNames = {"Product ID", "Name", "Price", "Quantity", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(tableModel);
        inventoryTable.setFillsViewportHeight(true);
        inventoryTable.setAutoCreateRowSorter(true);
        sorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(sorter);
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add a listener to filter the table
        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        refreshInventoryTable();
    }

    private void filterTable() {
        String text = filterField.getText().trim();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void refreshInventoryTable() {
        tableModel.setRowCount(0);
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
            tableModel.addRow(rowData);
        }
    }

    // Updated addProduct method: Open the AddProductForm dialog
    private void addProduct() {
        AddProductForm addForm = new AddProductForm(this, inventory);
        addForm.setVisible(true);
        refreshInventoryTable();
    }

    private void removeProduct() {
        String productId = JOptionPane.showInputDialog(this, "Enter Product ID to remove:");
        if (productId == null || productId.trim().isEmpty()) return;
        try {
            inventory.removeProduct(productId);
            refreshInventoryTable();
            JOptionPane.showMessageDialog(this, "Product removed successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateProduct() {
        String productId = JOptionPane.showInputDialog(this, "Enter Product ID to update:");
        if (productId == null || productId.trim().isEmpty()) return;
        String quantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:");
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered.");
            return;
        }
        try {
            inventory.updateProductQuantity(productId, newQuantity);
            refreshInventoryTable();
            JOptionPane.showMessageDialog(this, "Product quantity updated successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchProduct() {
        String name = JOptionPane.showInputDialog(this, "Enter product name to search:");
        if (name == null || name.trim().isEmpty()) return;
        try {
            Product p = inventory.searchProduct(name);
            String details = "";
            if (p instanceof Electronics) {
                details = "Warranty: " + ((Electronics) p).getWarrantyMonths() + " months";
            } else if (p instanceof GroceryItem) {
                details = "Expiry: " + ((GroceryItem) p).getExpiryDate();
            } else if (p instanceof Clothing) {
                Clothing c = (Clothing) p;
                details = "Size: " + c.getSize() + ", Material: " + c.getMaterial();
            }
            String msg = "Product ID: " + p.getProductId() + "\nName: " + p.getName() +
                    "\nPrice: " + p.getPrice() + "\nQuantity: " + p.getQuantity() +
                    "\nDetails: " + details;
            JOptionPane.showMessageDialog(this, msg, "Product Found", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void createBill() {
        // Open the CreateBillForm dialog
        CreateBillForm createBillForm = new CreateBillForm(this, inventory);
        createBillForm.setVisible(true);
        Bill bill = createBillForm.getCreatedBill();
        if (bill != null) {
            JOptionPane.showMessageDialog(this,
                    "Bill created successfully with total: $" + bill.getTotal(),
                    "Bill Created", JOptionPane.INFORMATION_MESSAGE);
            refreshInventoryTable();
        } else {
            JOptionPane.showMessageDialog(this, "Bill creation was canceled.", "Canceled", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            InventoryManagementGUI gui = new InventoryManagementGUI();
            gui.setVisible(true);
        });
    }
}