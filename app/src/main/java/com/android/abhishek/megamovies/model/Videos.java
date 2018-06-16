package com.android.abhishek.megamovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {

    @SerializedName(EndPoint.RESULTS)
    private List<VideosResults> movieVideosResults;

    public Videos(List<VideosResults> movieVideosResults) {
        this.movieVideosResults = movieVideosResults;
    }

    public List<VideosResults> getVideosResults() {
        return movieVideosResults;
    }
}

