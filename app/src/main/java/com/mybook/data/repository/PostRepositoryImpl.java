package com.mybook.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mybook.data.model.Post;
import com.mybook.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostRepositoryImpl implements PostRepository {
    private final MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public PostRepositoryImpl() {
        loadMockData();
    }

    private void loadMockData() {
        executor.execute(() -> {
            // 模拟网络请求延迟
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 创建模拟数据
            List<Post> mockPosts = new ArrayList<>();

            // 创建模拟用户
            User user1 = new User("1", "用户1", "https://via.placeholder.com/100", "这是用户1的简介", 1200, 800);
            User user2 = new User("2", "用户2", "https://via.placeholder.com/100", "这是用户2的简介", 800, 500);

            // 创建模拟帖子
            List<String> images1 = new ArrayList<>();
            images1.add("https://via.placeholder.com/600x400");
            images1.add("https://via.placeholder.com/600x400");

            List<String> images2 = new ArrayList<>();
            images2.add("https://via.placeholder.com/600x400");

            mockPosts.add(new Post("1", user1, "这是第一个帖子的内容，分享一下我的生活点滴。", images1, 156, 23, 8, false, "2025-12-01 14:30"));
            mockPosts.add(new Post("2", user2, "今天天气真好，出来晒太阳了！", images2, 234, 45, 12, true, "2025-12-01 13:15"));
            mockPosts.add(new Post("3", user1, "新入手的相机，拍出来的效果真不错！", images1, 345, 67, 23, false, "2025-12-01 12:45"));
            mockPosts.add(new Post("4", user2, "今天学会了一道新菜，分享给大家！", images2, 189, 34, 9, true, "2025-12-01 11:20"));
            mockPosts.add(new Post("5", user1, "周末去了一趟海边，风景真美！", images1, 456, 89, 34, false, "2025-12-01 10:30"));

            postsLiveData.postValue(mockPosts);
        });
    }

    @Override
    public LiveData<List<Post>> getPosts() {
        return postsLiveData;
    }

    @Override
    public LiveData<Post> getPostById(String id) {
        MutableLiveData<Post> postLiveData = new MutableLiveData<>();
        executor.execute(() -> {
            List<Post> posts = postsLiveData.getValue();
            if (posts != null) {
                for (Post post : posts) {
                    if (post.getId().equals(id)) {
                        postLiveData.postValue(post);
                        break;
                    }
                }
            }
        });
        return postLiveData;
    }

    @Override
    public void likePost(String id) {
        executor.execute(() -> {
            List<Post> posts = postsLiveData.getValue();
            if (posts != null) {
                for (Post post : posts) {
                    if (post.getId().equals(id)) {
                        if (post.isLiked()) {
                            post.setLikes(post.getLikes() - 1);
                        } else {
                            post.setLikes(post.getLikes() + 1);
                        }
                        post.setLiked(!post.isLiked());
                        postsLiveData.postValue(posts);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void commentPost(String id, String comment) {
        executor.execute(() -> {
            List<Post> posts = postsLiveData.getValue();
            if (posts != null) {
                for (Post post : posts) {
                    if (post.getId().equals(id)) {
                        post.setComments(post.getComments() + 1);
                        postsLiveData.postValue(posts);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void sharePost(String id) {
        executor.execute(() -> {
            List<Post> posts = postsLiveData.getValue();
            if (posts != null) {
                for (Post post : posts) {
                    if (post.getId().equals(id)) {
                        post.setShares(post.getShares() + 1);
                        postsLiveData.postValue(posts);
                        break;
                    }
                }
            }
        });
    }
}