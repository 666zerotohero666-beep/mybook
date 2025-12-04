package com.mybook.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Glide配置类，用于优化Glide图片加载性能
 */
@GlideModule
public final class GlideConfig extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // 设置图片质量为ARGB_8888
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .disallowHardwareConfig() // 禁用硬件加速，避免某些设备上的渲染问题
        );
        
        // 设置内存缓存大小
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20MB
        builder.setMemoryCache(new com.bumptech.glide.load.engine.cache.LruResourceCache(memoryCacheSizeBytes));
        
        // 设置磁盘缓存大小
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100MB
        builder.setDiskCache(
                new com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory(context, "glide_cache", diskCacheSizeBytes)
        );
        
        // 使用Glide默认的线程池配置
        // builder.setSourceExecutor(...)  // 移除错误的线程池配置
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        // 可以在这里注册自定义的ModelLoader或ResourceDecoder
        // 例如：registry.register(MyModel.class, Bitmap.class, new MyModelLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        // 禁用清单解析，提高Glide初始化速度
        return false;
    }
}