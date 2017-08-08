package com.galihpw.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by galihpw on 8/7/2017.
 */

public final class MovieContract implements BaseColumns {

    public static final String PROVIDER_NAME = "com.galihpw.popularmovies.movieprovider";
    public static final String URL_PROVIDER = "content://" + PROVIDER_NAME + "/movie";
    public static final Uri CONTENT_URI = Uri.parse(URL_PROVIDER);
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String IMAGE_URL = "imageurl";
    public static final String SYNOPSIS = "_sinopsis";
    public static final String USER_RATING = "userrating";
    public static final String RELEASE_DATE = "releasedate";
    public static final String BACKDROP_URL = "backdropurl";

}
