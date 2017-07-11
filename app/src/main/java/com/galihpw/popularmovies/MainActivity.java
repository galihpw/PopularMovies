package com.galihpw.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.galihpw.popularmovies.config.CustomItemOffset;

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
    private ProgressBar mLoading;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = new ArrayList<Movie>();

        if(savedInstanceState != null && savedInstanceState.containsKey("movie")){
            mMovieList = savedInstanceState.getParcelableArrayList("movie");
        }

        noInternet = (TextView) findViewById(R.id.noInternet);
        mLoading = (ProgressBar) findViewById(R.id.loadingListMovie);
        mRecyclerView = (RecyclerView) findViewById(R.id.movieRecycle);
        mAdapter = new MovieAdapter(mMovieList, MainActivity.this, MainActivity.this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new CustomItemOffset(MainActivity.this, R.dimen.margin_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
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
                new MovieDBQueryTask().execute();
                return true;
            case R.id.topRated:
                new MovieDBQueryTask().execute();
                status = 1;
                return true;
            case R.id.popular:
                new MovieDBQueryTask().execute();
                status = 0;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRecyclerViewItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", mMovieList.get(position));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new MovieDBQueryTask().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMovieList != null){
            outState.putParcelableArrayList("movie", mMovieList);
        }
    }

    public class MovieDBQueryTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLoading.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                String URL;
                if(status == 1){
                    URL = Constant.URL_TOP_RATED;
                }else{
                    URL = Constant.URL_POPULAR;
                }

                URL url = new URL(URL + BuildConfig.THE_MOVIE_DB_API_TOKEN + Constant.LANGUAGE + Constant.PAGE);
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
            mLoading.setVisibility(View.GONE);
            Log.i("INFO", response);
        }

        private void showJSON(String response) {
            try {
                JSONObject object = new JSONObject(response);
                JSONArray result = object.getJSONArray("results");
                mMovieList.clear();
                mAdapter.notifyDataSetChanged();
                // Parsing json
                for (int i = 0; i < result.length(); i++) {
                    Movie movie = new Movie(result.getJSONObject(i));
                    mMovieList.add(movie);
                    mAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
