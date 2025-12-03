package com.mybook.base.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * 事件总线
 * 基于LiveData实现，用于模块间通信
 * 支持生命周期感知，避免内存泄漏
 */
public class EventBus {
    private static volatile EventBus instance;
    private final MutableLiveData<Event<Object>> events = new MutableLiveData<>();

    /**
     * 私有构造函数
     */
    private EventBus() {
        // 单例模式，防止外部实例化
    }

    /**
     * 获取EventBus实例
     * @return EventBus实例
     */
    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    /**
     * 发布事件
     * @param event 事件内容
     */
    public void post(Object event) {
        events.postValue(new Event<>(event));
    }

    /**
     * 获取事件LiveData
     * @return 事件LiveData
     */
    public LiveData<Event<Object>> getEvents() {
        return events;
    }

    /**
     * 发布特定类型的事件
     * @param event 事件内容
     * @param <T> 事件类型
     */
    public <T> void postEvent(T event) {
        events.postValue(new Event<>(event));
    }
}