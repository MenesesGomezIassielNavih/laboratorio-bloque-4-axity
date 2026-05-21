package com.axity.dinosaurpark.model;

public class SatisfactionSurvey {

    private final int touristId;
    private final int score; // 1..5
    private final String comment;

    public SatisfactionSurvey(int touristId, int score, String comment) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("score debe estar entre 1 y 5");
        }
        this.touristId = touristId;
        this.score = score;
        this.comment = comment;
    }

    public int getTouristId() { return touristId; }
    public int getScore()     { return score; }
    public String getComment(){ return comment; }
}