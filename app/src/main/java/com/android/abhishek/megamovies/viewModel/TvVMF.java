package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.android.abhishek.megamovies.db.ShowDatabase;

public class TvVMF extends ViewModelProvider.NewInstanceFactory {

    private final ShowDatabase showDatabase;
    private final String Id;

    public TvVMF(ShowDatabase showDatabase, String movieId) {
        this.showDatabase = showDatabase;
        this.Id = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TvDetailDVM(showDatabase,Id);
    }
}
