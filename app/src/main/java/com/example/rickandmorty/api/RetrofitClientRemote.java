package com.example.rickandmorty.api;

import com.example.rickandmorty.resource.remote.RickAndMortyService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientRemote {
    private static final String BASE_URL = "https://rickandmortyapi.com/api/";
    private static RetrofitClientRemote instance;
    private Retrofit retrofit;

    private RetrofitClientRemote() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClientRemote getInstance() {
        if (instance == null) {
            instance = new RetrofitClientRemote();
        }
        return instance;
    }

    public RickAndMortyService getRickAndMortyService() {
        return retrofit.create(RickAndMortyService.class);
    }
}
