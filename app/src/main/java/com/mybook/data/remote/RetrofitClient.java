package com.mybook.data.remote;

import com.mybook.MyBookApplication;
import com.mybook.data.remote.api.ApiService;
import com.mybook.data.remote.interceptor.RequestInterceptor;
import com.mybook.data.remote.interceptor.ResponseInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit客户端封装类
 * 用于初始化Retrofit和OkHttp，并提供ApiService实例
 */
public class RetrofitClient {
    private static volatile RetrofitClient instance;
    private final Retrofit retrofit;
    private final ApiService apiService;

    /**
     * 基础URL
     */
    private static final String BASE_URL = "https://api.example.com";

    /**
     * 连接超时时间（秒）
     */
    private static final long CONNECT_TIMEOUT = 30;

    /**
     * 读取超时时间（秒）
     */
    private static final long READ_TIMEOUT = 30;

    /**
     * 写入超时时间（秒）
     */
    private static final long WRITE_TIMEOUT = 30;

    /**
     * 私有构造函数
     */
    private RetrofitClient() {
        // 创建OkHttp客户端
        OkHttpClient okHttpClient = createOkHttpClient();

        // 初始化Retrofit
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建ApiService实例
        this.apiService = retrofit.create(ApiService.class);
    }

    /**
     * 创建OkHttp客户端
     * @return OkHttpClient实例
     */
    private OkHttpClient createOkHttpClient() {
        // 创建日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 创建请求拦截器
        RequestInterceptor requestInterceptor = new RequestInterceptor();
        // 创建响应拦截器
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();

        // 构建OkHttpClient
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor) // 添加请求拦截器
                .addInterceptor(loggingInterceptor) // 添加日志拦截器
                .addInterceptor(responseInterceptor) // 添加响应拦截器
                .build();
    }

    /**
     * 获取RetrofitClient实例
     * @return RetrofitClient实例
     */
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    /**
     * 获取ApiService实例
     * @return ApiService实例
     */
    public ApiService getApiService() {
        return apiService;
    }

    /**
     * 获取Retrofit实例
     * @return Retrofit实例
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }
}