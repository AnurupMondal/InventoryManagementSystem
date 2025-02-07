package com.example.inventory;

public class GroceryItem extends Product {
    private String expiryDate;

    public GroceryItem(String productId, String name, double price, int quantity, String expiryDate) {
        super(productId, name, price, quantity);
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    @Override
    public void displayProductInfo() {
        System.out.println("Grocery: " + getName() + " [ID: " + getProductId() +
                "] Price: $" + getPrice() + " Qty: " + getQuantity() +
                " Expiry: " + expiryDate);
    }
}