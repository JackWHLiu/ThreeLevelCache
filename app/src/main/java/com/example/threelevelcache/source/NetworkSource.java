package com.example.threelevelcache.source;

import com.example.threelevelcache.Person;
import com.example.threelevelcache.http.RetrofitFactory;
import com.example.threelevelcache.http.service.CommonService;

import java.io.IOException;
import java.util.List;

import dora.db.dao.OrmDao;
import retrofit2.Call;
import retrofit2.Response;

public class NetworkSource<T> implements CacheSource<String, T> {

    @Override
    public T fetch() {
        return null;
    }

    @Override
    public List<T> fetchAll() {
        DatabaseSource<Person> source = new DatabaseSource<>(Person.class);
        OrmDao<Person> dao = source.getDao();
        //这里还要优化，不应该用具体的东西
        Call<List<Person>> call = RetrofitFactory.getService(CommonService.class).fetchPersons();
        try {
            Response<List<Person>> response = call.execute();
            List<Person> body = response.body();
            dao.deleteAll();
            dao.insert(body);
            return (List<T>) body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
