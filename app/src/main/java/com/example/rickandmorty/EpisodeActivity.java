package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.rickandmorty.adapter.CharacterAdapter;
import com.example.rickandmorty.api.RetrofitClientRemote;
import com.example.rickandmorty.resource.remote.Character;
import com.example.rickandmorty.resource.remote.Episode;
import com.example.rickandmorty.resource.remote.RickAndMortyService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodeActivity extends AppCompatActivity {
    private TextView textViewId, textViewName, textViewEpisode, textViewDate;
    private RecyclerView recyclerView;
    private List<Character> characterList = new ArrayList<>();
    private RickAndMortyService rickAndMortyService;
    private CharacterAdapter characterAdapter;
    private int episodeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        textViewId = findViewById(R.id.textViewId);
        textViewName = findViewById(R.id.textViewName);
        textViewEpisode = findViewById(R.id.textViewEpisode);
        textViewDate = findViewById(R.id.textViewDate);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        characterAdapter = new CharacterAdapter(this);
        recyclerView.setAdapter(characterAdapter);

        rickAndMortyService = RetrofitClientRemote.getInstance().getRickAndMortyService();

        episodeId = getIntent().getIntExtra("episode_id", 0);

        getEpisodeDetail();
    }

    private void getEpisodeDetail() {
        Call<Episode> call = rickAndMortyService.getEpisodeById(episodeId);
        call.enqueue(new Callback<Episode>() {
            @Override
            public void onResponse(Call<Episode> call, Response<Episode> response) {
                if (response.isSuccessful()) {
                    Episode episode = response.body();
                    textViewName.setText(episode.getName());
                    textViewEpisode.setText(episode.getEpisode());
                    textViewDate.setText(episode.getAirDate());
                    getCharacters(episode.getCharacters());
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

    private void getCharacters(List<String> characterUrls) {
        for (String url : characterUrls) {
            int id = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
            Call<Character> call = rickAndMortyService.getCharacterById(id);
            call.enqueue(new Callback<Character>() {
                @Override
                public void onResponse(Call<Character> call, Response<Character> response) {
                    if (response.isSuccessful()) {
                        Character character = response.body();
                        characterList.add(character);
                        characterAdapter.setCharacterList(characterList);
                    } else {

                        // ...
                    }
                }
                @Override
                public void onFailure(Call<Character> call, Throwable t) {

                }
            });
        }
    }
}