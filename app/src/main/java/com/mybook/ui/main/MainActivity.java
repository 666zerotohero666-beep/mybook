package com.mybook.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.mybook.R;
import com.mybook.databinding.ActivityMainBinding;

/**
 * 应用主界面
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

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
    }

    /**
     * 初始化UI组件
     */
    private void initUI() {
        // 观察帖子数据
        viewModel.getPosts().observe(this, posts -> {
            // 这里将在后续实现中添加适配器逻辑
        });
        
        // 观察加载状态
        viewModel.getIsLoading().observe(this, isLoading -> {
            // 这里将在后续实现中添加加载状态处理
        });
        
        // 观察错误信息
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            // 这里将在后续实现中添加错误信息处理
        });
    }
}