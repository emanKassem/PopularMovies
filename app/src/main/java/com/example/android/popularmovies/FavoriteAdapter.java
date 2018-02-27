package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import data.MoviesContract;

/**
 * Created by LENOVO on 27/02/2018.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>{

    private Cursor mCursor;
    private Context mContext;

    public FavoriteAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public FavoriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        FavoriteHolder favoriteHolder = new FavoriteHolder(view);
        return favoriteHolder;
    }

    @Override
    public void onBindViewHolder(FavoriteHolder holder, int position) {
        mCursor.moveToPosition(position);
        String posterPath = mCursor.getString(
                mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));
        Picasso.with(holder.posterImageView.getContext())
                .load(posterPath)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public class FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView posterImageView;
        public FavoriteHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MovieProfile.class);
            mCursor.moveToPosition(getAdapterPosition());
            Movie currentMovie = getMovieFromCursor();
            intent.putExtra("MOVIE", currentMovie);
            view.getContext().startActivity(intent);
        }
    }

    private Movie getMovieFromCursor() {
        Movie currentMovie = new Movie();
        currentMovie.setmTitle(mCursor.getString(
                mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)));
        currentMovie.setId(mCursor.getInt(
                mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ID)));
        currentMovie.setmOverView(mCursor.getString(
                mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)));
        currentMovie.setmPosterPath(mCursor.getString(
                mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)));
        currentMovie.setmReleaseDate(mCursor.getString(
                mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));
        currentMovie.setmVoteAverage(mCursor.getDouble(
                mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
        return currentMovie;
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
