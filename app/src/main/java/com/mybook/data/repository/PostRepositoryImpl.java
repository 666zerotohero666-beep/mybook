package com.mybook.data.repository;

import androidx.lifecycle.LiveData;

import com.mybook.data.local.AppDatabase;
import com.mybook.data.local.LocalDataSource;
import com.mybook.data.local.LocalDataSourceImpl;
import com.mybook.data.model.Post;
import com.mybook.data.remote.RemoteDataSource;
import com.mybook.data.remote.RemoteDataSourceImpl;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 帖子仓库实现类
 * 实现数据仓库模式，集成本地数据源和远程数据源
 */
public class PostRepositoryImpl implements PostRepository {
    private final LocalDataSource localDataSource;
    private final RemoteDataSource remoteDataSource;
    private final Executor executor;

    /**
     * 构造函数，用于依赖注入和测试
     * @param localDataSource 本地数据源
     * @param remoteDataSource 远程数据源
     */
    public PostRepositoryImpl(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * 构造函数
     * @param context 上下文
     */
    public PostRepositoryImpl(Context context) {
        // 获取数据库实例
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        this.localDataSource = new LocalDataSourceImpl(database);
        this.remoteDataSource = new RemoteDataSourceImpl();
        this.executor = Executors.newSingleThreadExecutor();
        // 移除构造函数中的网络请求，将网络请求延迟到实际需要时再执行
        // refreshPosts();
    }

    /**
     * 从远程刷新帖子数据到本地
     */
    private void refreshPosts() {
        remoteDataSource.getAllPosts(new RemoteDataSource.RemoteCallback<List<Post>>() {
            public void onSuccess(List<Post> data) {
                // 将远程数据转换为本地数据并保存
                executor.execute(() -> {
                    localDataSource.savePosts(data);
                });
            }

            public void onFailure(Throwable throwable) {
                // 远程数据获取失败，使用本地缓存
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public LiveData<List<Post>> getPosts() {
        // 从本地数据源获取帖子列表
        LiveData<List<Post>> localPosts = localDataSource.getAllPosts();
        // 同时从远程刷新数据，但添加错误处理
        try {
            refreshPosts();
        } catch (Exception e) {
            e.printStackTrace();
            // 网络请求失败不会导致应用崩溃
        }
        return localPosts;
    }

    @Override
    public LiveData<Post> getPostById(String id) {
        // 优先从本地获取帖子
        LiveData<Post> localPost = localDataSource.getPostById(id);
        // 同时从远程刷新该帖子数据
        remoteDataSource.getPostById(id, new RemoteDataSource.RemoteCallback<Post>() {
            public void onSuccess(Post data) {
                executor.execute(() -> {
                    localDataSource.savePost(data);
                });
            }

            public void onFailure(Throwable throwable) {
                // 远程获取失败，使用本地缓存
                throwable.printStackTrace();
            }
        });
        return localPost;
    }

    public void likePost(String id) {
        // 1. 先更新本地数据（乐观更新）
        executor.execute(() -> {
            // 2. 然后更新远程数据
            remoteDataSource.likePost(id, new RemoteDataSource.RemoteCallback<Boolean>() {
                public void onSuccess(Boolean data) {
                    // 远程更新成功，无需额外操作
                }

                public void onFailure(Throwable throwable) {
                    // 远程更新失败，需要回滚本地数据
                    throwable.printStackTrace();
                    // 重新从远程获取最新数据并更新本地
                    refreshPosts();
                }
            });
        });
    }

    @Override
    public void commentPost(String id, String comment) {
        // 1. 先更新本地数据（乐观更新）
        executor.execute(() -> {
            // 2. 然后更新远程数据
            remoteDataSource.commentPost(id, comment, new RemoteDataSource.RemoteCallback<Boolean>() {
                public void onSuccess(Boolean data) {
                    // 远程更新成功，无需额外操作
                }

                public void onFailure(Throwable throwable) {
                    // 远程更新失败，需要回滚本地数据
                    throwable.printStackTrace();
                    // 重新从远程获取最新数据并更新本地
                    refreshPosts();
                }
            });
        });
    }

    public void sharePost(String id) {
        // 1. 先更新本地数据（乐观更新）
        executor.execute(() -> {
            // 2. 然后更新远程数据
            remoteDataSource.sharePost(id, new RemoteDataSource.RemoteCallback<Boolean>() {
                public void onSuccess(Boolean data) {
                    // 远程更新成功，无需额外操作
                }

                public void onFailure(Throwable throwable) {
                    // 远程更新失败，需要回滚本地数据
                    throwable.printStackTrace();
                    // 重新从远程获取最新数据并更新本地
                    refreshPosts();
                }
            });
        });
    }
}