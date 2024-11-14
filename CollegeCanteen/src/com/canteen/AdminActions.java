package com.canteen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AdminActions {

    
    public static void generateSalesReport() throws SQLException {
        String query = "SELECT order_id, total_price, order_time FROM Orders WHERE order_status = 'completed'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Sales Report:");
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Total Price: $" + rs.getDouble("total_price"));
                System.out.println("Order Time: " + rs.getTimestamp("order_time"));
            }
        }
    }

    
    public static void updateStock(int foodItemId, int newStock) throws SQLException {
        String query = "UPDATE FoodItems SET stock_quantity = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newStock);
            stmt.setInt(2, foodItemId);
            stmt.executeUpdate();
            System.out.println("Stock updated for item ID: " + foodItemId);
        }
    }
    public static void generateInventoryReport() throws SQLException {
        List<Inventory> inventoryList = Inventory.getLowStockItems();
        
        System.out.println("Inventory Report:");
        for (Inventory item : inventoryList) {
            System.out.println("Food Item ID: " + item.getFoodItemId() + ", Stock Level: " + item.getStockLevel());
        }
    }
}

