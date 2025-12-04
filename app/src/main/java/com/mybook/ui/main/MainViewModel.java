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
    private List<Post> allPosts = new ArrayList<>(); // 保存所有帖子数据

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
            allPosts = createMockPosts();
            
            // 更新LiveData
            postsLiveData.postValue(allPosts);
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
            // 为每个帖子添加多张图片，用于测试图片翻页效果
            int imageCount = 1 + (i % 3); // 每个帖子有1-3张图片
            for (int j = 0; j < imageCount; j++) {
                if (i % 2 == 0) {
                    images.add("https://picsum.photos/id/" + (i * 5 + j + 1) + "/600/800");
                } else {
                    images.add("https://picsum.photos/id/" + (i * 5 + j + 1) + "/600/1200");
                }
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
                    i % 4 == 0, // 每4个帖子有一个已关注
                    i % 5 == 0, // 每5个帖子有一个已收藏
                    200 + i, // 收藏数
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
                // 如果allPosts为空，则重新创建模拟数据
                if (allPosts.isEmpty()) {
                    allPosts = createMockPosts();
                }
                // 每次调用都更新LiveData，确保新帖子能立即显示
                postsLiveData.postValue(new ArrayList<>(allPosts));
            } catch (InterruptedException e) {
                e.printStackTrace();
                errorMessage.postValue("获取帖子失败：" + e.getMessage());
            } finally {
                isLoading.postValue(false);
            }
        });
        
        return postsLiveData;
    }
    
    /**
     * 添加新帖子
     * @param post 新帖子对象
     */
    public void addPost(Post post) {
        executor.execute(() -> {
            try {
                isLoading.postValue(true);
                
                // 添加新帖子到列表开头
                allPosts.add(0, post);
                
                // 更新LiveData，创建新的ArrayList确保数据引用变化，触发UI刷新
                postsLiveData.postValue(new ArrayList<>(allPosts));
                
                // 保存到本地数据库
                postRepository.addPost(post);
            } catch (Exception e) {
                errorMessage.postValue("发布帖子失败：" + e.getMessage());
            } finally {
                isLoading.postValue(false);
            }
        });
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
            // 模拟点赞操作，更新本地数据
            List<Post> currentPosts = postsLiveData.getValue();
            if (currentPosts != null) {
                for (Post post : currentPosts) {
                    if (post.getId().equals(postId)) {
                        post.setLiked(!post.isLiked());
                        post.setLikes(post.getLikes() + (post.isLiked() ? 1 : -1));
                        break;
                    }
                }
                // 更新LiveData
                postsLiveData.setValue(new ArrayList<>(currentPosts));
            }
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