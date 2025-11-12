package com.quizapp.Database;
import java.sql.*;

public class QuizDatabase {
	
	private static final String URL = "jdbc:sqlite:/home/supermax/eclipse-workspace/quizapp/quiz.db" ;

	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC") ;
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace(); 
		}
		
		return DriverManager.getConnection(URL) ;
 	}
	
	/*
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn = getConnection() ;
		Statement stmt = conn.createStatement() ;
		ResultSet rs = stmt.executeQuery("SELECT * FROM sa_questions") ;
		
		while(rs.next()) {
			System.out.println(rs.getString("question") + " " + rs.getString("answer") + " " + rs.getInt("point"));
		}
  
	}
	*/

}
