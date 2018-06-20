package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "TvCreatorByTb")
public class TvCreatedByResults {

    @SerializedName(EndPoint.ID)
    private String id;
    @SerializedName(EndPoint.NAME)
    private String name;
    @SerializedName(EndPoint.PROFILE_PATH)
    private String profilePath;
    private String movieId;
    @PrimaryKey(autoGenerate = true)
    private int key;

    @Ignore
    public TvCreatedByResults(String id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }

    public TvCreatedByResults(String id, String name, String profilePath, String movieId, int key) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
        this.movieId = movieId;
        this.key = key;
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

    public int getKey() {
        return key;
    }
}
