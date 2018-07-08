package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "TvDetailTb")
public class TvDetail {

    @PrimaryKey @NonNull @SerializedName(EndPoint.ID)
    private String movieId;
    @SerializedName(EndPoint.BACKDROP_PATH)
    private String backdropPath;
    @SerializedName(EndPoint.EPISODE_RUNTIME)
    @Ignore
    private List<Integer> runTime;
    @SerializedName(EndPoint.CREATED_BY)
    @Ignore
    private List<TvCreatedByResults> tvCreatedByResults;
    @SerializedName(EndPoint.FIRST_AIR_DATE)
    private String firstAirDate;
    @SerializedName(EndPoint.LAST_AIR_DATE)
    private String lastAirDate;
    @SerializedName(EndPoint.NAME)
    private String name;
    @SerializedName(EndPoint.NO_OF_EPISODE)
    private String noOfEpisode;
    @SerializedName(EndPoint.NO_OF_SEASON)
    private String noOfSeason;
    @SerializedName(EndPoint.OVERVIEW)
    private String overview;
    @SerializedName(EndPoint.POSTER_PATH)
    private String posterPath;
    @SerializedName(EndPoint.VOTE_AVERAGE)
    private String voteAvg;
    @SerializedName(EndPoint.VOTE_COUNT)
    private String voteCount;
    @Ignore
    @SerializedName(EndPoint.VIDEOS)
    private Videos videos;
    @Ignore
    @SerializedName(EndPoint.REVIEW)
    private Review review;
    @Ignore
    @SerializedName(EndPoint.PRODUCTION)
    private List<ProductionCompany> productionCompanies;

    @Ignore
    public TvDetail(@NonNull String movieId, String backdropPath, List<Integer> runTime, List<TvCreatedByResults> tvCreatedByResults, String firstAirDate, String lastAirDate, String name, String noOfEpisode, String noOfSeason, String overview, String posterPath, String voteAvg, String voteCount, Videos videos, Review review, List<ProductionCompany> productionCompanies) {
        this.movieId = movieId;
        this.backdropPath = backdropPath;
        this.runTime = runTime;
        this.tvCreatedByResults = tvCreatedByResults;
        this.firstAirDate = firstAirDate;
        this.lastAirDate = lastAirDate;
        this.name = name;
        this.noOfEpisode = noOfEpisode;
        this.noOfSeason = noOfSeason;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAvg = voteAvg;
        this.voteCount = voteCount;
        this.videos = videos;
        this.review = review;
        this.productionCompanies = productionCompanies;
    }

    public TvDetail(@NonNull String movieId, String backdropPath, String firstAirDate, String lastAirDate, String name, String noOfEpisode, String noOfSeason, String overview, String posterPath, String voteAvg, String voteCount) {
        this.movieId = movieId;
        this.backdropPath = backdropPath;
        this.firstAirDate = firstAirDate;
        this.lastAirDate = lastAirDate;
        this.name = name;
        this.noOfEpisode = noOfEpisode;
        this.noOfSeason = noOfSeason;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAvg = voteAvg;
        this.voteCount = voteCount;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Integer> getRunTime() {
        return runTime;
    }

    public List<TvCreatedByResults> getTvCreatedByResults() {
        return tvCreatedByResults;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public String getName() {
        return name;
    }

    public String getNoOfEpisode() {
        return noOfEpisode;
    }

    public String getNoOfSeason() {
        return noOfSeason;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public Videos getVideos() {
        return videos;
    }

    public Review getReview() {
        return review;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    @NonNull
    public String getMovieId() {
        return movieId;
    }
}
