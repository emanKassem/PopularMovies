package data;

import android.net.Uri;

/**
 * Created by LENOVO on 27/02/2018.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "movies";

    public static final class MovieEntry{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_OVERVIEW = "overview";
    }
}
