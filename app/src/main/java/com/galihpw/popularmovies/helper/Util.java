package com.galihpw.popularmovies.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.galihpw.popularmovies.BuildConfig;
import com.galihpw.popularmovies.config.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by galihpw on 8/8/2017.
 */

public class Util {

    private static final String TAG = Util.class.getSimpleName();

    //check network
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return ((cm.getActiveNetworkInfo() != null) && (cm.getActiveNetworkInfo().isConnected()));
    }

    public static String downloadUrl(URL url) throws IOException {
        Log.d(TAG, url.toString());
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;

        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(10000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(10000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-Environment", "android");
            // Open communications link (network traffic occurs here).
            connection.connect();
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    public static String readStream(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        StringBuilder total = new StringBuilder();
        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            total.append(line);
        }
        if (reader != null) {
            reader.close();
        }
        if(bufferedReader != null){
            bufferedReader.close();
        }
        return total.toString();
    }

    public static String getUrl(String orderBy){
        // Build URL
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.BASE_URL);
        stringBuilder.append(orderBy);
        stringBuilder.append("?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN);

        Log.d(TAG, stringBuilder.toString());

        return stringBuilder.toString();
    }

    public static String getUrlTrailer(long id){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.BASE_URL);
        stringBuilder.append(id);
        stringBuilder.append(Constant.END_URL_VIDEO);
        stringBuilder.append("?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN);

        Log.d(TAG, stringBuilder.toString());

        return stringBuilder.toString();
    }

    public static String getUrlUserReview(long id){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.BASE_URL);
        stringBuilder.append(id);
        stringBuilder.append(Constant.END_URL_REVIEWS);
        stringBuilder.append("?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN);

        Log.d(TAG, stringBuilder.toString());

        return stringBuilder.toString();
    }


}
