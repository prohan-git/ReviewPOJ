package com.example.tjun.reviewpoj.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/****
 * </pre> 
 *  Project_Name:    ReviewPOJ
 *  Copyright: 
 *  Version:         1.0.0.1
 *  Created:         Tijun on 2018/1/25 0025 17:15.
 *  E-mail:          prohankj@outlook.com
 *  Desc: 
 * </pre>            
 ****/
public class TimeUtils {
    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat HMS_FORMAT = new SimpleDateFormat("HH:mm:ss");


    /**
     * 将时间戳转为时间字符串
     * <p>格式为 yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, DEFAULT_FORMAT);
    }


    /**
     * 将时间戳转为时间字符串
     * <p>格式为 format</p>
     *
     * @param millis 毫秒时间戳
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String millis2String(final long millis, final DateFormat format) {
        return format.format(new Date(millis));
    }
}
