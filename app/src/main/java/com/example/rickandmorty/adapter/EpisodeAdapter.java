package com.example.rickandmorty.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickandmorty.EpisodeActivity;
import com.example.rickandmorty.R;
import com.example.rickandmorty.resource.remote.Episode;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>{
    private Context context;
    private List<Episode> episodeList;
    private List<Episode> allEpisodes;

    public EpisodeAdapter(Context context, List<Episode> episodeList) {
        this.context = context;
        this.episodeList = episodeList;
        this.allEpisodes = new ArrayList<>(episodeList);
    }
    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EpisodeAdapter.EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.EpisodeViewHolder holder, int position) {
        Episode episode = episodeList.get(position);
        holder.textViewName.setText(episode.getName());
        holder.textViewEpisode.setText(episode.getEpisode());
        holder.textViewDate.setText(episode.getAirDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EpisodeActivity.class);
                intent.putExtra("episode_id", episode.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeList == null ? 0 : episodeList.size();
    }

    public class EpisodeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName, textViewEpisode, textViewDate;
        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEpisode = itemView.findViewById(R.id.textViewEpisode);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}
