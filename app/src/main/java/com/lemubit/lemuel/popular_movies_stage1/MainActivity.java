package com.lemubit.lemuel.popular_movies_stage1;


import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    RecyclerView movieRecycler;
    static String APIkey = BuildConfig.Movie_API;
    private static final String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=" + APIkey;
    private static final String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=" + APIkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieRecycler = (RecyclerView) findViewById(R.id.movie_recycler_view);
        new AsyncMovie(MainActivity.this).execute(POPULAR_URL);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
