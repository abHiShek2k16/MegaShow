package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvDetailAVM extends ViewModel{

    private MutableLiveData<TvDetail> tvDetailFromApi;

    public LiveData<TvDetail> getTvDetailFromApi(String tvId,String ApiKey,String AppendQuery){
        if(tvDetailFromApi == null){
            tvDetailFromApi = new MutableLiveData<>();
            loadTvDetailFromApi(tvId,ApiKey,AppendQuery);
        }
        return tvDetailFromApi;
    }
    private void loadTvDetailFromApi(String tvId,String ApiKey,String AppendQuery){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        final Call<TvDetail> tvDetailCall = apiInterface.getTvDetail(tvId,ApiKey,AppendQuery);
        tvDetailCall.enqueue(new Callback<TvDetail>() {
            @Override
            public void onResponse(Call<TvDetail> call, Response<TvDetail> response) {
                tvDetailFromApi.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvDetail> call, Throwable t) {

            }
        });
    }
}

