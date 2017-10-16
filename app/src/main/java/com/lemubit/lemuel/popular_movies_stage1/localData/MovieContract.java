package com.lemubit.lemuel.popular_movies_stage1.localData;

import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {


    public static final String AUTHORITY = "com.lemubit.lemuel.popular_movies_stage1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "myMovies";


    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();

        //Movie table and columns
        public static final String TABLE_NAME = "myMovies";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_ID = "movieID";
        public static final String MOVIE_IMAGE_URL="imageURL";
    }
}
