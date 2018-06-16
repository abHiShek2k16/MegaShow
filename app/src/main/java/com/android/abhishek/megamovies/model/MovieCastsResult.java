package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "MovieCastTb")
public class MovieCastsResult {

    @SerializedName(EndPoint.CHARACTER)
    private String character;
    @SerializedName(EndPoint.NAME)
    private String name;
    @SerializedName(EndPoint.PROFILE_PATH)
    private String profilePath;
    @SerializedName(EndPoint.ID)
    private String id;
    private String movieId;
    @PrimaryKey(autoGenerate = true)
    private int key;

    @Ignore
    public MovieCastsResult(String character, String name, String profilePath, String id) {
        this.character = character;
        this.name = name;
        this.profilePath = profilePath;
        this.id = id;
    }

    public MovieCastsResult(String character, String name, String profilePath, String id, String movieId, int key) {
        this.character = character;
        this.name = name;
        this.profilePath = profilePath;
        this.id = id;
        this.movieId = movieId;
        this.key = key;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getKey() {
        return key;
    }
}