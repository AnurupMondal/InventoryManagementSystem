package com.example.inventory;

public class Clothing extends Product {
    private String size;
    private String material;

    public Clothing(String productId, String name, double price, int quantity, String size, String material) {
        super(productId, name, price, quantity);
        this.size = size;
        this.material = material;
    }

    public String getSize() { return size; }
    public String getMaterial() { return material; }

    public void setSize(String size) { this.size = size; }
    public void setMaterial(String material) { this.material = material; }

    @Override
    public void displayProductInfo() {
        System.out.println("Clothing: " + getName() + " [ID: " + getProductId() +
                "] Price: $" + getPrice() + " Qty: " + getQuantity() +
                " Size: " + size + " Material: " + material);
    }
}