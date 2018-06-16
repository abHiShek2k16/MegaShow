package com.android.abhishek.megamovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieCasts {

    @SerializedName(EndPoint.CAST)
    private List<MovieCastsResult> movieCastsResults;

    public MovieCasts(List<MovieCastsResult> movieCastsResults) {
        this.movieCastsResults = movieCastsResults;
    }

    public List<MovieCastsResult> getMovieCastsResults() {
        return movieCastsResults;
    }
}
