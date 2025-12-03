package com.mybook.ui.main;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mybook.data.model.Post;
import com.mybook.data.repository.PostRepository;
import com.mybook.data.repository.PostRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {
    private final PostRepository postRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public MainViewModel(android.app.Application application) {
        super(application);
        this.postRepository = new PostRepositoryImpl(application);
        // 初始化模拟数据
        initMockData();
    }

    /**
     * 初始化模拟数据
     */
    private void initMockData() {
        executor.execute(() -> {
            // 模拟网络延迟
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 创建模拟帖子数据
            List<Post> mockPosts = createMockPosts();
            
            // 更新LiveData
            postsLiveData.postValue(mockPosts);
        });
    }

    /**
     * 创建模拟帖子数据
     * @return 模拟帖子列表
     */
    private List<Post> createMockPosts() {
        List<Post> posts = new ArrayList<>();
        
        // 创建10条模拟帖子
        for (int i = 0; i < 10; i++) {
            List<String> images = new ArrayList<>();
            // 为每个帖子添加不同高度的图片
            if (i % 2 == 0) {
                images.add("https://picsum.photos/id/" + (i + 1) + "/600/800");
            } else {
                images.add("https://picsum.photos/id/" + (i + 1) + "/600/1200");
            }
            
            Post post = new Post(
                    "post_" + i,
                    "user_" + i,
                    "用户" + i,
                    "https://picsum.photos/id/" + (i + 1) + "/100/100",
                    "这是用户" + i + "的简介",
                    100 + i,
                    50 + i,
                    "这是第" + i + "条帖子的内容，用于测试瀑布流布局和下拉刷新功能。" +
                            "内容长度适中，确保能够在UI上正常显示。",
                    images,
                    1000 + i,
                    500 + i,
                    100 + i,
                    i % 3 == 0, // 每3个帖子有一个已点赞
                    "2025-12-04T12:0" + i + ":00Z"
            );
            
            posts.add(post);
        }
        
        return posts;
    }

    /**
     * 获取帖子列表
     * @return 帖子列表的LiveData
     */
    public LiveData<List<Post>> getPosts() {
        isLoading.setValue(true);
        
        // 模拟网络请求，延迟1秒后返回数据
        executor.execute(() -> {
            try {
                Thread.sleep(1000);
                // 使用模拟数据
                List<Post> mockPosts = createMockPosts();
                postsLiveData.postValue(mockPosts);
            } catch (InterruptedException e) {
                e.printStackTrace();
                errorMessage.postValue("获取帖子失败：" + e.getMessage());
            } finally {
                isLoading.postValue(false);
            }
        });
        
        return postsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void likePost(String postId) {
        try {
            isLoading.setValue(true);
            postRepository.likePost(postId);
        } catch (Exception e) {
            errorMessage.setValue("点赞失败：" + e.getMessage());
        } finally {
            isLoading.setValue(false);
        }
    }

    public void commentPost(String postId, String comment) {
        try {
            isLoading.setValue(true);
            postRepository.commentPost(postId, comment);
        } catch (Exception e) {
            errorMessage.setValue("评论失败：" + e.getMessage());
        } finally {
            isLoading.setValue(false);
        }
    }

    public void sharePost(String postId) {
        try {
            isLoading.setValue(true);
            postRepository.sharePost(postId);
        } catch (Exception e) {
            errorMessage.setValue("分享失败：" + e.getMessage());
        } finally {
            isLoading.setValue(false);
        }
    }
}