package com.lemubit.lemuel.popular_movies_stage1;

import android.app.Activity;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by charl on 17/10/2017.
 */

public class AsyncMovieTrailer extends AsyncTask<String, Void, String> {
    private Activity movieDetail;
    private Spinner trailerSpinner;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 20000;
    private HttpURLConnection conn;
    private URL url = null;

    static String APIkey = BuildConfig.Movie_API;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private String MOVIE_ID = "";
    private static final String QUERY = "/videos?api_key=" + APIkey + "&language=en-US";

    public AsyncMovieTrailer(Activity movieDetail) {
        this.movieDetail = movieDetail;
        trailerSpinner = movieDetail.findViewById(R.id.trailerSpinner);

    }

    @Override
    protected String doInBackground(String... movieID) {
        MOVIE_ID = movieID[0];
        String fullURL = BASE_URL + MOVIE_ID + QUERY;

        /*
        * *GET url from string**/
        try {
            url = new URL(fullURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /*
        * Make HTTP request
        * */
        try {

            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.connect();

        } catch (IOException reviewget) {

            reviewget.printStackTrace();
            return reviewget.toString();
        }


    /*Check for code response and handle input stream
      *
      */
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
    protected void onPostExecute(String movieTrailers) {
        super.onPostExecute(movieTrailers);
        ArrayList<String> trailerName = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(movieTrailers);
            JSONArray movaray = root.getJSONArray("results");

            for (int i = 0; i < movaray.length(); i++) {
                JSONObject json_data = movaray.getJSONObject(i);
                trailerName.add(i, json_data.getString("name"));
                MovieDetail.TrailerID.add(i, json_data.getString("key"));
            }
        } catch (Exception e) {

        }
        ArrayAdapter<String> movieSpnr = new ArrayAdapter<>(movieDetail,
                android.R.layout.simple_spinner_item, trailerName);

        trailerSpinner.setAdapter(movieSpnr);


        trailerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                * Set current_Trailer ID to ID of selected Trailer
                * */
                MovieDetail.currentTrailerID = MovieDetail.TrailerID.get(adapterView.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
