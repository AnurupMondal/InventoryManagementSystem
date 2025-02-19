package com.example.auth;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class AuthenticationManager {
    private static List<Employee> employees = new ArrayList<>();
    private static final String EMPLOYEE_DATA_FILE = "employees.dat";

    static {
        loadEmployees();
        // Preload with an admin and two employees (passwords in plain text for demonstration only)
        if (employees.isEmpty()) {
            employees.add(new Employee("EMP001", "admin", "admin123", Role.ADMIN, true, true, true));
            employees.add(new Employee("EMP002", "john", "password", Role.EMPLOYEE, true, false, true));
            employees.add(new Employee("EMP003", "jane", "password", Role.EMPLOYEE, false, false, true));
            saveEmployees();
        }
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
                saveEmployees();
                return;
            }
        }
    }

    public static void addEmployee(Employee employee) {
        employees.add(employee);
        saveEmployees();
    }

    private static void saveEmployees() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMPLOYEE_DATA_FILE))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadEmployees() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMPLOYEE_DATA_FILE))) {
            employees = (List<Employee>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
