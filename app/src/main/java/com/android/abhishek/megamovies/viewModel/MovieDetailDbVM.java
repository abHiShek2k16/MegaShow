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

    public MovieDetailDbVM(ShowDatabase showDatabase, String movieId) {
        movieDetail = showDatabase.showDao().getMovieDetail(movieId);
    }

    public LiveData<MovieDetail> getMovieDetail() {
        return movieDetail;
    }
}
