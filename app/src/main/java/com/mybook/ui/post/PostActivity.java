package com.mybook.ui.post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.mybook.R;
import com.mybook.data.model.Post;
import com.mybook.ui.main.MainViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布新帖子的Activity
 */
public class PostActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 200;

    private MaterialToolbar toolbar;
    private EditText etTitle;
    private EditText etContent;
    private Button btnSelectImage;
    private LinearLayout selectedImagesLayout;
    private Button btnPost;
    private List<Uri> selectedImages = new ArrayList<>();
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // 初始化UI组件
        initUI();

        // 初始化ViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

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
        // 统一使用旧版权限，兼容所有Android版本
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        
        // 请求相册权限
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // 检查是否需要显示权限说明
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // 显示权限说明对话框
                showPermissionRationaleDialog();
            } else {
                // 直接请求权限
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            }
        } else {
            // 权限已授予，调用系统相册
            openGallery();
        }
    }
    
    /**
     * 显示权限说明对话框
     */
    private void showPermissionRationaleDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("权限说明")
                .setMessage("需要相册权限才能选择图片发布帖子")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 统一使用旧版权限，兼容所有Android版本
                    String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                    // 请求权限
                    ActivityCompat.requestPermissions(PostActivity.this, new String[]{permission}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 打开系统相册
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 允许选择多张图片
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 处理权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功，打开相册
                openGallery();
            } else {
                // 权限授予失败，显示提示
                Toast.makeText(this, "需要读取相册权限才能选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 处理从相册返回的结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                // 处理选中的图片
                if (data.getClipData() != null) {
                    // 选择了多张图片
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        selectedImages.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    // 选择了单张图片
                    Uri imageUri = data.getData();
                    selectedImages.add(imageUri);
                }
                
                // 更新图片预览
                updateImagePreview();
            }
        }
    }

    /**
     * 更新图片预览
     */
    private void updateImagePreview() {
        // 清空现有预览
        selectedImagesLayout.removeAllViews();
        
        // 显示选中的图片
        for (int i = 0; i < selectedImages.size(); i++) {
            final int index = i;
            Uri imageUri = selectedImages.get(i);
            
            // 创建图片预览视图
            ViewGroup previewItem = (ViewGroup) getLayoutInflater().inflate(R.layout.item_image_preview, null);
            ImageView imageView = previewItem.findViewById(R.id.image_preview);
            ImageView btnRemove = previewItem.findViewById(R.id.btn_remove_image);
            
            // 加载图片
            Bitmap bitmap = decodeUri(imageUri);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            
            // 设置删除按钮点击事件
            btnRemove.setOnClickListener(v -> {
                // 从列表中移除
                selectedImages.remove(index);
                // 更新预览
                updateImagePreview();
            });
            
            // 添加到预览布局
            selectedImagesLayout.addView(previewItem);
        }
    }

    /**
     * 解码图片Uri为Bitmap
     */
    private Bitmap decodeUri(Uri uri) {
        try {
            // 获取图片尺寸
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            getContentResolver().openInputStream(uri).close();
            
            // 计算缩放比例
            options.inSampleSize = calculateInSampleSize(options, 300, 300);
            options.inJustDecodeBounds = false;
            
            // 解码图片
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算图片缩放比例
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 计算最大的inSampleSize值，该值是2的幂，同时保持高度和宽度均大于要求的高度和宽度
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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

        // 将选中的图片Uri转换为字符串列表
        List<String> imageUrls = new ArrayList<>();
        for (Uri uri : selectedImages) {
            imageUrls.add(uri.toString());
        }

        // 创建新帖子对象
        Post newPost = new Post(
                "post_" + System.currentTimeMillis(),
                "user_current",
                "当前用户",
                "https://picsum.photos/id/1005/100/100",
                "当前用户的简介",
                100,
                50,
                content,
                imageUrls,
                0,
                0,
                0,
                false,
                false,
                false,
                0,
                "2025-12-04T12:00:00Z"
        );

        // 添加新帖子到数据源
        mainViewModel.addPost(newPost);

        // 这里简化实现，实际项目中应该调用API发布帖子
        Toast.makeText(this, "帖子发布成功", Toast.LENGTH_SHORT).show();

        // 发送广播通知首页刷新列表
        Intent refreshIntent = new Intent("com.mybook.ACTION_REFRESH_POSTS");
        sendBroadcast(refreshIntent);

        // 发布成功后返回首页
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}