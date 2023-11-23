package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rickandmorty.adapter.EpisodeAdapter;
import com.example.rickandmorty.api.RetrofitClientRemote;
import com.example.rickandmorty.resource.remote.Character;
import com.example.rickandmorty.resource.remote.Episode;
import com.example.rickandmorty.resource.remote.RickAndMortyService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textViewName, textViewStatus, textViewSpecies;
    private RecyclerView recyclerView;
    private EpisodeAdapter episodeAdapter;
    private List<Episode> episodeList;
    private RickAndMortyService rickAndMortyService;
    private int characterId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        imageView = findViewById(R.id.imageView);
        textViewName = findViewById(R.id.textViewName);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewSpecies = findViewById(R.id.textViewSpecies);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        episodeList = new ArrayList<>();
        episodeAdapter = new EpisodeAdapter(this, episodeList);
        recyclerView.setAdapter(episodeAdapter);

        rickAndMortyService = RetrofitClientRemote.getInstance().getRickAndMortyService();

        characterId = getIntent().getIntExtra("character_id", 0);

        getCharacterDetail();
    }

    private void getCharacterDetail() {
        Call<Character> call = rickAndMortyService.getCharacterById(characterId);
        call.enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if (response.isSuccessful()){
                    Character character = response.body();
                    Glide.with(CharacterActivity.this).load(character.getImage()).into(imageView);
                    textViewName.setText(character.getName());
                    textViewStatus.setText(character.getStatus());
                    textViewSpecies.setText(character.getSpecies());
                    getEpisodes(character.getEpisode());
                }
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {

            }
        });
    }

    private void getEpisodes(List<String> episodeUrls) {
        for (String url : episodeUrls) {
            int id = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
            Call<Episode> call = rickAndMortyService.getEpisodeById(id);
            call.enqueue(new Callback<Episode>() {
                @Override
                public void onResponse(Call<Episode> call, Response<Episode> response) {
                    if (response.isSuccessful()) {
                        Episode episode = response.body();
                        episodeList.add(episode);
                        episodeAdapter.setEpisodeList(episodeList);
                    } else {
                        // ...
                    }
                }
                @Override
                public void onFailure(Call<Episode> call, Throwable t) {
                    // ...
                }
            });
        }
    }
}