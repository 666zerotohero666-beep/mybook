package com.mybook.data.remote.interceptor;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

/**
 * 响应拦截器
 * 用于处理公共响应和错误
 */
public class ResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 执行请求，获取响应
        Response response = chain.proceed(chain.request());

        // 处理响应
        handleResponse(response);

        return response;
    }

    /**
     * 处理响应
     * @param response 响应对象
     */
    private void handleResponse(Response response) {
        int code = response.code();

        // 根据状态码处理响应
        switch (code) {
            case 200:
            case 201:
                // 成功响应，不需要特殊处理
                break;
            case 401:
                // 未授权，需要重新登录
                handleUnauthorized();
                break;
            case 403:
                // 禁止访问，可能是权限问题
                handleForbidden();
                break;
            case 404:
                // 资源不存在
                handleNotFound();
                break;
            case 500:
                // 服务器错误
                handleServerError();
                break;
            default:
                // 其他错误
                handleOtherError(code);
                break;
        }
    }

    /**
     * 处理未授权错误
     */
    private void handleUnauthorized() {
        // 这里可以发送广播或事件，通知UI层需要重新登录
        // 例如使用EventBus发送登录事件
        // EventBus.getInstance().post(new LoginRequiredEvent());
    }

    /**
     * 处理禁止访问错误
     */
    private void handleForbidden() {
        // 处理权限问题
    }

    /**
     * 处理资源不存在错误
     */
    private void handleNotFound() {
        // 处理资源不存在问题
    }

    /**
     * 处理服务器错误
     */
    private void handleServerError() {
        // 处理服务器错误
    }

    /**
     * 处理其他错误
     * @param code 错误状态码
     */
    private void handleOtherError(int code) {
        // 处理其他错误
    }
}