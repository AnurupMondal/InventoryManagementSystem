package com.example.gui;

import com.example.auth.AuthenticationManager;
import com.example.auth.Employee;
import com.example.auth.Role;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EmployeeManagementPanel extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField filterField;

    public EmployeeManagementPanel() {
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel for filtering.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter:"));
        filterField = new JTextField(20);
        topPanel.add(filterField);
        add(topPanel, BorderLayout.NORTH);

        // Table to display employees.
        String[] columns = {"Employee ID", "Username", "Role", "Can Add", "Can Remove", "Can Update"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addEmpBtn = new JButton("Add Employee");
        addEmpBtn.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            AddEmployeeForm addEmpForm = new AddEmployeeForm(parentFrame);
            addEmpForm.setVisible(true);
            refreshTable();
        });
        JButton updateEmpBtn = new JButton("Update Access");
        updateEmpBtn.addActionListener(e -> updateEmployeeAccess());
        JButton removeEmpBtn = new JButton("Remove Employee");
        removeEmpBtn.addActionListener(e -> removeEmployee());
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTable());
        buttonPanel.add(addEmpBtn);
        buttonPanel.add(updateEmpBtn);
        buttonPanel.add(removeEmpBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Filter listener.
        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Employee> employees = AuthenticationManager.getEmployees();
        String filterText = filterField.getText().trim().toLowerCase();
        for (Employee emp : employees) {
            if (!filterText.isEmpty() && !emp.getUsername().toLowerCase().contains(filterText)) {
                continue;
            }
            Object[] row = {
                    emp.getEmployeeId(),
                    emp.getUsername(),
                    emp.getRole().toString(),
                    emp.isCanAddProducts(),
                    emp.isCanRemoveProducts(),
                    emp.isCanUpdateProducts()
            };
            tableModel.addRow(row);
        }
    }

    private void updateEmployeeAccess() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        String empId = (String) tableModel.getValueAt(modelRow, 0);
        Employee selectedEmp = null;
        for (Employee emp : AuthenticationManager.getEmployees()) {
            if (emp.getEmployeeId().equals(empId)) {
                selectedEmp = emp;
                break;
            }
        }
        if (selectedEmp != null) {
            UpdateEmployeeForm updateForm = new UpdateEmployeeForm(SwingUtilities.getWindowAncestor(this), selectedEmp);
            updateForm.setVisible(true);
            refreshTable();
        }
    }

    private void removeEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        String empId = (String) tableModel.getValueAt(modelRow, 0);
        // Prevent removing an admin.
        for (Employee emp : AuthenticationManager.getEmployees()) {
            if (emp.getEmployeeId().equals(empId)) {
                if (emp.getRole() == Role.ADMIN) {
                    JOptionPane.showMessageDialog(this, "Cannot remove an admin employee.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;
            }
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this employee?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            AuthenticationManager.getEmployees().removeIf(emp -> emp.getEmployeeId().equals(empId));
            refreshTable();
        }
    }
}