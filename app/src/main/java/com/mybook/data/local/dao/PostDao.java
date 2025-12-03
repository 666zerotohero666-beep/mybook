package com.mybook.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mybook.data.model.Post;

import java.util.List;

/**
 * Post数据访问对象
 * 定义对posts表的操作方法
 */
@Dao
public interface PostDao {
    /**
     * 获取所有帖子
     * @return 帖子列表的LiveData
     */
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    LiveData<List<Post>> getAllPosts();

    /**
     * 根据ID获取帖子
     * @param postId 帖子ID
     * @return 帖子的LiveData
     */
    @Query("SELECT * FROM posts WHERE id = :postId")
    LiveData<Post> getPostById(String postId);

    /**
     * 插入帖子，冲突时替换
     * @param post 要插入的帖子
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);

    /**
     * 插入帖子列表，冲突时替换
     * @param posts 要插入的帖子列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPosts(List<Post> posts);

    /**
     * 更新帖子
     * @param post 要更新的帖子
     */
    @Update
    void updatePost(Post post);

    /**
     * 删除帖子
     * @param post 要删除的帖子
     */
    @Delete
    void deletePost(Post post);

    /**
     * 根据ID删除帖子
     * @param postId 要删除的帖子ID
     */
    @Query("DELETE FROM posts WHERE id = :postId")
    void deletePostById(String postId);

    /**
     * 清空所有帖子
     */
    @Query("DELETE FROM posts")
    void clearAllPosts();
}