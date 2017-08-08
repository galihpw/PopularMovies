package com.galihpw.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.galihpw.popularmovies.adapter.TrailerAdapter;
import com.galihpw.popularmovies.adapter.UserReviewAdapter;
import com.galihpw.popularmovies.config.Constant;
import com.galihpw.popularmovies.helper.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.galihpw.popularmovies.database.MovieContract.BACKDROP_URL;
import static com.galihpw.popularmovies.database.MovieContract.CONTENT_URI;
import static com.galihpw.popularmovies.database.MovieContract.IMAGE_URL;
import static com.galihpw.popularmovies.database.MovieContract.RELEASE_DATE;
import static com.galihpw.popularmovies.database.MovieContract.SYNOPSIS;
import static com.galihpw.popularmovies.database.MovieContract.TITLE;
import static com.galihpw.popularmovies.database.MovieContract.USER_RATING;
import static com.galihpw.popularmovies.database.MovieContract._ID;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerItemClickListener, View.OnClickListener{

    private static final String TAG = DetailActivity.class.getSimpleName();

    Movie mMovie;
    ImageView mImgBackdrop, mImgPoster;
    TextView mTitle, mReleaseDate, mSynopsis, mUserRating;
    RecyclerView mRecyclerTrailer, mRecyclerUserReview;
    ImageButton mImageButtonFavorite;
    TrailerAdapter mTrailerAdapter;
    UserReviewAdapter mUserReviewAdapter;

    private ArrayList<TrailerFilm> mTrailerFilmsList;
    private ArrayList<UserReview> mUserReviewsList;

    boolean isBookmark = false;
    boolean isTrailer = false;
    boolean isUserReview = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImgPoster = (ImageView) findViewById(R.id.posterImage);
        mImgBackdrop = (ImageView) findViewById(R.id.backdropImage);
        mTitle = (TextView) findViewById(R.id.movieTitle);
        mReleaseDate = (TextView) findViewById(R.id.releaseDate);
        mSynopsis = (TextView) findViewById(R.id.synopsisMovie);
        mUserRating = (TextView) findViewById(R.id.userRating);
        mRecyclerTrailer = (RecyclerView) findViewById(R.id.recycle_view_trailer);
        mRecyclerUserReview = (RecyclerView) findViewById(R.id.recycle_view_user_review);
        mImageButtonFavorite = (ImageButton) findViewById(R.id.fav_love);

        if(savedInstanceState != null && savedInstanceState.containsKey("movie")){
            mMovie = savedInstanceState.getParcelable("movie");
        }else{
            mMovie = getIntent().getParcelableExtra("movie");
        }

        if(savedInstanceState != null && savedInstanceState.containsKey("isDownloadTrailer")){
            isTrailer = savedInstanceState.getBoolean("isDownloadTrailer");
        }
        if(savedInstanceState != null && savedInstanceState.containsKey("trailer")){
            mTrailerFilmsList = savedInstanceState.getParcelableArrayList("trailer");
        }else{
            mTrailerFilmsList = new ArrayList<TrailerFilm>();
            if(!isTrailer){
                new DownloadTrailer().execute(Util.getUrlTrailer(mMovie.getId()));
            }
        }

        //fetching data user review
        if(savedInstanceState != null && savedInstanceState.containsKey("isDownloadUserReview")){
            isUserReview = savedInstanceState.getBoolean("isDownloadUserReview");
        }
        if(savedInstanceState != null && savedInstanceState.containsKey("userreview")){
            mUserReviewsList = savedInstanceState.getParcelableArrayList("userreview");
        }else{
            mUserReviewsList = new ArrayList<UserReview>();
            if(!isUserReview){
                new DownloadUserRating().execute(Util.getUrlUserReview(mMovie.getId()));
            }
        }


        Picasso.with(DetailActivity.this)
                .load(Constant.IMG_URL + mMovie.getImage())
                .resize(150, 225)
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(mImgPoster);

        Picasso.with(DetailActivity.this)
                .load(Constant.IMG_BACKDROP_URL + mMovie.getImageBackdrop())
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into(mImgBackdrop);

        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mSynopsis.setText(mMovie.getSynopsis());
        mUserRating.setText(mMovie.getUserRating() + "/10");


        setImageBookmark();

        //Trailer
        mTrailerAdapter = new TrailerAdapter(mTrailerFilmsList, DetailActivity.this, DetailActivity.this);
        RecyclerView.LayoutManager layoutManagerTrailer = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL,false);
        mRecyclerTrailer.setLayoutManager(layoutManagerTrailer);
        mRecyclerTrailer.setItemAnimator(new DefaultItemAnimator());
        mRecyclerTrailer.setAdapter(mTrailerAdapter);

        mUserReviewAdapter = new UserReviewAdapter(mUserReviewsList, DetailActivity.this);
        RecyclerView.LayoutManager layoutManagerUserReview = new LinearLayoutManager(DetailActivity.this);
        mRecyclerUserReview.setLayoutManager(layoutManagerUserReview);
        mRecyclerUserReview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerUserReview.setAdapter(mUserReviewAdapter);

        mImageButtonFavorite.setOnClickListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mUserReviewsList != null){
            outState.putParcelableArrayList("userreview", mUserReviewsList);
        }
        if(mTrailerFilmsList != null){
            outState.putParcelableArrayList("trailer", mTrailerFilmsList);
        }
        if(mMovie != null){
            outState.putParcelable("movie",mMovie);
        }

        outState.putBoolean("isDownloadTrailer", isTrailer);
        outState.putBoolean("isDownloadUserReview", isUserReview);

    }

    private class DownloadTrailer extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            if(strings != null){
                String urlString = strings[0];
                //get data from api
                try {
                    URL url = new URL(urlString);
                    result = Util.downloadUrl(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                if(!s.isEmpty()){
                    Log.d(TAG,s);
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONArray results = object.getJSONArray("results");

                        mTrailerFilmsList.clear();
                        mTrailerAdapter.notifyDataSetChanged();
                        for(int i=0; i<results.length(); i++){
                            TrailerFilm trailerFilm = new TrailerFilm(results.getJSONObject(i));
                            mTrailerFilmsList.add(trailerFilm);
                            mTrailerAdapter.notifyDataSetChanged();
                        }
                        isTrailer = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG,e.toString());
                    }
                }
            }

        }
    }

    private class DownloadUserRating extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            if(strings != null){
                String urlString = strings[0];
                //get data from api
                try {
                    URL url = new URL(urlString);
                    result = Util.downloadUrl(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (!s.isEmpty()) {
                    Log.d(TAG,s);
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONArray results = object.getJSONArray("results");

                        mUserReviewsList.clear();
                        mUserReviewAdapter.notifyDataSetChanged();
                        for (int i = 0; i < results.length(); i++) {
                            UserReview userReview = new UserReview(results.getJSONObject(i));
                            mUserReviewsList.add(userReview);
                            mUserReviewAdapter.notifyDataSetChanged();
                        }
                        isUserReview = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                    }
                }
            }
        }
    }

    @Override
    public void onTrailerItemClicked(String youtubeUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(youtubeUrl));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        isBookmark = checkMovie();
        if(isBookmark){
            deleteRecord();
        }else{
            insertMovie();
        }
        setImageBookmark();
    }

    private void setImageBookmark() {
        isBookmark = checkMovie();
        if(isBookmark){
            mImageButtonFavorite.setBackgroundResource(R.drawable.love);
        }else {
            mImageButtonFavorite.setBackgroundResource(R.drawable.love_white);
        }
    }

    private void insertMovie() {
        ContentValues values = new ContentValues();
        values.put(_ID, mMovie.getId());
        values.put(TITLE, mMovie.getTitle());
        values.put(IMAGE_URL, mMovie.getImage());
        values.put(SYNOPSIS, mMovie.getSynopsis());
        values.put(USER_RATING, mMovie.getUserRating());
        values.put(RELEASE_DATE, mMovie.getReleaseDate());
        values.put(BACKDROP_URL, mMovie.getImageBackdrop());

        Uri uri = getContentResolver().insert(
                CONTENT_URI, values);
        Log.d(TAG, uri.toString());
        Toast.makeText(this, "Add to favorite movie", Toast.LENGTH_SHORT).show();
    }

    private boolean checkMovie(){
        String[] projection = {
                TITLE
        };
        Uri uri = Uri.parse(CONTENT_URI + "/" + mMovie.getId());
        Cursor cursor = getContentResolver().query(uri, projection,null,null,null);
        if(cursor == null){
            return false;
        }
        if(cursor.getCount() == 0){
            return false;
        }
        return true;
    }

    private void deleteRecord(){
        Uri uri = Uri.parse(CONTENT_URI + "/" + mMovie.getId());
        getContentResolver().delete(uri,null,null);
        Toast.makeText(this, "Delete from favorite movie", Toast.LENGTH_SHORT).show();
    }

}
