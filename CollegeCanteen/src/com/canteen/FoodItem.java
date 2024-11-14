package com.canteen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItem {
    private int item_id;
    private static String name;
    private double price;
    private int stockQuantity;

    public FoodItem(int item_id, String name, double price, String category, int stockQuantity) {
        this.item_id = item_id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    
    public int getItemId() {
        return item_id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    
    public static void addItem(String name, double price, int stockQuantity) throws SQLException {
        String sql = "INSERT INTO FoodItems (name, price, stockQuantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stockQuantity);
            pstmt.executeUpdate();
            System.out.println("Food item added successfully.");
        }
    }

    
    public static void deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM FoodItems WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Food item deleted successfully.");
            } else {
                System.out.println("Food item with ID " + itemId + " not found.");
            }
        }
    }

    
    public static void updateItem(int itemId, String name, double price, int stockQuantity) throws SQLException {
        String sql = "UPDATE FoodItems SET name = ?, price = ?, stockQuantity = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stockQuantity);
            pstmt.setInt(4, itemId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Food item updated successfully.");
            } else {
                System.out.println("Food item with ID " + itemId + " not found.");
            }
        }
    }

    
    public static List<FoodItem> getAllItems() throws SQLException {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM FoodItems";  

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int itemId = rs.getInt("item_id");  
                double price = rs.getDouble("price");
                String category = rs.getString("category");
                int stockQuantity = rs.getInt("stock_quantity");

                FoodItem foodItem = new FoodItem(itemId, name, price, category, stockQuantity);
                foodItems.add(foodItem);
            }
        }

        return foodItems;
    }
}
