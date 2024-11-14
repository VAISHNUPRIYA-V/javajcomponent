package com.canteen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int foodItemId;
    private int quantity;
    private double price;

    public OrderItem(int orderId, int foodItemId, int quantity, double price) {
        this.orderId = orderId;
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.price = price;
    }
    public OrderItem(int orderItemId, int orderId, int foodItemId, int quantity, double price) {
        this.setOrderItemId(orderItemId);
        this.orderId = orderId;
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.price = price;
    }
    public void insertOrderItem() throws SQLException {
        String query = "INSERT INTO OrderItems (order_id, food_item_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, this.orderId);
            stmt.setInt(2, this.foodItemId);
            stmt.setInt(3, this.quantity);
            stmt.setDouble(4, this.price);
            stmt.executeUpdate();
        }
    }

    
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public static List<OrderItem> getOrderItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM OrderItems WHERE order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int orderItemId = rs.getInt("order_item_id");
                int foodItemId = rs.getInt("food_item_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                
                orderItems.add(new OrderItem(orderItemId, orderId, foodItemId, quantity, price));
            }
        }
        
        return orderItems;
    }
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
}
