package com.example.mrh.wordleaning.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.bean.Attention;
import com.example.mrh.wordleaning.data.DataAccess;
import com.example.mrh.wordleaning.ui.MainActivity;
import com.example.mrh.wordleaning.utils.ThreadManager;
import com.example.mrh.wordleaning.utils.Utils;

import java.util.ArrayList;

/**
 * Created by MR.H on 2016/7/26 0026.
 */
public class ReviewFragment extends Fragment implements View.OnClickListener {
    private ListView lv_word_layout;
    private View mRootView;
    private ReviewWordAdapter mReviewWordAdapter;
    private MainActivity mActivity;
    private ArrayList<Attention> mAttentions;
    private TextView tv_review_layout;
    private Button btn_review_del;
    private int mPosition = 0;
    private Button btn_review_meaning;
    private TextView tv_review_layout2;
    private TextView tv_review_layout3;
    private View mPreView;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        initReceiver();
    }

    private void initData () {
        ThreadManager.getThreadPool().startThread(new Runnable() {
            @Override
            public void run () {
                getData();
            }
        });
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        mActivity.unregisterReceiver(mBroadcastReceiver);
    }

    //廣播更新數據
    private void initReceiver () {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive (Context context, Intent intent) {
                ThreadManager.getThreadPool().startThread(new Runnable() {
                    @Override
                    public void run () {
                        getData();
                    }
                });
            }
        };
        IntentFilter Filter = new IntentFilter();
        Filter.addAction("update data1");
        mActivity.registerReceiver(mBroadcastReceiver, Filter);
    }

    private void getData () {
        mAttentions = new DataAccess().queryAttention(getContext(), "REVIEW_WORD = '" + "1" + "'",
                null);
        while (mReviewWordAdapter == null){
            SystemClock.sleep(10);
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run () {
                mReviewWordAdapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        initData();
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.review_word_layout, null);
            initView(mRootView);
        }
        initView(mRootView);
        return mRootView;
    }

    private void initClick () {
        lv_word_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                setListviewItemColor(view);

                tv_review_layout.setText("");
                tv_review_layout2.setText("");
                tv_review_layout3.setText("");
            }
        });
    }

    private void setListviewItemColor (View view) {
        if (mPreView != null){
            mPreView.setBackgroundColor(Color.TRANSPARENT);
        }
        mPreView = view;
        view.setBackgroundColor(getResources().getColor(R.color.primary_light));
    }

    private void initView (View mRootView) {
        lv_word_layout = (ListView) mRootView.findViewById(R.id.lv_word_layout);
        lv_word_layout.setDividerHeight(0);
        mReviewWordAdapter = new ReviewWordAdapter();
        lv_word_layout.setAdapter(mReviewWordAdapter);
        tv_review_layout = (TextView) mRootView.findViewById(R.id.tv_review_layout);
        tv_review_layout2 = (TextView) mRootView.findViewById(R.id.tv_review_layout2);
        tv_review_layout3 = (TextView) mRootView.findViewById(R.id.tv_review_layout3);
        btn_review_del = (Button) mRootView.findViewById(R.id.btn_review_del);
        btn_review_meaning = (Button) mRootView.findViewById(R.id.btn_review_meaning);
        initClick();

        btn_review_del.setOnClickListener(this);
        btn_review_meaning.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.btn_review_del:
            if (mAttentions.size() > mPosition && mAttentions.size() > 0){
                new DataAccess().UpdateReviewWord(mAttentions.get(mPosition), getContext());
                mAttentions.remove(mPosition);
                mReviewWordAdapter.notifyDataSetChanged();
            }
            break;
        case R.id.btn_review_meaning:
            if (mAttentions.size() > mPosition && mAttentions.size() > 0){
                tv_review_layout.setText((mPosition + 1) + "." + mAttentions.get(mPosition)
                        .getSpelling());
                tv_review_layout2.setText(mAttentions.get(mPosition).getPhonetic_alphabet());
                tv_review_layout3.setText(mAttentions.get(mPosition).getMeaning());
            }
            break;
        }
    }

    class ReviewWordAdapter extends BaseAdapter {
        @Override
        public int getCount () {
            if (mAttentions != null && mAttentions.size() > 0){
                return mAttentions.size();
            }
            return 1;
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
            if (mAttentions != null && mAttentions.size() > 0){
                WordViewHolder wordViewHolder;
                if (convertView == null || convertView.getTag(R.layout.review_word_list) == null){
                    convertView = View.inflate(getContext(), R.layout.review_word_list, null);
                    wordViewHolder = WordViewHolder.getViewHolder(convertView);
                } else{
                    wordViewHolder = (WordViewHolder) convertView.getTag(R.layout.review_word_list);
                }
                wordViewHolder.tv_word_name.setText((position + 1) + "." + mAttentions.get(position)
                        .getSpelling());
                return wordViewHolder.rootView;
            }
            TextView view = new TextView(getContext());
            view.setPadding(Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 20),
                    Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 20));
            view.setGravity(Gravity.CENTER);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {

                }
            });
            view.setText("还没有单词......");
            return view;

        }
    }

    public static class WordViewHolder {
        public View rootView;
        public TextView tv_word_name;

        private WordViewHolder (View rootView) {
            this.rootView = rootView;
            this.tv_word_name = (TextView) rootView.findViewById(R.id.tv_word_name);
            rootView.setTag(R.layout.review_word_list, this);
        }

        public static WordViewHolder getViewHolder (View rootView) {
            return new WordViewHolder(rootView);
        }
    }
}
