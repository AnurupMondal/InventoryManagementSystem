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

    // Add the getter for customerName
    public String getCustomerName() {
        return customerName;
    }

    public void addItem(BillItem item) {
        items.add(item);
    }

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