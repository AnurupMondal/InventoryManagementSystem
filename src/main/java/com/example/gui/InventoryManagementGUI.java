package com.example.gui;

import com.example.inventory.Inventory;
import com.example.inventory.Product;
import com.example.inventory.Electronics;
import com.example.inventory.GroceryItem;
import com.example.inventory.Clothing;
import com.example.inventory.SampleDataPopulator;
import com.example.billing.Bill;
import com.example.billing.BillingService;
import com.example.auth.Employee;
import com.example.auth.Role;
import com.example.auth.AuthenticationManager;
import com.example.transactions.TransactionManager;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InventoryManagementGUI extends JFrame {
    private Inventory inventory;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField filterField;
    private boolean sampleDataLoaded = false;

    // Instance variables for buttons (to enable/disable based on permissions)
    private JButton addProductBtn;
    private JButton removeProductBtn;
    private JButton updateProductBtn;
    // Search, Create Bill, and View Transactions are available to all users.

    // The currently logged-in employee.
    private Employee loggedInEmployee;

    // Status bar for messages.
    private JLabel statusBar;

    // Constructor accepts the logged-in employee.
    public InventoryManagementGUI(Employee loggedInEmployee) {
        this.loggedInEmployee = loggedInEmployee;
        inventory = new Inventory();
        initComponents();
        setTitle("Inventory Management System");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Create Menu Bar with File and Help menus.
        JMenuBar menuBar = new JMenuBar();

        // File menu.
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Help menu.
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Main panel with padding.
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left panel: Buttons using GridLayout.
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add Product button.
        addProductBtn = new JButton("Add Product");
        addProductBtn.setToolTipText("Add a new product to inventory (Alt+P)");
        addProductBtn.setMnemonic(KeyEvent.VK_P);
        addProductBtn.addActionListener(e -> addProduct());
        buttonPanel.add(addProductBtn);

        // Remove Product button.
        removeProductBtn = new JButton("Remove Product");
        removeProductBtn.setToolTipText("Remove a product from inventory (Alt+R)");
        removeProductBtn.setMnemonic(KeyEvent.VK_R);
        removeProductBtn.addActionListener(e -> removeProduct());
        buttonPanel.add(removeProductBtn);

        // Update Product button.
        updateProductBtn = new JButton("Update Product");
        updateProductBtn.setToolTipText("Update product quantity (Alt+U)");
        updateProductBtn.setMnemonic(KeyEvent.VK_U);
        updateProductBtn.addActionListener(e -> updateProduct());
        buttonPanel.add(updateProductBtn);

        // Search Product button.
        JButton searchProductBtn = new JButton("Search Product");
        searchProductBtn.setToolTipText("Search for a product (Alt+S)");
        searchProductBtn.setMnemonic(KeyEvent.VK_S);
        searchProductBtn.addActionListener(e -> searchProduct());
        buttonPanel.add(searchProductBtn);

        // Populate Sample Data button.
        if (!sampleDataLoaded) {
            JButton populateSampleBtn = new JButton("Populate Sample Data");
            populateSampleBtn.setToolTipText("Load sample data into inventory");
            populateSampleBtn.addActionListener(e -> {
                SampleDataPopulator.populateSampleProducts(inventory);
                sampleDataLoaded = true;
                refreshInventoryTable();
                setStatus("Sample data loaded successfully.");
                JOptionPane.showMessageDialog(this, "Sample data loaded successfully.");
            });
            buttonPanel.add(populateSampleBtn);
        }

        // Create Bill button.
        JButton createBillBtn = new JButton("Create Bill");
        createBillBtn.setToolTipText("Create a new bill (Alt+B)");
        createBillBtn.setMnemonic(KeyEvent.VK_B);
        createBillBtn.addActionListener(e -> createBill());
        buttonPanel.add(createBillBtn);

        // View Transactions button.
        JButton viewTransactionsBtn = new JButton("View Transactions");
        viewTransactionsBtn.setToolTipText("View previous transactions");
        viewTransactionsBtn.addActionListener(e -> {
            TransactionLogForm logForm = new TransactionLogForm(this);
            logForm.setVisible(true);
        });
        buttonPanel.add(viewTransactionsBtn);

        // Exit button.
        JButton exitBtn = new JButton("Exit");
        exitBtn.setToolTipText("Exit the application");
        exitBtn.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitBtn);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Center panel: Filter panel and inventory table.
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterField = new JTextField(20);
        filterPanel.add(filterField);
        centerPanel.add(filterPanel, BorderLayout.NORTH);

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

        // Add a listener to filter the table.
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        // Add a right-click context menu to view product details.
        inventoryTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = inventoryTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        inventoryTable.setRowSelectionInterval(row, row);
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem detailsItem = new JMenuItem("View Details");
                        detailsItem.addActionListener(ev -> {
                            int modelRow = inventoryTable.convertRowIndexToModel(row);
                            String prodId = (String) tableModel.getValueAt(modelRow, 0);
                            String details = (String) tableModel.getValueAt(modelRow, 4);
                            JOptionPane.showMessageDialog(InventoryManagementGUI.this,
                                    "Product ID: " + prodId + "\nDetails: " + details,
                                    "Product Details",
                                    JOptionPane.INFORMATION_MESSAGE);
                        });
                        popup.add(detailsItem);
                        popup.show(inventoryTable, e.getX(), e.getY());
                    }
                }
            }
        });

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Status bar at the bottom.
        statusBar = new JLabel(" ");
        statusBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.add(statusBar, BorderLayout.SOUTH);

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

    // Open the dedicated AddProductForm dialog.
    private void addProduct() {
        int prevSize = inventory.getProducts().size();
        AddProductForm addForm = new AddProductForm(this, inventory);
        addForm.setVisible(true);
        int newSize = inventory.getProducts().size();
        if (newSize > prevSize) {
            Product addedProduct = inventory.getProducts().get(newSize - 1);
            TransactionManager.logTransaction(
                    loggedInEmployee.getEmployeeId(),
                    "ADD PRODUCT",
                    "Added product: " + addedProduct.getName()
            );
            setStatus("Product '" + addedProduct.getName() + "' added successfully.");
        }
        refreshInventoryTable();
    }

    private void removeProduct() {
        String productId = JOptionPane.showInputDialog(this, "Enter Product ID to remove:");
        if (productId == null || productId.trim().isEmpty()) return;
        try {
            inventory.removeProduct(productId);
            refreshInventoryTable();
            JOptionPane.showMessageDialog(this, "Product removed successfully.");
            TransactionManager.logTransaction(
                    loggedInEmployee.getEmployeeId(),
                    "REMOVE PRODUCT",
                    "Removed product with ID: " + productId
            );
            setStatus("Product with ID '" + productId + "' removed.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            setStatus("Failed to remove product: " + e.getMessage());
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
            setStatus("Invalid quantity entered.");
            return;
        }
        try {
            inventory.updateProductQuantity(productId, newQuantity);
            refreshInventoryTable();
            JOptionPane.showMessageDialog(this, "Product quantity updated successfully.");
            TransactionManager.logTransaction(
                    loggedInEmployee.getEmployeeId(),
                    "UPDATE PRODUCT",
                    "Updated product with ID: " + productId + " to new quantity: " + newQuantity
            );
            setStatus("Product with ID '" + productId + "' updated.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            setStatus("Failed to update product: " + e.getMessage());
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
            setStatus("Product '" + p.getName() + "' found.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            setStatus("Search failed: " + e.getMessage());
        }
    }

    private void createBill() {
        CreateBillForm createBillForm = new CreateBillForm(this, inventory);
        createBillForm.setVisible(true);
        Bill bill = createBillForm.getCreatedBill();
        if (bill != null) {
            JOptionPane.showMessageDialog(this,
                    "Bill created successfully with total: $" + bill.getTotal(),
                    "Bill Created", JOptionPane.INFORMATION_MESSAGE);
            refreshInventoryTable();
            TransactionManager.logTransaction(
                    loggedInEmployee.getEmployeeId(),
                    "CREATE BILL",
                    "Created bill with total: $" + bill.getTotal()
            );
            setStatus("Bill created successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Bill creation was canceled.", "Canceled", JOptionPane.WARNING_MESSAGE);
            setStatus("Bill creation canceled.");
        }
    }

    // Display a modern About dialog.
    private void showAboutDialog() {
        String aboutText = "Inventory Management System\n" +
                "Version 2.0\n" +
                "Developed by Anurup Chandra Mondal\n" +
                "© 2025 Personal Domination";
        JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // Set a message in the status bar.
    private void setStatus(String message) {
        statusBar.setText(message);
    }

    // Method to apply permissions based on the logged-in employee.
    public void applyPermissions(Employee emp) {
        if (!emp.isCanAddProducts()) {
            addProductBtn.setEnabled(false);
        }
        if (!emp.isCanRemoveProducts()) {
            removeProductBtn.setEnabled(false);
        }
        if (!emp.isCanUpdateProducts()) {
            updateProductBtn.setEnabled(false);
        }
        // Search, Create Bill, and View Transactions remain enabled for everyone.
    }

    // Main method integrated with login and access control.
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            // Launch the login form first.
            LoginForm loginForm = new LoginForm(null);
            loginForm.setVisible(true);
            Employee loggedInEmployee = loginForm.getLoggedInEmployee();
            if (loggedInEmployee != null) {
                InventoryManagementGUI gui = new InventoryManagementGUI(loggedInEmployee);
                // If the logged-in user is an admin, display the AdminPanel at the top.
                if (loggedInEmployee.getRole() == Role.ADMIN) {
                    AdminPanel adminPanel = new AdminPanel();
                    gui.getContentPane().add(adminPanel, BorderLayout.NORTH);
                }
                // Apply button permissions based on the employee.
                gui.applyPermissions(loggedInEmployee);
                gui.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}