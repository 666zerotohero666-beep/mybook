package com.mybook.data.remote.error;

/**
 * 网络异常类
 * 封装网络请求过程中可能出现的各种错误
 */
public class NetworkException extends Exception {
    private final ErrorType errorType;
    private final int errorCode;

    /**
     * 构造函数
     * @param errorType 错误类型
     * @param message 错误消息
     */
    public NetworkException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.errorCode = -1;
    }

    /**
     * 构造函数
     * @param errorType 错误类型
     * @param message 错误消息
     * @param cause 原始异常
     */
    public NetworkException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.errorCode = -1;
    }

    /**
     * 构造函数
     * @param errorType 错误类型
     * @param errorCode 错误代码
     * @param message 错误消息
     */
    public NetworkException(ErrorType errorType, int errorCode, String message) {
        super(message);
        this.errorType = errorType;
        this.errorCode = errorCode;
    }

    /**
     * 获取错误类型
     * @return 错误类型
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * 获取错误代码
     * @return 错误代码
     */
    public int getErrorCode() {
        return errorCode;
    }
}