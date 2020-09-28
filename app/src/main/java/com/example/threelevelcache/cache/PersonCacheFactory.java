package com.example.threelevelcache.cache;

import com.example.threelevelcache.Person;
import com.example.threelevelcache.priority.CachePriority;
import com.example.threelevelcache.priority.DefaultCachePriority;
import com.example.threelevelcache.provider.CacheProvider;
import com.example.threelevelcache.provider.DatabaseProvider;
import com.example.threelevelcache.provider.MemoryProvider;
import com.example.threelevelcache.provider.NetworkProvider;
import com.example.threelevelcache.source.CacheSource;

/**
 * 这个类应该是要通过APT生成得到的，使用注解{@link ThreeLevelCache}生成。
 */
public class PersonCacheFactory implements CacheFactory<Person> {

    @Override
    public CacheProvider<String, Person> createCacheProvider() {
        DatabaseProvider<Person> databaseProvider = new DatabaseProvider<>();
        NetworkProvider<Person> networkProvider = new NetworkProvider<>(databaseProvider);
        return new MemoryProvider<>(networkProvider);
    }

    @Override
    public CacheSource<String, Person> createCacheSource() {
        CachePriority priority = new DefaultCachePriority();
        CacheContext cacheContext = new CacheContext();
        return priority.selectSource(cacheContext, Person.class);
    }
}
