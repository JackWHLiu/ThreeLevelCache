package com.example.threelevelcache.cache;

import com.example.threelevelcache.provider.CacheProvider;
import com.example.threelevelcache.source.CacheSource;

import java.util.List;

public abstract class Cache<K, V> implements CacheSource<K, V> , CacheProvider<K, V> {

    @Override
    public void add(V value) {
    }

    @Override
    public void addAll(List<V> values) {

    }

    @Override
    public void remove(V value) {

    }

    @Override
    public void removeAll(List<V> values) {

    }

    @Override
    public void save(V value) {

    }

    @Override
    public void saveAll(List<V> values) {

    }

    @Override
    public V fetch() {
        return null;
    }

    @Override
    public List<V> fetchAll() {
        return null;
    }
}
