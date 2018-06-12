package com.android.abhishek.megamovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ReviewResults implements Parcelable{

    @SerializedName(EndPoint.AUTHOR)
    private String author;
    @SerializedName(EndPoint.CONTENT)
    private String content;

    public ReviewResults(String author, String content) {
        this.author = author;
        this.content = content;
    }

    protected ReviewResults(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewResults> CREATOR = new Creator<ReviewResults>() {
        @Override
        public ReviewResults createFromParcel(Parcel in) {
            return new ReviewResults(in);
        }

        @Override
        public ReviewResults[] newArray(int size) {
            return new ReviewResults[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
