package com.lemubit.lemuel.popular_movies_stage1;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.lemubit.lemuel.popular_movies_stage1.localData.MovieContract;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetail extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {


    @BindView(R.id.movie_image)
    ImageView movieImage;
    @BindView(R.id.movie_title_txt)
    TextView title;
    @BindView(R.id.overview_txt)
    TextView overview;
    @BindView(R.id.release_date_txt)
    TextView release_date;
    @BindView(R.id.user_rating_txt)
    TextView rating;
    @BindView(R.id.favBtn)
    MaterialFavoriteButton favMovie;

    GradientDrawable ratingCircle;


    //UserAction: To tell whether is is the loader trying to click the Favorite Button
    public Boolean UserAction = true;


    public String LOADMovieID;
    private static final int MOVIE_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);//to bind views

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*Get extras**/
        final String titleExtra = getIntent().getExtras().getString("title");
        final String overviewExtra = getIntent().getExtras().getString("overview");
        final String dateExtra = getIntent().getExtras().getString("date");
        final String MovieIdExtra = getIntent().getExtras().getString("id");
        LOADMovieID = MovieIdExtra;
        final String imagePathExtra = getIntent().getExtras().getString("image");
        final String ratingExtra = getIntent().getExtras().getString("rating");
        final String imageBase = "http://image.tmdb.org/t/p/w185/";

        /**Set rating colour*/
        rating.setText(ratingExtra);
        ratingCircle = (GradientDrawable) rating.getBackground();
        int ratingcolor = setRating(Double.parseDouble(ratingExtra));
        ratingCircle.setColor(ratingcolor);

        //set Image, Title, Overview and Date
        Picasso.with(this).load(imageBase + imagePathExtra).resize(300, 450).centerCrop().into(movieImage);
        title.setText(titleExtra);
        overview.setText(overviewExtra);
        release_date.setText(dateExtra);


        favMovie.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                //If just changed to favourite then insert
                String imageUrl = imageBase + imagePathExtra;

                if (favorite && UserAction) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.MOVIE_TITLE, titleExtra);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_ID, MovieIdExtra);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_IMAGE_URL, imageUrl);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_OVERVIEW, overviewExtra);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_IMAGE_PATH, imagePathExtra);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_RATING, ratingExtra);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE, dateExtra);

                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                    if (uri != null) {
                        Toast.makeText(getBaseContext(), "Added to favourites", Toast.LENGTH_LONG).show();
                    }
                } else if(UserAction && favorite==false) {
                    Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(MovieIdExtra).build();
                    int deleted=getContentResolver().delete(uri, null, null);

                    if(deleted>0)
                    Toast.makeText(getBaseContext(), "Removed from favourites", Toast.LENGTH_LONG).show();

                }
            }
        });

        /**
         * Initialize Loader
         * */
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }


    public int setRating(Double rating) {
        int id = 0;

        if (rating <= 4.9) {
            id = ContextCompat.getColor(MovieDetail.this, R.color.bad);
        } else if (rating > 4.9 && rating <= 6.0) {
            id = ContextCompat.getColor(MovieDetail.this, R.color.fair);
        } else if (rating > 6.0 && rating <= 7.9) {
            id = ContextCompat.getColor(MovieDetail.this, R.color.good);
        } else if (rating > 7.9 && rating <= 10.0) {
            id = ContextCompat.getColor(MovieDetail.this, R.color.great);
        }

        return id;
    }


    /**
     * CHECK IF MOVIE HAS BEEN SET AS FAVOURITE, IF SO DISPLAY IT USING THE MaterialFavoriteButton
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(LOADMovieID).build(),
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
        };


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > 0) {
            UserAction = false;//SET IT TO FALSE BECAUSE IT IS THE LOADER ACTING NOW
            favMovie.setFavorite(true, true);//SET THE FAVOURITE BUTTON
            UserAction=true;//SET IT TO TRUE AFTER OPERATION SO THAT USER CAN PERFORM ACTION
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
