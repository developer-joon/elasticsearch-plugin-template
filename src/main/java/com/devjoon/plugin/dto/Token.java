package com.devjoon.plugin.dto;

public class Token {
    private final String term;

    public Token(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return term;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof String && term.equals(obj);
    }
}
