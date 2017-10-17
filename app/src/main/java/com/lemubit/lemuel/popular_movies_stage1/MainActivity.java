package com.lemubit.lemuel.popular_movies_stage1;


import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lemubit.lemuel.popular_movies_stage1.localData.MovieContract;
import com.lemubit.lemuel.popular_movies_stage1.localData.MovieDbAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.movie_recycler_view)
    RecyclerView movieRecycler;

    static String APIkey = BuildConfig.Movie_API;
    private static final String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=" + APIkey;
    private static final String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=" + APIkey;
    private MovieDbAdapter mAdapter;
    private static final int MOVIE_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new AsyncMovie(MainActivity.this).execute(POPULAR_URL);
        mAdapter = new MovieDbAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //to make it more user friendly :)
        if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieRecycler.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        } else {
            movieRecycler.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.popular:
                new AsyncMovie(MainActivity.this).execute(POPULAR_URL);
                return true;
            case R.id.rating:
                new AsyncMovie(MainActivity.this).execute(TOP_RATED);
                return true;
            case R.id.favourite:
                movieRecycler.setAdapter(mAdapter);
                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*LOADER FOR CONTENT PROVIDER, TO ENABLE OFFLINE VIEW*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {
            // Initialize a Cursor to hold movie data
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.MOVIE_TITLE
                    );

                } catch (Exception e) {
                    Log.e("DB GET error", "Failed to load data");
                    e.printStackTrace();
                    return null;
                }

            }


            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }

        };


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
