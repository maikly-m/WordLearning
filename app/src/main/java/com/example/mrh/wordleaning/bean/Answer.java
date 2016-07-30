package com.example.mrh.wordleaning.bean;

import java.io.Serializable;

/**
 * Created by MR.H on 2016/7/30 0030.
 */
public class Answer implements Serializable{
    private Word word;
    private String answerChar;

    public Answer () {
    }

    public Answer (Word word, String answerChar) {
        this.word = word;
        this.answerChar = answerChar;
    }

    public Word getWord () {
        return word;
    }

    public void setWord (Word word) {
        this.word = word;
    }

    public String getAnswerChar () {
        return answerChar;
    }

    public void setAnswerChar (String answerChar) {
        this.answerChar = answerChar;
    }
}
