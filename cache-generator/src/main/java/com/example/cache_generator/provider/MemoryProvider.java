package com.example.cache_generator.provider;

import java.util.List;

public class MemoryProvider<V> extends CacheProviderWrapper<V> {

    public MemoryProvider() {
    }

    public MemoryProvider(CacheProvider<String, V> provider) {
        super(provider);
    }

    @Override
    public void add(V value) {
        super.add(value);
    }

    @Override
    public void addAll(List<V> values) {
        super.addAll(values);
    }

    @Override
    public void remove(V value) {
        super.remove(value);
    }

    @Override
    public void removeAll(List<V> values) {
        super.removeAll(values);
    }

    @Override
    public void save(V value) {
        super.save(value);
    }

    @Override
    public void saveAll(List<V> values) {
        super.saveAll(values);
    }
}
