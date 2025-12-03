package com.mybook.data.remote.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 请求拦截器
 * 用于添加公共参数、认证信息等
 */
public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取原始请求
        Request originalRequest = chain.request();

        // 构建新的请求，添加公共参数和认证信息
        Request newRequest = originalRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                // 添加认证token（如果有）
                // .header("Authorization", "Bearer " + getAuthToken())
                // 添加时间戳
                .header("X-Timestamp", String.valueOf(System.currentTimeMillis()))
                // 添加设备信息
                .header("X-Device-Type", "Android")
                // 添加应用版本
                // .header("X-App-Version", BuildConfig.VERSION_NAME)
                .build();

        // 继续执行请求
        return chain.proceed(newRequest);
    }

    /**
     * 获取认证token
     * @return 认证token
     */
    private String getAuthToken() {
        // 这里应该从本地存储中获取token，例如SharedPreferences
        return "your_auth_token"; // 示例值，实际应该从本地获取
    }
}