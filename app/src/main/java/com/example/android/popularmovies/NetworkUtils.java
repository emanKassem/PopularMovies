package com.example.android.popularmovies;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by LENOVO on 04/01/2018.
 */

public class NetworkUtils {

    final static String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    final static String IMAGE_BASE_URL =
            "http://image.tmdb.org/t/p/";

    final static String IMAGE_SIZE =  "w185";

    private static final String API_KEY = BuildConfig.API_KEY;



    public static URL buildUrl(String sortby) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL+sortby).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlForTrailerOrReviews(String id, String type){
        Uri buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(id).appendPath(type)
                .appendQueryParameter("api_key", API_KEY).build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    public static ArrayList<Movie> extractResultFromJson(String movieJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(movieJSON);
            JSONArray resultArray = reader.getJSONArray("results");
            for(int i=0; i< resultArray.length(); i++) {
                JSONObject movie = resultArray.getJSONObject(i);
                String title = movie.getString("title");
                String date = movie.getString("release_date");
                String posterPath = IMAGE_BASE_URL + IMAGE_SIZE + movie.getString("poster_path");
                Double vote = movie.getDouble("vote_average");
                String overView =  movie.getString("overview");
                int id = movie.getInt("id");
                Movie m = new Movie();
                m.setmTitle(title);
                m.setmReleaseDate(date);
                m.setmPosterPath(posterPath);
                m.setmVoteAverage(vote);
                m.setmOverView(overView);
                m.setId(id);
                movies.add(m);
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing the news JSON results", e);
        }

        return movies;
    }

    public static ArrayList<Review> extactReviewsFromJson(String reviewsJson){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(reviewsJson)) {
            return null;
        }

        ArrayList<Review> reviews = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(reviewsJson);
            JSONArray resultArray = reader.getJSONArray("results");
            for(int i=0; i< resultArray.length(); i++) {
                JSONObject review = resultArray.getJSONObject(i);
                String contents = review.getString("content");
                String author = review.getString("author");
                Review r = new Review();
                r.setAuthor(author);
                r.setContent(contents);
                reviews.add(r);
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing the news JSON results", e);
        }

        return reviews;
    }


    public static ArrayList<Trailer> extactTrailersFromJson(String trailersJson){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(trailersJson)) {
            return null;
        }

        ArrayList<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(trailersJson);
            JSONArray resultArray = reader.getJSONArray("results");
            for(int i=0; i< resultArray.length(); i++) {
                JSONObject trailer = resultArray.getJSONObject(i);
                String id = trailer.getString("id");
                String key = trailer.getString("key");
                String name = trailer.getString("name");
                String site = trailer.getString("site");
                String type = trailer.getString("type");
                Trailer t = new Trailer();
                t.setId(id);
                t.setKey(key);
                t.setName(name);
                t.setSite(site);
                t.setType(type);
                trailers.add(t);
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing the news JSON results", e);
        }

        return trailers;
    }
}
