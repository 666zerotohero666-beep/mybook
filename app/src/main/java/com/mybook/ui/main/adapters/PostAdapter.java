package com.mybook.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mybook.R;
import com.mybook.data.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 帖子适配器
 * 用于RecyclerView展示帖子数据，支持双列瀑布流布局
 */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 数据类型常量
    private static final int TYPE_ITEM = 0; // 帖子项
    private static final int TYPE_LOADING = 1; // 加载更多项

    private List<Post> mPosts;
    private Context mContext;
    private OnPostClickListener mListener;
    private boolean isLoadingMore = false;

    /**
     * 点击事件监听器
     */
    public interface OnPostClickListener {
        void onPostClick(Post post);
        void onLikeClick(Post post);
        void onCommentClick(Post post);
        void onSaveClick(Post post);
        void onLoadMore();
    }

    /**
     * 构造函数
     * @param context 上下文
     * @param posts 帖子列表
     * @param listener 点击事件监听器
     */
    public PostAdapter(Context context, List<Post> posts, OnPostClickListener listener) {
        this.mContext = context;
        this.mPosts = posts;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            // 创建帖子项ViewHolder
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view);
        } else {
            // 创建加载更多项ViewHolder
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            // 绑定帖子数据
            Post post = mPosts.get(position);
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.bind(post);
        } else if (holder instanceof LoadingViewHolder) {
            // 绑定加载更多数据
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.bind();
        }
    }

    @Override
    public int getItemCount() {
        return mPosts == null ? 0 : mPosts.size() + (isLoadingMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一项且正在加载更多时，返回加载更多类型
        return (position == mPosts.size() && isLoadingMore) ? TYPE_LOADING : TYPE_ITEM;
    }

    /**
     * 设置加载更多状态
     * @param isLoadingMore 是否正在加载更多
     */
    public void setLoadingMore(boolean isLoadingMore) {
        this.isLoadingMore = isLoadingMore;
        notifyDataSetChanged();
    }

    /**
     * 添加帖子列表
     * @param posts 要添加的帖子列表
     */
    public void addPosts(List<Post> posts) {
        int startPosition = mPosts.size();
        mPosts.addAll(posts);
        notifyItemRangeInserted(startPosition, posts.size());
    }

    /**
     * 更新帖子列表
     * @param posts 新的帖子列表
     */
    public void updatePosts(List<Post> posts) {
        mPosts = posts;
        notifyDataSetChanged();
    }

    /**
     * 帖子项ViewHolder
     */
    class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName;
        TextView postTime;
        TextView postContent;
        ImageView postImage;
        ImageView likeButton;
        TextView likeCount;
        ImageView commentButton;
        TextView commentCount;
        ImageView saveButton;
        TextView saveCount;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            postTime = itemView.findViewById(R.id.post_time);
            postContent = itemView.findViewById(R.id.post_content);
            postImage = itemView.findViewById(R.id.post_image);
            likeButton = itemView.findViewById(R.id.like_button);
            likeCount = itemView.findViewById(R.id.like_count);
            commentButton = itemView.findViewById(R.id.comment_button);
            commentCount = itemView.findViewById(R.id.comment_count);
            saveButton = itemView.findViewById(R.id.save_button);
            saveCount = itemView.findViewById(R.id.save_count);

            // 设置点击事件
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onPostClick(mPosts.get(position));
                }
            });

            likeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onLikeClick(mPosts.get(position));
                }
            });

            commentButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onCommentClick(mPosts.get(position));
                }
            });

            saveButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onSaveClick(mPosts.get(position));
                }
            });
        }

        /**
         * 绑定帖子数据
         * @param post 帖子对象
         */
        void bind(Post post) {
            // 设置用户头像
            Glide.with(mContext)
                    .load(post.getAvatar())
                    .circleCrop()
                    .placeholder(R.drawable.ic_placeholder_circle)
                    .into(userAvatar);

            // 设置用户名
            userName.setText(post.getName());

            // 设置发布时间
            postTime.setText(formatDate(post.getCreatedAt()));

            // 设置帖子内容
            postContent.setText(post.getContent());

            // 设置帖子图片
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                Glide.with(mContext)
                        .load(post.getImages().get(0))
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder_square)
                        .into(postImage);
            }

            // 设置点赞数量
            likeCount.setText(String.valueOf(post.getLikes()));

            // 设置评论数量
            commentCount.setText(String.valueOf(post.getComments()));

            // 设置收藏数量
            saveCount.setText(String.valueOf(post.getShares()));

            // 设置点赞状态
            likeButton.setImageResource(post.isLiked() ? R.drawable.ic_placeholder_circle : R.drawable.ic_placeholder_circle);
        }

        /**
         * 格式化日期
         * @param dateString ISO格式的日期字符串
         * @return 格式化后的日期字符串
         */
        private String formatDate(String dateString) {
            try {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                Date date = isoFormat.parse(dateString);
                if (date != null) {
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    return outputFormat.format(date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dateString;
        }
    }

    /**
     * 加载更多项ViewHolder
     */
    class LoadingViewHolder extends RecyclerView.ViewHolder {
        TextView loadingText;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingText = itemView.findViewById(R.id.loading_text);
        }

        /**
         * 绑定加载更多数据
         */
        void bind() {
            loadingText.setText(mContext.getString(R.string.loading_more));
            // 触发加载更多
            if (mListener != null) {
                mListener.onLoadMore();
            }
        }
    }
}