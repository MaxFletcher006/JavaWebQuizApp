package com.quizapp.DAO;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import com.quizapp.Database.QuizDatabase;

public class AnswerDAO {
	
	private Connection conn = null ; 
	
	public AnswerDAO() {
		try {
			conn = QuizDatabase.getConnection() ;
 		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveUserAnswer(String username, int questionId, String answer) {
		String sql = "INSERT INTO answer (user, id, answer) VALUES (?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setInt(2, questionId);
			ps.setString(3, answer);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getUserAnswers(String user, int id) {
		List<String> answers = new ArrayList<>();
		String sql = "SELECT * FROM answer WHERE user = ? AND id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user);
			stmt.setInt(2, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				answers.add(rs.getString("answer"));
			}
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answers;
	}
	
	public void deleteAnswer(String user, int id) {
		String sql = "DELETE FROM answer WHERE user = ? AND id = ?" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user); 
			stmt.setInt(2, id);
			
			stmt.executeUpdate() ;
		}
		
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args) {
		AnswerDAO dao = new AnswerDAO() ;
		List<String> dummy = new ArrayList<>() ;
		
		dummy = dao.getUserAnswers("max", 1) ;
		
		for(String answer: dummy) {
			System.out.println(answer);
		}
	}
}