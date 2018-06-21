package com.android.abhishek.megamovies.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.TvDetail;

import java.util.List;

public class FavVM extends AndroidViewModel {

    private final LiveData<List<MovieDetail>> movieDetailList;
    private final LiveData<List<TvDetail>> tvDetailList;

    public FavVM(@NonNull Application application) {
        super(application);
        ShowDatabase showDatabase = ShowDatabase.getShowDatabase(this.getApplication());
        movieDetailList = showDatabase.showDao().getListOfMv();
        tvDetailList = showDatabase.showDao().getListOfTv();
    }

    public LiveData<List<MovieDetail>> getMovieDetailList() {
        return movieDetailList;
    }

    public LiveData<List<TvDetail>> getTvDetailList() {
        return tvDetailList;
    }
}
