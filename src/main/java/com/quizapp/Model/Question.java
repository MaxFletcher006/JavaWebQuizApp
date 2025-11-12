package com.quizapp.Model;

public abstract class Question implements Comparable<Question> {
	private int id = 0 ; 
	private String question = null ; 
	private String answer = null ; 
	private int point = 0 ; 
	private int order = 1 ; 
	
	public Question(int id, String question, String answer, int point, int order) {
		this.id = id ; 
		this.question = question ; 
		this.answer = answer ; 
		this.point = point ; 
		this.order = order ; 
	}
	
	public void setQuestion(String question) {
		this.question = question ;
	}
	
	public String getQuestion() {
		return this.question ; 
	}
	
	public void setAnswer(String answer) {
		this.answer = answer ; 
	}
	
	public String getAnswer() {
		return this.answer  ; 
	}
	
	public int getId() {
	    return this.id;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public int getPoint() {
	    return this.point;
	}

	public void setPoint(int point) {
	    this.point = point;
	}
	
	public int getOrder() {
		return this.order ; 
	}
	
	public void setOrder(int order) {
		this.order = order ; 
	}
	
	@Override
	public int compareTo(Question other) {
		return Integer.compare(this.order, other.order) ;
	}
}
