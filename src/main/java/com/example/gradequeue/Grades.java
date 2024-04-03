package com.example.gradequeue;

public class Grades {
    String name;
    String category;
    int score;

    public Grades (String name, String category, int score) {
        this.name = name;
        this.category = category;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
