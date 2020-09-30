package com.example.cache_generator.priority;

import com.example.cache_generator.cache.CacheContext;
import com.example.cache_generator.source.CacheSource;

public class DefaultCachePriority implements CachePriority {

    @Override
    public <T> CacheSource<String, T> selectSource(CacheContext cacheContext, Class<T> clazz) {
        if (cacheContext.isNetworkAvailable()) {
//            return new NetworkSource<T>();
        } else {
//            return new DatabaseSource(clazz);
        }
        return null;
    }
}
