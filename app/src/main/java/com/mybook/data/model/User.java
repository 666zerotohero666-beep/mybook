package com.mybook.data.model;

public class User {
    private String id;
    private String name;
    private String avatar;
    private String bio;
    private int followers;
    private int following;

    public User() {
    }

    public User(String id, String name, String avatar, String bio, int followers, int following) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.bio = bio;
        this.followers = followers;
        this.following = following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}