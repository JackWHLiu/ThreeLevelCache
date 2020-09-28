package com.example.threelevelcache.cache;

import com.example.threelevelcache.Person;
import com.example.threelevelcache.provider.CacheProvider;
import com.example.threelevelcache.source.CacheSource;

import java.util.List;

/**
 * 这个类也生成。
 */
public class PersonCache extends Cache<String, Person> {

    private PersonCacheFactory mFactory;
    private CacheSource<String, Person> mSource;
    private CacheProvider<String, Person> mProvider;

    public PersonCache() {
        mFactory = new PersonCacheFactory();
        mSource = mFactory.createCacheSource();
        mProvider = mFactory.createCacheProvider();
    }

    @Override
    public void add(Person value) {
        if (mProvider != null) {
            mProvider.add(value);
        }
        super.add(value);
    }

    @Override
    public void addAll(List<Person> values) {
        if (mProvider != null) {
            mProvider.addAll(values);
        }
        super.addAll(values);
    }

    @Override
    public void remove(Person value) {
        if (mProvider != null) {
            mProvider.remove(value);
        }
        super.remove(value);
    }

    @Override
    public void removeAll(List<Person> values) {
        if (mProvider != null) {
            mProvider.removeAll(values);
        }
        super.removeAll(values);
    }

    @Override
    public void save(Person value) {
        if (mProvider != null) {
            mProvider.save(value);
        }
        super.save(value);
    }

    @Override
    public void saveAll(List<Person> values) {
        if (mProvider != null) {
            mProvider.saveAll(values);
        }
        super.saveAll(values);
    }

    @Override
    public Person fetch() {
        if (mSource != null) {
            return mSource.fetch();
        }
        return super.fetch();
    }

    @Override
    public List<Person> fetchAll() {
        if (mSource != null) {
            return mSource.fetchAll();
        }
        return super.fetchAll();
    }
}
