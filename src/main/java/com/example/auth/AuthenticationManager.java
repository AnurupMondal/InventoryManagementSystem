package com.example.auth;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationManager {
    private static List<Employee> employees = new ArrayList<>();

    static {
        // Preload with an admin and two employees (passwords in plain text for demonstration only)
        employees.add(new Employee("EMP001", "admin", "admin123", Role.ADMIN, true, true, true));
        employees.add(new Employee("EMP002", "john", "password", Role.EMPLOYEE, true, false, true));
        employees.add(new Employee("EMP003", "jane", "password", Role.EMPLOYEE, false, false, true));
    }

    public static Employee authenticate(String username, String password) {
        for (Employee emp : employees) {
            if (emp.getUsername().equals(username) && emp.getPassword().equals(password)) {
                return emp;
            }
        }
        return null;
    }

    public static List<Employee> getEmployees() {
        return employees;
    }

    // New method to update an existing employee.
    public static void updateEmployee(Employee updatedEmp) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId().equals(updatedEmp.getEmployeeId())) {
                employees.set(i, updatedEmp);
                return;
            }
        }
    }

    public static void addEmployee(Employee employee) {
        employees.add(employee);
    }
}
