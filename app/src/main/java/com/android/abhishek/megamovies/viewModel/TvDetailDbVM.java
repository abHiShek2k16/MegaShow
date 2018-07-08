package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.model.TvDetail;

public class TvDetailDbVM extends ViewModel {

    private LiveData<TvDetail> tvDetail;

    public TvDetailDbVM(ShowDatabase showDatabase, String tvId) {
        tvDetail = showDatabase.showDao().getTvDetail(tvId);
    }

    public LiveData<TvDetail> getTvDetail() {
        return tvDetail;
    }
}
