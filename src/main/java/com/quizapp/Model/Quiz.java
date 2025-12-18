package com.quizapp.Model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.quizapp.DAO.QuizDAO;

public class Quiz {
    private int id = 0;
    private String user = null;
    private String title = null;
    private List<Question> questions = new ArrayList<>() ;
    private int totalPoints = 0 ; 
    private int time;
    
    public Quiz() {}

    public Quiz(String title, String user, int time) {
        //this.id = id;
        this.title = title;
        this.user = user ; 
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    public void addQuestions(Question newQuestion) {
    	this.questions.add(newQuestion) ;
    	Collections.sort(this.questions);
    }
    
    public List<Question> getQuestions() {
    	return this.questions ;
    }
    
    public int calculateScore(List<String> userAnswers) {
        int totalScore = 0;

        int size = Math.min(this.questions.size(), userAnswers.size());

        for (int i = 0; i < size; i++) {
            Question q = this.questions.get(i);
            String userAnswer = userAnswers.get(i);

            if (q.getAnswer().equalsIgnoreCase(userAnswer)) {
                totalScore += q.getPoint();
            }
        }

        this.totalPoints = totalScore;
        return totalScore;
    }
    
    public int getMaxScore() {
        int sum = 0;
        for (Question q : this.questions) {
            sum += q.getPoint();
        }
        return sum;
    }
    
    public List<Integer> getQuestionOrders() {
    	List<Integer> question_orders = new ArrayList<>() ;
    	
    	for(Question questions: this.questions) {
    		question_orders.add(questions.getOrder());
    	}
    	
    	return question_orders ; 
    }
    
    public void printTotalPoints() {
    	System.out.println(this.totalPoints);
    }
    
    public void printQuestions() {
    	for(Question questions: this.questions) {
    		System.out.println(questions.getQuestion()); 
    	}
    }
    
    public void printAllAnswers() {
    	for(Question questions: this.questions) {
    		System.out.println(questions.getAnswer()); 
    	}
    }
       
    public static void main(String[] args) {    	
        QuizDAO dao = new QuizDAO();    	
        Quiz quiz = new Quiz();
        
        dao.fetchQuizDataByTitle(quiz, "Монгол улсын түүх");
        dao.fetchQuestions(quiz, quiz.getId()); 
        
        quiz.printQuestions(); 
        quiz.printAllAnswers(); 
                
        List<String> userAnswers = new ArrayList<>() ;
        userAnswers.add("1206");
        userAnswers.add("Сайн Ноён Хан Намнансүрэн");
        userAnswers.add("1961");
        userAnswers.add("1981");
        
//        for(String answers: userAnswers) {
//        	System.out.println(answers);       
//        }
        
        //System.out.println(quiz.calculateScore(userAnswers));
        
        System.out.println(dao.getMCQQuestionOrder()) ;
        System.out.println(dao.getSAQQuestionOrder()) ;
        
        for(Integer id: quiz.getQuestionOrders()) {
        	System.out.println("id: " + id) ;
        }
        
        quiz.printQuestions(); 
    }
}
