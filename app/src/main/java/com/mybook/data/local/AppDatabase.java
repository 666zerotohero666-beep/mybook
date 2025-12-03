package com.mybook.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.mybook.data.local.converter.Converters;
import com.mybook.data.local.dao.PostDao;
import com.mybook.data.model.Post;

/**
 * 应用数据库类
 * Room数据库的核心类，用于配置数据库和获取DAO实例
 */
@Database(entities = {Post.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    /**
     * 获取PostDao实例
     * @return PostDao实例
     */
    public abstract PostDao postDao();

    /**
     * 获取数据库实例
     * @param context 上下文
     * @return AppDatabase实例
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "mybook_database"
            )
                    .fallbackToDestructiveMigration() // 数据库版本更新时销毁旧数据库
                    .build();
        }
        return instance;
    }

    /**
     * 关闭数据库
     */
    public void closeDatabase() {
        if (instance != null && instance.isOpen()) {
            instance.close();
            instance = null;
        }
    }
}