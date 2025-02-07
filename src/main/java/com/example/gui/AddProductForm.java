package com.example.gui;

import com.example.inventory.Electronics;
import com.example.inventory.GroceryItem;
import com.example.inventory.Clothing;
import com.example.inventory.Inventory;
import com.example.inventory.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductForm extends JDialog {

    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JComboBox<String> productTypeCombo;

    // Panels to hold additional fields
    private JPanel additionalFieldsPanel;

    // Fields for Electronics
    private JTextField warrantyField;

    // Field for Grocery Item
    private JTextField expiryDateField;

    // Fields for Clothing
    private JComboBox<String> sizeCombo;
    private JComboBox<String> materialCombo;

    private JButton submitButton;
    private JButton cancelButton;

    // Reference to the inventory (so that the product can be added)
    private Inventory inventory;

    public AddProductForm(Frame owner, Inventory inventory) {
        super(owner, "Add New Product", true);
        this.inventory = inventory;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Product ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        productIdField = new JTextField(20);
        inputPanel.add(productIdField, gbc);

        // Row 1: Product Name
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        productNameField = new JTextField(20);
        inputPanel.add(productNameField, gbc);

        // Row 2: Price
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        inputPanel.add(priceField, gbc);

        // Row 3: Quantity
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(20);
        inputPanel.add(quantityField, gbc);

        // Row 4: Product Type (Dropdown)
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Product Type:"), gbc);
        gbc.gridx = 1;
        productTypeCombo = new JComboBox<>(new String[] {"Electronics", "Grocery Item", "Clothing"});
        inputPanel.add(productTypeCombo, gbc);

        // Row 5: Additional Fields (dynamic panel)
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        additionalFieldsPanel = new JPanel(new CardLayout());
        additionalFieldsPanel.setBorder(BorderFactory.createTitledBorder("Additional Details"));

        // Panel for Electronics
        JPanel electronicsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        electronicsPanel.add(new JLabel("Warranty (months):"));
        warrantyField = new JTextField(10);
        electronicsPanel.add(warrantyField);

        // Panel for Grocery Item
        JPanel groceryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        groceryPanel.add(new JLabel("Expiry Date (dd/mm/yyyy):"));
        expiryDateField = new JTextField(10);
        groceryPanel.add(expiryDateField);

        // Panel for Clothing
        JPanel clothingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clothingPanel.add(new JLabel("Size:"));
        sizeCombo = new JComboBox<>(new String[] {"S", "M", "L", "XL"});
        clothingPanel.add(sizeCombo);
        clothingPanel.add(new JLabel("Material:"));
        materialCombo = new JComboBox<>(new String[] {"Cotton", "Polyester", "Wool", "Silk"});
        clothingPanel.add(materialCombo);

        // Add these panels to the card layout
        additionalFieldsPanel.add(electronicsPanel, "Electronics");
        additionalFieldsPanel.add(groceryPanel, "Grocery Item");
        additionalFieldsPanel.add(clothingPanel, "Clothing");

        inputPanel.add(additionalFieldsPanel, gbc);

        // Listen to product type changes and update the additional fields panel
        productTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)(additionalFieldsPanel.getLayout());
                String selected = (String) productTypeCombo.getSelectedItem();
                cl.show(additionalFieldsPanel, selected);
            }
        });

        // Set initial card view
        CardLayout cl = (CardLayout)(additionalFieldsPanel.getLayout());
        cl.show(additionalFieldsPanel, (String) productTypeCombo.getSelectedItem());

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductAction();
            }
        });
        cancelButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

    // Validate fields, create the appropriate Product instance, add it to the inventory, then close the dialog.
    private void addProductAction() {
        String id = productIdField.getText().trim();
        String name = productNameField.getText().trim();
        String priceText = priceField.getText().trim();
        String qtyText = quantityField.getText().trim();

        if(id.isEmpty() || name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all standard fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceText);
            quantity = Integer.parseInt(qtyText);
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and Quantity must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String type = (String) productTypeCombo.getSelectedItem();
        Product product = null;

        // Create the product based on the type and additional fields
        if("Electronics".equals(type)) {
            String warrantyText = warrantyField.getText().trim();
            if(warrantyText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter warranty period for Electronics.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int warranty;
            try {
                warranty = Integer.parseInt(warrantyText);
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Warranty must be numeric (in months).", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            product = new Electronics(id, name, price, quantity, warranty);
        } else if("Grocery Item".equals(type)) {
            String expiry = expiryDateField.getText().trim();
            if(expiry.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter expiry date for Grocery Item.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // You can add more robust date validation if needed.
            product = new GroceryItem(id, name, price, quantity, expiry);
        } else if("Clothing".equals(type)) {
            String size = (String) sizeCombo.getSelectedItem();
            String material = (String) materialCombo.getSelectedItem();
            product = new Clothing(id, name, price, quantity, size, material);
        }

        if(product != null) {
            inventory.addProduct(product);
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Unable to create product. Please check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // To test the form independently, you can include a main method:
    public static void main(String[] args) {
        // For testing, create a dummy inventory.
        Inventory dummyInventory = new Inventory();
        JFrame frame = new JFrame();
        AddProductForm form = new AddProductForm(frame, dummyInventory);
        form.setVisible(true);
        // After closing the form, you could print out the inventory for debugging.
        dummyInventory.getProducts().forEach(p -> System.out.println(p.getName()));
    }
}