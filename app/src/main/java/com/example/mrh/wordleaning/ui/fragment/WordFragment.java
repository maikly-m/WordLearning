package com.example.mrh.wordleaning.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
public class WordFragment extends Fragment {
    private ListView lv_word_layout;
    private View mRootView;
    private WordAdapter mWordAdapter;
    private MainActivity mActivity;
    private ArrayList<Attention> mAttentions;
    private PopupWindow mPopupWindow;
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
        Filter.addAction("update data3");
        mActivity.registerReceiver(mBroadcastReceiver, Filter);
    }

    private void getData () {
        mAttentions = new DataAccess().queryAttention(getContext(), "NEW_WORD = '" + "1" + "'",
                null);
        for (int i = 0; i < mAttentions.size(); i++){

            Log.d("TAG", "getData: "+mAttentions.get(i).getSpelling());
        }
        while (mWordAdapter == null){
            SystemClock.sleep(10);
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run () {
                mWordAdapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        initData();
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.word_layout, null);
            lv_word_layout = (ListView) mRootView.findViewById(R.id.lv_word_layout);
            mWordAdapter = new WordAdapter();
            lv_word_layout.setAdapter(mWordAdapter);
            initClick();
        }
        return mRootView;
    }

    private void initClick () {
        lv_word_layout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long
                    id) {
                showPopuWindow(view, position);
                return false;
            }
        });
    }

    private void showPopuWindow (View view, final int position) {
        final View popup = View.inflate(getContext(), R.layout.popup_window, null);
        TextView tv_popup = (TextView) popup.findViewById(R.id.tv_popup);
        mPopupWindow = new PopupWindow(popup, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        //设置popuwindow动画
        mPopupWindow.setAnimationStyle(R.style.CustomPopuAnimation2);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_color));
        //设定popuwindow的位置
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        popup.measure(0, 0);
        int measuredWidth = popup.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int offX = widthPixels - measuredWidth;
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(view, offX / 2, -measuredHeight / 4);

        tv_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mPopupWindow.dismiss();
                new DataAccess().UpdateAttention(mAttentions.get(position), getContext());
                mAttentions.remove(position);
                mWordAdapter.notifyDataSetChanged();
            }
        });
    }

    class WordAdapter extends BaseAdapter {
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
                if (convertView == null || convertView.getTag(R.layout.word_list) == null){
                    convertView = View.inflate(getContext(), R.layout.word_list, null);
                    wordViewHolder = WordViewHolder.getViewHolder(convertView);
                } else{
                    wordViewHolder = (WordViewHolder) convertView.getTag(R.layout.word_list);
                }
                wordViewHolder.tv_word_name.setText((position + 1) + "." + mAttentions.get(position)
                        .getSpelling());
                wordViewHolder.tv_word_spelling.setText(mAttentions.get(position)
                        .getPhonetic_alphabet());
                wordViewHolder.tv_word_meaning.setText(mAttentions.get(position).getMeaning());
                return wordViewHolder.rootView;
            } else{
                TextView view = new TextView(getContext());
                view.setPadding(Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(), 30),
                        Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(), 30));
                view.setGravity(Gravity.CENTER);
                view.setText("还没有生词......");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {

                    }
                });
                return view;
            }
        }
    }

    public static class WordViewHolder {
        public View rootView;
        public TextView tv_word_name;
        public TextView tv_word_spelling;
        public TextView tv_word_meaning;

        private WordViewHolder (View rootView) {
            this.rootView = rootView;
            this.tv_word_name = (TextView) rootView.findViewById(R.id.tv_word_name);
            this.tv_word_spelling = (TextView) rootView.findViewById(R.id.tv_word_spelling);
            this.tv_word_meaning = (TextView) rootView.findViewById(R.id.tv_word_meaning);
            rootView.setTag(R.layout.word_list, this);
        }

        public static WordViewHolder getViewHolder (View rootView) {
            return new WordViewHolder(rootView);
        }
    }
}
