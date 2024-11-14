package com.canteen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String name;
    private String role;
    private String email;
    private String password;

    public User(int userId, String name, String role, String email, String password) {
        this.setUserId(userId);
        this.setName(name);
        this.setRole(role);
        this.setEmail(email);
        this.setPassword(password);
    }

    
    public static User getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM Users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        }
        return null; 
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
