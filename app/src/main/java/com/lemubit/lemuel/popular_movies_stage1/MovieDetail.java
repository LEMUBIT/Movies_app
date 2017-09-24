package com.lemubit.lemuel.popular_movies_stage1;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


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

    GradientDrawable ratingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);//to bind views

        //////////////////
        String ratingInt = getIntent().getExtras().getString("rating");
        rating.setText(String.valueOf(ratingInt));
        ratingCircle = (GradientDrawable) rating.getBackground();
        int ratingcolor = setRating(Double.parseDouble(ratingInt));
        ratingCircle.setColor(ratingcolor);
        /////////////////
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + getIntent().getExtras().getString("image")).resize(300, 450).centerCrop().into(movieImage);

        title.setText(getIntent().getExtras().getString("title"));
        overview.setText(getIntent().getExtras().getString("overview"));
        release_date.setText(getIntent().getExtras().getString("date"));

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
