package com.example.gui;

import com.example.auth.Employee;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UpdateEmployeeForm extends JDialog {
    private Employee employee;
    private JCheckBox addCheck;
    private JCheckBox removeCheck;
    private JCheckBox updateCheck;

    public UpdateEmployeeForm(Window owner, Employee employee) {
        super(owner, "Update Employee Access", ModalityType.APPLICATION_MODAL);
        this.employee = employee;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(employee.getEmployeeId()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(employee.getUsername()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Can Add Products:"), gbc);
        gbc.gridx = 1;
        addCheck = new JCheckBox();
        addCheck.setSelected(employee.isCanAddProducts());
        panel.add(addCheck, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Can Remove Products:"), gbc);
        gbc.gridx = 1;
        removeCheck = new JCheckBox();
        removeCheck.setSelected(employee.isCanRemoveProducts());
        panel.add(removeCheck, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Can Update Products:"), gbc);
        gbc.gridx = 1;
        updateCheck = new JCheckBox();
        updateCheck.setSelected(employee.isCanUpdateProducts());
        panel.add(updateCheck, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        saveBtn.addActionListener(e -> {
            // If Employee class has setters, you would update its fields here.
            // For this example, we'll assume you can update the employee's access in place.
            employee = new Employee(
                    employee.getEmployeeId(),
                    employee.getUsername(),
                    employee.getPassword(),
                    employee.getRole(),
                    addCheck.isSelected(),
                    removeCheck.isSelected(),
                    updateCheck.isSelected()
            );
            JOptionPane.showMessageDialog(this, "Employee permissions updated.");
            dispose();
        });
        cancelBtn.addActionListener(e -> dispose());

        add(panel);
    }
}
