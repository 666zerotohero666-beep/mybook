package com.mybook.ui.post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.mybook.R;
import com.mybook.data.model.Comment;
import com.mybook.data.model.Post;
import com.mybook.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子详情页Activity
 */
public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST_ID = "extra_post_id";

    private MaterialToolbar toolbar;
    private ImageView toolbarUserAvatar;
    private TextView toolbarUserName;
    private com.google.android.material.button.MaterialButton toolbarFollowButton;
    private ImageView toolbarShareButton;
    
    private TextView postTime;
    private TextView postTitle;
    private TextView postContent;
    private androidx.viewpager2.widget.ViewPager2 mediaViewPager;
    private LinearLayout imageIndicator;
    private ImageView likeButton;
    private TextView likeCount;
    private ImageView commentButton;
    private TextView commentCount;
    private ImageView saveButton;
    private TextView saveCount;
    private EditText commentInput;
    private ImageView sendButton;
    private androidx.recyclerview.widget.RecyclerView commentsRecyclerView;
    
    private MediaAdapter mediaAdapter;
    private CommentAdapter commentAdapter;
    private List<Comment> comments = new ArrayList<>();

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
            ToastUtil.showShort(this, "页面初始化失败，请返回重试");
            finish();
        }
    }

    /**
     * 初始化UI组件
     */
    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        toolbarUserAvatar = findViewById(R.id.toolbar_user_avatar);
        toolbarUserName = findViewById(R.id.toolbar_user_name);
        toolbarFollowButton = findViewById(R.id.toolbar_follow_button);
        toolbarShareButton = findViewById(R.id.toolbar_share_button);
        
        postTime = findViewById(R.id.post_time);
        postTitle = findViewById(R.id.post_title);
        postContent = findViewById(R.id.post_content);
        mediaViewPager = findViewById(R.id.media_view_pager);
        imageIndicator = findViewById(R.id.image_indicator);
        likeButton = findViewById(R.id.like_button);
        likeCount = findViewById(R.id.like_count);
        commentButton = findViewById(R.id.comment_button);
        commentCount = findViewById(R.id.comment_count);
        saveButton = findViewById(R.id.save_button);
        saveCount = findViewById(R.id.save_count);
        commentInput = findViewById(R.id.comment_input);
        sendButton = findViewById(R.id.send_button);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);

        // 设置顶部栏返回按钮
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        // 初始化评论列表
        initCommentsRecyclerView();
    }
    
    /**
     * 初始化评论列表
     */
    private void initCommentsRecyclerView() {
        // 创建并设置适配器
        commentAdapter = new CommentAdapter(comments);
        commentsRecyclerView.setAdapter(commentAdapter);
        
        // 设置布局管理器
        androidx.recyclerview.widget.LinearLayoutManager layoutManager = new androidx.recyclerview.widget.LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 设置点击事件
     */
    private void setClickListeners() {
        // 顶部栏返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
            // 设置返回过渡动画
            overridePendingTransition(R.anim.activity_return_enter, R.anim.activity_return_exit);
        });

        // 关注按钮点击事件
        toolbarFollowButton.setOnClickListener(v -> followUser());

        // 点赞按钮点击事件
        likeButton.setOnClickListener(v -> likePost());

        // 评论按钮点击事件
        commentButton.setOnClickListener(v -> commentPost());

        // 收藏按钮点击事件
        saveButton.setOnClickListener(v -> savePost());
        
        // 分享按钮点击事件
        toolbarShareButton.setOnClickListener(v -> sharePost());
        
        // 发送评论按钮点击事件
        sendButton.setOnClickListener(v -> sendComment());
        
        // 评论输入框发送事件
        commentInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendComment();
                return true;
            }
            return false;
        });
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
            // 暂时使用模拟数据，但确保ID与传入ID一致
            currentPost = createMockPost();
            
            // 展示帖子详情
            displayPostDetail(currentPost);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(this, "加载帖子详情失败");
            // 显示默认数据或返回
        }    
    }

    /**
     * 创建模拟帖子数据
     * @return 模拟帖子对象
     */
    private Post createMockPost() {
        // 解析帖子ID，生成对应的模拟数据
        int postIndex = 0;
        if (postId != null && postId.startsWith("post_")) {
            try {
                postIndex = Integer.parseInt(postId.replace("post_", ""));
            } catch (NumberFormatException e) {
                postIndex = 0;
            }
        }
        
        // 创建多张图片列表
        java.util.List<String> images = new java.util.ArrayList<>();
        // 根据帖子索引生成1-3张图片
        int imageCount = 1 + (postIndex % 3);
        for (int i = 0; i < imageCount; i++) {
            images.add("https://picsum.photos/id/" + (postIndex * 5 + i + 1) + "/600/" + (800 + (i * 200)));
        }
        
        // 创建一个模拟帖子对象，内容与首页保持一致
        return new Post(
                postId != null ? postId : "post_001",
                "user_" + postIndex,
                "用户" + postIndex,
                "https://picsum.photos/id/" + (postIndex + 1) + "/100/100",
                "这是用户" + postIndex + "的简介",
                100 + postIndex,
                50 + postIndex,
                "这是第" + postIndex + "条帖子的内容，用于测试瀑布流布局和详情页展示。" +
                        "内容长度适中，确保能够在UI上正常显示。",
                images,
                1000 + postIndex,
                500 + postIndex,
                100 + postIndex,
                postIndex % 3 == 0, // 每3个帖子有一个已点赞
                postIndex % 4 == 0, // 每4个帖子有一个已关注
                postIndex % 5 == 0, // 每5个帖子有一个已收藏
                200 + postIndex, // 收藏数
                "2025-12-04T12:0" + (postIndex % 10) + ":00Z"
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
            // 加载用户头像 - 顶部工具栏
            if (toolbarUserAvatar != null) {
                Glide.with(this)
                        .load(post.getAvatar() != null ? post.getAvatar() : "")
                        .circleCrop()
                        .placeholder(R.drawable.ic_placeholder_circle)
                        .error(R.drawable.ic_placeholder_circle)
                        .into(toolbarUserAvatar);
            }

            // 设置用户名 - 顶部工具栏
            if (toolbarUserName != null) {
                toolbarUserName.setText(post.getName() != null ? post.getName() : "未知用户");
            }

            // 设置发布时间
            if (postTime != null) {
                postTime.setText(formatDate(post.getCreatedAt() != null ? post.getCreatedAt() : ""));
            }

            // 设置关注状态 - 顶部工具栏
            if (toolbarFollowButton != null) {
                if (post.isFollowing()) {
                    toolbarFollowButton.setText("已关注");
                    toolbarFollowButton.setStrokeWidth(1);
                    toolbarFollowButton.setStrokeColor(getResources().getColorStateList(R.color.primary));
                    toolbarFollowButton.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                    toolbarFollowButton.setTextColor(getResources().getColorStateList(R.color.primary));
                } else {
                    toolbarFollowButton.setText("关注");
                    toolbarFollowButton.setStrokeWidth(0);
                    toolbarFollowButton.setBackgroundTintList(getResources().getColorStateList(R.color.primary));
                    toolbarFollowButton.setTextColor(getResources().getColorStateList(R.color.white));
                }
            }

            // 设置帖子标题
            if (postTitle != null) {
                postTitle.setText(post.getContent() != null ? post.getContent() : "");
            }

            // 设置帖子正文
            if (postContent != null) {
                postContent.setText(post.getContent() != null ? post.getContent() : "");
            }

            // 设置媒体内容
            if (mediaViewPager != null && post.getImages() != null) {
                List<String> mediaList = post.getImages();
                if (!mediaList.isEmpty()) {
                    // 创建并设置适配器
                    mediaAdapter = new MediaAdapter(mediaList);
                    mediaViewPager.setAdapter(mediaAdapter);
                    
                    // 设置图片指示器
                    setupImageIndicator(mediaList.size());
                    
                    // 设置ViewPager2页面变化监听器
                    mediaViewPager.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            updateImageIndicator(position);
                        }
                    });
                }
            }

            // 设置点赞数量
            likeCount.setText(String.valueOf(post.getLikes()));
            
            // 设置评论数量
            commentCount.setText(String.valueOf(post.getComments()));
            
            // 设置收藏数量
            saveCount.setText(String.valueOf(post.getSaves()));
            
            // 设置收藏状态和图标
            if (saveButton != null) {
                if (post.isSaved()) {
                    saveButton.setImageResource(R.drawable.ic_star_filled);
                } else {
                    saveButton.setImageResource(R.drawable.ic_star);
                }
            }

            // 设置点赞状态和图标
            if (likeButton != null) {
                if (post.isLiked()) {
                    likeButton.setImageResource(R.drawable.ic_heart_filled);
                } else {
                    likeButton.setImageResource(R.drawable.ic_heart);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(this, "显示帖子详情失败");
        }
    }

    /**
     * 设置图片指示器
     * @param count 图片数量
     */
    private void setupImageIndicator(int count) {
        if (imageIndicator == null || count <= 1) {
            if (imageIndicator != null) {
                imageIndicator.setVisibility(View.GONE);
            }
            return;
        }

        // 清空现有指示器
        imageIndicator.removeAllViews();
        imageIndicator.setVisibility(View.VISIBLE);

        // 创建指示器点
        for (int i = 0; i < count; i++) {
            ImageView indicatorDot = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    8, 8); // 设置固定大小，缩小至视觉协调的尺寸
            params.setMargins(4, 0, 4, 0);
            indicatorDot.setLayoutParams(params);
            indicatorDot.setImageResource(R.drawable.ic_placeholder_circle);
            indicatorDot.setColorFilter(ContextCompat.getColor(this, R.color.gray));
            indicatorDot.setAlpha(0.5f);
            imageIndicator.addView(indicatorDot);
        }

        // 默认选中第一个
        updateImageIndicator(0);
    }

    /**
     * 更新图片指示器状态
     * @param position 当前选中位置
     */
    private void updateImageIndicator(int position) {
        if (imageIndicator == null || imageIndicator.getChildCount() == 0) {
            return;
        }

        for (int i = 0; i < imageIndicator.getChildCount(); i++) {
            ImageView dot = (ImageView) imageIndicator.getChildAt(i);
            if (i == position) {
                dot.setColorFilter(ContextCompat.getColor(this, R.color.primary));
                dot.setAlpha(1.0f);
                dot.setScaleX(1.2f); // 缩小放大比例，保持视觉协调
                dot.setScaleY(1.2f);
            } else {
                dot.setColorFilter(ContextCompat.getColor(this, R.color.gray));
                dot.setAlpha(0.5f);
                dot.setScaleX(1.0f);
                dot.setScaleY(1.0f);
            }
        }
    }

    /**
     * 媒体适配器类，用于处理ViewPager2的媒体展示
     */
    private class MediaAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

        private List<String> mediaList;

        public MediaAdapter(List<String> mediaList) {
            this.mediaList = mediaList;
        }

        @Override
        public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_media, parent, false);
            return new MediaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MediaViewHolder holder, int position) {
            String mediaUrl = mediaList.get(position);
            // 使用Glide加载图片
            Glide.with(holder.imageView.getContext())
                    .load(mediaUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder_square)
                    .error(R.drawable.ic_placeholder_square)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mediaList.size();
        }

        public class MediaViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            ImageView imageView;

            public MediaViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.media_image);
            }
        }
    }

    /**
     * 评论适配器类，用于处理RecyclerView的评论展示
     */
    private class CommentAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        private List<Comment> commentList;

        public CommentAdapter(List<Comment> commentList) {
            this.commentList = commentList;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = commentList.get(position);
            
            // 加载用户头像
            Glide.with(holder.commentAvatar.getContext())
                    .load(comment.getAvatar() != null ? comment.getAvatar() : "")
                    .circleCrop()
                    .placeholder(R.drawable.ic_placeholder_circle)
                    .error(R.drawable.ic_placeholder_circle)
                    .into(holder.commentAvatar);
            
            // 设置用户名
            holder.commentName.setText(comment.getName() != null ? comment.getName() : "匿名用户");
            
            // 设置评论时间
            holder.commentTime.setText(formatDate(comment.getCreatedAt() != null ? comment.getCreatedAt() : ""));
            
            // 设置评论内容
            holder.commentContent.setText(comment.getContent() != null ? comment.getContent() : "");
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        /**
         * 添加新评论到列表
         */
        public void addComment(Comment comment) {
            commentList.add(comment);
            notifyItemInserted(commentList.size() - 1);
        }

        /**
         * 更新评论列表
         */
        public void updateComments(List<Comment> newComments) {
            commentList.clear();
            commentList.addAll(newComments);
            notifyDataSetChanged();
        }

        public class CommentViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            ImageView commentAvatar;
            TextView commentName;
            TextView commentTime;
            TextView commentContent;

            public CommentViewHolder(View itemView) {
                super(itemView);
                commentAvatar = itemView.findViewById(R.id.comment_avatar);
                commentName = itemView.findViewById(R.id.comment_name);
                commentTime = itemView.findViewById(R.id.comment_time);
                commentContent = itemView.findViewById(R.id.comment_content);
            }
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
            if (currentPost.isLiked()) {
                likeButton.setImageResource(R.drawable.ic_heart_filled);
            } else {
                likeButton.setImageResource(R.drawable.ic_heart);
            }
            likeCount.setText(String.valueOf(currentPost.getLikes()));
            
            ToastUtil.showShort(this, currentPost.isLiked() ? "点赞成功" : "取消点赞");
        }
    }

    /**
     * 评论帖子
     */
    private void commentPost() {
        // 点击评论按钮，聚焦到评论输入框
        commentInput.requestFocus();
        // 显示软键盘
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(commentInput, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
        }
    }
    
    /**
     * 发送评论
     */
    private void sendComment() {
        String commentText = commentInput.getText().toString().trim();
        if (commentText.isEmpty()) {
            ToastUtil.showShort(this, "请输入评论内容");
            return;
        }
        
        // 创建新评论对象
        String commentId = "comment_" + System.currentTimeMillis();
        Comment newComment = new Comment(
                commentId,
                postId,
                "user_current",
                "当前用户",
                "https://picsum.photos/id/1005/100/100",
                commentText,
                "2025-12-04T" + java.time.LocalTime.now().toString() + "Z"
        );
        
        // 添加评论到列表
        comments.add(newComment);
        commentAdapter.notifyItemInserted(comments.size() - 1);
        
        // 更新评论数量
        if (currentPost != null) {
            int newComments = currentPost.getComments() + 1;
            currentPost.setComments(newComments);
            commentCount.setText(String.valueOf(newComments));
        }
        
        // 滚动到最新评论
        commentsRecyclerView.scrollToPosition(comments.size() - 1);
        
        // 清空评论输入框
        commentInput.setText("");
        
        // 隐藏软键盘
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
        }
        
        // 显示发送成功提示
        ToastUtil.showShort(this, "评论已发送");
    }

    /**
     * 分享帖子
     */
    private void sharePost() {
        // 这里简化实现，实际项目中应该调用系统分享功能
        ToastUtil.showShort(this, "分享功能将在后续实现");
    }

    /**
     * 收藏/取消收藏帖子
     */
    private void savePost() {
        // 这里简化实现，实际项目中应该调用API收藏/取消收藏帖子
        if (currentPost != null) {
            currentPost.setSaved(!currentPost.isSaved());
            int newSaves = currentPost.getSaves() + (currentPost.isSaved() ? 1 : -1);
            currentPost.setSaves(newSaves);
            
            // 更新UI
            if (currentPost.isSaved()) {
                saveButton.setImageResource(R.drawable.ic_star_filled);
            } else {
                saveButton.setImageResource(R.drawable.ic_star);
            }
            saveCount.setText(String.valueOf(currentPost.getSaves()));
            
            ToastUtil.showShort(this, currentPost.isSaved() ? "收藏成功" : "取消收藏");
        }
    }

    /**
     * 关注/取消关注用户
     */
    private void followUser() {
        // 这里简化实现，实际项目中应该调用API关注/取消关注用户
        if (currentPost != null) {
            currentPost.setFollowing(!currentPost.isFollowing());
            
            // 更新关注按钮状态
            if (currentPost.isFollowing()) {
                toolbarFollowButton.setText("已关注");
                toolbarFollowButton.setStrokeWidth(1);
                toolbarFollowButton.setStrokeColor(getResources().getColorStateList(R.color.primary));
                toolbarFollowButton.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                toolbarFollowButton.setTextColor(getResources().getColorStateList(R.color.primary));
            } else {
                toolbarFollowButton.setText("关注");
                toolbarFollowButton.setStrokeWidth(0);
                toolbarFollowButton.setBackgroundTintList(getResources().getColorStateList(R.color.primary));
                toolbarFollowButton.setTextColor(getResources().getColorStateList(R.color.white));
            }
            
            ToastUtil.showShort(this, currentPost.isFollowing() ? "关注成功" : "取消关注");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        // 设置返回过渡动画
        overridePendingTransition(R.anim.activity_return_enter, R.anim.activity_return_exit);
        return true;
    }
}