package com.example.gui;

import com.example.auth.Employee;
import com.example.auth.Role;
import com.example.auth.AuthenticationManager;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard(Employee loggedInEmployee) {
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        initComponents(loggedInEmployee);
    }

    private void initComponents(Employee loggedInEmployee) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Inventory Management Tab.
        InventoryManagementGUI inventoryPanel = new InventoryManagementGUI(loggedInEmployee);
        // We can extract the inventory management panel from InventoryManagementGUI.
        // For this example, we add the content pane of the current frame.
        tabbedPane.addTab("Inventory Management", inventoryPanel.getContentPane());

        // Employee Management Tab (only available for admin).
        if (loggedInEmployee.getRole() == Role.ADMIN) {
            EmployeeManagementPanel empPanel = new EmployeeManagementPanel();
            tabbedPane.addTab("Employee Management", empPanel);
        }

        add(tabbedPane, BorderLayout.CENTER);
    }

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
                MainDashboard dashboard = new MainDashboard(loggedInEmployee);
                dashboard.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}