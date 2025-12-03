package com.mybook.base.event;

/**
 * 事件包装类
 * 用于LiveData的事件传递，确保事件只被消费一次
 * @param <T> 事件数据类型
 */
public class Event<T> {
    private final T content;
    private boolean hasBeenHandled = false;

    /**
     * 构造函数
     * @param content 事件内容
     */
    public Event(T content) {
        this.content = content;
    }

    /**
     * 获取事件内容，如果事件已被处理则返回null
     * @return 事件内容或null
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        }
        hasBeenHandled = true;
        return content;
    }

    /**
     * 获取事件内容，无论事件是否已被处理
     * @return 事件内容
     */
    public T peekContent() {
        return content;
    }

    /**
     * 检查事件是否已被处理
     * @return 是否已处理
     */
    public boolean isHandled() {
        return hasBeenHandled;
    }
}