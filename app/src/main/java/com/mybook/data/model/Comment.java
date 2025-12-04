package com.mybook.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 评论实体类
 * 用于Room数据库的评论表
 */
@Entity(tableName = "comments")
public class Comment {
    @PrimaryKey
    @NonNull
    private String id;
    private String postId;
    private String userId;
    private String name;
    private String avatar;
    private String content;
    private String createdAt;

    public Comment() {
    }

    /**
     * 完整构造函数，包含所有字段
     */
    public Comment(String id, String postId, String userId, String name, String avatar, String content, String createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
