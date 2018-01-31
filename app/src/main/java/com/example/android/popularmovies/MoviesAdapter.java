package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LENOVO on 04/01/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    ArrayList<Movie>movies;
    Context context;

    public interface OnItemClickListener {

        void onItemClick(Movie movie);
    }
    private final OnItemClickListener listener;


    public MoviesAdapter(Context context ,ArrayList<Movie> movies, OnItemClickListener listener){
        this.movies = movies;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public MoviesAdapter.MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false);
        MoviesHolder moviesHolder = new MoviesHolder(row);
        return moviesHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesHolder holder, int position) {

        holder.bind(movies.get(position), listener);
    }

    @Override
    public int getItemCount() {
       return movies.size();
    }


    class MoviesHolder extends RecyclerView.ViewHolder{

        ImageView poster;
        public MoviesHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.image);
        }

        public void bind(final Movie movie, final OnItemClickListener listener) {

            Picasso.with(poster.getContext())
                    .load(movie.getmPosterPath())
                    .into(poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(movie);
                }
            });
        }
    }
}
