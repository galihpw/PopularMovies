package com.galihpw.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galihpw.popularmovies.R;

import java.util.List;

/**
 * Created by galihpw on 8/7/2017.
 */

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewHolder>{

    private List<com.galihpw.popularmovies.UserReview> mUserReviewList;

    public UserReviewAdapter(List<com.galihpw.popularmovies.UserReview> userReviewList, Context context) {
        mUserReviewList = userReviewList;
        Context mContext;
        mContext = context;
    }

    @Override
    public UserReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_review, parent, false);
        return new UserReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserReviewHolder holder, int position) {
        com.galihpw.popularmovies.UserReview userReview = mUserReviewList.get(position);
        holder.namaUserReview.setText(userReview.getName());
        holder.mReview.setText(userReview.getReview());
    }

    @Override
    public int getItemCount() {
        return mUserReviewList.size();
    }

    class UserReviewHolder extends RecyclerView.ViewHolder{

        TextView namaUserReview;
        TextView mReview;

        UserReviewHolder(View itemView) {
            super(itemView);
            namaUserReview = (TextView) itemView.findViewById(R.id.rev_name);
            mReview = (TextView) itemView.findViewById(R.id.review);
        }
    }


}
