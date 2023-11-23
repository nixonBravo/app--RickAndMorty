package com.example.rickandmorty.resource.local;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AutenticacionService {
    @POST("auth/register")
    Call<User> registerUser(@Body User user);

    @POST("auth/login")
    Call<User> loginUser(@Body User user);

    @DELETE("auth/logout")
    Call<Void> logoutUser(@Header("Authorization")String token);
}
