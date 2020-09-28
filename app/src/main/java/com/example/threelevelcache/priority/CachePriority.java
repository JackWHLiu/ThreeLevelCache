package com.example.threelevelcache.priority;

import com.example.threelevelcache.cache.CacheContext;
import com.example.threelevelcache.source.CacheSource;

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
