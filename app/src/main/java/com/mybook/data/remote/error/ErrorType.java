package com.mybook.data.remote.error;

/**
 * 错误类型枚举
 * 定义所有可能的错误类型
 */
public enum ErrorType {
    /**
     * 网络错误
     */
    NETWORK_ERROR,

    /**
     * 服务器错误
     */
    SERVER_ERROR,

    /**
     * 未授权错误
     */
    UNAUTHORIZED_ERROR,

    /**
     * 禁止访问错误
     */
    FORBIDDEN_ERROR,

    /**
     * 资源不存在错误
     */
    NOT_FOUND_ERROR,

    /**
     * 解析错误
     */
    PARSE_ERROR,

    /**
     * 超时错误
     */
    TIMEOUT_ERROR,

    /**
     * 未知错误
     */
    UNKNOWN_ERROR
}