package com.devjoon.plugin.dto;

public class ScoreCharToken extends Token {

    private final double score;

    public ScoreCharToken(String term, double score) {
        super(term);
        this.score = score;
    }

    public double getScore() {
        return score;
    }
}
