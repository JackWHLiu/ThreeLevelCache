package com.example.threelevelcache.source;

import java.util.List;

import dora.db.OrmTable;
import dora.db.dao.DaoFactory;
import dora.db.dao.OrmDao;

public class DatabaseSource<T extends OrmTable> implements CacheSource<String, T> {

    private final OrmDao<T> mDao;

    public DatabaseSource(Class<T> clazz) {
        mDao = DaoFactory.getDao(clazz);
    }

    public OrmDao<T> getDao() {
        return mDao;
    }

    @Override
    public T fetch() {
        return null;
    }

    @Override
    public List<T> fetchAll() {
        return null;
    }
}
