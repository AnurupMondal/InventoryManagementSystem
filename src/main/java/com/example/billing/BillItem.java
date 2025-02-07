package com.example.billing;

public class BillItem {
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double totalPrice;

    public BillItem(String productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = unitPrice * quantity;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return productName + " (ID: " + productId + ") - " +
                "Qty: " + quantity + ", Unit Price: $" + unitPrice +
                ", Total: $" + totalPrice;
    }
}
