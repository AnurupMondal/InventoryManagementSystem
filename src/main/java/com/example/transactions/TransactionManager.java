package com.example.transactions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static List<Transaction> transactions = new ArrayList<>();

    public static void logTransaction(String employeeId, String operation, String details) {
        Transaction t = new Transaction(LocalDateTime.now(), employeeId, operation, details);
        transactions.add(t);
        System.out.println("Transaction logged: " + t);
    }

    public static List<Transaction> getTransactions() {
        return transactions;
    }
}
