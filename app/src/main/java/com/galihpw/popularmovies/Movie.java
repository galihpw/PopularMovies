package com.galihpw.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.galihpw.popularmovies.helper.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GalihPW on 10/07/2017.
 */

public class Movie implements Parcelable{

    private long mId;
    private String mTitle;
    private String mReleaseDate;
    private String mSynopsis;
    private float mUserRating;
    private String mImage;
    private String mImageBackdrop;

    public Movie(JSONObject object) throws JSONException {
        mId = JsonHelper.getLongJson(object, "id");
        mTitle = JsonHelper.getStringJson(object, "original_title");
        mReleaseDate = JsonHelper.getStringJson(object, "release_date");
        mSynopsis = JsonHelper.getStringJson(object, "overview");
        mUserRating = JsonHelper.getFloatJson(object, "vote_average");
        mImage = JsonHelper.getStringJson(object, "poster_path");
        mImageBackdrop= JsonHelper.getStringJson(object, "backdrop_path");
    }

    public Movie(long id, String title, String image, String synopsis, float userRating, String releaseDate, String imageBackdrop) {
        mId = id;
        mTitle = title;
        mImage = image;
        mSynopsis = synopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mImageBackdrop = imageBackdrop;
    }

    public Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mSynopsis = in.readString();
        mUserRating = in.readFloat();
        mImage = in.readString();
        mImageBackdrop = in.readString();
    }


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String sinopsis) {
        mSynopsis = sinopsis;
    }

    public float getUserRating() {
        return mUserRating;
    }

    public void setUserRating(float userRating) {
        mUserRating = userRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getImageBackdrop() {
        return mImageBackdrop;
    }

    public void setImageBackdrop(String imageBackdrop) {
        mImageBackdrop = imageBackdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mSynopsis);
        parcel.writeFloat(mUserRating);
        parcel.writeString(mImage);
        parcel.writeString(mImageBackdrop);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}