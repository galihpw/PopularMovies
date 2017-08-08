package com.galihpw.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.galihpw.popularmovies.R;
import com.galihpw.popularmovies.TrailerFilm;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by galihpw on 8/7/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<TrailerFilm> mTrailerList;
    private Context mContext;
    final private OnTrailerItemClickListener mListener;

    public TrailerAdapter(List<TrailerFilm> trailerList, Context context, OnTrailerItemClickListener listener) {
        mTrailerList = trailerList;
        mContext = context;
        mListener = listener;

    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailer,parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TrailerFilm trillerFilm = mTrailerList.get(position);
        Picasso.with(mContext)
                .load(trillerFilm.getImageUrl())
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImageView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onTrailerItemClicked(mTrailerList.get(position).getYoutubeUrl());
        }
    }

    public interface OnTrailerItemClickListener {
        public void onTrailerItemClicked(String youtubeUrl);
    }

}
