package com.example.tjun.reviewpoj.application;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class App extends Application {
    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initModule();
    }

    private void initModule() {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("init Success");
    }
}