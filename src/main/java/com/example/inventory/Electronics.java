package com.example.inventory;

public class Electronics extends Product {
    private int warrantyMonths;

    public Electronics(String productId, String name, double price, int quantity, int warrantyMonths) {
        super(productId, name, price, quantity);
        this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    @Override
    public void displayProductInfo() {
        System.out.println("Electronics: " + getName() + " [ID: " + getProductId() +
                "] Price: $" + getPrice() + " Qty: " + getQuantity() +
                " Warranty: " + warrantyMonths + " months");
    }
}