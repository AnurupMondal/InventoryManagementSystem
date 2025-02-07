package com.example.inventory;

public class SampleDataPopulator {

    public static void populateSampleProducts(Inventory inventory) {
        // Add 30 Electronics products
        for (int i = 1; i <= 30; i++) {
            String id = "E" + (100 + i);
            String name = "Electronic Item " + i;
            double price = 100 + i * 10;  // e.g., $110, $120, etc.
            int quantity = 10 + i;        // Increasing quantity
            int warranty = 12 + (i % 12);   // Varying warranty months
            inventory.addProduct(new Electronics(id, name, price, quantity, warranty));
        }

        // Add 35 Grocery products
        for (int i = 1; i <= 35; i++) {
            String id = "G" + (200 + i);
            String name = "Grocery Item " + i;
            double price = 5 + i * 0.5;      // e.g., $5.50, $6.00, etc.
            int quantity = 20 + i;           // Increasing quantity
            String expiryDate = "2025-12-" + ((i % 28) + 1);
            inventory.addProduct(new GroceryItem(id, name, price, quantity, expiryDate));
        }

        // Add 35 Clothing products
        for (int i = 1; i <= 35; i++) {
            String id = "C" + (300 + i);
            String name = "Clothing Item " + i;
            double price = 10 + i * 2;       // e.g., $12, $14, etc.
            int quantity = 50 + i;           // Increasing quantity
            String size = (i % 3 == 0) ? "S" : (i % 3 == 1) ? "M" : "L";
            String material = (i % 2 == 0) ? "Cotton" : "Polyester";
            inventory.addProduct(new Clothing(id, name, price, quantity, size, material));
        }

        System.out.println("Sample data populated successfully.");
    }
}