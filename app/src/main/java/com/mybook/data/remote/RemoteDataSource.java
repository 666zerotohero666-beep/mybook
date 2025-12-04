package com.mybook.data.remote;

import com.mybook.data.model.Post;

import java.util.List;

/**
 * 远程数据源接口
 * 定义从网络获取数据的操作
 */
public interface RemoteDataSource {
    /**
     * 获取所有帖子
     * @param callback 回调接口，用于处理请求结果
     */
    void getAllPosts(RemoteCallback<List<Post>> callback);

    /**
     * 根据ID获取帖子
     * @param postId 帖子ID
     * @param callback 回调接口，用于处理请求结果
     */
    void getPostById(String postId, RemoteCallback<Post> callback);

    /**
     * 点赞帖子
     * @param postId 帖子ID
     * @param callback 回调接口，用于处理请求结果
     */
    void likePost(String postId, RemoteCallback<Boolean> callback);

    /**
     * 评论帖子
     * @param postId 帖子ID
     * @param comment 评论内容
     * @param callback 回调接口，用于处理请求结果
     */
    void commentPost(String postId, String comment, RemoteCallback<Boolean> callback);

    /**
     * 分享帖子
     * @param postId 帖子ID
     * @param callback 回调接口，用于处理请求结果
     */
    void sharePost(String postId, RemoteCallback<Boolean> callback);
    
    /**
     * 添加新帖子
     * @param post 新帖子对象
     * @param callback 回调接口，用于处理请求结果
     */
    void addPost(Post post, RemoteCallback<Boolean> callback);

    /**
     * 远程回调接口
     * @param <T> 回调数据类型
     */
    interface RemoteCallback<T> {
        /**
         * 请求成功时调用
         * @param data 返回的数据
         */
        void onSuccess(T data);

        /**
         * 请求失败时调用
         * @param throwable 失败原因
         */
        void onFailure(Throwable throwable);
    }
}