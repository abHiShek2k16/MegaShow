package com.android.abhishek.megamovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "MovieDetailTb")
public class MovieDetail {

    //  Detail Api
    @PrimaryKey @NonNull @SerializedName(EndPoint.ID)
    private String movieId;
    @SerializedName(EndPoint.BACKDROP_PATH)
    private String backdropPath;
    @SerializedName(EndPoint.OVERVIEW)
    private String overview;
    @SerializedName(EndPoint.POSTER_PATH)
    private String posterPath;
    @Ignore
    @SerializedName(EndPoint.PRODUCTION)
    private List<ProductionCompany> productionCompanies;
    @SerializedName(EndPoint.RELEASE_DATE)
    private String releaseDate;
    @SerializedName(EndPoint.RUNTIME)
    private String runtime;
    @SerializedName(EndPoint.TITLE)
    private String title;
    @SerializedName(EndPoint.VOTE_AVERAGE)
    private String voteAvg;
    @SerializedName(EndPoint.VOTE_COUNT)
    private String voteCount;

    //  Videos
    @Ignore
    @SerializedName(EndPoint.VIDEOS)
    private Videos movieVideos;

    //  Review
    @Ignore
    @SerializedName(EndPoint.REVIEW)
    private Review movieReview;

    //  Casts
    @Ignore
    @SerializedName(EndPoint.CASTS)
    private MovieCasts movieCasts;

    @Ignore
    public MovieDetail(@NonNull String movieId, String backdropPath, String overview, String posterPath, List<ProductionCompany> productionCompanies, String releaseDate, String runtime, String title, String voteAvg, String voteCount, Videos movieVideos, Review movieReview, MovieCasts movieCasts) {
        this.movieId = movieId;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.posterPath = posterPath;
        this.productionCompanies = productionCompanies;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.title = title;
        this.voteAvg = voteAvg;
        this.voteCount = voteCount;
        this.movieVideos = movieVideos;
        this.movieReview = movieReview;
        this.movieCasts = movieCasts;
    }

    public MovieDetail(String movieId, String backdropPath, String overview, String posterPath, String releaseDate, String runtime, String title, String voteAvg, String voteCount) {
        this.movieId = movieId;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.title = title;
        this.voteAvg = voteAvg;
        this.voteCount = voteCount;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getTitle() {
        return title;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public Videos getMovieVideos() {
        return movieVideos;
    }

    public Review getMovieReview() {
        return movieReview;
    }

    public MovieCasts getMovieCasts() {
        return movieCasts;
    }

    public String getMovieId() {
        return movieId;
    }
}
