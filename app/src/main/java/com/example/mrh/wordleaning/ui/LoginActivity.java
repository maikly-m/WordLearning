package com.example.mrh.wordleaning.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.example.mrh.wordleaning.ActivityManager;
import com.example.mrh.wordleaning.R;
import com.example.mrh.wordleaning.data.SqlHelper;
import com.example.mrh.wordleaning.utils.ThreadManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by MR.H on 2016/7/26 0026.
 */
public class LoginActivity extends AppCompatActivity {

    private View mView;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mView = findViewById(R.id.ll_login);
        ActivityManager.getActivityManager().addActivity(this);
        SqlHelper.DB_NAME = getFilesDir().getAbsolutePath() + "/database/word.db";
        File dir = new File(getFilesDir().getAbsolutePath() + "/database");
        if (!dir.exists())
            dir.mkdirs();
        if (!(new File(SqlHelper.DB_NAME)).exists()){
            FileOutputStream fos;
            try{
                fos = new FileOutputStream(SqlHelper.DB_NAME);

                byte[] buffer = new byte[1024];
                int count;
                InputStream is = getResources().openRawResource(
                        R.raw.word);
                while ((count = is.read(buffer)) != 0){
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        initAnimation();
    }

    private void initAnimation () {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, ScaleAnimation
                .RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(2000);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        mView.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart (Animation animation) {

            }

            @Override
            public void onAnimationEnd (Animation animation) {
                ThreadManager.getThreadPool().startThread(new Runnable() {
                    @Override
                    public void run () {
                        SystemClock.sleep(1000);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityManager.getActivityManager().removeActivity(LoginActivity.this);
                    }
                });

            }

            @Override
            public void onAnimationRepeat (Animation animation) {

            }
        });
    }
}
