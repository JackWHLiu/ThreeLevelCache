package com.example.cache_generator.cache;

import com.example.cache_generator.provider.CacheProvider;
import com.example.cache_generator.source.CacheSource;

public interface CacheFactory<T> {

    CacheProvider<String, T> createCacheProvider();
    CacheSource<String, T> createCacheSource();
}
