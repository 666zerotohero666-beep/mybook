package com.mybook.data.remote.error;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * 错误处理工具类
 * 用于解析和处理各种网络错误
 */
public class ErrorHandler {
    /**
     * 将原始异常转换为自定义的NetworkException
     * @param throwable 原始异常
     * @return 自定义的NetworkException
     */
    public static NetworkException handleError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            // HTTP错误
            return handleHttpError((HttpException) throwable);
        } else if (throwable instanceof SocketTimeoutException) {
            // 超时错误
            return new NetworkException(ErrorType.TIMEOUT_ERROR, "请求超时，请稍后重试");
        } else if (throwable instanceof UnknownHostException) {
            // 网络连接错误
            return new NetworkException(ErrorType.NETWORK_ERROR, "网络连接失败，请检查网络设置");
        } else if (throwable instanceof IOException) {
            // IO错误
            return new NetworkException(ErrorType.NETWORK_ERROR, "网络请求失败，请稍后重试");
        } else {
            // 其他未知错误
            return new NetworkException(ErrorType.UNKNOWN_ERROR, "未知错误，请稍后重试", throwable);
        }
    }

    /**
     * 处理HTTP错误
     * @param httpException HTTP异常
     * @return 自定义的NetworkException
     */
    private static NetworkException handleHttpError(HttpException httpException) {
        int statusCode = httpException.code();
        String message = httpException.message();

        switch (statusCode) {
            case 401:
                return new NetworkException(ErrorType.UNAUTHORIZED_ERROR, statusCode, "未授权，请重新登录");
            case 403:
                return new NetworkException(ErrorType.FORBIDDEN_ERROR, statusCode, "禁止访问，权限不足");
            case 404:
                return new NetworkException(ErrorType.NOT_FOUND_ERROR, statusCode, "请求的资源不存在");
            case 500:
            case 501:
            case 502:
            case 503:
            case 504:
            case 505:
                return new NetworkException(ErrorType.SERVER_ERROR, statusCode, "服务器错误，请稍后重试");
            default:
                return new NetworkException(ErrorType.SERVER_ERROR, statusCode, "HTTP错误: " + message);
        }
    }

    /**
     * 根据错误类型获取友好的错误消息
     * @param errorType 错误类型
     * @return 友好的错误消息
     */
    public static String getFriendlyErrorMessage(ErrorType errorType) {
        switch (errorType) {
            case NETWORK_ERROR:
                return "网络连接失败，请检查网络设置";
            case SERVER_ERROR:
                return "服务器错误，请稍后重试";
            case UNAUTHORIZED_ERROR:
                return "未授权，请重新登录";
            case FORBIDDEN_ERROR:
                return "禁止访问，权限不足";
            case NOT_FOUND_ERROR:
                return "请求的资源不存在";
            case PARSE_ERROR:
                return "数据解析错误，请稍后重试";
            case TIMEOUT_ERROR:
                return "请求超时，请稍后重试";
            default:
                return "未知错误，请稍后重试";
        }
    }
}