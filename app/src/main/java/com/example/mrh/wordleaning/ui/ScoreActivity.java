package com.example.mrh.wordleaning.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrh.wordleaning.ActivityManager;
import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.bean.Answer;
import com.example.mrh.wordleaning.bean.CheckAnswer;

import java.util.ArrayList;

/**
 * Created by MR.H on 2016/7/30 0030.
 */
public class ScoreActivity extends AppCompatActivity {

    private ArrayList<Answer> mAnswerArrayList;
    private ArrayList<CheckAnswer> mCheckAnswerArrayList;
    private LinearLayout ll_title_back;
    private TextView tv_title;
    private RelativeLayout rl_leaning_layout;
    private TextView tv_score;
    private ListView lv_score;
    private ScoreAdapter mScoreAdapter;
    private int mScore;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mAnswerArrayList = (ArrayList<Answer>) getIntent().getSerializableExtra("mAnswerArrayList");
        mCheckAnswerArrayList = (ArrayList<CheckAnswer>) getIntent().getSerializableExtra
                ("mCheckAnswerArrayList");
        setContentView(R.layout.score_activity);
        initView();
        initData();
    }

    private void initData () {
        mScore = 0;
        for (int i = 0; i < mAnswerArrayList.size(); i++){
            String answerChar = mAnswerArrayList.get(i).getAnswerChar();
            String aChar = mCheckAnswerArrayList.get(i).getAnswerChar();
            if (answerChar.equals(aChar)){
                mScore++;
            }
        }
        tv_score.setText("总计：" + mScore + "/" + mAnswerArrayList.size());
        lv_score.setAdapter(mScoreAdapter);
    }

    private void initView () {
        ll_title_back = (LinearLayout) findViewById(R.id.ll_title_back);
        rl_leaning_layout = (RelativeLayout) findViewById(R.id.rl_leaning_layout);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_score = (TextView) findViewById(R.id.tv_score);
        lv_score = (ListView) findViewById(R.id.lv_score);
        mScoreAdapter = new ScoreAdapter();

        ll_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                ActivityManager.getActivityManager().removeActivity(ScoreActivity.this);
            }
        });
    }

    class ScoreAdapter extends BaseAdapter {
        @Override
        public int getCount () {
            return mAnswerArrayList.size();
        }

        @Override
        public Object getItem (int position) {
            return null;
        }

        @Override
        public long getItemId (int position) {
            return 0;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = View.inflate(ScoreActivity.this, R.layout.score_list, null);
                viewHolder = ViewHolder.getViewHolder(convertView);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String str1 = (position+1) +"."+ mAnswerArrayList.get(position).getWord()
                    .getSpelling()+"\n" +"  正确答案: "+ mAnswerArrayList.get(position).getAnswerChar()
                    +" " +
                    mAnswerArrayList.get(position).getWord().getMeanning();
            String str2 = "选择的答案: " + mCheckAnswerArrayList.get(position).getAnswerChar() +" "+
                    mCheckAnswerArrayList.get(position).getWord().getMeanning();

            viewHolder.tv_score_01.setText(str1);
            viewHolder.tv_score_02.setText(str2);
            return viewHolder.rootView;
        }
    }
    public static class ViewHolder {
        public View rootView;
        public TextView tv_score_01;
        public TextView tv_score_02;

        private ViewHolder (View rootView) {
            this.rootView = rootView;
            this.tv_score_01 = (TextView) rootView.findViewById(R.id.tv_score_01);
            this.tv_score_02 = (TextView) rootView.findViewById(R.id.tv_score_02);
            rootView.setTag(this);
        }

        public static ViewHolder getViewHolder(View rootView){
            return new ViewHolder(rootView);
        }
    }
    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(ScoreActivity.this);
    }
}
