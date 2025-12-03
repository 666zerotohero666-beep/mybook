package com.mybook.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mybook.R;
import com.mybook.databinding.ActivityMainBinding;
import com.mybook.data.model.Post;
import com.mybook.ui.main.adapters.PostAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用主界面
 */
public class MainActivity extends AppCompatActivity implements PostAdapter.OnPostClickListener {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private PostAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    private boolean isLoading = false;
    private boolean hasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 使用DataBinding初始化布局
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        // 设置标题
        setTitle(getString(R.string.app_name));
        
        // 设置数据绑定
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        
        // 初始化UI
        initUI();
        
        // 观察帖子数据
        observePosts();
        
        // 观察加载状态
        observeLoading();
        
        // 观察错误信息
        observeError();
        
        // 初始加载数据
        loadPosts();
    }

    /**
     * 初始化UI组件
     */
    private void initUI() {
        // 初始化RecyclerView
        initRecyclerView();
        
        // 初始化下拉刷新
        initSwipeRefresh();
        
        // 初始化重试按钮点击事件
        initRetryButtons();
        
        // 初始化底部发布按钮点击事件
        initPublishButton();
    }
    
    /**
     * 初始化底部发布按钮点击事件
     */
    private void initPublishButton() {
        // 设置发布按钮点击事件
        com.google.android.material.floatingactionbutton.FloatingActionButton fabPost = findViewById(R.id.fab_post);
        fabPost.setOnClickListener(v -> {
            // 跳转到发布页
            startActivity(new android.content.Intent(MainActivity.this, com.mybook.ui.post.PostActivity.class));
        });
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        // 创建StaggeredGridLayoutManager，实现双列瀑布流布局
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 设置布局管理器
        binding.postRecyclerView.setLayoutManager(layoutManager);
        
        // 创建适配器
        postAdapter = new PostAdapter(this, posts, this);
        
        // 设置适配器
        binding.postRecyclerView.setAdapter(postAdapter);
        
        // 设置滚动监听器，实现上拉加载更多
        binding.postRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当滚动停止且有更多数据时，加载更多
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading && hasMore) {
                        loadMorePosts();
                    }
                }
            }
        });
    }

    /**
     * 初始化下拉刷新
     */
    private void initSwipeRefresh() {
        // 设置刷新颜色
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.secondary);
        
        // 设置刷新监听器
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            // 刷新数据
            refreshPosts();
        });
    }

    /**
     * 初始化重试按钮点击事件
     */
    private void initRetryButtons() {
        // 空数据状态重试按钮
        binding.emptyStateLayout.findViewById(R.id.empty_state_button).setOnClickListener(v -> {
            refreshPosts();
        });
        
        // 网络错误状态重试按钮
        binding.networkErrorLayout.findViewById(R.id.network_error_button).setOnClickListener(v -> {
            refreshPosts();
        });
    }

    /**
     * 观察帖子数据
     */
    private void observePosts() {
        viewModel.getPosts().observe(this, postList -> {
            if (postList != null) {
                posts.clear();
                posts.addAll(postList);
                postAdapter.updatePosts(posts);
                
                // 显示/隐藏空数据状态
                updateEmptyState();
                
                // 隐藏网络错误状态
                binding.networkErrorLayout.setVisibility(View.GONE);
            } else {
                // 显示空数据状态
                updateEmptyState();
            }
        });
    }

    /**
     * 观察加载状态
     */
    private void observeLoading() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            this.isLoading = isLoading;
            binding.swipeRefreshLayout.setRefreshing(isLoading);
        });
    }

    /**
     * 观察错误信息
     */
    private void observeError() {
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // 显示网络错误状态
                binding.networkErrorLayout.setVisibility(View.VISIBLE);
                
                // 隐藏空数据状态
                binding.emptyStateLayout.setVisibility(View.GONE);
                
                // 显示错误信息
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                // 隐藏网络错误状态
                binding.networkErrorLayout.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始加载数据
     */
    private void loadPosts() {
        viewModel.getPosts();
    }

    /**
     * 刷新数据
     */
    private void refreshPosts() {
        // 重置加载状态
        hasMore = true;
        
        // 加载数据
        loadPosts();
    }

    /**
     * 加载更多数据
     */
    private void loadMorePosts() {
        if (isLoading || !hasMore) {
            return;
        }
        
        // 设置加载更多状态
        postAdapter.setLoadingMore(true);
        isLoading = true;
        
        // 模拟加载更多数据
        // 这里将在后续实现中替换为实际的加载更多逻辑
        new Thread(() -> {
            try {
                // 模拟网络请求延迟
                Thread.sleep(2000);
                
                // 运行在主线程
                runOnUiThread(() -> {
                    // 取消加载更多状态
                    postAdapter.setLoadingMore(false);
                    isLoading = false;
                    
                    // 模拟没有更多数据
                    hasMore = false;
                    
                    // 显示没有更多数据提示
                    Toast.makeText(this, getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 更新空数据状态
     */
    private void updateEmptyState() {
        if (posts.isEmpty()) {
            // 显示空数据状态
            binding.emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            // 隐藏空数据状态
            binding.emptyStateLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 帖子点击事件
     * @param post 帖子对象
     */
    @Override
    public void onPostClick(Post post) {
        try {
            // 添加详细日志
            Log.d("MainActivity", "onPostClick called");
            Log.d("MainActivity", "Post object: " + post);
            Log.d("MainActivity", "Post ID: " + (post != null ? post.getId() : "null"));
            
            // 跳转到帖子详情页
            if (post != null && post.getId() != null) {
                Log.d("MainActivity", "Creating Intent");
                Log.d("MainActivity", "PostDetailActivity class: " + com.mybook.ui.post.PostDetailActivity.class);
                Intent intent = new Intent(MainActivity.this, com.mybook.ui.post.PostDetailActivity.class);
                Log.d("MainActivity", "Intent created successfully");
                intent.putExtra(com.mybook.ui.post.PostDetailActivity.EXTRA_POST_ID, post.getId());
                Log.d("MainActivity", "Put extra EXTRA_POST_ID: " + post.getId());
                Log.d("MainActivity", "Starting Activity");
                startActivity(intent);
                Log.d("MainActivity", "Activity started successfully");
            } else {
                Log.d("MainActivity", "Invalid post or postId");
                Toast.makeText(this, "帖子信息无效", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in onPostClick", e);
            Toast.makeText(this, "跳转失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点赞点击事件
     * @param post 帖子对象
     */
    @Override
    public void onLikeClick(Post post) {
        // 处理点赞逻辑
        viewModel.likePost(post.getId());
        Toast.makeText(this, "点赞：" + post.getId(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 评论点击事件
     * @param post 帖子对象
     */
    @Override
    public void onCommentClick(Post post) {
        // 这里将在后续实现中添加评论点击逻辑
        Toast.makeText(this, "评论：" + post.getId(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 收藏点击事件
     * @param post 帖子对象
     */
    @Override
    public void onSaveClick(Post post) {
        // 这里将在后续实现中添加收藏点击逻辑
        Toast.makeText(this, "收藏：" + post.getId(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载更多事件
     */
    @Override
    public void onLoadMore() {
        // 加载更多数据
        loadMorePosts();
    }
}