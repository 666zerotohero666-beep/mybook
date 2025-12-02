package com.mybook;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * 应用入口类
 */
public class MyBookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        // 设置默认主题为浅色主题
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
