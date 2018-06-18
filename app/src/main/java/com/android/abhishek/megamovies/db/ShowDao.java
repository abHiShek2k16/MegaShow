package com.android.abhishek.megamovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.ReviewResults;
import com.android.abhishek.megamovies.model.TvCreatedByResults;
import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.model.VideosResults;

import java.util.List;

@Dao
public interface ShowDao {

    @Query("SELECT * FROM MovieDetailTb")
    LiveData<List<MovieDetail>> getListOfMv();

    @Query("SELECT * FROM TvDetailTb")
    LiveData<List<TvDetail>> getListOfTv();

    @Query("SELECT * FROM MovieDetailTb WHERE movieId = :movieId")
    LiveData<MovieDetail> getMovieDetail(String movieId);
    @Insert
    void addMovieDetail(MovieDetail movieDetail);
    @Delete
    void removeMovieDetail(MovieDetail movieDetail);

    @Query("SELECT * FROM TvDetailTb WHERE movieId = :movieId")
    LiveData<TvDetail> getTvDetail(String movieId);
    @Insert
    void addTvDetail(TvDetail tvDetail);
    @Delete
    void removeTvDetail(TvDetail tvDetail);

    @Query("SELECT * FROM MovieCastTb WHERE movieId = :movieId")
    LiveData<List<MovieCastsResult>> getMovieCast(String movieId);
    @Insert
    void addMovieCast(MovieCastsResult movieCastsResult);
    @Delete
    void removeMovieCast(MovieCastsResult movieCastsResult);

    @Query("SELECT * FROM TvCreatorByTb WHERE movieId = :movieId")
    LiveData<List<TvCreatedByResults>> getTvCreator(String movieId);
    @Insert
    void addTvCreator(TvCreatedByResults tvCreatedByResults);
    @Delete
    void removeTvCreator(TvCreatedByResults tvCreatedByResults);

    @Query("SELECT * FROM ProductionCompanyTb WHERE movieId = :movieId")
    LiveData<ProductionCompany> getProductionCompany(String movieId);
    @Insert
    void addProductionCompany(ProductionCompany productionCompany);
    @Delete
    void removeProductionCompany(ProductionCompany productionCompany);

    @Query("SELECT * FROM VideosTb WHERE movieId = :movieId")
    LiveData<List<VideosResults>> getVideos(String movieId);
    @Insert
    void addVideos(VideosResults videosResults);
    @Delete
    void removeVideos(VideosResults videosResults);

}
