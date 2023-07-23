package com.devjoon.plugin.tokenfilter;

import org.apache.lucene.analysis.compound.hyphenation.CharVector;

import java.util.Enumeration;

public class ModelNameTokenFilter implements Enumeration<CharVector> {
    private final int currentPosition;
    private final int maxPosition;
    private final CharVector charVector;

    public ModelNameTokenFilter(CharVector modelName) {
        this.charVector = modelName;
        this.maxPosition = charVector.length();
        this.currentPosition = 0;
    }

    public ModelNameTokenFilter(String modelName) {
        this(new CharVector(modelName.toCharArray()));
    }

    @Override
    public boolean hasMoreElements() {
        return false;
    }

    @Override
    public CharVector nextElement() {
        return null;
    }
}
