package com.galihpw.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.galihpw.popularmovies.adapter.MovieAdapter;
import com.galihpw.popularmovies.config.Constant;
import com.galihpw.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnRecyclerViewItemClickListener{

    private ArrayList<Movie> mMovieList;
    private TextView noInternet;
    private ProgressBar mLoadingList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = new ArrayList<Movie>();
        noInternet = (TextView) findViewById(R.id.noInternet);
        mLoadingList = (ProgressBar) findViewById(R.id.loadingListMovie);
        mRecyclerView = (RecyclerView) findViewById(R.id.movieRecycle);
        mAdapter = new MovieAdapter(mMovieList, MainActivity.this, MainActivity.this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        new MovieDBQueryTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                //tulis
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRecyclerViewItemClicked(int position) {

    }

    public class MovieDBQueryTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLoadingList.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL(Constant.URL + Constant.API_KEY + Constant.LANGUAGE + Constant.PAGE);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
                noInternet.setVisibility(View.VISIBLE);
            }else{
                showJSON(response);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            mLoadingList.setVisibility(View.GONE);
            Log.i("INFO", response);
        }

        private void showJSON(String response) {
            try {
                JSONArray result = new JSONArray(response);
                mMovieList.clear();
                mAdapter.notifyDataSetChanged();
                // Parsing json
                for (int i = 0; i < result.length(); i++) {
                    JSONObject Data = result.getJSONObject(i);
                    Movie movie = new Movie("" + Data.getLong(Constant.KEY_ID), "" + Data.getString(Constant.KEY_TITLE), "" + Data.getString(Constant.KEY_IMAGE), "" + Data.getString(Constant.KEY_SYNOPSIS), "" + Data.getDouble(Constant.KEY_USER_RATING), "" + Data.getString(Constant.KEY_RELEASE_DATE), "" + Data.getString(Constant.KEY_IMAGE_BACKDROP));
                    mMovieList.add(movie);
                    mAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
