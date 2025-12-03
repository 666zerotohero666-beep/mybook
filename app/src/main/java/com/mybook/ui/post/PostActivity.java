package com.mybook.ui.post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.mybook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布新帖子的Activity
 */
public class PostActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private EditText etTitle;
    private EditText etContent;
    private Button btnSelectImage;
    private LinearLayout selectedImagesLayout;
    private Button btnPost;
    private List<Uri> selectedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // 初始化UI组件
        initUI();

        // 设置点击事件
        setClickListeners();
    }

    /**
     * 初始化UI组件
     */
    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        btnSelectImage = findViewById(R.id.btn_select_image);
        selectedImagesLayout = findViewById(R.id.selected_images_layout);
        btnPost = findViewById(R.id.btn_post);

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

        // 图片选择按钮点击事件
        btnSelectImage.setOnClickListener(v -> selectImage());

        // 发布按钮点击事件
        btnPost.setOnClickListener(v -> post());
    }

    /**
     * 选择图片
     */
    private void selectImage() {
        // 这里简化实现，实际项目中应该使用系统相册或第三方图片选择库
        Toast.makeText(this, "图片选择功能将在后续实现", Toast.LENGTH_SHORT).show();
    }

    /**
     * 发布新帖子
     */
    private void post() {
        // 获取输入内容
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        // 验证输入
        if (title.isEmpty()) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "请输入正文内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 这里简化实现，实际项目中应该调用API发布帖子
        Toast.makeText(this, "帖子发布成功", Toast.LENGTH_SHORT).show();

        // 发布成功后返回首页
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}