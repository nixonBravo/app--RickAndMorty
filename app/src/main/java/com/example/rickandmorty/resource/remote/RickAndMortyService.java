package com.example.rickandmorty.resource.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RickAndMortyService {
    /*@GET("episode")
    Call<EpisodesResponse> getAllEpisodes(@Query("page") int page);*/
    @GET("episode")
    Call<EpisodesResponse> getAllEpisodes(
            @Query("page") int page,
            @Query("name") String name // Nuevo parámetro para filtrar por nombre
    );

    @GET("episode/{id}")
    Call<Episode> getEpisodeById(@Path("id") int id);
    @GET("character/{id}")
    Call<Character> getCharacterById(@Path("id") int id);
}
