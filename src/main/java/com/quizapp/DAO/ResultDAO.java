package com.quizapp.DAO;

import com.quizapp.Database.QuizDatabase;
import com.quizapp.Model.Result; 
import java.sql.*;
import java.util.*;

public class ResultDAO {
	private Connection conn = null ; 
	
	public ResultDAO() {
		try {
			conn = QuizDatabase.getConnection() ;
		}
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public int isResultExist(int id, String user) {
	    int result = 0; 
	    
	    String sql = "SELECT COUNT(*) FROM result WHERE id = ? AND user = ?";
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setInt(1, id);
	        stmt.setString(2, user);
	        
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            int count = rs.getInt(1);
	            if (count > 0) {
	                result = 1; 
	            }
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return result;
	}
	
	public void saveResult(int id, String user, int point) {
		String sql = "INSERT INTO result (id, user, point) VALUES(?,?,?)" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id); 
			stmt.setString(2, user);
			stmt.setInt(3, point);
			
			stmt.executeUpdate() ;
		}
		catch(SQLException e) {
			e.printStackTrace() ; 
		}
	}
	
	public Result getResult(String user, int id) {
		String sql = "SELECT * FROM result WHERE id = ? AND user = ?" ;
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.setString(2, user);
			
			ResultSet rs = stmt.executeQuery() ;
			
			return new Result(rs.getInt("id"), rs.getString("user"), rs.getInt("point")) ;
		}
		catch(SQLException e) {
			e.printStackTrace(); 
			return new Result() ;
		}
	}
	
	public void deleteResult(int id, String user) {
		String sql = "DELETE FROM result WHERE id = ?  AND user = ? " ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.setString(2, user);
			stmt.executeUpdate() ;
 		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ResultDAO dao = new ResultDAO() ;
		System.out.println(dao.isResultExist(1, "max"));
	}
}