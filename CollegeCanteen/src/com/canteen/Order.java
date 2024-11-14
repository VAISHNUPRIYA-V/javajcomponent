package com.canteen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private double totalPrice;
    private String orderStatus;
	private List<OrderItem> orderItems;

    public Order(int orderId, int userId, double totalPrice, String orderStatus) {
        this.setOrderId(orderId);
        this.setUserId(userId);
        this.setTotalPrice(totalPrice);
        this.setOrderStatus(orderStatus);
    }

    public Order(int orderId, int userId, List<OrderItem> orderItems, double totalPrice, String orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

	public static void placeOrder(int userId, List<OrderItem> items) throws SQLException {
        double totalPrice = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        String insertOrderQuery = "INSERT INTO Orders (user_id, total_price, order_status) VALUES (?, ?, 'pending')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {

            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, totalPrice);
            orderStmt.executeUpdate();

            ResultSet rs = orderStmt.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                for (OrderItem item : items) {
                    item.setOrderId(orderId);
                    item.insertOrderItem();
                }
            }
        }
    }

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public static List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String orderStatus = rs.getString("order_status");
                orders.add(new Order(orderId, userId, new ArrayList<>(), totalPrice, orderStatus));
            }
        }
        return orders;
    }
	public static List<Order> getUserOrders(int userId) throws SQLException {
        List<Order> userOrders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                double totalPrice = rs.getDouble("total_price");
                String orderStatus = rs.getString("order_status");
                
                
                List<OrderItem> orderItems = OrderItem.getOrderItemsByOrderId(orderId);
                
                Order order = new Order(orderId, userId, orderItems, totalPrice, orderStatus);
                userOrders.add(order);
            }
        }
        
        return userOrders;
    }
    
}
