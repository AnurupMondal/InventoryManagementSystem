package com.example.auth;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationManager {
    private static List<Employee> employees = new ArrayList<>();

    // Pre-load with one admin and a couple of employees
    static {
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

    public static void addEmployee(Employee employee) {
        employees.add(employee);
        // Optionally, log this operation via TransactionManager
    }

    public static List<Employee> getEmployees() {
        return employees;
    }
}