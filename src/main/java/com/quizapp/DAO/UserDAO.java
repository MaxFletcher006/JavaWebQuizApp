package com.quizapp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.quizapp.Database.QuizDatabase; 
import com.quizapp.Model.User;

public class UserDAO {
	
	private Connection conn = null ; 
	
	public UserDAO() {
		try {
			conn = QuizDatabase.getConnection() ;
		}
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public boolean register(User user) {
		
		String sql = "INSERT INTO user (username, password, email, phone_number) VALUES(?,?,?,?)" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, user.getUsername());
				stmt.setString(2, user.getPassword());
				stmt.setString(3, user.getEmail()) ;
				stmt.setString(4, user.getPhone_number()) ;
				stmt.executeUpdate() ;
				return true ; 
		}
		
		catch(SQLException e) {
			e.printStackTrace();
			return false ; 
		}
	}
	
	public boolean login(String username, String password) {
		String sql = "SELECT * FROM user WHERE username = ? AND password = ?" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery() ; 
			
			return rs.next() ; 
		}
		
		catch(SQLException e) {
			e.printStackTrace(); 
			return false ; 
		}
	}
	
	public int resetPassword(String username, String email, String phone_number, String newPassword) {
		String sql = "UPDATE user SET password = ? WHERE username = ? AND email = ? AND phone_number = ?" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, newPassword);
			stmt.setString(2, username);
			stmt.setString(3, email);
			stmt.setString(4, phone_number); 
			
			int rows = stmt.executeUpdate() ; 
			return rows ; 
		}
		
		catch(SQLException e) {
			e.printStackTrace(); 
			return 0 ; 
		}
	}
	
	public boolean checkIfUserExisting(String username, String email, String phone_number) {
		String sql = "SELECT * FROM user WHERE username = ?  AND email = ? AND phone_number = ?" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, email);
			stmt.setString(3, phone_number); 
			ResultSet rs = stmt.executeQuery() ;
			
			return rs.next() ;
		}
		
		catch(SQLException e) {
			e.printStackTrace(); 
			return false ; 
		}
	}
}
