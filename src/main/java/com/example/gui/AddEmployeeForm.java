package com.example.gui;

import com.example.auth.Employee;
import com.example.auth.AuthenticationManager;
import com.example.auth.Role;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddEmployeeForm extends JDialog {
    private JTextField empIdField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<Role> roleCombo;
    private JCheckBox addProductsCheck;
    private JCheckBox removeProductsCheck;
    private JCheckBox updateProductsCheck;
    private JButton submitButton;
    private JButton cancelButton;

    public AddEmployeeForm(Frame owner) {
        super(owner, "Add New Employee", true);
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        empIdField = new JTextField(15);
        panel.add(empIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(Role.values());
        panel.add(roleCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Can Add Products:"), gbc);
        gbc.gridx = 1;
        addProductsCheck = new JCheckBox();
        panel.add(addProductsCheck, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Can Remove Products:"), gbc);
        gbc.gridx = 1;
        removeProductsCheck = new JCheckBox();
        panel.add(removeProductsCheck, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Can Update Products:"), gbc);
        gbc.gridx = 1;
        updateProductsCheck = new JCheckBox();
        panel.add(updateProductsCheck, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        submitButton = new JButton("Submit");
        panel.add(submitButton, gbc);

        gbc.gridy = 8;
        cancelButton = new JButton("Cancel");
        panel.add(cancelButton, gbc);

        submitButton.addActionListener(e -> addEmployeeAction());
        cancelButton.addActionListener(e -> dispose());
        add(panel);
    }

    private void addEmployeeAction() {
        String empId = empIdField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        Role role = (Role) roleCombo.getSelectedItem();

        if (empId.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean canAdd = addProductsCheck.isSelected();
        boolean canRemove = removeProductsCheck.isSelected();
        boolean canUpdate = updateProductsCheck.isSelected();

        Employee newEmp = new Employee(empId, username, password, role, canAdd, canRemove, canUpdate);
        AuthenticationManager.addEmployee(newEmp);
        JOptionPane.showMessageDialog(this, "Employee added successfully!");
        dispose();
    }
}