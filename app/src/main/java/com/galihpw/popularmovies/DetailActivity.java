package com.galihpw.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.galihpw.popularmovies.config.Constant;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Movie mMovie;
    ImageView mImageBackdrop, mImagePoster;
    TextView mTextViewTitle, mTextViewReleaseDate, mTextViewSinopsis, mTextViewUserRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImagePoster = (ImageView) findViewById(R.id.posterImage);
        mImageBackdrop = (ImageView) findViewById(R.id.backdropImage);
        mTextViewTitle = (TextView) findViewById(R.id.movieTitle);
        mTextViewReleaseDate = (TextView) findViewById(R.id.releaseDate);
        /*mTextViewSinopsis = (TextView) findViewById(R.id.tv_sinopsis);
        mTextViewUserRating = (TextView) findViewById(R.id.tv_userRating);*/

        //fetching data from percable object
        mMovie = getIntent().getParcelableExtra("movie");

        Picasso.with(DetailActivity.this)
                .load(Constant.IMG_URL + mMovie.getImage())
                .resize(150, 225)
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(mImagePoster);

        Picasso.with(DetailActivity.this)
                .load(Constant.IMG_BACKDROP_URL + mMovie.getImageBackdrop())
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(mImageBackdrop);

        mTextViewTitle.setText(mMovie.getTitle());
        mTextViewReleaseDate.setText(mMovie.getReleaseDate());
        /*mTextViewSinopsis.setText(mMovie.getSinopsis());
        mTextViewUserRating.setText(mMovie.getUserRating() + "/10");*/

    }


}
