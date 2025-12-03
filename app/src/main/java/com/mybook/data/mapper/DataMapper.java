package com.mybook.data.mapper;

import com.mybook.data.model.Post;

/**
 * 数据转换类
 * 用于在不同数据源之间转换数据格式
 */
public class DataMapper {
    /**
     * 将远程Post转换为本地Post
     * @param remotePost 远程Post
     * @return 本地Post
     */
    public static Post remotePostToLocalPost(Post remotePost) {
        // 当前Post模型在本地和远程使用相同的结构，直接返回
        return remotePost;
    }

    /**
     * 将本地Post转换为远程Post
     * @param localPost 本地Post
     * @return 远程Post
     */
    public static Post localPostToRemotePost(Post localPost) {
        // 当前Post模型在本地和远程使用相同的结构，直接返回
        return localPost;
    }
}