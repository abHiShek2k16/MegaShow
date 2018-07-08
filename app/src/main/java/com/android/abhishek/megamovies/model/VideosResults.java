package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "VideosTb")
public class VideosResults {

    @SerializedName(EndPoint.KEY) @PrimaryKey @NonNull
    private String videoKey;

    private String movieId;

    @Ignore
    public VideosResults(@NonNull String videoKey) {
        this.videoKey = videoKey;
    }

    public VideosResults(String movieId, @NonNull String videoKey) {
        this.movieId = movieId;
        this.videoKey = videoKey;
    }

    public String getMovieId() {
        return movieId;
    }

    @NonNull
    public String getVideoKey() {
        return videoKey;
    }

}
