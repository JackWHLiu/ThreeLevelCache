package com.example.cache_generator.provider;

import java.util.List;

public interface CacheProvider<K, V> {

    void add(V value);

    void addAll(List<V> values);

    void remove(V value);

    void removeAll(List<V> values);

    void save(V value);

    void saveAll(List<V> values);
}
