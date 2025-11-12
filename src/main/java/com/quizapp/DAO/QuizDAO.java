package com.quizapp.DAO;

import java.sql.* ;
import java.util.ArrayList;
import java.util.List;

import com.quizapp.Database.QuizDatabase;
import com.quizapp.Model.*;

public class QuizDAO {
	
	private Connection conn = null ; 
	
	public QuizDAO(){
		try {
			this.conn = QuizDatabase.getConnection() ;
		}
		
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public int createQuiz(Quiz quiz) {
	    String sql = "INSERT INTO quiz (title, user, time) VALUES (?, ?, ?)";

	    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	        stmt.setString(1, quiz.getTitle());
	        stmt.setString(2, quiz.getUser());
	        stmt.setInt(3, quiz.getTime());

	        int rowsAffected = stmt.executeUpdate();

	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                int newId = rs.getInt(1);
	                quiz.setId(newId);
	            }
	        }

	        return rowsAffected;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	
	public int addMCQ(Quiz quiz, MCQ multiple_choice_question) {
	    // id талбарыг quiz_id болгож өөрчилсөн
	    String sql = "INSERT INTO mc_questions (id, question, choice_1, choice_2, choice_3, choice_4, answer, point, `order`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, quiz.getId());
	        stmt.setString(2, multiple_choice_question.getQuestion());
	        stmt.setString(3, multiple_choice_question.getChoice_1());
	        stmt.setString(4, multiple_choice_question.getChoice_2());
	        stmt.setString(5, multiple_choice_question.getChoice_3());
	        stmt.setString(6, multiple_choice_question.getChoice_4());
	        stmt.setString(7, multiple_choice_question.getAnswer());
	        stmt.setInt(8, multiple_choice_question.getPoint());
	        stmt.setInt(9, multiple_choice_question.getOrder()); // order нэмсэн
	        
	        return stmt.executeUpdate();     
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	public int addSAQ(Quiz quiz, SAQ short_answer_question) {
	    // order талбарыг нэмсэн
	    String sql = "INSERT INTO sa_questions (id, question, answer, point, `order`) VALUES (?,?,?,?,?)" ;
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, quiz.getId());
	        stmt.setString(2, short_answer_question.getQuestion());
	        stmt.setString(3, short_answer_question.getAnswer());
	        stmt.setInt(4, short_answer_question.getPoint());
	        stmt.setInt(5, short_answer_question.getOrder()); // order нэмсэн
	        
	        return stmt.executeUpdate();
	    }
	    catch(SQLException e) {
	        e.printStackTrace(); 
	        return 0; 
	    }
	}
	
	public int editMCQ(MCQ multiple_choice_question) {
	    String sql = "UPDATE mc_questions SET question = ?, choice_1 = ?, choice_2 = ?, choice_3 = ?, choice_4 = ?, answer = ?, point = ? WHERE id = ?";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, multiple_choice_question.getQuestion());
	        stmt.setString(2, multiple_choice_question.getChoice_1());
	        stmt.setString(3, multiple_choice_question.getChoice_2());
	        stmt.setString(4, multiple_choice_question.getChoice_3());
	        stmt.setString(5, multiple_choice_question.getChoice_4());
	        stmt.setString(6, multiple_choice_question.getAnswer());
	        stmt.setInt(7, multiple_choice_question.getPoint());
	        stmt.setInt(8, multiple_choice_question.getID()); 

	        return stmt.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	public int editSAQ(SAQ short_answer_question) {
	    String sql = "UPDATE sa_questions SET question = ?, answer = ?, point = ? WHERE id = ?";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, short_answer_question.getQuestion());
	        stmt.setString(2, short_answer_question.getAnswer());
	        stmt.setInt(3, short_answer_question.getPoint());
	        stmt.setInt(4, short_answer_question.getId());

	        return stmt.executeUpdate();

	    } catch(SQLException e) {
	        e.printStackTrace(); 
	        return 0;
	    }
	}
	
	public List<Quiz> fetchQuizData() {
		
		List<Quiz> quizzes = new ArrayList<>() ; 
		
		String sql = "SELECT title, user, time FROM quiz" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			 
			ResultSet rs = stmt.executeQuery() ;
			
			while(rs.next()) {
				
				System.out.println(rs.getString("title") + " " + rs.getString("user") + " " + rs.getInt("time")) ;
				
				quizzes.add(new Quiz(rs.getString("title"), rs.getString("user"), rs.getInt("time"))) ;
			}
			
			return quizzes ; 
		}
		
		catch(SQLException e) {
			e.printStackTrace();
			return quizzes ; 
		}
	}
	
	public void fetchQuizDataByTitle(Quiz quiz, String title) {
		String sql = "SELECT * FROM quiz WHERE title = ? " ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, title);
			
			ResultSet rs = stmt.executeQuery() ;
			
			while(rs.next()) {
				quiz.setId(rs.getInt("id"));
				quiz.setTitle(rs.getString("title"));
				quiz.setUser(rs.getString("user")); 
				quiz.setTime(rs.getInt("time"));
			}
 		}
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public void fetchQuestions(Quiz quiz, int quizId) {
	    String saqQuery = "SELECT id, question, answer, point, `order` FROM sa_questions WHERE id = ?";
	    String mcqQuery = "SELECT id, question, choice_1, choice_2, choice_3, choice_4, answer, point, `order` FROM mc_questions WHERE id = ?";

	    try (PreparedStatement saqStmt = conn.prepareStatement(saqQuery);
	         PreparedStatement mcqStmt = conn.prepareStatement(mcqQuery)) {

	        saqStmt.setInt(1, quizId);
	        mcqStmt.setInt(1, quizId);

	        // SAQ асуултуудыг fetch хийх
	        try (ResultSet saqResult = saqStmt.executeQuery()) {
	            while (saqResult.next()) {
	                SAQ saq = new SAQ(
	                    saqResult.getInt("id"),
	                    saqResult.getString("question"),
	                    saqResult.getString("answer"),
	                    saqResult.getInt("point"),
	                    saqResult.getInt("order")
	                );
	                quiz.addQuestions(saq);

	                // DEBUG хэвлэлт
//	                System.out.println("=== SAQ CREATED ===");
//	                System.out.println("ID: " + saq.getId());
//	                System.out.println("Question: " + saq.getQuestion());
//	                System.out.println("Answer: " + saq.getAnswer());
//	                System.out.println("Points: " + saq.getPoint());
//	                System.out.println("Order: " + saq.getOrder());
//	                System.out.println();
	            }
	        }

	        // MCQ асуултуудыг fetch хийх
	        try (ResultSet mcqResult = mcqStmt.executeQuery()) {
	            while (mcqResult.next()) {
	                MCQ mcq = new MCQ(
	                    mcqResult.getInt("id"),
	                    mcqResult.getString("question"),
	                    mcqResult.getString("choice_1"),
	                    mcqResult.getString("choice_2"),
	                    mcqResult.getString("choice_3"),
	                    mcqResult.getString("choice_4"),
	                    mcqResult.getString("answer"),
	                    mcqResult.getInt("point"),
	                    mcqResult.getInt("order")
	                );
	                quiz.addQuestions(mcq);

	                // DEBUG хэвлэлт
//	                System.out.println("=== MCQ CREATED ===");
//	                System.out.println("ID: " + mcq.getId());
//	                System.out.println("Question: " + mcq.getQuestion());
//	                System.out.println("Choices: " + mcq.getChoice_1() + ", " + mcq.getChoice_2() 
//	                                   + ", " + mcq.getChoice_3() + ", " + mcq.getChoice_4());
//	                System.out.println("Answer: " + mcq.getAnswer());
//	                System.out.println("Points: " + mcq.getPoint());
//	                System.out.println("Order: " + mcq.getOrder());
//	                System.out.println();
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void deleteMCQ(Quiz quiz, int id) {
		String sql = "DELETE FROM mc_questions WHERE id = ?" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, quiz.getId()) ;
			
			stmt.executeUpdate() ;
 			
		}
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public void deleteSAQ(Quiz quiz, int id) {
		String sql = "DELETE FROM sa_questions WHERE id = ?" ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, quiz.getId()) ;
			
			stmt.executeUpdate() ;
 			
		}
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public int deleteQuiz(Quiz quiz) {
	    String deleteMCQ = "DELETE FROM mc_questions WHERE id = ?";
	    String deleteSAQ = "DELETE FROM sa_questions WHERE id = ?";
	    String deleteAnswer = "DELETE FROM answer WHERE id = ?";
	    String deleteResult = "DELETE FROM result WHERE id = ?";
	    String deleteQuiz = "DELETE FROM quiz WHERE id = ?";
	    
	    try {
	    	conn.setAutoCommit(false);

	    	try (PreparedStatement stmtMCQ = conn.prepareStatement(deleteMCQ);
	             PreparedStatement stmtSAQ = conn.prepareStatement(deleteSAQ);
	             PreparedStatement stmtAnswer = conn.prepareStatement(deleteAnswer);
	             PreparedStatement stmtResult = conn.prepareStatement(deleteResult);
	             PreparedStatement stmtQuiz = conn.prepareStatement(deleteQuiz)) {

	            // MCQ устгах
	            stmtMCQ.setInt(1, quiz.getId());
	            stmtMCQ.executeUpdate();

	            // SAQ устгах
	            stmtSAQ.setInt(1, quiz.getId());
	            stmtSAQ.executeUpdate();

	            // Answer устгах
	            stmtAnswer.setInt(1, quiz.getId());
	            stmtAnswer.executeUpdate();

	            // Result устгах
	            stmtResult.setInt(1, quiz.getId());
	            stmtResult.executeUpdate();

	            // Quiz устгах
	            stmtQuiz.setInt(1, quiz.getId());
	            int rowsAffected = stmtQuiz.executeUpdate();

	            conn.commit();
	            return rowsAffected;  
	    	} 
	    }
	    
	    catch (SQLException e) {
	        e.printStackTrace();
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return 0;

	    } 
	    
	    finally {
	        if (conn != null) {
	            try {
	                conn.setAutoCommit(true);
	                conn.close();
	            } 
	    catch (SQLException e) {
	                e.printStackTrace();
	    		}
	        }
	    }
	}
	
	public List<String> fetchQuizTitle() {
		
		String sql = "SELECT title FROM quiz" ;
		
		List<String> quizTitles = new ArrayList<>() ;
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			ResultSet rs = stmt.executeQuery() ;
 			
			while(rs.next()) {
				quizTitles.add(rs.getString("title")) ;
			}
 					
			return quizTitles ;
		}
		catch(SQLException e) {
			e.printStackTrace(); 
			return quizTitles ; 
		}
	}
		
	public static void main(String[] args) {
		List<Quiz> quizzes ;
		
		QuizDAO dao = new QuizDAO() ;
		
		quizzes = dao.fetchQuizData() ;
		
		for(Quiz quiz: quizzes) {
			System.out.println(quiz.getTitle() + " " + quiz.getUser() + " " + quiz.getTime()); 
		}
		
		Quiz dummyQuiz = new Quiz() ;
		
		dao.fetchQuizDataByTitle(dummyQuiz, "Монгол улсын түүх");
		dao.fetchQuestions(dummyQuiz, dummyQuiz.getId()); 
	}
}
