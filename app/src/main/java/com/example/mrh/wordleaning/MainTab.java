package com.example.mrh.wordleaning;

import com.example.mrh.wordleaning.ui.fragment.LearnFragment;
import com.example.mrh.wordleaning.ui.fragment.ReviewFragment;
import com.example.mrh.wordleaning.ui.fragment.TestFragment;
import com.example.mrh.wordleaning.ui.fragment.WordFragment;

/**
 * 导航枚举类
 * Created by MR.H on 2016/7/7 0007.
 */
public enum  MainTab {
    LEAN(1, "学习", R.drawable.books_read, LearnFragment.class),
    REVIEW(2, "复习", R.drawable.review, ReviewFragment.class),
    TEST(3, "测试", R.drawable.test, TestFragment.class),
    WORD(4, "生词", R.drawable.new_word, WordFragment.class);

    private int id;
    private String title;
    private int imageID;
    private Class<?> fragment;

    MainTab (int id, String title, int imageID, Class<?> fragment) {
        this.id = id;
        this.title = title;
        this.imageID = imageID;
        this.fragment = fragment;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public int getImageID () {
        return imageID;
    }

    public void setImageID (int imageID) {
        this.imageID = imageID;
    }

    public Class<?> getFragment () {
        return fragment;
    }

    public void setFragment (Class<?> fragment) {
        this.fragment = fragment;
    }
}
