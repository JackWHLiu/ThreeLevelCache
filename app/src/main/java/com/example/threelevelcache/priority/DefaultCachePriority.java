package com.example.threelevelcache.priority;

import com.example.threelevelcache.cache.CacheContext;
import com.example.threelevelcache.source.CacheSource;
import com.example.threelevelcache.source.DatabaseSource;
import com.example.threelevelcache.source.NetworkSource;

public class DefaultCachePriority implements CachePriority {

    @Override
    public <T> CacheSource<String, T> selectSource(CacheContext cacheContext, Class<T> clazz) {
        if (cacheContext.isNetworkAvailable()) {
            return new NetworkSource<T>();
        } else {
            return new DatabaseSource(clazz);
        }
    }
}
