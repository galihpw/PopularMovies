package com.galihpw.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.galihpw.popularmovies.helper.JsonHelper;

import org.json.JSONObject;

/**
 * Created by galihpw on 8/7/2017.
 */

public class TrailerFilm implements Parcelable{

    private String imageUrl;
    private String youtubeUrl;

    public TrailerFilm(String imageUrl, String youtubeUrl) {
        this.imageUrl = imageUrl;
        this.youtubeUrl = youtubeUrl;
    }

    public TrailerFilm(JSONObject object){
        String keyYoutube = JsonHelper.getStringJson(object,"key");
        imageUrl = "http://img.youtube.com/vi/"+ keyYoutube  +"/0.jpg";
        youtubeUrl = "https://www.youtube.com/watch?v=" + keyYoutube;
    }

    public TrailerFilm(Parcel in){
        imageUrl = in.readString();
        youtubeUrl = in.readString();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(youtubeUrl);
    }

    public static final Creator<TrailerFilm> CREATOR = new Creator<TrailerFilm>() {
        @Override
        public TrailerFilm createFromParcel(Parcel in) {
            return new TrailerFilm(in);
        }

        @Override
        public TrailerFilm[] newArray(int size) {
            return new TrailerFilm[size];
        }
    };
}
