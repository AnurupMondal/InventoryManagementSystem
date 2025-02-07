package com.example.billing;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private String customerName;
    private List<BillItem> items;
    private double total;

    public Bill() {
        items = new ArrayList<>();
        total = 0;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void addItem(BillItem item) {
        items.add(item);
    }

    // New method added to allow access to the list of bill items.
    public List<BillItem> getItems() {
        return items;
    }

    public void calculateTotal() {
        total = 0;
        for (BillItem item : items) {
            total += item.getTotalPrice();
        }
    }

    public double getTotal() {
        return total;
    }

    public void printBill() {
        System.out.println("----- Customer Bill -----");
        System.out.println("Customer: " + customerName);
        for (BillItem item : items) {
            System.out.println(item);
        }
        System.out.println("Total Amount: $" + total);
        System.out.println("-------------------------");
    }
}