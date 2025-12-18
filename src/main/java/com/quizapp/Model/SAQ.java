package com.quizapp.Model;

public class SAQ extends Question {

    public SAQ(int id, String question, String answer, int point, int order) {
        super(id, question, answer, point, order);
    }

    public int getId() {
        return super.getId();
    }

    public String getQuestion() {
        return super.getQuestion();
    }

    public void setQuestion(String question) {
        super.setQuestion(question);
    }

    public String getAnswer() {
        return super.getAnswer();
    }

    public void setAnswer(String answer) {
        super.setAnswer(answer);
    }

    public int getPoint() {
        return super.getPoint();
    }

    public void setPoint(int point) {
        super.setPoint(point);
    }
    
    @Override
    public String toString() {
        return "Question: " + getQuestion() + "\n"
             + "Answer: " + getAnswer() + "\n"
             + "Points: " + getPoint();
    }
}
