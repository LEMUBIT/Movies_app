package com.lemubit.lemuel.popular_movies_stage1;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    static String APIkey=  BuildConfig.Movie_API;
    private static final String POPULAR_URL="https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key="+APIkey;
    private static final String TOP_RATED="https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key="+APIkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncMovie(MainActivity.this).execute(POPULAR_URL);
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
