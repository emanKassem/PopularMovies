package com.example.android.popularmovies;

import android.os.Parcelable;
import android.os.Parcel;


/**
 * Created by LENOVO on 04/01/2018.
 */

public class Movie implements Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in){
        this.mTitle = in.readString();
        this.mReleaseDate = in.readString();
        this.mPosterPath =  in.readString();
        this.mVoteAverage = in.readDouble();
        this.mOverView =  in.readString();
    }

    private String mTitle;
    private String mReleaseDate;
    private String mPosterPath;
    private Double mVoteAverage;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public Double getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(Double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmOverView() {
        return mOverView;
    }

    public void setmOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    private String mOverView;

    public Movie(){
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.mTitle);
        parcel.writeString(this.mReleaseDate);
        parcel.writeString(this.mPosterPath);
        parcel.writeDouble(this.mVoteAverage);
        parcel.writeString(this.mOverView);
    }
}
