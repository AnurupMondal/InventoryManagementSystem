package com.example;

import com.example.inventory.Inventory;
import com.example.inventory.SampleDataPopulator;
import com.example.inventory.Product;
import com.example.inventory.Electronics;  // For adding products manually (if needed)
import com.example.billing.Bill;
import com.example.billing.BillingService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
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
                System.out.println("7. Create Bill");
                System.out.println("8. Exit");
            } else {
                System.out.println("6. Create Bill");
                System.out.println("7. Exit");
            }
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                if (!sampleDataLoaded) {
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
                            // For demonstration, add an Electronics product with a default warranty.
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
                            // Populate sample data and mark as loaded.
                            SampleDataPopulator.populateSampleProducts(inventory);
                            sampleDataLoaded = true;
                            break;
                        case 7:
                            // Create bill option (only available after sample data is loaded).
                            Bill bill = BillingService.createBill(inventory);
                            bill.printBill();
                            break;
                        case 8:
                            exit = true;
                            System.out.println("Exiting Inventory Management System.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    // Menu when sample data has already been loaded.
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
                            // Create bill option.
                            Bill bill = BillingService.createBill(inventory);
                            bill.printBill();
                            break;
                        case 7:
                            exit = true;
                            System.out.println("Exiting Inventory Management System.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}