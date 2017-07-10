package com.galihpw.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.galihpw.popularmovies.Movie;
import com.galihpw.popularmovies.R;
import com.galihpw.popularmovies.config.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GalihPW on 10/07/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{

    private List<Movie> mMovieList;
    private Context mContext;

    final private OnRecyclerViewItemClickListener mListener;

    public MovieAdapter(List<Movie> movieList, Context context, OnRecyclerViewItemClickListener listener) {
        mMovieList = movieList;
        mContext = context;
        mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        holder.movieTitle.setText(movie.getTitle());
        Picasso.with(mContext)
                .load(Constant.IMG_URL + movie.getImage())
                .resize(200,300)
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView movieImage;
        public TextView movieTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.moviePoster);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onRecyclerViewItemClicked(position);
        }
    }

    public interface OnRecyclerViewItemClickListener{
        public void onRecyclerViewItemClicked(int position);
    }


}