package com.example.mrh.wordleaning.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.wordleaning.ActivityManager;
import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.bean.Answer;
import com.example.mrh.wordleaning.bean.CheckAnswer;
import com.example.mrh.wordleaning.bean.Word;
import com.example.mrh.wordleaning.bean.WordList;
import com.example.mrh.wordleaning.data.DataAccess;
import com.example.mrh.wordleaning.utils.ThreadManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by MR.H on 2016/7/30 0030.
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private List<WordList> mBookList;
    private int mListNum;
    private LinearLayout ll_title_back;
    private TextView tv_title;
    private RelativeLayout rl_leaning_layout;
    private TextView tv_list_name;
    private TextView tv_word_name;
    private RadioGroup rg_answer;
    private TextView tv_answer;
    private Button btn_answer;
    private Button btn_next;
    private List<Word> mWords;
    private ArrayList<Word> mWordList;
    private RadioButton rb_answer_01;
    private RadioButton rb_answer_02;
    private RadioButton rb_answer_03;
    private RadioButton rb_answer_04;
    private List<Word> mLast_list;
    private Word mSelectSpelling;
    private Word mCorrectspelling;
    private String mSelectChar = "null";
    private String mCorrectChar;
    private Answer mAnswer;
    private CheckAnswer mCheckAnswer;
    private ArrayList<Answer> mAnswerArrayList = new ArrayList<>();
    private ArrayList<CheckAnswer> mCheckAnswerArrayList = new ArrayList<>();
    private String mCount;
    private boolean isNext = false;
    private boolean isLock = false;
    private FrameLayout fl_test_activity;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mBookList = (List<WordList>) getIntent().getSerializableExtra("bookList");
        mListNum = getIntent().getIntExtra("listNum", 0);
        mCount = mBookList.get(mListNum).getCount();
        setContentView(R.layout.test_activity);
        initData();
        initView();

    }

    private void initData () {
        ThreadManager.getThreadPool().startThread(new Runnable() {
            @Override
            public void run () {
                mWords = new ArrayList<>();
                Random random = new Random();
                int randomWordIndex = random.nextInt(Integer.parseInt(mBookList.get(mListNum)
                        .getCount()));
                mWordList = new DataAccess().QueryWord(TestActivity.this, mBookList
                        .get(mListNum).getBookID(), "LIST = '" + (mListNum + 1) + "'", null);
                mWords.add(mWordList.get(randomWordIndex));
                mWordList.remove(randomWordIndex);
                getData();
            }
        });
    }

    private void getData () {
        Random random = new Random();
        for (int i = 0; i < 3; i++){
            int randomListIndex = random.nextInt(mBookList.size());
            if (randomListIndex == mListNum){
                i--;
                continue;
            }
            int randomTestIndex = random.nextInt(Integer.parseInt(mBookList.get
                    (randomListIndex).getCount()));
            ArrayList<Word> words2 = new DataAccess().QueryWord(TestActivity.this, mBookList
                    .get(randomListIndex).getBookID(), "LIST = '" + (randomListIndex + 1) + "'", null);
            mWords.add(words2.get(randomTestIndex));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run () {
                loadData();
            }
        });
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);
    }

    private void loadData () {

        int doCount = Integer.parseInt(mCount) - mWordList.size();
        tv_list_name.setText("list: " + mBookList.get(mListNum).getList() + "(" + doCount + "/" +
                mCount + ")");
        tv_word_name.setText(doCount + "." + mWords.get(0).getSpelling());


        List<Word> list = new ArrayList<>();
        mLast_list = new ArrayList<>();
        Random random = new Random();
        int randomIndex;
        Iterator<Word> iterator = mWords.iterator();

        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        for (int i = 0; i < 4; i++){
            randomIndex = random.nextInt(list.size());
            mLast_list.add(list.get(randomIndex));
            list.remove(randomIndex);
        }

        rb_answer_01.setText("A." + mLast_list.get(0).getMeanning());
        rb_answer_02.setText("B." + mLast_list.get(1).getMeanning());
        rb_answer_03.setText("C." + mLast_list.get(2).getMeanning());
        rb_answer_04.setText("D." + mLast_list.get(3).getMeanning());

        for (int i = 0; i < mLast_list.size(); i++){
            if (mLast_list.get(i).getSpelling().equals(mWords.get(0).getSpelling())){
                switch (i){
                case 0:
                    mCorrectChar = "A";
                    break;
                case 1:
                    mCorrectChar = "B";
                    break;
                case 2:
                    mCorrectChar = "C";
                    break;
                case 3:
                    mCorrectChar = "D";
                    break;
                default:
                    break;
                }
            }
        }
    }

    private void initView () {
        ll_title_back = (LinearLayout) findViewById(R.id.ll_title_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_leaning_layout = (RelativeLayout) findViewById(R.id.rl_leaning_layout);
        tv_list_name = (TextView) findViewById(R.id.tv_list_name);
        tv_word_name = (TextView) findViewById(R.id.tv_word_name);
        rg_answer = (RadioGroup) findViewById(R.id.rg_answer);
        rb_answer_01 = (RadioButton) findViewById(R.id.rb_answer_01);
        rb_answer_02 = (RadioButton) findViewById(R.id.rb_answer_02);
        rb_answer_03 = (RadioButton) findViewById(R.id.rb_answer_03);
        rb_answer_04 = (RadioButton) findViewById(R.id.rb_answer_04);
        tv_answer = (TextView) findViewById(R.id.tv_answer);
        btn_answer = (Button) findViewById(R.id.btn_answer);
        btn_next = (Button) findViewById(R.id.btn_next);
        fl_test_activity = (FrameLayout) findViewById(R.id.fl_test_activity);

        ll_title_back.setOnClickListener(this);
        btn_answer.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        rg_answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group, int checkedId) {

                if (isLock){
                    return;
                }
                isNext = true;

                switch (checkedId){
                case R.id.rb_answer_01:
                    mSelectSpelling = mLast_list.get(0);
                    mSelectChar = "A";
                    break;
                case R.id.rb_answer_02:
                    mSelectSpelling = mLast_list.get(1);
                    mSelectChar = "B";
                    break;
                case R.id.rb_answer_03:
                    mSelectSpelling = mLast_list.get(2);
                    mSelectChar = "C";
                    break;
                case R.id.rb_answer_04:
                    mSelectSpelling = mLast_list.get(3);
                    mSelectChar = "D";
                    break;
                default:
                    break;
                }
            }
        });

    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.btn_answer:
            if (mSelectChar.equals("null")){
                Toast.makeText(TestActivity.this, "请选择答案", Toast.LENGTH_SHORT).show();
                break;
            } else if (!isLock){
                isLock = true;
                if (mCorrectChar.equals(mSelectChar)){
                    Toast.makeText(TestActivity.this, "选择正确", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(TestActivity.this, "选择错误", Toast.LENGTH_SHORT).show();
                }
                tv_answer.setText("正确答案: " + mCorrectChar + "." + mWords.get(0).getMeanning());
            }
            break;
        case R.id.btn_next:
            if (!isNext){
                Toast.makeText(TestActivity.this, "请选择答案后进入下一个", Toast.LENGTH_SHORT).show();
            } else{
                if (mWordList.size() != 0){
                    saveData();
                    loadNextData();
                } else{
                    saveData();

                    Toast.makeText(TestActivity.this, "查看成绩", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ScoreActivity.class);
                    intent.putExtra("mAnswerArrayList", mAnswerArrayList);
                    intent.putExtra("mCheckAnswerArrayList", mCheckAnswerArrayList);
                    startActivity(intent);
                    ActivityManager.getActivityManager().removeActivity(this);
                }
            }
            break;
        case R.id.ll_title_back:
            ActivityManager.getActivityManager().removeActivity(this);
            break;
        }
    }

    private void saveData () {
        mCorrectspelling = mWords.get(0);
        mAnswer = new Answer(mCorrectspelling, mCorrectChar);
        mCheckAnswer = new CheckAnswer(mSelectSpelling, mSelectChar);
        mAnswerArrayList.add(mAnswer);
        mCheckAnswerArrayList.add(mCheckAnswer);
    }

    private void loadNextData () {
        ThreadManager.getThreadPool().startThread(new Runnable() {
            @Override
            public void run () {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run () {
                        tv_answer.setText("");
                        rg_answer.clearCheck();
                    }
                });
                Random random = new Random();
                int randomWordIndex = random.nextInt(mWordList.size());
                mWords.clear();
                mWords.add(mWordList.get(randomWordIndex));
                mWordList.remove(randomWordIndex);
                getData();
                mSelectChar = "null";
                isNext = false;
                isLock = false;
            }
        });
    }
}
