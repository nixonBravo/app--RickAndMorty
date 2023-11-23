package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.rickandmorty.adapter.EpisodeAdapter;
import com.example.rickandmorty.api.RetrofitClientRemote;
import com.example.rickandmorty.resource.remote.Episode;
import com.example.rickandmorty.resource.remote.EpisodesResponse;
import com.example.rickandmorty.resource.remote.RickAndMortyService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EpisodeAdapter episodeAdapter;
    private RickAndMortyService rickAndMortyService;
    private List<Episode> allEpisodes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        episodeAdapter = new EpisodeAdapter(this, allEpisodes);
        recyclerView.setAdapter(episodeAdapter);

        rickAndMortyService = RetrofitClientRemote.getInstance().getRickAndMortyService();

        setupSearchView();
        getAllEpisodes(null);
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getAllEpisodes(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getAllEpisodes(newText);
                return true;
            }
        });
    }

    private void getAllEpisodes(String query) {
        Call<EpisodesResponse> call = rickAndMortyService.getAllEpisodes(1, query);

        call.enqueue(new Callback<EpisodesResponse>() {
            @Override
            public void onResponse(Call<EpisodesResponse> call, Response<EpisodesResponse> response) {
                if (response.isSuccessful()) {

                    EpisodesResponse episodesResponse = response.body();
                    allEpisodes.clear();
                    allEpisodes.addAll(episodesResponse.getResults());
                    int totalPages = episodesResponse.getInfo().getPages();

                    for (int i = 2; i <= totalPages; i++) {
                        int finalI = i;
                        rickAndMortyService.getAllEpisodes(i, query).enqueue(new Callback<EpisodesResponse>() {
                            @Override
                            public void onResponse(Call<EpisodesResponse> call, Response<EpisodesResponse> response) {
                                if (response.isSuccessful()) {
                                    EpisodesResponse episodesResponse = response.body();
                                    allEpisodes.addAll(episodesResponse.getResults());
                                    if (finalI == totalPages) {
                                        updateUI(query);
                                    }
                                } else {
                                    Toast.makeText(EpisodesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<EpisodesResponse> call, Throwable t) {
                                Toast.makeText(EpisodesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    updateUI(query);
                } else {
                    Toast.makeText(EpisodesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<EpisodesResponse> call, Throwable t) {
                Toast.makeText(EpisodesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(String query) {
        if (query != null && !query.isEmpty()) {
            filterEpisodes(query);
        } else {
            episodeAdapter.setEpisodeList(allEpisodes);
        }
    }

    private void filterEpisodes(String query) {
        List<Episode> filteredEpisodes = new ArrayList<>();

        for (Episode episode : allEpisodes) {
            if (episode.getName().toLowerCase().contains(query.toLowerCase()) ||
                    episode.getName().equalsIgnoreCase(query)) {
                filteredEpisodes.add(episode);
            }
        }

        episodeAdapter.setEpisodeList(filteredEpisodes.isEmpty() && !query.isEmpty() ? allEpisodes : filteredEpisodes);
    }
}