package com.galihpw.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.galihpw.popularmovies.adapter.MovieAdapter;
import com.galihpw.popularmovies.database.MovieContract;
import com.galihpw.popularmovies.config.CustomItemOffset;
import com.galihpw.popularmovies.helper.Util;

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
import static com.galihpw.popularmovies.database.MovieContract.SINOPSIS;
import static com.galihpw.popularmovies.database.MovieContract.TITLE;
import static com.galihpw.popularmovies.database.MovieContract.USER_RATING;
import static com.galihpw.popularmovies.database.MovieContract._ID;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnRecyclerViewItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mNoData;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpSharedreference();

        mMovieList = new ArrayList<Movie>();

        if(savedInstanceState != null && savedInstanceState.containsKey("movie")){
            mMovieList = savedInstanceState.getParcelableArrayList("movie");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.movieRecycle);
        mProgressBar = (ProgressBar) findViewById(R.id.loadingListMovie);
        mNoData = (TextView) findViewById(R.id.noInternet);

        mAdapter = new MovieAdapter(mMovieList, MainActivity.this, MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new CustomItemOffset(MainActivity.this, R.dimen.margin_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        if(mMovieList.isEmpty()){
            getMovie();
        }
    }

    public void setUpSharedreference(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.refresh:
                getMovie();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, MyPreferenceActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMovieList != null){
            outState.putParcelableArrayList("movie", mMovieList);
        }
    }

    //function for download movie
    public void getMovie(){
        String sortOrder = mSharedPreferences.getString(getString(R.string.sort_order_key), getString(R.string.sort_order_default));
        if(sortOrder.equals("favorite")){
            getMovieFromDatabase();
        }else{
            String url = Util.getUrl(sortOrder);
            if(Util.isNetworkConnected(MainActivity.this)){
                new DownloadMovie().execute(url);
            }else {
                showNoConnectionView();
            }
        }
    }

    public void getMovieFromDatabase(){
        String[] projection = {
                _ID,
                TITLE,
                IMAGE_URL,
                SINOPSIS,
                USER_RATING,
                RELEASE_DATE,
                BACKDROP_URL

        };
        Uri uri = CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                mMovieList.clear();
                mAdapter.notifyDataSetChanged();
                do{
                    long id = cursor.getLong(cursor.getColumnIndex(MovieContract._ID));
                    String title = cursor.getString(cursor.getColumnIndex(MovieContract.TITLE));
                    String image = cursor.getString(cursor.getColumnIndex(MovieContract.IMAGE_URL));
                    String sinopsis = cursor.getString(cursor.getColumnIndex(MovieContract.SINOPSIS));
                    float userrating = cursor.getFloat(cursor.getColumnIndex(MovieContract.USER_RATING));
                    String releasedate = cursor.getString(cursor.getColumnIndex(MovieContract.RELEASE_DATE));
                    String imagebackdrop = cursor.getString(cursor.getColumnIndex(MovieContract.BACKDROP_URL));
                    Movie movie = new Movie(id,title, image, sinopsis,userrating, releasedate,imagebackdrop);
                    mMovieList.add(movie);
                    mAdapter.notifyDataSetChanged();
                }while (cursor.moveToNext());
            }else {
                showNoFavoriteMovie();
            }
        }else {
            showNoFavoriteMovie();
        }
    }

    @Override
    public void onRecyclerViewItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", mMovieList.get(position));
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sort_order_key))){
            getMovie();
        }
    }

    private class DownloadMovie extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
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
            Log.d(TAG, s);
            mProgressBar.setVisibility(View.GONE);
            if(s != null && !s.equals("")){
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray results = object.getJSONArray("results");
                    showRecyclerView();
                    mMovieList.clear();
                    mAdapter.notifyDataSetChanged();
                    for(int i=0; i<results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        mMovieList.add(movie);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG,e.toString());
                    showNoDataView();
                }
            }else {
                showNoDataView();
            }
        }
    }

    private void showProgressbar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mNoData.setVisibility(View.GONE);
    }

    private void showNoDataView() {
        Toast.makeText(MainActivity.this, "Oops an error occurred", Toast.LENGTH_SHORT).show();
        mRecyclerView.setVisibility(View.GONE);
        mNoData.setVisibility(View.VISIBLE);
    }

    private void showNoConnectionView() {
        Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        mRecyclerView.setVisibility(View.GONE);
        mNoData.setText(getString(R.string.no_internet_connection));
        mNoData.setVisibility(View.VISIBLE);
    }

    private void showNoFavoriteMovie() {
        mRecyclerView.setVisibility(View.GONE);
        mNoData.setText(R.string.no_data_favorite_movie);
        mNoData.setVisibility(View.VISIBLE);
    }


    private void showRecyclerView() {
        if(mRecyclerView.getVisibility() == View.GONE){
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if(mNoData.getVisibility() == View.VISIBLE){
            mNoData.setVisibility(View.GONE);
        }
    }
}
