package com.canteen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int foodItemId;
    private int stockLevel;
    private int restockLevel;

    public Inventory(int foodItemId, int stockLevel, int restockLevel) {
        this.setFoodItemId(foodItemId);
        this.setStockLevel(stockLevel);
        this.setRestockLevel(restockLevel);
    }

   
    public static List<Inventory> getInventory() throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT * FROM Inventory";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                inventoryList.add(new Inventory(
                    rs.getInt("food_item_id"),
                    rs.getInt("stock_level"),
                    rs.getInt("restock_level")
                ));
            }
        }
        return inventoryList;
    }

  
    public static void decreaseStock(int foodItemId, int quantity) throws SQLException {
        String query = "UPDATE Inventory SET stock_level = stock_level - ? WHERE food_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, foodItemId);
            stmt.executeUpdate();
        }
    }

    
    public static List<Inventory> getLowStockItems() throws SQLException {
        List<Inventory> lowStockItems = new ArrayList<>();
        String query = "SELECT * FROM Inventory WHERE stock_level < restock_level";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lowStockItems.add(new Inventory(
                    rs.getInt("food_item_id"),
                    rs.getInt("stock_level"),
                    rs.getInt("restock_level")
                ));
            }
        }
        return lowStockItems;
    }

    
    public static void restockItem(int foodItemId, int quantity) throws SQLException {
        String query = "UPDATE Inventory SET stock_level = stock_level + ? WHERE food_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, foodItemId);
            stmt.executeUpdate();
            System.out.println("Item with ID " + foodItemId + " restocked by " + quantity + " units.");
        }
    }

	public int getFoodItemId() {
		return foodItemId;
	}

	public void setFoodItemId(int foodItemId) {
		this.foodItemId = foodItemId;
	}

	public int getStockLevel() {
		return stockLevel;
	}

	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}

	public int getRestockLevel() {
		return restockLevel;
	}

	public void setRestockLevel(int restockLevel) {
		this.restockLevel = restockLevel;
	}
}

