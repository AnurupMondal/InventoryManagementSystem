package com.example.billing;

import com.example.inventory.Inventory;
import com.example.inventory.Product;

import java.util.Scanner;

public class BillingService {
    /**
     * Interactively creates a bill for a customer.
     * It prompts the counter employee for customer name and then repeatedly for product IDs and quantities.
     * For each valid product purchase, it creates a BillItem and reduces the product's quantity in the inventory.
     * The user can type "exit" as a product ID to finish the bill.
     *
     * @param inventory The inventory from which products are purchased.
     * @return A completed Bill.
     */
    public static Bill createBill(Inventory inventory) {
        Scanner scanner = new Scanner(System.in);
        Bill bill = new Bill();

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        bill.setCustomerName(customerName);

        boolean addMoreItems = true;
        while (addMoreItems) {
            System.out.print("Enter product ID (or type 'exit' to finish): ");
            String productId = scanner.nextLine().trim();
            if (productId.equalsIgnoreCase("exit")) {
                System.out.println("Exiting bill creation.");
                break;
            }

            Product product;
            try {
                product = inventory.searchProductById(productId);
            } catch (Exception e) {
                System.out.println("Product not found! " + e.getMessage());
                System.out.print("Do you want to try again? (yes/no): ");
                String tryAgain = scanner.nextLine().trim();
                if (!tryAgain.equalsIgnoreCase("yes")) {
                    System.out.println("Exiting bill creation.");
                    break;
                } else {
                    continue;
                }
            }

            System.out.print("Enter quantity: ");
            int quantity = 0;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid quantity input. Please try again.");
                continue;
            }

            if (quantity > product.getQuantity()) {
                System.out.println("Not enough stock available. Available: " + product.getQuantity());
                System.out.print("Do you want to try again with a different product? (yes/no): ");
                String tryAgain = scanner.nextLine().trim();
                if (!tryAgain.equalsIgnoreCase("yes")) {
                    System.out.println("Exiting bill creation.");
                    break;
                } else {
                    continue;
                }
            }

            // Create a bill item and add it to the bill
            BillItem item = new BillItem(product.getProductId(), product.getName(), product.getPrice(), quantity);
            bill.addItem(item);

            // Update the inventory: reduce the quantity of the purchased product
            int newQuantity = product.getQuantity() - quantity;
            try {
                inventory.updateProductQuantity(product.getProductId(), newQuantity);
            } catch (Exception e) {
                System.out.println("Error updating inventory: " + e.getMessage());
            }

            System.out.print("Do you want to add another item? (yes/no): ");
            String response = scanner.nextLine().trim();
            if (!response.equalsIgnoreCase("yes")) {
                addMoreItems = false;
            }
        }

        // Calculate total only if items were added
        if (bill.getTotal() == 0 && bill.getItems().isEmpty()) {
            System.out.println("No items added. Bill creation canceled.");
        } else {
            bill.calculateTotal();
            System.out.println("Bill created successfully.");
        }

        return bill;
    }
}