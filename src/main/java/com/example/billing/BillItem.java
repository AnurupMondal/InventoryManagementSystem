package com.example.billing;

public class BillItem {
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double totalPrice;

    // Constructor
    public BillItem(String productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        recalculateTotal();
    }

    // Helper method to calculate the total price
    private void recalculateTotal() {
        this.totalPrice = this.unitPrice * this.quantity;
    }

    // Getter methods
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Setter for quantity that recalculates the total price
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        recalculateTotal();
    }
}