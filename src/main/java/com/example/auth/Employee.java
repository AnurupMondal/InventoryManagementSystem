package com.example.auth;

public class Employee {
    private final String employeeId;
    private final String username;
    private String password; // (Plain text for demonstration only. In production, hash the password.)
    private Role role;
    private boolean canAddProducts;
    private boolean canRemoveProducts;
    private boolean canUpdateProducts;

    public Employee(String employeeId, String username, String password, Role role,
                    boolean canAddProducts, boolean canRemoveProducts, boolean canUpdateProducts) {
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.canAddProducts = canAddProducts;
        this.canRemoveProducts = canRemoveProducts;
        this.canUpdateProducts = canUpdateProducts;
    }

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public boolean isCanAddProducts() { return canAddProducts; }
    public boolean isCanRemoveProducts() { return canRemoveProducts; }
    public boolean isCanUpdateProducts() { return canUpdateProducts; }
}
