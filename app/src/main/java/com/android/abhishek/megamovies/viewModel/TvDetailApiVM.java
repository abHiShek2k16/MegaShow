package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.network.ApiRepository;

public class TvDetailApiVM extends ViewModel{

    private MutableLiveData<TvDetail> tvDetailFromApi;
    private ApiRepository apiRepository = new ApiRepository();

    public LiveData<TvDetail> getTvDetailFromApi(String tvId,String ApiKey,String AppendQuery){
        if(tvDetailFromApi == null){
            tvDetailFromApi = apiRepository.loadTvDetailFromApi(tvId,ApiKey,AppendQuery);
        }
        return tvDetailFromApi;
    }

}

