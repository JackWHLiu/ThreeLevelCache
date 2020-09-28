package com.example.threelevelcache.source;

import java.util.List;

public class MemorySource<T> implements CacheSource<String, T> {

    @Override
    public T fetch() {
        return null;
    }

    @Override
    public List<T> fetchAll() {
        return null;
    }
}
