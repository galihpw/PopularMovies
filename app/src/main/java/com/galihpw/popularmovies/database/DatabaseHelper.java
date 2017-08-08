package com.galihpw.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.galihpw.popularmovies.database.MovieContract.BACKDROP_URL;
import static com.galihpw.popularmovies.database.MovieContract.IMAGE_URL;
import static com.galihpw.popularmovies.database.MovieContract.RELEASE_DATE;
import static com.galihpw.popularmovies.database.MovieContract.SINOPSIS;
import static com.galihpw.popularmovies.database.MovieContract.TITLE;
import static com.galihpw.popularmovies.database.MovieContract.USER_RATING;
import static com.galihpw.popularmovies.database.MovieContract._ID;

/**
 * Created by galihpw on 8/7/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "Movie";
    static final String MOVIE_TABLE_NAME = "movie";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_DB_TABLE =
            " CREATE TABLE " + MOVIE_TABLE_NAME + "(" +
                    _ID + " LONG PRIMARY KEY , " +
                    TITLE + " TEXT NOT NULL, " +
                    IMAGE_URL + " TEXT NOT NULL, " +
                    SINOPSIS+ " TEXT NOT NULL, " +
                    USER_RATING + " FLOAT NOT NULL, " +
                    RELEASE_DATE + " TEXT NOT NULL, " +
                    BACKDROP_URL + " TEXT NOT NULL); ";


    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,CREATE_DB_TABLE);
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  MOVIE_TABLE_NAME);
        onCreate(db);
    }
}