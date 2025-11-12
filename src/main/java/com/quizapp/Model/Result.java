package com.quizapp.Model;

public class Result {
    private int id = 0;
    private String user = null;
    private int point = 0;

    public Result() {} 
    
    public Result(int id, String user, int point) {
        this.id = id;
        this.user = user;
        this.point = point;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", point=" + point +
                '}';
    }
}
