package com.lemubit.lemuel.popular_movies_stage1.localData;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lemubit.lemuel.popular_movies_stage1.MovieAdapter;
import com.lemubit.lemuel.popular_movies_stage1.MovieData;
import com.lemubit.lemuel.popular_movies_stage1.MovieDetail;
import com.lemubit.lemuel.popular_movies_stage1.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by charl on 16/10/2017.
 */

public class MovieDbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor mCursor;
    private Context context;
    private LayoutInflater inflater;

    public MovieDbAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_container, parent, false);
        MyMovieHolder holder = new MyMovieHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyMovieHolder myHolder = (MyMovieHolder) holder;
        int movieUrl = mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_IMAGE_URL);
        int movieID = mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID);

        mCursor.moveToPosition(position);


        try {
            Picasso.with(context).load(mCursor.getString(movieUrl)).networkPolicy(NetworkPolicy.OFFLINE).into(myHolder.moviePoster);


        } catch (Exception e) {
            Log.e("image eeer:", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {

        if (mCursor == cursor) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = cursor; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
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
            int movieID = mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID);
            int title=mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE);
            int overview=mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_OVERVIEW);
            int date=mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_RELEASE_DATE);
            int imagePath=mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_IMAGE_PATH);
            int rating=mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_RATING);

            Intent movie = new Intent(context, MovieDetail.class);
            movie.putExtra("image", mCursor.getString(imagePath));
            movie.putExtra("overview", mCursor.getString(overview));
            movie.putExtra("title", mCursor.getString(title));
            movie.putExtra("date", mCursor.getString(date));
            movie.putExtra("rating", mCursor.getString(rating));
            movie.putExtra("id",mCursor.getString(movieID));

            context.startActivity(movie);
        }
    }
}
