package com.example.gui;

import com.example.auth.Employee;
import com.example.auth.AuthenticationManager;
import com.example.auth.Role;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    private JTextField shopNameField;
    private JButton updateShopNameBtn;
    private JButton addEmployeeBtn;

    public AdminPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Grocery Shop Name:"), gbc);
        gbc.gridx = 1;
        shopNameField = new JTextField(20);
        add(shopNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        updateShopNameBtn = new JButton("Update Shop Name");
        add(updateShopNameBtn, gbc);
        updateShopNameBtn.addActionListener(e -> {
            String newName = shopNameField.getText().trim();
            if (!newName.isEmpty()) {
                // Here, you could update a shared configuration or notify other parts of the system.
                JOptionPane.showMessageDialog(this, "Shop name updated to: " + newName);
            }
        });

        gbc.gridx = 0; gbc.gridy = 2;
        addEmployeeBtn = new JButton("Add New Employee");
        add(addEmployeeBtn, gbc);
        addEmployeeBtn.addActionListener(e -> {
            AddEmployeeForm addEmpForm = new AddEmployeeForm((Frame) SwingUtilities.getWindowAncestor(this));
            addEmpForm.setVisible(true);
        });
    }
}
