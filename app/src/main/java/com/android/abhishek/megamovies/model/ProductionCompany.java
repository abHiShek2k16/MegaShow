package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
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
    public ProductionCompany(String name) {
        this.name = name;
    }

    public ProductionCompany(String name, String movieId) {
        this.name = name;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getName() {
        return name;
    }

}
