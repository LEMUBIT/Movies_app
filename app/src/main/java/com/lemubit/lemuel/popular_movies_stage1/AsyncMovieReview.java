package com.lemubit.lemuel.popular_movies_stage1;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;

public class AsyncMovieReview extends AsyncTask<String, Void, String> {
    private Activity movieDetail;
    private ProgressBar movieProgress;
    private TextView reviewTxt;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 20000;
    private HttpURLConnection conn;
    private URL url = null;

    static String APIkey = BuildConfig.Movie_API;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private String MOVIE_ID = "";
    private static final String QUERY = "/reviews?api_key=" + APIkey + "&language=en-US&page=1";

    public AsyncMovieReview(Activity movieDetail) {
        this.movieDetail = movieDetail;
        movieProgress = movieDetail.findViewById(R.id.detailProgressBar);
        reviewTxt = movieDetail.findViewById(R.id.review_content_txt);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        movieProgress.setVisibility(View.VISIBLE);
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
    protected void onPostExecute(String movieReview) {
        super.onPostExecute(movieReview);
        movieProgress.setVisibility(View.INVISIBLE);

        try {
            JSONObject root = new JSONObject(movieReview);
            JSONArray movaray = root.getJSONArray("results");

            for (int i = 0; i < movaray.length(); i++) {
                JSONObject json_data = movaray.getJSONObject(i);
                reviewTxt.append("**Author**: " + json_data.getString("author") + "\n");
                reviewTxt.append("Review:" + "\n");
                reviewTxt.append(json_data.getString("content") + "\n\n");
            }

        } catch (Exception e) {
            Toast.makeText(movieDetail, "Could not get reviews!", Toast.LENGTH_LONG).show();
            Log.e("JSON:", e.getMessage());
        }
    }
}
