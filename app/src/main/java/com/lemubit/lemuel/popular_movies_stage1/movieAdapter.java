package com.lemubit.lemuel.popular_movies_stage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class movieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<movieData> movies = Collections.emptyList();

    public movieAdapter(Context context, List<movieData> movies) {
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
        movieData current = movies.get(position);

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
        }


        @Override
        public void onClick(View view) {
            movieData current = movies.get(getAdapterPosition());

            Intent movie = new Intent(context, Movie_detail.class);
            movie.putExtra("image", current.posterPath);
            movie.putExtra("overview", current.Overview);
            movie.putExtra("title", current.title);
            movie.putExtra("date", current.releaseDate);
            movie.putExtra("rating", current.voteAverage);
            context.startActivity(movie);
        }
    }


}
