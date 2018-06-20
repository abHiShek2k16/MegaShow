package com.android.abhishek.megamovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowList {

    @SerializedName(EndPoint.TOTAL_PAGES)
    private int totalPages;
    @SerializedName(EndPoint.RESULTS)
    private java.util.List<ListResults> results;

    public ShowList(int totalPages, List<ListResults> results) {
        this.totalPages = totalPages;
        this.results = results;
    }

    public ShowList() {
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<ListResults> getResults() {
        return results;
    }
}
