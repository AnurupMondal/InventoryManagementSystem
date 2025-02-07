package com.example.inventory;

import java.util.Scanner;

public class InventoryManagementSystem {

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Flag to ensure the sample data option is offered only once.
        boolean sampleDataLoaded = false;

        while (!exit) {
            // Display the menu. Show the sample data option only if it hasn't been loaded.
            System.out.println("\nInventory Management System Menu:");
            System.out.println("1. Display Inventory");
            System.out.println("2. Add Product");
            System.out.println("3. Remove Product");
            System.out.println("4. Update Product Quantity");
            System.out.println("5. Search Product");
            if (!sampleDataLoaded) {
                System.out.println("6. Populate Sample Data");
                System.out.println("7. Exit");
            } else {
                System.out.println("6. Exit");
            }
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                if (!sampleDataLoaded) {
                    // When sample data has not been loaded, the valid choices are 1-7.
                    switch (choice) {
                        case 1:
                            inventory.displayInventory();
                            break;
                        case 2:
                            // For simplicity, add a new Electronics product.
                            System.out.print("Enter product ID: ");
                            String productId = scanner.nextLine();
                            System.out.print("Enter product name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter price: ");
                            double price = scanner.nextDouble();
                            System.out.print("Enter quantity: ");
                            int quantity = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            // Assume product is Electronics with a default warranty of 12 months.
                            inventory.addProduct(new Electronics(productId, name, price, quantity, 12));
                            break;
                        case 3:
                            System.out.print("Enter product ID to remove: ");
                            String removeId = scanner.nextLine();
                            inventory.removeProduct(removeId);
                            break;
                        case 4:
                            System.out.print("Enter product ID to update: ");
                            String updateId = scanner.nextLine();
                            System.out.print("Enter new quantity: ");
                            int newQuantity = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            inventory.updateProductQuantity(updateId, newQuantity);
                            break;
                        case 5:
                            System.out.print("Enter product name to search: ");
                            String searchName = scanner.nextLine();
                            Product product = inventory.searchProduct(searchName);
                            product.displayProductInfo();
                            break;
                        case 6:
                            // Populate sample data.
                            SampleDataPopulator.populateSampleProducts(inventory);
                            sampleDataLoaded = true;
                            break;
                        case 7:
                            exit = true;
                            System.out.println("Exiting Inventory Management System.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    // When sample data has already been loaded, valid choices are 1-6.
                    switch (choice) {
                        case 1:
                            inventory.displayInventory();
                            break;
                        case 2:
                            System.out.print("Enter product ID: ");
                            String productId = scanner.nextLine();
                            System.out.print("Enter product name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter price: ");
                            double price = scanner.nextDouble();
                            System.out.print("Enter quantity: ");
                            int quantity = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            inventory.addProduct(new Electronics(productId, name, price, quantity, 12));
                            break;
                        case 3:
                            System.out.print("Enter product ID to remove: ");
                            String removeId = scanner.nextLine();
                            inventory.removeProduct(removeId);
                            break;
                        case 4:
                            System.out.print("Enter product ID to update: ");
                            String updateId = scanner.nextLine();
                            System.out.print("Enter new quantity: ");
                            int newQuantity = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            inventory.updateProductQuantity(updateId, newQuantity);
                            break;
                        case 5:
                            System.out.print("Enter product name to search: ");
                            String searchName = scanner.nextLine();
                            Product product = inventory.searchProduct(searchName);
                            product.displayProductInfo();
                            break;
                        case 6:
                            exit = true;
                            System.out.println("Exiting Inventory Management System.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (Exception e) {
                // Simple exception handling: print the error message and continue.
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}