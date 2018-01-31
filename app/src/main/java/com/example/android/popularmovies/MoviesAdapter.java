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
    public MoviesAdapter(Context context ,ArrayList<Movie> movies){
        this.movies = movies;
        this.context = context;
    }
    @Override
    public MoviesAdapter.MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false);
        MoviesHolder moviesHolder = new MoviesHolder(row);
        return moviesHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesHolder holder, int position) {

        final Movie movie = movies.get(position);
        Picasso.with(holder.poster.getContext())
                .load(movie.getmPosterPath())
                .into(holder.poster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MovieProfile.class);
                i.putExtra("MOVIE", movie);
                context.startActivity(i);
            }
        });
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
    }
}
