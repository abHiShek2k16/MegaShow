package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.VideosResults;

import java.util.List;

public class MovieDetailDVM extends ViewModel {

    private LiveData<MovieDetail> movieDetail;
    private LiveData<ProductionCompany> productionName;
    private LiveData<List<MovieCastsResult>> movieCasts;
    private LiveData<List<VideosResults>> videos;

    public MovieDetailDVM(ShowDatabase showDatabase, String movieId) {
        try{
            movieDetail = showDatabase.showDao().getMovieDetail(movieId);
        }catch (Exception e){}
        try{
            productionName = showDatabase.showDao().getProductionCompany(movieId);
        }catch (Exception e){}
        try{
            movieCasts = showDatabase.showDao().getMovieCast(movieId);
        }catch (Exception e){}
        try{
            videos = showDatabase.showDao().getVideos(movieId);
        }catch (Exception e){}
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
