package com.lemubit.lemuel.popular_movies_stage1;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 20000;
    private RecyclerView movieRecycler;
    private movieAdapter mAdapter;
    private ProgressBar movieProgress;
    static String APIkey=""; //TODO: Hi! Your moviedb API key from themoviedb.org goes in here in order for the App to work
    private static final String POPULAR_URL="https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key="+APIkey;
    private static final String TOP_RATED="https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key="+APIkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieProgress=(ProgressBar) findViewById(R.id.movie_progress);
        new AsyncMovie().execute(POPULAR_URL);
    }

    public class AsyncMovie extends AsyncTask<String,Void,String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            movieProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... path) {

            /////////////////////////////GET url from string
            try {
                url=new URL(path[0]);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            ///////////////////////Make HTTP request
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.connect();

                // setDoOutput to true as we receive data from json file
                // conn.setDoOutput(true);

            } catch (IOException movieerr) {

                movieerr.printStackTrace();
                return movieerr.toString();
            }


            ////////////////////////////Check for code response and handle input stream
            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String movieResult) {
            super.onPostExecute(movieResult);
            movieProgress.setVisibility(View.INVISIBLE);
            List<movieData> movies=new ArrayList<>();


            try {
                JSONObject root=new JSONObject(movieResult);
                JSONArray movaray=root.getJSONArray("results");

                /////
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<movaray.length();i++){
                    JSONObject json_data = movaray.getJSONObject(i);
                    movieData aMovie = new movieData();
                    aMovie.Overview= json_data.getString("overview");
                    aMovie.posterPath= json_data.getString("poster_path");
                    Log.e("poster path:",json_data.getString("poster_path"));
                    aMovie.title= json_data.getString("title");
                    aMovie.releaseDate= json_data.getString("release_date");
                    aMovie.voteAverage=String.valueOf(json_data.getDouble("vote_average"));
                    movies.add(aMovie);
                }

                movieRecycler=(RecyclerView) findViewById(R.id.movie_recycler_view);
                mAdapter = new movieAdapter(MainActivity.this, movies);
                movieRecycler.setAdapter(mAdapter);
                movieRecycler.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e("JSON:",e.getMessage());
            }


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
                new AsyncMovie().execute(POPULAR_URL);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.rating:
                new AsyncMovie().execute(TOP_RATED);
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
