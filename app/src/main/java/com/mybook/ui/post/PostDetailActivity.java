package com.mybook.ui.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.mybook.R;
import com.mybook.data.model.Post;

/**
 * 帖子详情页Activity
 */
public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST_ID = "extra_post_id";

    private MaterialToolbar toolbar;
    private ImageView userAvatar;
    private TextView userName;
    private TextView postTime;
    private TextView postTitle;
    private TextView postContent;
    private ImageView postImage;
    private ImageView likeButton;
    private TextView likeCount;
    private ImageView commentButton;
    private TextView commentCount;
    private ImageView shareButton;
    private TextView shareCount;

    private String postId;
    private Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_post_detail);

            // 初始化UI组件
            initUI();

            // 设置点击事件
            setClickListeners();

            // 获取帖子ID
            getPostIdFromIntent();

            // 加载帖子详情
            loadPostDetail();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "页面初始化失败，请返回重试", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 初始化UI组件
     */
    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        userAvatar = findViewById(R.id.user_avatar);
        userName = findViewById(R.id.user_name);
        postTime = findViewById(R.id.post_time);
        postTitle = findViewById(R.id.post_title);
        postContent = findViewById(R.id.post_content);
        postImage = findViewById(R.id.post_image);
        likeButton = findViewById(R.id.like_button);
        likeCount = findViewById(R.id.like_count);
        commentButton = findViewById(R.id.comment_button);
        commentCount = findViewById(R.id.comment_count);
        shareButton = findViewById(R.id.share_button);
        shareCount = findViewById(R.id.share_count);

        // 设置顶部栏返回按钮
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * 设置点击事件
     */
    private void setClickListeners() {
        // 顶部栏返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 点赞按钮点击事件
        likeButton.setOnClickListener(v -> likePost());

        // 评论按钮点击事件
        commentButton.setOnClickListener(v -> commentPost());

        // 分享按钮点击事件
        shareButton.setOnClickListener(v -> sharePost());
    }

    /**
     * 从Intent中获取帖子ID
     */
    private void getPostIdFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra(EXTRA_POST_ID);
        }
    }

    /**
     * 加载帖子详情
     */
    private void loadPostDetail() {
        try {
            // 这里简化实现，实际项目中应该根据帖子ID从服务器或本地数据库获取帖子详情
            // 暂时使用模拟数据
            currentPost = createMockPost();
            
            // 展示帖子详情
            displayPostDetail(currentPost);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载帖子详情失败", Toast.LENGTH_SHORT).show();
            // 显示默认数据或返回
        }    
    }

    /**
     * 创建模拟帖子数据
     * @return 模拟帖子对象
     */
    private Post createMockPost() {
        // 创建一个模拟帖子对象
        return new Post(
                postId != null ? postId : "post_001",
                "user_001",
                "测试用户",
                "https://picsum.photos/id/1/100/100",
                "这是一个测试用户的简介",
                100,
                50,
                "这是一个测试帖子的标题",
                java.util.Arrays.asList("https://picsum.photos/id/1/600/800"),
                1000,
                500,
                100,
                false,
                "2025-12-04T12:00:00Z"
        );
    }

    /**
     * 展示帖子详情
     * @param post 帖子对象
     */
    private void displayPostDetail(Post post) {
        if (post == null) {
            return;
        }

        try {
            // 加载用户头像
            if (userAvatar != null) {
                Glide.with(this)
                        .load(post.getAvatar() != null ? post.getAvatar() : "")
                        .circleCrop()
                        .placeholder(R.drawable.ic_placeholder_circle)
                        .error(R.drawable.ic_placeholder_circle)
                        .into(userAvatar);
            }

            // 设置用户名
            if (userName != null) {
                userName.setText(post.getName() != null ? post.getName() : "未知用户");
            }

            // 设置发布时间
            if (postTime != null) {
                postTime.setText(formatDate(post.getCreatedAt() != null ? post.getCreatedAt() : ""));
            }

            // 设置帖子标题
            if (postTitle != null) {
                postTitle.setText(post.getContent() != null ? post.getContent() : "");
            }

            // 设置帖子正文
            if (postContent != null) {
                postContent.setText(post.getContent() != null ? post.getContent() : "");
            }

            // 设置帖子图片
            if (postImage != null && post.getImages() != null && !post.getImages().isEmpty()) {
                Glide.with(this)
                        .load(post.getImages().get(0))
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder_square)
                        .error(R.drawable.ic_placeholder_square)
                        .into(postImage);
            }

            // 设置点赞数量
            if (likeCount != null) {
                likeCount.setText(String.valueOf(post.getLikes()));
            }

            // 设置评论数量
            if (commentCount != null) {
                commentCount.setText(String.valueOf(post.getComments()));
            }

            // 设置分享数量
            if (shareCount != null) {
                shareCount.setText(String.valueOf(post.getShares()));
            }

            // 设置点赞状态
            if (likeButton != null) {
                likeButton.setImageResource(post.isLiked() ? R.drawable.ic_placeholder_circle : R.drawable.ic_placeholder_circle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "显示帖子详情失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 格式化日期
     * @param dateString ISO格式的日期字符串
     * @return 格式化后的日期字符串
     */
    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        
        try {
            java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault());
            java.util.Date date = isoFormat.parse(dateString);
            if (date != null) {
                java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
                return outputFormat.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    /**
     * 点赞帖子
     */
    private void likePost() {
        // 这里简化实现，实际项目中应该调用API点赞帖子
        if (currentPost != null) {
            currentPost.setLiked(!currentPost.isLiked());
            int newLikes = currentPost.getLikes() + (currentPost.isLiked() ? 1 : -1);
            currentPost.setLikes(newLikes);
            
            // 更新UI
            likeCount.setText(String.valueOf(currentPost.getLikes()));
            likeButton.setImageResource(currentPost.isLiked() ? R.drawable.ic_placeholder_circle : R.drawable.ic_placeholder_circle);
            
            Toast.makeText(this, currentPost.isLiked() ? "点赞成功" : "取消点赞", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 评论帖子
     */
    private void commentPost() {
        // 这里简化实现，实际项目中应该跳转到评论页或显示评论输入框
        Toast.makeText(this, "评论功能将在后续实现", Toast.LENGTH_SHORT).show();
    }

    /**
     * 分享帖子
     */
    private void sharePost() {
        // 这里简化实现，实际项目中应该调用系统分享功能
        Toast.makeText(this, "分享功能将在后续实现", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}