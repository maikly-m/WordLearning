package com.example.mrh.wordleaning.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.wordleaning.ActivityManager;
import com.example.mrh.wordleaning.MainTab;
import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.bean.BookList;
import com.example.mrh.wordleaning.bean.WordList;
import com.example.mrh.wordleaning.data.DataAccess;
import com.example.mrh.wordleaning.utils.ThreadManager;
import com.example.mrh.wordleaning.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar tb_main;
    private FrameLayout fl_main;
    private FragmentTabHost mTabHost;
    private LinearLayout ll_main_right;
    long[] mHits = new long[2]; //返回键双击事件记录数组
    private PopupWindow mPopupWindow;
    public ArrayList<BookList> mBookLists;
    public ArrayList<WordList> mWordLists;
    public List<WordList> mBookList1;
    public List<WordList> mBookList2;
    public List<WordList> mBookList3;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }
    private void initData () {
        ThreadManager.getThreadPool().startThread(new Runnable() {
            @Override
            public void run () {
                getData();
            }
        });
    }

    public void getData () {
        mBookLists = new DataAccess().QueryBook(MainActivity.this, null, null);
        mWordLists = new DataAccess().QueryList(MainActivity.this, null, null);
        mBookList1 = new ArrayList<>();
        mBookList2 = new ArrayList<>();
        mBookList3 = new ArrayList<>();
        for (int i = 0; i < mWordLists.size(); i++){
            if (mWordLists.get(i).getBookID().equals("book1")){
                mBookList1.add(mWordLists.get(i));
            } else if (mWordLists.get(i).getBookID().equals("book2")){
                mBookList2.add(mWordLists.get(i));
            } else if (mWordLists.get(i).getBookID().equals("book3")){
                mBookList3.add(mWordLists.get(i));
            }
        }
    }

    @Override
    protected void onPause () {
        super.onPause();
        mBookLists = null;
        mWordLists = null;
        mBookList1 = null;
        mBookList2 = null;
        mBookList3 = null;
    }

    private void initView () {
        ll_main_right = (LinearLayout) findViewById(R.id.ll_main_right);
        tb_main = (Toolbar) findViewById(R.id.tb_main);
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        tb_main.setTitle(R.string.title);
        tb_main.setTitleTextAppearance(this, R.style.menuStyle);
        setSupportActionBar(tb_main);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.fl_main);
        mTabHost.getTabWidget().setShowDividers(0);
        MainTab[] mainTabs = MainTab.values();
        for (int i = 0; i < mainTabs.length; i++){
            View view = View.inflate(this, R.layout.main_tab_indicator, null);
            ImageView iv_indicator = (ImageView) view.findViewById(R.id.iv_indicator);
            TextView tv_indicator = (TextView) view.findViewById(R.id.tv_indicator);
            iv_indicator.setImageResource(mainTabs[i].getImageID());
            tv_indicator.setText(mainTabs[i].getTitle());
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mainTabs[i].getTitle()).setIndicator
                    (view);
            mTabHost.addTab(tabSpec, mainTabs[i].getFragment(), null);

        }
        //设置第一页为当前页
//        mTabHost.setCurrentTab(0);
        setListener();
    }


    public void setListener () {
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged (String tabId) {
                switch (tabId){
                case "学习":

                    break;
                case "复习":

                    break;
                case "测试":

                    break;
                case "生词":

                    break;
                default:
                    break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()){
        case R.id.popuWindow:
            showPopuWindow();
            break;
        default:
            break;
        }
        return true;
    }

    private void showPopuWindow () {
        View popu = View.inflate(MainActivity.this, R.layout.more_popuwindow, null);
        LinearLayout ll_popuwindow_first = (LinearLayout) popu.findViewById(R.id
                .ll_popuwindow_first);
        LinearLayout ll_popuwindow_seconde = (LinearLayout) popu.findViewById(R.id
                .ll_popuwindow_seconde);
        mPopupWindow = new PopupWindow(popu, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        //设置popuwindow动画
//        mPopupWindow.setAnimationStyle(R.style.CustomPopuAnimation);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popu_window_bg));
        //设定popuwindow的位置
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int measuredWidth = popu.getMeasuredWidth();
        int offX = widthPixels - measuredWidth;
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(tb_main, offX, 0);

        ll_popuwindow_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mPopupWindow.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View rootView = View.inflate(MainActivity.this, R.layout.setting_view, null);
                TextView cb_setting_01 = (TextView) rootView.findViewById(R.id.cb_setting_01);
                TextView cb_setting_02 = (TextView) rootView.findViewById(R.id.cb_setting_02);
                TextView cb_setting_03 = (TextView) rootView.findViewById(R.id.cb_setting_03);
                TextView cb_setting_04 = (TextView) rootView.findViewById(R.id.cb_setting_04);
                builder.setView(rootView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(Utils.dip2px(MainActivity.this, 160),
                        Utils.dip2px(MainActivity.this, 175));
                cb_setting_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        alertDialog.dismiss();
                        Toast.makeText(MainActivity.this, "已清空学习的单词", Toast.LENGTH_SHORT)
                                    .show();
                            ThreadManager.getThreadPool().startThread(new Runnable() {
                                @Override
                                public void run () {
                                    new DataAccess().deleteLearnedWord(MainActivity.this);
                                    getData();
                                    if (mTabHost.getTabWidget().getChildAt(0).isShown()){
                                        sendBroadcast(new Intent("update data"));
                                    }
                                }
                            });
                    }
                });
                cb_setting_02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        alertDialog.dismiss();
                        Toast.makeText(MainActivity.this, "已清空复习的单词", Toast.LENGTH_SHORT)
                                    .show();
                            ThreadManager.getThreadPool().startThread(new Runnable() {
                                @Override
                                public void run () {
                                    new DataAccess().deleteReviewWord(MainActivity.this);
                                    if (mTabHost.getTabWidget().getChildAt(1).isShown()){
                                        sendBroadcast(new Intent("update data1"));
                                    }
                                }
                            });
                    }
                });
                cb_setting_03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        alertDialog.dismiss();
                        Toast.makeText(MainActivity.this, "已清空生词", Toast.LENGTH_SHORT).show();
                            ThreadManager.getThreadPool().startThread(new Runnable() {
                                @Override
                                public void run () {
                                    new DataAccess().deleteNewWord(MainActivity.this);
                                    if (mTabHost.getTabWidget().getChildAt(3).isShown()){
                                        sendBroadcast(new Intent("update data3"));
                                    }
                                }
                            });
                    }
                });
                cb_setting_04.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        ll_popuwindow_seconde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();

        ActivityManager.getActivityManager().removeActivity(this);
    }

    //双击返回键退出程序
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //每点击一次 实现左移一格数据
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            //给数组的最后赋当前时钟值
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            //当0出的值大于当前时间-1500时  证明在1500秒内点击了2次
            if (mHits[0] > SystemClock.uptimeMillis() - 1500){
                ActivityManager.getActivityManager().exitApp();
            } else{
                Toast.makeText(this, R.string.twiceclick_exit, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

