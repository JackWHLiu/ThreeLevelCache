package com.example.threelevelcache.cache;

/**
 * 缓存需要的上下文环境。
 */
public class CacheContext {

    public boolean isNetworkAvailable() {
        return false;
    }

    public boolean isDatabaseAvailable() {
        return false;
    }

    public boolean isMemoryAvailable() {
        return false;
    }
}
