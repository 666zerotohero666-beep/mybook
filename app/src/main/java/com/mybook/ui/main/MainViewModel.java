package com.mybook.ui.main;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mybook.data.model.Post;
import com.mybook.data.repository.PostRepository;
import com.mybook.data.repository.PostRepositoryImpl;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final PostRepository postRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MainViewModel(android.app.Application application) {
        super(application);
        this.postRepository = new PostRepositoryImpl();
    }

    public LiveData<List<Post>> getPosts() {
        return postRepository.getPosts();
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