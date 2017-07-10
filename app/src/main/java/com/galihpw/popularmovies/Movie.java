package com.galihpw.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GalihPW on 10/07/2017.
 */

public class Movie implements Parcelable{

    private long mId;
    private String mTitle;
    private String mImage;
    private String mSinopsis;
    private float mUserRating;
    private String mReleaseDate;
    private String mImageBackdrop;

    public Movie(JSONObject object) throws JSONException {
        mId = JsonHelper.getLongJson(object, "id");
        mTitle = JsonHelper.getStringJson(object, "original_title");
        mImage = JsonHelper.getStringJson(object, "poster_path");
        mSinopsis = JsonHelper.getStringJson(object, "overview");
        mReleaseDate = JsonHelper.getStringJson(object, "release_date");
        mUserRating = JsonHelper.getFloatJson(object, "vote_average");
        mImageBackdrop= JsonHelper.getStringJson(object, "backdrop_path");
    }

    public Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mImage = in.readString();
        mSinopsis = in.readString();
        mUserRating = in.readFloat();
        mReleaseDate = in.readString();
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

    public String getSinopsis() {
        return mSinopsis;
    }

    public void setSinopsis(String sinopsis) {
        mSinopsis = sinopsis;
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
        parcel.writeString(mImage);
        parcel.writeString(mSinopsis);
        parcel.writeFloat(mUserRating);
        parcel.writeString(mReleaseDate);
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