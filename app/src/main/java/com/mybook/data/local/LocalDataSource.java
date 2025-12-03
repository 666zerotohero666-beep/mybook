package com.mybook.data.local;

import androidx.lifecycle.LiveData;

import com.mybook.data.model.Post;

import java.util.List;

/**
 * 本地数据源接口
 * 定义本地数据的增删改查操作
 */
public interface LocalDataSource {
    /**
     * 获取所有帖子
     * @return 帖子列表的LiveData
     */
    LiveData<List<Post>> getAllPosts();

    /**
     * 根据ID获取帖子
     * @param postId 帖子ID
     * @return 帖子的LiveData
     */
    LiveData<Post> getPostById(String postId);

    /**
     * 保存帖子到本地
     * @param post 要保存的帖子
     */
    void savePost(Post post);

    /**
     * 保存帖子列表到本地
     * @param posts 要保存的帖子列表
     */
    void savePosts(List<Post> posts);

    /**
     * 删除帖子
     * @param postId 要删除的帖子ID
     */
    void deletePost(String postId);

    /**
     * 清空所有帖子
     */
    void clearAllPosts();
}