package com.example.threelevelcache.provider;

import java.util.List;

public class DatabaseProvider<V> extends CacheProviderWrapper<V> {

    public DatabaseProvider() {
    }

    public DatabaseProvider(CacheProvider<String, V> provider) {
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
