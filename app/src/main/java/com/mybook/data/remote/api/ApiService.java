package com.mybook.data.remote.api;

import com.mybook.data.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;

/**
 * API服务接口
 * 定义所有网络请求方法
 */
public interface ApiService {
    /**
     * 获取所有帖子
     * @return 帖子列表的Call对象
     */
    @GET("/posts")
    Call<List<Post>> getPosts();

    /**
     * 根据ID获取帖子
     * @param postId 帖子ID
     * @return 帖子的Call对象
     */
    @GET("/posts/{id}")
    Call<Post> getPostById(@Path("id") String postId);

    /**
     * 点赞帖子
     * @param postId 帖子ID
     * @return 点赞结果的Call对象
     */
    @POST("/posts/{id}/like")
    Call<Void> likePost(@Path("id") String postId);

    /**
     * 评论帖子
     * @param postId 帖子ID
     * @return 评论结果的Call对象
     */
    @POST("/posts/{id}/comment")
    Call<Void> commentPost(@Path("id") String postId);

    /**
     * 分享帖子
     * @param postId 帖子ID
     * @return 分享结果的Call对象
     */
    @POST("/posts/{id}/share")
    Call<Void> sharePost(@Path("id") String postId);
}