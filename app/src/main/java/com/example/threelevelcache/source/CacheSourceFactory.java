package com.example.threelevelcache.source;

import com.example.threelevelcache.cache.CacheContext;
import com.example.threelevelcache.priority.CachePriority;
import com.example.threelevelcache.priority.DefaultCachePriority;

@Deprecated
public class CacheSourceFactory {

    private static CacheSourceFactory mInstance;

    private CacheSourceFactory() {
    }

    public static CacheSourceFactory getInstance() {
        if (mInstance == null) {
            synchronized (CacheSourceFactory.class) {
                if (mInstance == null) mInstance = new CacheSourceFactory();
            }
        }
        return mInstance;
    }

    public <T> CacheSource<String, T> createCacheSource(Class<T> clazz) {
        CachePriority priority = new DefaultCachePriority();
        CacheContext cacheContext = new CacheContext();
        return priority.selectSource(cacheContext, clazz);
    }
}
