package com.example.cache_generator.priority;

import com.example.cache_generator.cache.CacheContext;
import com.example.cache_generator.source.CacheSource;

/**
 * 缓存优先级。
 */
public interface CachePriority {

    /**
     * 挑选当前环境下最佳的数据源。
     *
     * @return
     */
    <T> CacheSource<String, T> selectSource(CacheContext cacheContext, Class<T> clazz);
}
