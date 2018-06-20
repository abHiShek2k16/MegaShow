package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.VideosResults;

import java.util.List;

public class MovieDetailDbVM extends ViewModel {

    private LiveData<MovieDetail> movieDetail;
    private LiveData<ProductionCompany> productionName;
    private LiveData<List<MovieCastsResult>> movieCasts;
    private LiveData<List<VideosResults>> videos;

    public MovieDetailDbVM(ShowDatabase showDatabase, String movieId) {
        movieDetail = showDatabase.showDao().getMovieDetail(movieId);
        productionName = showDatabase.showDao().getProductionCompany(movieId);
        movieCasts = showDatabase.showDao().getMovieCast(movieId);
        videos = showDatabase.showDao().getVideos(movieId);
    }

    public LiveData<MovieDetail> getMovieDetail() {
        return movieDetail;
    }

    public LiveData<ProductionCompany> getProductionName() {
        return productionName;
    }

    public LiveData<List<MovieCastsResult>> getMovieCasts() {
        return movieCasts;
    }

    public LiveData<List<VideosResults>> getVideos() {
        return videos;
    }
}
