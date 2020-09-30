package com.example.cache_generator.provider;

//import androidx.annotation.CallSuper;

import java.util.List;

public abstract class CacheProviderWrapper<V> implements CacheProvider<String, V> {

    protected CacheProvider<String, V> mBase;

    public CacheProviderWrapper() {
    }

    public CacheProviderWrapper(CacheProvider<String, V> provider) {
        this.mBase = provider;
    }

//    @CallSuper
    @Override
    public void add(V value) {
        if (mBase != null) {
            mBase.add(value);
        }
    }

//    @CallSuper
    @Override
    public void addAll(List<V> values) {
        if (mBase != null) {
            mBase.addAll(values);
        }
    }

//    @CallSuper
    @Override
    public void remove(V value) {
        if (mBase != null) {
            mBase.remove(value);
        }
    }

//    @CallSuper
    @Override
    public void removeAll(List<V> values) {
        if (mBase != null) {
            mBase.removeAll(values);
        }
    }

//    @CallSuper
    @Override
    public void save(V value) {
        if (mBase != null) {
            mBase.save(value);
        }
    }

//    @CallSuper
    @Override
    public void saveAll(List<V> values) {
        if (mBase != null) {
            mBase.saveAll(values);
        }
    }
}
