package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"movieId", "name"},tableName = "ProductionCompanyTb")
public class ProductionCompany {

    @SerializedName(EndPoint.NAME)
    @NonNull
    private String name;
    @NonNull
    private String movieId;

    @Ignore
    public ProductionCompany(@NonNull String name) {
        this.name = name;
    }

    public ProductionCompany(@NonNull String name, @NonNull String movieId) {
        this.name = name;
        this.movieId = movieId;
    }

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    @NonNull
    public String getName() {
        return name;
    }

}
