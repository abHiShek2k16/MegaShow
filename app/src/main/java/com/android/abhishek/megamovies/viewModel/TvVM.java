package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvVM extends ViewModel {

    private MutableLiveData<ShowList> popularTvList;
    private MutableLiveData<ShowList> topRatedTvList;
    private MutableLiveData<ShowList> onTheAirTvList;
    private MutableLiveData<ShowList> airingTodayTvList;

    public LiveData<ShowList> getPopularTvList(String API_KEY, int CURRENT_PAGE) {
        if (popularTvList == null) {
            popularTvList = new MutableLiveData<ShowList>();
            loadPopularTv(API_KEY,CURRENT_PAGE);
        }
        return popularTvList;
    }
    private void loadPopularTv(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getPopularTv(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                popularTvList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }

    public LiveData<ShowList> getTopRatedTvList(String API_KEY,int CURRENT_PAGE) {
        if (topRatedTvList == null) {
            topRatedTvList = new MutableLiveData<ShowList>();
            loadTopRatedTv(API_KEY,CURRENT_PAGE);
        }
        return topRatedTvList;
    }
    private void loadTopRatedTv(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getTopRatedTv(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                topRatedTvList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }

    public LiveData<ShowList> getOnTheAirTvList(String API_KEY,int CURRENT_PAGE) {
        if (onTheAirTvList == null) {
            onTheAirTvList = new MutableLiveData<ShowList>();
            loadOnTheAirTv(API_KEY,CURRENT_PAGE);
        }
        return onTheAirTvList;
    }
    private void loadOnTheAirTv(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getOnTheAir(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                onTheAirTvList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }

    public LiveData<ShowList> getAiringTodayTvList(String API_KEY,int CURRENT_PAGE) {
        if (airingTodayTvList == null) {
            airingTodayTvList = new MutableLiveData<ShowList>();
            loadAiringTodayTv(API_KEY,CURRENT_PAGE);
        }
        return airingTodayTvList;
    }
    private void loadAiringTodayTv(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getAiringToday(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                airingTodayTvList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }
}
