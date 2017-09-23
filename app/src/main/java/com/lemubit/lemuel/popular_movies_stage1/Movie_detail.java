package com.lemubit.lemuel.popular_movies_stage1;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


public class Movie_detail extends AppCompatActivity {
    private ImageView movieImage;
    private TextView title,overview,rating,release_date;
    GradientDrawable ratingCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieImage=(ImageView) findViewById(R.id.movie_image);
        title=(TextView) findViewById(R.id.movie_title_txt);
        overview=(TextView) findViewById(R.id.overview_txt);
        //////////////////
        String ratingInt=getIntent().getExtras().getString("rating");

        rating=(TextView) findViewById(R.id.user_rating_txt);
        rating.setText(String.valueOf(ratingInt));
        ratingCircle = (GradientDrawable) rating.getBackground();
        int ratingcolor = setRating(Double.parseDouble(ratingInt));
        ratingCircle.setColor(ratingcolor);
        /////////////////
        release_date=(TextView) findViewById(R.id.release_date_txt);

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/"+getIntent().getExtras().getString("image")).resize(350,0).into(movieImage);

        title.setText(getIntent().getExtras().getString("title"));
        overview.setText(getIntent().getExtras().getString("overview"));
        release_date.setText(getIntent().getExtras().getString("date"));


    }


    public int setRating(Double rating)
    {
        int id=0;

        if(rating<=4.9)
        {
            id = ContextCompat.getColor(Movie_detail.this, R.color.bad);
        }
        else if(rating>4.9 && rating<=6.0)
        {
            id = ContextCompat.getColor(Movie_detail.this, R.color.fair);
        }
        else if(rating>6.0 && rating<=7.9)
        {
            id = ContextCompat.getColor(Movie_detail.this, R.color.good);
        }
        else if(rating>7.9 && rating<=10.0)
        {
            id = ContextCompat.getColor(Movie_detail.this, R.color.great);
        }

        return id;
    }
}
