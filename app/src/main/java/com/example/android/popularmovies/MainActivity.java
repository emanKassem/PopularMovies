package com.example.android.popularmovies;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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
                MoviesAdapter moviesAdapter = new MoviesAdapter(MainActivity.this ,movies);
                moviesShow.setAdapter(moviesAdapter);
            } else {
                showErrorMessage();
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

}
