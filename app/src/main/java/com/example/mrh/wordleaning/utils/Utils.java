package com.example.mrh.wordleaning.utils;

import android.content.Context;

/**
 * Created by MR.H on 2016/7/27 0027.
 */
public class Utils {

    //像素转换
    public static int px2dip(Context context, float px){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
    public static int dip2px(Context context, float dip){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
}
