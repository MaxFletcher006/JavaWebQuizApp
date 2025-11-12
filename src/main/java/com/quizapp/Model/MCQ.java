package com.quizapp.Model;

public class MCQ extends Question{
	private String choice_1 ; 
	private String choice_2 ; 
	private String choice_3 ; 
	private String choice_4 ; 
	
	public MCQ(int id, String question, String ch1, String ch2, String ch3, String ch4, String answer, int point, int order) {
		super(id,question,answer,point, order) ;
		this.choice_1 = ch1 ; 
		this.choice_2 = ch2 ; 
		this.choice_3 = ch3 ; 
		this.choice_4 = ch4 ; 
	}
	
	public String getChoice_1() {
	    return this.choice_1;
	}

	public void setChoice_1(String choice_1) {
	    this.choice_1 = choice_1;
	}

	public String getChoice_2() {
	    return this.choice_2;
	}

	public void setChoice_2(String choice_2) {
	    this.choice_2 = choice_2;
	}

	public String getChoice_3() {
	    return this.choice_3;
	}

	public void setChoice_3(String choice_3) {
	    this.choice_3 = choice_3;
	}

	public String getChoice_4() {
	    return this.choice_4;
	}

	public void setChoice_4(String choice_4) {
	    this.choice_4 = choice_4;
	}	
	
	 public String getQuestion() {
	    return super.getQuestion();
	 }

	 public String getAnswer() {
	    return super.getAnswer();
	 }

	 public int getPoint() {
	    return super.getPoint();
	 }
	 
	 public int getID() {
		 return super.getId() ;
 	 }
	 
	 public int getOrder() {
		 return super.getOrder() ;
	 }
	 
	 @Override
	 public String toString() {
	     return "Question: " + getQuestion() + "\n"
	          + "A) " + choice_1 + "\n"
	          + "B) " + choice_2 + "\n"
	          + "C) " + choice_3 + "\n"
	          + "D) " + choice_4 + "\n"
	          + "Answer: " + getAnswer() + "\n"
	          + "Points: " + getPoint();
	 }
}
