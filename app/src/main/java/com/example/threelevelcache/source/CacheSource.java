package com.example.threelevelcache.source;

import java.util.List;

public interface CacheSource<K, V> {

    V fetch();
    List<V> fetchAll();
}
