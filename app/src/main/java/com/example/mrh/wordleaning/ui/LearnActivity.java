package com.example.mrh.wordleaning.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.wordleaning.ActivityManager;
import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.bean.Attention;
import com.example.mrh.wordleaning.bean.Word;
import com.example.mrh.wordleaning.bean.WordList;
import com.example.mrh.wordleaning.data.DataAccess;

import java.util.ArrayList;

/**
 * Created by MR.H on 2016/7/27 0027.
 */
public class LearnActivity extends AppCompatActivity implements View.OnClickListener {

    private WordList mWordList;
    private TextView tv_title;
    private TextView tv_word;
    private TextView tv_spelling;
    private TextView tv_meaning;
    private Button btn_pre;
    private Button btn_addWord;
    private Button btn_addPlan;
    private Button btn_next;
    private ArrayList<Word> mWords;
    private int mIndex;
    private int mLeanedCount;
    private int mCount;
    private LinearLayout ll_title_back;
    private RelativeLayout rl_leaning_layout;
    private int mInsertNum;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mWordList = (WordList) getIntent().getSerializableExtra("WordList");
        setContentView(R.layout.learning_layout);
        initView();
        initData();
    }

    private void initData () {
        mIndex = Integer.parseInt(mWordList.getList());
        mLeanedCount = Integer.parseInt(mWordList.getLearnedCount());
        mInsertNum = mLeanedCount;
        if (mLeanedCount == 0){
            mLeanedCount = 1;
        }
        mCount = Integer.parseInt(mWordList.getCount());
        mWords = new DataAccess().queryWord(LearnActivity.this, "LIST =" +
                " '" + mIndex + "'", null);
        tv_title.setText("list: " + mIndex);
        tv_word.setText(mLeanedCount + ":" + mWords.get(mLeanedCount-1).getSpelling());
        tv_spelling.setText(mWords.get(mLeanedCount-1).getPhonetic_alphabet());
        tv_meaning.setText(mWords.get(mLeanedCount-1).getMeanning());
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);

    }

    @Override
    protected void onPause () {
        super.onPause();

        if (mLeanedCount > mInsertNum){
            mInsertNum = mLeanedCount;
            mWordList.setLearnedCount(String.valueOf(mInsertNum));
        }

        new DataAccess().UpdateList(mWordList, LearnActivity.this);
        sendBroadcast(new Intent("update data"));
    }

    private void initView () {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_word = (TextView) findViewById(R.id.tv_word);
        tv_spelling = (TextView) findViewById(R.id.tv_spelling);
        tv_meaning = (TextView) findViewById(R.id.tv_meaning);
        btn_pre = (Button) findViewById(R.id.btn_pre);
        btn_addWord = (Button) findViewById(R.id.btn_addWord);
        btn_addPlan = (Button) findViewById(R.id.btn_addPlan);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_pre.setOnClickListener(this);
        btn_addWord.setOnClickListener(this);
        btn_addPlan.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        rl_leaning_layout = (RelativeLayout) findViewById(R.id.rl_leaning_layout);
        ll_title_back = (LinearLayout) findViewById(R.id.ll_title_back);
        ll_title_back.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.btn_pre:
            mLeanedCount--;
            if (mLeanedCount < 1){
                mLeanedCount = 1;
                Toast.makeText(LearnActivity.this, "没有了", Toast.LENGTH_SHORT).show();
            } else{
                tv_word.setText(mLeanedCount + ":" + mWords.get(mLeanedCount-1).getSpelling());
                tv_spelling.setText(mWords.get(mLeanedCount-1).getPhonetic_alphabet());
                tv_meaning.setText(mWords.get(mLeanedCount-1).getMeanning());
            }
            break;
        case R.id.btn_addWord:
            insertWord();
            break;
        case R.id.btn_addPlan:
            insertReview();
            break;
        case R.id.btn_next:
            mLeanedCount++;
            if (mLeanedCount > mWords.size()){
                mLeanedCount = mWords.size();
                Toast.makeText(LearnActivity.this, "没有了", Toast.LENGTH_SHORT).show();
                mWordList.setLearned(String.valueOf(1));
            } else{
                tv_word.setText(mLeanedCount + ":" + mWords.get(mLeanedCount-1).getSpelling());
                tv_spelling.setText(mWords.get(mLeanedCount-1).getPhonetic_alphabet());
                tv_meaning.setText(mWords.get(mLeanedCount-1).getMeanning());

                if (mLeanedCount > mInsertNum){
                    mInsertNum = mLeanedCount;
                    mWordList.setLearnedCount(String.valueOf(mInsertNum));
                }
                if (mLeanedCount == mWords.size()){
                    mWordList.setLearned(String.valueOf(1));
                }
            }
            break;
        case R.id.ll_title_back:
            ActivityManager.getActivityManager().removeActivity(this);
            break;
        }
    }

    private void insertReview () {
        String spelling = mWords.get(mLeanedCount - 1).getSpelling();
        Attention attention = new DataAccess().queryWordOne(LearnActivity.this, "SPELLING = '"
                + spelling + "'", null);
        if (attention == null){
            new DataAccess().InsertIntoReview(mWords.get(mLeanedCount-1), LearnActivity.this, "0");
            Toast.makeText(LearnActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
        } else {
            if (attention.getReviewword() == null || attention.getReviewword().equals("0")){
                if (attention.getNewword() == null || attention.getNewword().equals("0")){
                    new DataAccess().InsertIntoReview(mWords.get(mLeanedCount-1), LearnActivity
                            .this, "0");
                }else {
                    new DataAccess().InsertIntoReview(mWords.get(mLeanedCount-1), LearnActivity
                            .this, "1");
                }
                Toast.makeText(LearnActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(LearnActivity.this, "已经添加了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertWord () {
        String spelling = mWords.get(mLeanedCount - 1).getSpelling();
        Attention attention = new DataAccess().queryWordOne(LearnActivity.this, "SPELLING = '"
                + spelling + "'", null);
        if (attention ==null){
            new DataAccess().InsertIntoAttention(mWords.get(mLeanedCount-1), LearnActivity.this,
                    "0");
            Toast.makeText(LearnActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
        } else {
            if (attention.getNewword() == null || attention.getNewword().equals("0")){
                if (attention.getReviewword() == null || attention.getReviewword().equals("0")){
                    new DataAccess().InsertIntoAttention(mWords.get(mLeanedCount-1), LearnActivity
                            .this, "0");
                }else {
                    new DataAccess().InsertIntoAttention(mWords.get(mLeanedCount-1), LearnActivity
                            .this, "1");
                }
                Toast.makeText(LearnActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(LearnActivity.this, "已经添加了", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
