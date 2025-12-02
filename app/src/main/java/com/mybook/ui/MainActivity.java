package com.mybook.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.mybook.R;
import com.mybook.databinding.ActivityMainBinding;

/**
 * 应用主界面
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 使用DataBinding初始化布局
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        
        // 设置标题
        setTitle(getString(R.string.app_name));
        
        // 初始化UI
        initUI();
    }

    /**
     * 初始化UI组件
     */
    private void initUI() {
        // 设置Hello World文本
        binding.textView.setText(getString(R.string.hello_world));
    }
}
