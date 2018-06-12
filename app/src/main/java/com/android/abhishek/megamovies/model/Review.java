package com.android.abhishek.megamovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Review implements Parcelable{
    @SerializedName(EndPoint.RESULTS)
    private ArrayList<ReviewResults> movieReviewResults;

    public Review(ArrayList<ReviewResults> movieReviewResults) {
        this.movieReviewResults = movieReviewResults;
    }

    protected Review(Parcel in) {
        movieReviewResults = in.createTypedArrayList(ReviewResults.CREATOR);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public ArrayList<ReviewResults> getMovieReviewResults() {
        return movieReviewResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(movieReviewResults);
    }
}
