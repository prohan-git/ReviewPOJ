package com.example.tjun.reviewpoj.application;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/****
 * </pre> 
 *  Project_Name:    ReviewPOJ
 *  Copyright: 
 *  Version:         1.0.0.1
 *  Created:         Tijun on 2018/1/15 0015 12:48.
 *  E-mail:          prohankj@outlook.com
 *  Desc: 
 * </pre>            
 ****/
public class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onResume() {
        super.onResume();
        //        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Log.d("BaseActivity", "onCreate: " + getClass().getSimpleName());
        ActivityTaskManager.getInstance().putActivity(getClass().getSimpleName(), this);//加入管理器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTaskManager.getInstance().removeActivity(getClass().getSimpleName());
    }

    /**
     * 沉浸式状态栏，layout文件中不可有下面这两个
     * android:clipToPadding="false"
     * android:fitsSystemWindows="true"
     */
    public void setSystemTitleTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 设置状态栏ResID
     *
     * @param res
     */
    public void setSystemTitleRes(int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setBackgroundDrawableResource(res);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | lp.flags);
        }
    }

    /**
     * 全屏
     */
    public void setFullScreen() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}