package com.example.mathquest;

public class MiniLevel {

    private int score;
    private boolean completed;

    public MiniLevel() {
        this.score = 0;
        this.completed = false;
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
