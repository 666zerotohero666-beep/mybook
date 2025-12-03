package com.mybook.data.local;

import androidx.lifecycle.LiveData;

import com.mybook.data.local.dao.PostDao;
import com.mybook.data.model.Post;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 本地数据源实现
 * 使用Room数据库存储数据
 */
public class LocalDataSourceImpl implements LocalDataSource {
    private final PostDao postDao;
    private final Executor executor;

    /**
     * 构造函数，用于依赖注入和测试
     * @param postDao Post DAO实例
     */
    public LocalDataSourceImpl(PostDao postDao) {
        this.postDao = postDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * 构造函数
     * @param database 应用数据库实例
     */
    public LocalDataSourceImpl(AppDatabase database) {
        this.postDao = database.postDao();
        this.executor = Executors.newSingleThreadExecutor();
        
        // 初始化测试数据已移除，由ViewModel负责生成模拟数据
    }

    @Override
    public LiveData<List<Post>> getAllPosts() {
        // 直接返回Room的LiveData，它会自动处理数据变化通知
        return postDao.getAllPosts();
    }

    @Override
    public LiveData<Post> getPostById(String postId) {
        // 直接返回Room的LiveData
        return postDao.getPostById(postId);
    }

    @Override
    public void savePost(Post post) {
        // 插入操作在后台线程执行
        executor.execute(() -> postDao.insertPost(post));
    }

    @Override
    public void savePosts(List<Post> posts) {
        // 插入列表操作在后台线程执行
        executor.execute(() -> postDao.insertPosts(posts));
    }

    @Override
    public void deletePost(String postId) {
        // 删除操作在后台线程执行
        executor.execute(() -> postDao.deletePostById(postId));
    }

    @Override
    public void clearAllPosts() {
        // 清空操作在后台线程执行
        executor.execute(() -> postDao.clearAllPosts());
    }
}