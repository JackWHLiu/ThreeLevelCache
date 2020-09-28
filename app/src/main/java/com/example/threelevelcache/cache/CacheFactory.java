package com.example.threelevelcache.cache;

import com.example.threelevelcache.provider.CacheProvider;
import com.example.threelevelcache.source.CacheSource;

public interface CacheFactory<T> {

    CacheProvider<String, T> createCacheProvider();
    CacheSource<String, T> createCacheSource();
}
