package com.example.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory {
    private List<Product> products;

    public Inventory() {
        products = new ArrayList<>();
    }

    // Add a new product
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product.getName());
    }

    // Remove a product by productId
    public void removeProduct(String productId) throws Exception {
        Optional<Product> productToRemove = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
        if (productToRemove.isPresent()) {
            products.remove(productToRemove.get());
            System.out.println("Product removed: " + productToRemove.get().getName());
        } else {
            throw new Exception("Product with ID " + productId + " not found.");
        }
    }

    // Update product quantity
    public void updateProductQuantity(String productId, int newQuantity) throws Exception {
        Optional<Product> productToUpdate = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
        if (productToUpdate.isPresent()) {
            productToUpdate.get().setQuantity(newQuantity);
            System.out.println("Updated quantity for: " + productToUpdate.get().getName());
        } else {
            throw new Exception("Product with ID " + productId + " not found.");
        }
    }

    // Display all products in the inventory
    public void displayInventory() {
        System.out.println("Inventory List:");
        products.forEach(Product::displayProductInfo);
    }

    // Search for a product by name (case-insensitive)
    public Product searchProduct(String name) throws Exception {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new Exception("Product with name " + name + " not found."));
    }
}