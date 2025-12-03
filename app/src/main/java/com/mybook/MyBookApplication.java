package com.mybook;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * 应用入口类
 */
public class MyBookApplication extends Application {
    private static volatile MyBookApplication instance;

    /**
     * 获取应用实例
     * @return 应用实例
     */
    public static MyBookApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MyBookApplication has not been initialized yet.");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // 设置默认主题为浅色主题
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
