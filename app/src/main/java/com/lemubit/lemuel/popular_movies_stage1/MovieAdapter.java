package com.lemubit.lemuel.popular_movies_stage1;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<MovieData> movies = Collections.emptyList();

    public MovieAdapter(Context context, List<MovieData> movies) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_container, parent, false);
        MyMovieHolder holder = new MyMovieHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        MyMovieHolder myHolder = (MyMovieHolder) holder;
        MovieData current = movies.get(position);

        try {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + current.posterPath).into(myHolder.moviePoster);
        } catch (Exception e) {
            Log.e("image eeer:", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class MyMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePoster;

        // create constructor to get widget reference
        public MyMovieHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.setOnTouchListener(new View.OnTouchListener() {

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        view.findViewById(R.id.imagePackage).getBackground().setHotspot(motionEvent.getX(), motionEvent.getY());


                        return false;
                    }
                });
            }
        }


        @Override
        public void onClick(View view) {
            MovieData current = movies.get(getAdapterPosition());

            Intent movie = new Intent(context, MovieDetail.class);
            movie.putExtra("image", current.posterPath);
            movie.putExtra("overview", current.Overview);
            movie.putExtra("title", current.title);
            movie.putExtra("date", current.releaseDate);
            movie.putExtra("rating", current.voteAverage);
            movie.putExtra("id", current.movieId);

            context.startActivity(movie);
        }
    }


}
