package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.TvCreatedByResults;
import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.model.VideosResults;

import java.util.List;

public class TvDetailDVM extends ViewModel {

    private LiveData<TvDetail> tvDetail;
    private LiveData<ProductionCompany> productionName;
    private LiveData<List<TvCreatedByResults>> tvCreator;
    private LiveData<List<VideosResults>> videos;

    public TvDetailDVM(ShowDatabase showDatabase, String movieId) {
        try{
            tvDetail = showDatabase.showDao().getTvDetail(movieId);
        }catch (Exception e){}
        try{
            productionName = showDatabase.showDao().getProductionCompany(movieId);
        }catch (Exception e){}
        try{
            tvCreator = showDatabase.showDao().getTvCreator(movieId);
        }catch (Exception e){}
        try{
            videos = showDatabase.showDao().getVideos(movieId);
        }catch (Exception e){}
    }

    public LiveData<TvDetail> getTvDetail() {
        return tvDetail;
    }

    public LiveData<ProductionCompany> getProductionName() {
        return productionName;
    }

    public LiveData<List<TvCreatedByResults>> getTvCreator() {
        return tvCreator;
    }

    public LiveData<List<VideosResults>> getVideos() {
        return videos;
    }
}
