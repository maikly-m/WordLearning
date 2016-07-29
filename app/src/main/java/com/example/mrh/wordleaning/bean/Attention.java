package com.example.mrh.wordleaning.bean;

/**
 * Created by MR.H on 2016/7/28 0028.
 */
public class Attention {
    private String id;
    private String spelling;
    private String meaning;
    private String phonetic_alphabet;
    private String list;
    private String newword;
    private String reviewword;

    public Attention () {
    }

    public Attention (String reviewword, String newword, String list, String phonetic_alphabet,
                      String meaning, String spelling, String id) {
        this.reviewword = reviewword;
        this.newword = newword;
        this.list = list;
        this.phonetic_alphabet = phonetic_alphabet;
        this.meaning = meaning;
        this.spelling = spelling;
        this.id = id;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getSpelling () {
        return spelling;
    }

    public void setSpelling (String spelling) {
        this.spelling = spelling;
    }

    public String getMeaning () {
        return meaning;
    }

    public void setMeaning (String meaning) {
        this.meaning = meaning;
    }

    public String getPhonetic_alphabet () {
        return phonetic_alphabet;
    }

    public void setPhonetic_alphabet (String phonetic_alphabet) {
        this.phonetic_alphabet = phonetic_alphabet;
    }

    public String getList () {
        return list;
    }

    public void setList (String list) {
        this.list = list;
    }

    public String getNewword () {
        return newword;
    }

    public void setNewword (String newword) {
        this.newword = newword;
    }

    public String getReviewword () {
        return reviewword;
    }

    public void setReviewword (String reviewword) {
        this.reviewword = reviewword;
    }
}
