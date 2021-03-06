package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import data.MoviesContract;

public class MainActivity extends AppCompatActivity {

    RecyclerView moviesShow;
    TextView mErrorMessagetv;
    static String sortby = "popular";
    ArrayList<Movie> movies = new ArrayList<Movie>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesShow = findViewById(R.id.rv_show_movies);
        mErrorMessagetv = findViewById(R.id.error_message);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        moviesShow.setLayoutManager(layoutManager);
        makeMoviesSearchQuery();
    }

    private void makeMoviesSearchQuery() {

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            URL moviesSearchQuery = NetworkUtils.buildUrl(sortby);
            new MovieQueryTask().execute(moviesSearchQuery);
        }else {
            showErrorMessage();
        }
    }


    public class MovieQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieJsonResults = null;
            try {
                movieJsonResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieJsonResults;
        }

        @Override
        protected void onPostExecute(String movieJsonResults) {
            if (movieJsonResults != null && !movieJsonResults.equals("")) {
                showRecycleView();
                movies = NetworkUtils.extractResultFromJson(movieJsonResults);
                MoviesAdapter moviesAdapter = new MoviesAdapter(MainActivity.this, movies, new MoviesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        Intent i = new Intent(MainActivity.this, MovieProfile.class);
                        i.putExtra("MOVIE", movie);
                        startActivity(i);
                    }
                });
                moviesShow.setAdapter(moviesAdapter);
            } else {
                showErrorMessage();
            }
        }
    }

    public class FavoriteQueryTask extends AsyncTask<Void, Void, Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                return getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(cursor != null) {
                showRecycleView();
                FavoriteAdapter favoriteAdapter = new FavoriteAdapter(MainActivity.this);
                favoriteAdapter.swapCursor(cursor);
                moviesShow.setAdapter(favoriteAdapter);
            }
        }
    }

    private void showRecycleView() {
        mErrorMessagetv.setVisibility(View.INVISIBLE);
        moviesShow.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        moviesShow.setVisibility(View.INVISIBLE);
        mErrorMessagetv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.sorting_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.topRated){
            sortby = "top_rated";
            makeMoviesSearchQuery();
            return true;
        }
        if(id == R.id.mostPopular){
            sortby = "popular";
            makeMoviesSearchQuery();
            return true;
        }
        else if(id == R.id.favorite){
            new FavoriteQueryTask().execute();
        }
        return super.onOptionsItemSelected(item);
    }
}
