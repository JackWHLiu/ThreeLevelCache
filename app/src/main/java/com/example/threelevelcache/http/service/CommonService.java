package com.example.threelevelcache.http.service;

import com.example.threelevelcache.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface CommonService {

    @POST("/")
    Call<List<Person>> fetchPersons();
    @POST("/")
    Call<Person> fetchPerson();
}
