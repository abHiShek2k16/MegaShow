package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ProductionCompanyTb")
public class ProductionCompany {

    @SerializedName(EndPoint.NAME)
    private String name;
    private String movieId;
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    public ProductionCompany(String name) {
        this.name = name;
    }

    public ProductionCompany(String name, String movieId, int id) {
        this.name = name;
        this.movieId = movieId;
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
