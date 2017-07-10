package com.galihpw.popularmovies;

/**
 * Created by GalihPW on 10/07/2017.
 */

public class Movie{

    private String mId;
    private String mTitle;
    private String mImage;
    private String mSynopsis;
    private String mUserRating;
    private String mReleaseDate;
    private String mImageBackdrop;

    public Movie(String mId, String mTitle, String mImage, String mSynopsis, String mUserRating, String mReleaseDate, String mImageBackdrop) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mImage = mImage;
        this.mSynopsis = mSynopsis;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mImageBackdrop = mImageBackdrop;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
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
        return mSynopsis;
    }

    public void setSinopsis(String sinopsis) {
        mSynopsis = sinopsis;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String userRating) {
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
}
