package com.canteen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CanteenManagementApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nWelcome to the College Canteen Management System!");
            System.out.println("1. Login as User (Student/Staff)");
            System.out.println("2. Login as Admin");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    userMenu(scanner);
                    break;
                case 2:
                    adminMenu(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the system.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
    private static void userMenu(Scanner scanner) {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        try {
            User user = User.getUserByEmail(email);
            if (user == null) {
                System.out.println("User not found!");
                return;
            }

            while (true) {
                System.out.println("\n--- User Menu ---");
                System.out.println("1. Browse Food Items");
                System.out.println("2. Place Order");
                System.out.println("3. View Order History");
                System.out.println("4. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  

                switch (choice) {
                    case 1:
                        browseFoodItems();
                        break;
                    case 2:
                        placeOrder(scanner, user);
                        break;
                    case 3:
                        viewOrderHistory(user);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Manage Food Items");
            System.out.println("2. Inventory Tracking");
            System.out.println("3. View Orders");
            System.out.println("4. Generate Reports");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    manageFoodItems(scanner);
                    break;
                case 2:
                    manageInventory(scanner);
                    break;
                case 3:
                    viewAllOrders();
                    break;
                case 4:
                    generateReports();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void browseFoodItems() {
        try {
            List<FoodItem> items = FoodItem.getAllItems();
            System.out.println("Available Food Items:");
            for (FoodItem item : items) {
                System.out.println("ID: " + item.getItemId() + ", Name: " + item.getName() + ", Price: $" + item.getPrice());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void placeOrder(Scanner scanner, User user) {
        try {
            List<FoodItem> items = FoodItem.getAllItems();
            List<OrderItem> orderItems = new ArrayList<>();

            System.out.println("Select food items to order (enter item ID and quantity, -1 to finish):");
            while (true) {
                System.out.print("Enter Item ID: ");
                int itemId = scanner.nextInt();
                if (itemId == -1) break;
                
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();

                FoodItem item = items.stream().filter(f -> f.getItemId() == itemId).findFirst().orElse(null);
                if (item == null || item.getStockQuantity() < quantity) {
                    System.out.println("Item not found or insufficient stock. Try again.");
                } else {
                    orderItems.add(new OrderItem(0, itemId, quantity, item.getPrice()));
                    Inventory.decreaseStock(itemId, quantity);
                }
            }
            
            Order.placeOrder(user.getUserId(), orderItems);
            System.out.println("Order placed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewOrderHistory(User user) {
        try {
            List<Order> orders = Order.getUserOrders(user.getUserId());
            if (orders.isEmpty()) {
                System.out.println("No order history found.");
            } else {
                System.out.println("Order History:");
                for (Order order : orders) {
                    System.out.println("Order ID: " + order.getOrderId() + ", Status: " + order.getOrderStatus() + ", Total: $" + order.getTotalPrice());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void manageFoodItems(Scanner scanner) {
        System.out.println("Manage Food Items:");
        System.out.println("1. Add Food Item");
        System.out.println("2. Update Food Item");
        System.out.println("3. Delete Food Item");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1:
                	System.out.print("Enter food item id: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter food item name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter stock quantity: ");
                    int quantity = scanner.nextInt();
                    FoodItem.addItem(  name, price, quantity);
                    System.out.println("Food item added successfully.");
                    break;
                case 2:
                    System.out.print("Enter food item ID to update: ");
                    int itemId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    System.out.print("Enter new stock quantity: ");
                    int newQuantity = scanner.nextInt();
                    FoodItem.updateItem(itemId, newName, newPrice, newQuantity);
                    System.out.println("Food item updated successfully.");
                    break;
                case 3:
                    System.out.print("Enter food item ID to delete: ");
                    int deleteId = scanner.nextInt();
                    FoodItem.deleteItem(deleteId);
                    System.out.println("Food item deleted successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void manageInventory(Scanner scanner) {
        try {
            List<Inventory> lowStockItems = Inventory.getLowStockItems();
            System.out.println("Low Stock Items:");
            for (Inventory item : lowStockItems) {
                System.out.println("Item ID: " + item.getFoodItemId() + ", Stock Level: " + item.getStockLevel());
            }

            System.out.print("Enter item ID to restock: ");
            int itemId = scanner.nextInt();
            System.out.print("Enter quantity to add: ");
            int quantity = scanner.nextInt();

            Inventory.restockItem(itemId, quantity);
            System.out.println("Item restocked successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllOrders() {
        try {
            List<Order> orders = Order.getAllOrders();
            System.out.println("All Orders:");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateReports() {
        try {
            System.out.println("Generating sales report...");
            AdminActions.generateSalesReport();

            System.out.println("Generating inventory report...");
            AdminActions.generateInventoryReport();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
