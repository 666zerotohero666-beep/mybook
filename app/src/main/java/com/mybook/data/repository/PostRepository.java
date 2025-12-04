package com.mybook.data.repository;

import androidx.lifecycle.LiveData;

import com.mybook.data.model.Post;

import java.util.List;

public interface PostRepository {
    LiveData<List<Post>> getPosts();
    LiveData<Post> getPostById(String id);
    void likePost(String id);
    void commentPost(String id, String comment);
    void sharePost(String id);
    void addPost(Post post);
}