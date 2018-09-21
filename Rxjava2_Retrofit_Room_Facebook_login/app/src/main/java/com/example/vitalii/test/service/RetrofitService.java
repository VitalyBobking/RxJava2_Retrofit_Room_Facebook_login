package com.example.vitalii.test.service;


import com.example.vitalii.test.model.Person;

import java.util.List;

import retrofit2.http.GET;
import io.reactivex.Observable;

public interface RetrofitService {
    @GET("/users")
    Observable<List<Person>> getPersons();
}
