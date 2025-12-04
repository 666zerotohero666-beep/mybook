package com.mybook.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Toast工具类，用于控制Toast显示时长
 */
public class ToastUtil {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static final int TOAST_DURATION = 500; // 自定义Toast显示时长，500毫秒

    /**
     * 显示短时长Toast
     * @param context 上下文
     * @param message 消息内容
     */
    public static void showShort(Context context, String message) {
        show(context, message, TOAST_DURATION);
    }

    /**
     * 显示自定义时长Toast
     * @param context 上下文
     * @param message 消息内容
     * @param duration 显示时长（毫秒）
     */
    public static void show(Context context, String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
            }
        }, duration);
    }
}
