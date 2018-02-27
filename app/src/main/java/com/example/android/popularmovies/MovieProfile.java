package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import data.MoviesContract;
import data.MoviesContract.MovieEntry;

public class MovieProfile extends AppCompatActivity {

   TextView title, realese_date, rate, overview;
   ImageView poster, favorite;
   LinearLayout reviewsLinearLayout, trailersLinearLayout;
   RecyclerView reviewRecyclerView, trailerRecyclerView;
   ArrayList<Review> reviews = new ArrayList<>();
   ArrayList<Trailer> trailers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.movietitleTV);
        realese_date = findViewById(R.id.moviereleasedateTV);
        rate = findViewById(R.id.movieratTV);
        overview = findViewById(R.id.moviedescTV);
        poster = findViewById(R.id.movieposteIMG);
        favorite = findViewById(R.id.imageViewFavorite);
        reviewsLinearLayout = findViewById(R.id.layoutForReviews);
        trailersLinearLayout = findViewById(R.id.layoutForTrailer);
        reviewRecyclerView = findViewById(R.id.reviewsRecycleView);
        trailerRecyclerView = findViewById(R.id.trailerRecycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MovieProfile.this);
        reviewRecyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(MovieProfile.this);
        trailerRecyclerView.setLayoutManager(layoutManager2);

        Bundle bundle = getIntent().getExtras();

        final Movie movie = bundle.getParcelable("MOVIE");
        title.setText(movie.getmTitle());
        realese_date.setText(movie.getmReleaseDate());
        rate.setText(movie.getmVoteAverage().toString());
        overview.setText(movie.getmOverView());
        Picasso.with(poster.getContext())
                .load(movie.getmPosterPath())
                .into(poster);

        makeReviewsAndTrailersSearchQuery(movie.getId());

        if(checkIfMovieInDb(movie)){
            favorite.setImageResource(R.drawable.ic_favorite_black_24px);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddMovie(movie);
            }
        });
    }

    private void makeReviewsAndTrailersSearchQuery(int id) {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            URL reviewSearchQuery = NetworkUtils.buildUrlForTrailerOrReviews(String.valueOf(id), "reviews");
            new ReviewQueryTask().execute(reviewSearchQuery);
            URL trailersSearchQuery = NetworkUtils.buildUrlForTrailerOrReviews(String.valueOf(id), "videos");
            new TrailerQueryTask().execute(trailersSearchQuery);
        }
    }

    public void onClickAddMovie(Movie movie) {
        boolean b = checkIfMovieInDb(movie);
        if(b != true) {
            insertMovieIntoDb(movie);
            favorite.setImageResource(R.drawable.ic_favorite_black_24px);
        }
        else{
            favorite.setImageResource(R.drawable.ic_favorite_border_black_24px);
            deleteMovieFromDb(movie);
        }
    }

    private void deleteMovieFromDb(Movie movie) {
        String[] selectionArg = {String.valueOf(movie.getId())};
        getContentResolver().delete(MovieEntry.CONTENT_URI, MovieEntry.COLUMN_ID+"=?", selectionArg);
    }

    private boolean checkIfMovieInDb(Movie movie) {
        Cursor cursor = getContentResolver().query(
                MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int movieId = cursor.getInt(
                        cursor.getColumnIndex(MovieEntry.COLUMN_ID));
                if (movieId == movie.getId()) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    private void insertMovieIntoDb(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getmTitle());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getmReleaseDate());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getmPosterPath());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getmVoteAverage());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getmOverView());
        getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
    }

    public class ReviewQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String reviewJsonResults = null;
            try {
                reviewJsonResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviewJsonResults;
        }

        @Override
        protected void onPostExecute(String s) {
            reviews = NetworkUtils.extactReviewsFromJson(s);
            if(reviews != null){
                reviewsLinearLayout.setVisibility(View.VISIBLE);
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(MovieProfile.this, reviews);
                reviewRecyclerView.setAdapter(reviewsAdapter);

            }
        }
    }

    public class TrailerQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String trailerJsonResults = null;
            try {
                trailerJsonResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailerJsonResults;
        }

        @Override
        protected void onPostExecute(String s) {
            trailers = NetworkUtils.extactTrailersFromJson(s);
            if(trailers != null){
                trailersLinearLayout.setVisibility(View.VISIBLE);
                TrailersAdapter trailersAdapter = new TrailersAdapter(MovieProfile.this, trailers, new TrailersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Trailer trailer) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.youtube.com/watch?v="+trailer.getKey()));
                        startActivity(intent);
                    }
                });
                trailerRecyclerView.setAdapter(trailersAdapter);
            }
        }
    }
}
