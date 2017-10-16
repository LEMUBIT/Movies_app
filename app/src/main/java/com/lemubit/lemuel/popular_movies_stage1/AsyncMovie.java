package com.lemubit.lemuel.popular_movies_stage1;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


public class AsyncMovie extends AsyncTask<String, Void, String> {

    private RecyclerView movieRecycler;
    private MovieAdapter mAdapter;
    private ProgressBar movieProgress;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 20000;
    private HttpURLConnection conn;
    private URL url = null;
    private Activity movieActivity;


    //////////////////constructor

    public AsyncMovie(Activity movieActivity) {
        this.movieActivity = movieActivity;
        movieProgress = movieActivity.findViewById(R.id.movie_progress);
    }

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
            url = new URL(path[0]);
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
        List<MovieData> movies = new ArrayList<>();


        try {
            JSONObject root = new JSONObject(movieResult);
            JSONArray movaray = root.getJSONArray("results");


            // Extract data from json and store into ArrayList as class objects
            for (int i = 0; i < movaray.length(); i++) {
                JSONObject json_data = movaray.getJSONObject(i);
                MovieData aMovie = new MovieData();
                aMovie.Overview = json_data.getString("overview");
                aMovie.posterPath = json_data.getString("poster_path");
                aMovie.title = json_data.getString("title");
                aMovie.releaseDate = json_data.getString("release_date");
                aMovie.voteAverage = String.valueOf(json_data.getDouble("vote_average"));
                aMovie.movieId=String.valueOf(json_data.getInt("id"));
                movies.add(aMovie);
            }

            movieRecycler = movieActivity.findViewById(R.id.movie_recycler_view);
            mAdapter = new MovieAdapter(movieActivity, movies);
            movieRecycler.setAdapter(mAdapter);

            if (movieActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                movieRecycler.setLayoutManager(new GridLayoutManager(movieActivity, 2));
            } else {
                movieRecycler.setLayoutManager(new GridLayoutManager(movieActivity, 4));
            }


        } catch (JSONException e) {
            Toast.makeText(movieActivity, "Could not get movies!", Toast.LENGTH_LONG).show();
            Log.e("JSON:", e.getMessage());
        }


    }
}
