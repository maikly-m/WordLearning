package com.example.mrh.wordleaning.bean;

import java.io.Serializable;

public class WordList implements Serializable{

    private String learnedCount;
    private String count;
    private String bookID;
    private String list;
    private String learned;
    private String learnedTime;
    private String review_times;
    private String reviewTime;
    private String bestScore;
    private String shouldReview;

    public WordList () {

    }

    public WordList (String bookID, String list, String learned, String learnedTime,
                     String review_times, String reviewTime, String bestScore, String
                             shouldReview, String count, String learnedCount) {
        this.bookID = bookID;
        this.list = list;
        this.learned = learned;
        this.learnedTime = learnedTime;
        this.review_times = review_times;
        this.reviewTime = reviewTime;
        this.bestScore = bestScore;
        this.shouldReview = shouldReview;
        this.count = count;
        this.learnedCount = learnedCount;
    }

    public void setBookID (String bookID) {
        this.bookID = bookID;
    }

    public String getBookID () {
        return bookID;
    }

    public void setList (String list) {
        this.list = list;
    }

    public String getList () {
        return list;
    }

    public void setLearned (String learned) {
        this.learned = learned;
    }

    public String getLearned () {
        return learned;
    }

    public String getLearnedTime () {
        return learnedTime;
    }

    public void setLearnedTime (String learnedTime) {
        this.learnedTime = learnedTime;
    }

    public String getReview_times () {
        return review_times;
    }

    public void setReview_times (String reviewTimes) {
        review_times = reviewTimes;
    }

    public String getReviewTime () {
        return reviewTime;
    }

    public void setReviewTime (String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getBestScore () {
        return bestScore;
    }

    public void setBestScore (String bestScore) {
        this.bestScore = bestScore;
    }

    public String getShouldReview () {
        return shouldReview;
    }

    public void setShouldReview (String shouldReview) {
        this.shouldReview = shouldReview;
    }

    public String getCount () {
        return count;
    }

    public void setCount (String conut) {
        this.count = conut;
    }
    public String getLearnedCount () {
        return learnedCount;
    }

    public void setLearnedCount (String learnedCount) {
        this.learnedCount = learnedCount;
    }
}