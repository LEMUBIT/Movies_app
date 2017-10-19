package com.lemubit.lemuel.popular_movies_stage1.localData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by charl on 16/10/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movie.db";
    public static final int VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String MOVIE_DB_CREATE_SQL = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
                + "(" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_ID + " INTEGER NOT NULL ," +
                MovieContract.MovieEntry.MOVIE_IMAGE_URL + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_IMAGE_PATH + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_RATING + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                "UNIQUE(" + MovieContract.MovieEntry.MOVIE_ID + ")" +
                ")";

        sqLiteDatabase.execSQL(MOVIE_DB_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //Will use Alter table when upgraded :)

    }
}
