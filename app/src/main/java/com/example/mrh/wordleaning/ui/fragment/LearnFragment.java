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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.bean.BookList;
import com.example.mrh.wordleaning.bean.WordList;
import com.example.mrh.wordleaning.data.DataAccess;
import com.example.mrh.wordleaning.ui.LearnActivity;
import com.example.mrh.wordleaning.ui.MainActivity;
import com.example.mrh.wordleaning.utils.ThreadManager;
import com.example.mrh.wordleaning.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/7/26 0026.
 */
public class LearnFragment extends Fragment {

    private View mRootView;
    private ListView lv_learn;
    private ListView lv_word;
    public LearnAdapter mLearnAdapter;
    private MainActivity mActivity;
    public ArrayList<BookList> BookLists;
    public List<WordList> BookList1;
    public List<WordList> BookList2;
    public List<WordList> BookList3;
    public List<WordList> BookList;
    public ListAdapter mListAdapter;
    private TextView tv_select_progress;
    private TextView tv_select_name;
    private BroadcastReceiver mBroadcastReceiver;
    private View mPreView;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        initReceiver();
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
                        mActivity.getData();

                        BookLists = mActivity.mBookLists;
                        BookList1 = mActivity.mBookList1;
                        BookList2 = mActivity.mBookList2;
                        BookList3 = mActivity.mBookList3;
                        DataAccess.BOOK_ID = "book1";
                        BookList = BookList1;

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                mLearnAdapter.notifyDataSetChanged();
                                mListAdapter.notifyDataSetChanged();

                                int count = getLeanedCount(BookList1);
                                tv_select_progress.setText("进度：" + count + "/" + BookLists.get(0)
                                        .getNumOfList());
                                tv_select_name.setText("已选：" + BookLists.get(0).getName());
                            }
                        });
                    }
                });
            }
        };
        IntentFilter Filter = new IntentFilter();
        Filter.addAction("update data");
        mActivity.registerReceiver(mBroadcastReceiver, Filter);
    }

    public void getWordListCondition () {
        ThreadManager.getThreadPool().startThread(new Runnable() {
            @Override
            public void run () {
                while (BookList == null){
                    SystemClock.sleep(20);
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run () {
                        int count = getLeanedCount(BookList1);
                        tv_select_progress.setText("进度：" + count + "/" + BookLists.get(0)
                                .getNumOfList());
                        tv_select_name.setText("已选：" + BookLists.get(0).getName());
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        getWordListCondition();
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.learn_layout, null);
            lv_learn = (ListView) mRootView.findViewById(R.id.lv_learn);
            lv_word = (ListView) mRootView.findViewById(R.id.lv_word);
            tv_select_progress = (TextView) mRootView.findViewById(R.id.tv_select_progress);
            tv_select_name = (TextView) mRootView.findViewById(R.id.tv_select_name);

            if (BookLists == null){
                BookLists = mActivity.mBookLists;
                BookList1 = mActivity.mBookList1;
                BookList2 = mActivity.mBookList2;
                BookList3 = mActivity.mBookList3;
                DataAccess.BOOK_ID = "book1";
                BookList = BookList1;
            }
            mLearnAdapter = new LearnAdapter();
            mListAdapter = new ListAdapter();
            lv_learn.setAdapter(mLearnAdapter);
            lv_word.setAdapter(mListAdapter);
            lv_learn.setDividerHeight(0);
            lv_word.setDividerHeight(0);
            initClick();
        }
        return mRootView;
    }

    private void initClick () {
        lv_learn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                if (mPreView != null){
                    mPreView.setBackgroundColor(Color.TRANSPARENT);
                }
                mPreView = view;
                view.setBackgroundColor(getResources().getColor(R.color.primary_button_press));
                switch (position){
                case 0:
                    DataAccess.BOOK_ID = "book1";
                    BookList = BookList1;
                    if (!tv_select_name.getText().equals(BookLists.get(0).getName())){
                        int count = getLeanedCount(BookList1);
                        tv_select_progress.setText("进度：" + count + "/" + BookLists.get(0)
                                .getNumOfList());
                        tv_select_name.setText("已选：" + BookLists.get(0).getName());
                    }
                    break;
                case 1:
                    DataAccess.BOOK_ID = "book2";
                    BookList = BookList2;
                    if (!tv_select_name.getText().equals(BookLists.get(1).getName())){
                        int count = getLeanedCount(BookList2);
                        tv_select_progress.setText("进度：" + count + "/" + BookLists.get(1)
                                .getNumOfList());
                        tv_select_name.setText("已选：" + BookLists.get(1).getName());
                    }
                    break;
                case 2:
                    DataAccess.BOOK_ID = "book3";
                    BookList = BookList3;
                    if (!tv_select_name.getText().equals(BookLists.get(2).getName())){
                        int count = getLeanedCount(BookList3);
                        tv_select_progress.setText("进度：" + count + "/" + BookLists.get(2)
                                .getNumOfList());
                        tv_select_name.setText("已选：" + BookLists.get(2).getName());
                    }
                    break;
                default:
                    break;
                }
                mListAdapter.notifyDataSetChanged();
                lv_word.setSelection(0);
            }
        });
        lv_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), LearnActivity.class);
                intent.putExtra("WordList", BookList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public class ListAdapter extends BaseAdapter {

        @Override
        public int getCount () {
            return BookList.size();
        }

        @Override
        public Object getItem (int position) {
            return BookList.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            if (BookList != null){
                ViewHolder viewHolder;
                if (convertView == null){
                    convertView = View.inflate(getContext(), R.layout.learn_list, null);
                    viewHolder = ViewHolder.getViewHolder(convertView);
                } else{
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tv_lean_name.setText("list " + BookList.get(position).getList());
                int i = Integer.parseInt(BookList.get(position).getLearnedCount());
                int integer = Integer.parseInt(BookList.get(position).getCount());
                if (i == 0){
                    viewHolder.pb_lean_list.setProgress(0);
                } else{
                    viewHolder.pb_lean_list.setProgress(i * 100 / integer);
                }
                viewHolder.tv_lean_progress.setText(i + "/" + integer);
                return viewHolder.rootView;
            }
            return null;
        }
    }

    public static class ViewHolder {
        public View rootView;
        public ProgressBar pb_lean_list;
        public TextView tv_lean_name;
        public TextView tv_lean_progress;

        private ViewHolder (View rootView) {
            this.rootView = rootView;
            this.pb_lean_list = (ProgressBar) rootView.findViewById(R.id.pb_lean_list);
            this.tv_lean_name = (TextView) rootView.findViewById(R.id.tv_lean_name);
            this.tv_lean_progress = (TextView) rootView.findViewById(R.id.tv_lean_progress);
            rootView.setTag(this);
        }

        public static ViewHolder getViewHolder (View rootView) {
            return new ViewHolder(rootView);
        }
    }

    public class LearnAdapter extends BaseAdapter {

        @Override
        public int getCount () {
            return BookLists.size();
        }

        @Override
        public Object getItem (int position) {
            return BookLists.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            if (BookLists != null){
                TextView textView = new TextView(getContext());
                textView.setText(BookLists.get(position).getName());
                textView.setTextSize(15);
                textView.setPadding(Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5),
                        Utils.dip2px(getContext(), 5), Utils.dip2px(getContext(), 5));
                return textView;
            }
            return null;
        }
    }

    public int getLeanedCount (List<WordList> booklist) {
        int count = 0;
        for (int i = 0; i < booklist.size(); i++){
            count += Integer.parseInt(booklist.get(i).getLearned());
        }
        return count;
    }
}
