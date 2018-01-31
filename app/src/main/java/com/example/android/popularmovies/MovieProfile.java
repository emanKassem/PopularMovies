package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieProfile extends AppCompatActivity {

   TextView title, realese_date, rate, overview;
   ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.movietitleTV);
        realese_date = (TextView) findViewById(R.id.moviereleasedateTV);
        rate = (TextView) findViewById(R.id.movieratTV);
        overview = (TextView) findViewById(R.id.moviedescTV);
        poster = (ImageView) findViewById(R.id.movieposteIMG);

        Bundle bundle = getIntent().getExtras();

        Movie movie = (Movie) bundle.getParcelable("MOVIE");
        title.setText(movie.getmTitle());
        realese_date.setText(movie.getmReleaseDate());
        rate.setText(movie.getmVoteAverage().toString());
        overview.setText(movie.getmOverView());
        Picasso.with(poster.getContext())
                .load(movie.getmPosterPath())
                .into(poster);

    }
}
