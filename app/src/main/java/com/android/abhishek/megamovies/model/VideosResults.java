package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "VideosTb")
public class VideosResults {

    private String movieId;
    @PrimaryKey @NonNull @SerializedName(EndPoint.KEY)
    private String videoKey;

    @Ignore
    public VideosResults(String videoKey) {
        this.videoKey = videoKey;
    }

    public VideosResults(String movieId, String videoKey) {
        this.movieId = movieId;
        this.videoKey = videoKey;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getVideoKey() {
        return videoKey;
    }
}
