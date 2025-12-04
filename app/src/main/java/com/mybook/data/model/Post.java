package com.mybook.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * 帖子实体类
 * 用于Room数据库的帖子表
 */
@Entity(tableName = "posts")
public class Post {
    @PrimaryKey
    @NonNull
    private String id;
    // Room不支持直接存储复杂对象，需要将User对象的字段拆分为基本类型
    private String userId;
    private String name;
    private String avatar;
    private String bio;
    private int followers;
    private int following;
    private String content;
    private List<String> images;
    private int likes;
    private int comments;
    private int shares;
    private boolean isLiked;
    private boolean isFollowing;
    private boolean isSaved;
    private int saves;
    private String createdAt;

    public Post() {
    }

    /**
     * 完整构造函数，包含所有字段
     */
    @Ignore
    public Post(String id, String userId, String name, String avatar, String bio, int followers, int following, 
                String content, List<String> images, int likes, int comments, int shares, boolean isLiked, boolean isFollowing, boolean isSaved, int saves, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.bio = bio;
        this.followers = followers;
        this.following = following;
        this.content = content;
        this.images = images;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
        this.isLiked = isLiked;
        this.isFollowing = isFollowing;
        this.isSaved = isSaved;
        this.saves = saves;
        this.createdAt = createdAt;
    }

    /**
     * 从User对象构造Post的辅助构造函数
     */
    @Ignore
    public Post(String id, User user, String content, List<String> images, int likes, int comments, int shares, boolean isLiked, boolean isFollowing, boolean isSaved, int saves, String createdAt) {
        this.id = id;
        if (user != null) {
            this.userId = user.getId();
            this.name = user.getName();
            this.avatar = user.getAvatar();
            this.bio = user.getBio();
            this.followers = user.getFollowers();
            this.following = user.getFollowing();
        }
        this.content = content;
        this.images = images;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
        this.isLiked = isLiked;
        this.isFollowing = isFollowing;
        this.isSaved = isSaved;
        this.saves = saves;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}