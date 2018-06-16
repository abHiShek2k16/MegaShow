package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "TvCreatorByTb")
public class TvCreatedByResults {

    @PrimaryKey
    @NonNull
    @SerializedName(EndPoint.ID)
    private String id;
    @SerializedName(EndPoint.NAME)
    private String name;
    @SerializedName(EndPoint.PROFILE_PATH)
    private String profilePath;
    private String movieId;

    @Ignore
    public TvCreatedByResults(String id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }

    public TvCreatedByResults(String id, String name, String profilePath, String movieId) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
        this.movieId = movieId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getProfilePath() {
        return profilePath;
    }
}
