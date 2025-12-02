package com.mybook.data.model;

import java.util.List;

public class Post {
    private String id;
    private User user;
    private String content;
    private List<String> images;
    private int likes;
    private int comments;
    private int shares;
    private boolean isLiked;
    private String createdAt;

    public Post() {
    }

    public Post(String id, User user, String content, List<String> images, int likes, int comments, int shares, boolean isLiked, String createdAt) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.images = images;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
        this.isLiked = isLiked;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}