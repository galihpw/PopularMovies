package com.galihpw.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.galihpw.popularmovies.helper.JsonHelper;

import org.json.JSONObject;

/**
 * Created by galihpw on 8/7/2017.
 */

public class UserReview implements Parcelable{

    private String name;
    private String review;

    public UserReview(String name, String review) {
        this.name = name;
        this.review = review;
    }

    public UserReview(JSONObject object) {
        name = JsonHelper.getStringJson(object,"author");
        review = JsonHelper.getStringJson(object,"content");
    }

    public UserReview(Parcel in){
        name = in.readString();
        review = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(review);
    }

    public static final Creator<UserReview> CREATOR = new Creator<UserReview>() {
        @Override
        public UserReview createFromParcel(Parcel in) {
            return new UserReview(in);
        }

        @Override
        public UserReview[] newArray(int size) {
            return new UserReview[size];
        }
    };
}
