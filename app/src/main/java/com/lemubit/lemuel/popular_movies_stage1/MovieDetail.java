package com.lemubit.lemuel.popular_movies_stage1;

import android.content.ContentValues;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.lemubit.lemuel.popular_movies_stage1.localData.MovieContract;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetail extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);//to bind views

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*Get extras**/
        final String titleExtra = getIntent().getExtras().getString("title");
        final String overviewExtra = getIntent().getExtras().getString("overview");
        final String dateExrta = getIntent().getExtras().getString("date");
        final String MovieIdExtra = getIntent().getExtras().getString("id");
        final String imagePathExtra = getIntent().getExtras().getString("image");
        final String ratingInt = getIntent().getExtras().getString("rating");
        final String imageBase="http://image.tmdb.org/t/p/w185/";

        /**Set rating colour*/
        rating.setText(String.valueOf(ratingInt));
        ratingCircle = (GradientDrawable) rating.getBackground();
        int ratingcolor = setRating(Double.parseDouble(ratingInt));
        ratingCircle.setColor(ratingcolor);

        //set Image, Title, Overview and Date
        Picasso.with(this).load(imageBase + imagePathExtra).resize(300, 450).centerCrop().into(movieImage);
        title.setText(titleExtra);
        overview.setText(overviewExtra);
        release_date.setText(dateExrta);


        favMovie.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                //If just changed to favourite then insert
                String imageUrl=imageBase+imagePathExtra;
                if (favorite) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.MOVIE_TITLE, titleExtra);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_ID, MovieIdExtra);
                    contentValues.put(MovieContract.MovieEntry.MOVIE_IMAGE_URL,imageUrl);

                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                    if (uri != null) {
                        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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


}
