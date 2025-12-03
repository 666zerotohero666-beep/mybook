package com.mybook.data.remote;

import com.mybook.data.model.Post;
import com.mybook.data.remote.api.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 远程数据源实现
 * 使用Retrofit+OkHttp进行网络请求
 */
public class RemoteDataSourceImpl implements RemoteDataSource {
    private final ApiService apiService;

    /**
     * 构造函数，用于依赖注入和测试
     * @param apiService ApiService实例
     */
    public RemoteDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * 构造函数
     */
    public RemoteDataSourceImpl() {
        // 获取ApiService实例，添加错误处理
        ApiService tempApiService = null;
        try {
            tempApiService = RetrofitClient.getInstance().getApiService();
        } catch (Exception e) {
            e.printStackTrace();
            // RetrofitClient初始化失败，apiService为null，但应用不会崩溃
        }
        this.apiService = tempApiService;
    }

    @Override
    public void getAllPosts(RemoteCallback<List<Post>> callback) {
        // 检查apiService是否为null
        if (apiService == null) {
            callback.onFailure(new RuntimeException("ApiService not initialized"));
            return;
        }
        // 发起网络请求
        apiService.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new RuntimeException("Failed to get posts: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getPostById(String postId, RemoteCallback<Post> callback) {
        // 检查apiService是否为null
        if (apiService == null) {
            callback.onFailure(new RuntimeException("ApiService not initialized"));
            return;
        }
        // 发起网络请求
        apiService.getPostById(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new RuntimeException("Failed to get post: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void likePost(String postId, RemoteCallback<Boolean> callback) {
        // 检查apiService是否为null
        if (apiService == null) {
            callback.onFailure(new RuntimeException("ApiService not initialized"));
            return;
        }
        // 发起网络请求
        apiService.likePost(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(true);
                } else {
                    callback.onFailure(new RuntimeException("Failed to like post: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void commentPost(String postId, String comment, RemoteCallback<Boolean> callback) {
        // 检查apiService是否为null
        if (apiService == null) {
            callback.onFailure(new RuntimeException("ApiService not initialized"));
            return;
        }
        // 发起网络请求
        apiService.commentPost(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(true);
                } else {
                    callback.onFailure(new RuntimeException("Failed to comment post: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void sharePost(String postId, RemoteCallback<Boolean> callback) {
        // 检查apiService是否为null
        if (apiService == null) {
            callback.onFailure(new RuntimeException("ApiService not initialized"));
            return;
        }
        // 发起网络请求
        apiService.sharePost(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(true);
                } else {
                    callback.onFailure(new RuntimeException("Failed to share post: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}