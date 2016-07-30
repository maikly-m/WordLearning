package com.example.mrh.wordleaning.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.bean.WordList;
import com.example.mrh.wordleaning.data.DataAccess;
import com.example.mrh.wordleaning.ui.MainActivity;
import com.example.mrh.wordleaning.ui.TestActivity;
import com.example.mrh.wordleaning.utils.ThreadManager;
import com.example.mrh.wordleaning.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MR.H on 2016/7/26 0026.
 */
public class TestFragment extends Fragment {

    private View mRootView;
    private ExpandableListView lv_list;
    private TestAdapter mTestAdapter;
    private RadioGroup rg_test;
    private MainActivity mActivity;
    private List<WordList> bookList1;
    private List<WordList> bookList2;
    private List<WordList> bookList3;
    private List<WordList> booklist;
    private ArrayList<WordList> wordLists;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity)getActivity();
        getData();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.test_list_layout, null);
            initView(mRootView);
        }

        return mRootView;

    }

    private void getData () {
        booklist = new ArrayList<>();
        ThreadManager.getThreadPool().startThread(new Runnable() {
            @Override
            public void run () {

                wordLists = new DataAccess().QueryList(getContext(), null, null);
                bookList1 = new ArrayList<>();
                bookList2 = new ArrayList<>();
                bookList3 = new ArrayList<>();
                for (int i = 0; i < wordLists.size(); i++){
                    if (wordLists.get(i).getBookID().equals("book1")){
                        bookList1.add(wordLists.get(i));
                    } else if (wordLists.get(i).getBookID().equals("book2")){
                        bookList2.add(wordLists.get(i));
                    } else if (wordLists.get(i).getBookID().equals("book3")){
                        bookList3.add(wordLists.get(i));
                    }
                }
            }
        });
    }

    private void initView (View mRootView) {
        rg_test = (RadioGroup) mRootView.findViewById(R.id.rg_test);
        lv_list = (ExpandableListView) mRootView.findViewById(R.id.lv_list);
        mTestAdapter = new TestAdapter();
        lv_list.setAdapter(mTestAdapter);
        lv_list.setDividerHeight(0);
        lv_list.setGroupIndicator(null);
        rg_test.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group, int checkedId) {
                switch (checkedId){
                case R.id.rb_test_01:
                    booklist = bookList1;
                    mTestAdapter.notifyDataSetChanged();
                    break;
                case R.id.rb_test_02:
                    booklist = bookList2;
                    mTestAdapter.notifyDataSetChanged();
                    break;
                case R.id.rb_test_03:
                    booklist = bookList3;
                    mTestAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
                }
            }
        });

        lv_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick (ExpandableListView parent, View v, int groupPosition,
                                         long id) {
                if (groupPosition == 0){
                    Intent intent = new Intent(getContext(), TestActivity.class);
                    Random random = new Random();
                    int listNum = random.nextInt(booklist.size());
                    intent.putExtra("bookList", (Serializable) booklist);
                    intent.putExtra("listNum", listNum);
                    startActivity(intent);
                }
                return false;
            }
        });
        lv_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick (ExpandableListView parent, View v, int groupPosition,
                                         int childPosition, long id) {
                if (groupPosition == 1){
                    Intent intent = new Intent(getContext(), TestActivity.class);
                    intent.putExtra("bookList", (Serializable) booklist);
                    intent.putExtra("listNum", childPosition);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    class TestAdapter extends BaseExpandableListAdapter {
        private TextView mList_view;
        @Override
        public int getGroupCount () {
            if (booklist != null && booklist.size() > 0){
                return 2;
            }
            return 0;
        }

        @Override
        public int getChildrenCount (int groupPosition) {
            if (groupPosition == 0){
                return 0;
            }else if (groupPosition == 1){
                return booklist.size();
            }
            return 0;
        }

        @Override
        public Object getGroup (int groupPosition) {
            return null;
        }

        @Override
        public Object getChild (int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId (int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId (int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds () {
            return false;
        }

        @Override
        public View getGroupView (int groupPosition, boolean isExpanded, View convertView,
                                  ViewGroup parent) {
            if (groupPosition == 0){
                TextView textView = new TextView(getContext());
                textView.setPadding(Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(),
                        10), Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(), 10));
                textView.setText("随机测试");
                return textView;
            }else if (groupPosition == 1){
                TextView textView = new TextView(getContext());
                textView.setPadding(Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(),
                        10), Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(), 10));
                textView.setText("分组测试");
                return textView;
            }
            return null;
        }

        @Override
        public View getChildView (int groupPosition, int childPosition, boolean isLastChild, View
                convertView, ViewGroup parent) {
            if (groupPosition == 1){
                if (convertView == null){
                    mList_view = new TextView(getContext());
                    mList_view.setPadding(Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(),
                            5), Utils.dip2px(getContext(), 10), Utils.dip2px(getContext(), 5));
                    convertView = mList_view;
                    convertView.setTag(mList_view);
                }else {
                    mList_view = (TextView) convertView.getTag();
                }
                mList_view.setText("LIST: "+booklist.get(childPosition).getList());
                return mList_view;
            }
            return null;
        }

        @Override
        public boolean isChildSelectable (int groupPosition, int childPosition) {
            return true;
        }
    }
}
