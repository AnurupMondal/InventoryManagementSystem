package com.example.transactions;

import java.time.LocalDateTime;

public class Transaction {
    private LocalDateTime timestamp;
    private String employeeId;
    private String operation;
    private String details;

    public Transaction(LocalDateTime timestamp, String employeeId, String operation, String details) {
        this.timestamp = timestamp;
        this.employeeId = employeeId;
        this.operation = operation;
        this.details = details;
    }

    // Added getters:
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getOperation() {
        return operation;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] (" + employeeId + ") " + operation + " - " + details;
    }
}
