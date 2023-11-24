package com.example.rickandmorty.api;

import com.example.rickandmorty.resource.local.AutenticacionService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientLocal {
    private static final String BASE_URL = "http://10.0.2.2:8000/api/"; //127.0.0.1:8000
    private static RetrofitClientLocal instance;
    private Retrofit retrofit;

    private RetrofitClientLocal() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClientLocal getInstance() {
        if (instance == null) {
            instance = new RetrofitClientLocal();
        }
        return instance;
    }

    public AutenticacionService getAutenticacionService() {
        return retrofit.create(AutenticacionService.class);
    }
}
