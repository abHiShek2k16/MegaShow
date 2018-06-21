package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.network.ApiRepository;

public class TvListVM extends ViewModel {

    private MutableLiveData<ShowList> popularTvList;
    private MutableLiveData<ShowList> topRatedTvList;
    private MutableLiveData<ShowList> onTheAirTvList;
    private MutableLiveData<ShowList> airingTodayTvList;

    private ApiRepository apiRepository = new ApiRepository();

    public LiveData<ShowList> getPopularTvList(String API_KEY, int currentPage,int previousPage) {
        if (popularTvList == null || currentPage != previousPage) {
            popularTvList = apiRepository.loadPopularTv(API_KEY,currentPage);
        }
        return popularTvList;
    }

    public LiveData<ShowList> getTopRatedTvList(String API_KEY,int currentPage,int previousPage) {
        if (topRatedTvList == null || currentPage != previousPage) {
            topRatedTvList = apiRepository.loadTopRatedTv(API_KEY,currentPage);
        }
        return topRatedTvList;
    }

    public LiveData<ShowList> getOnTheAirTvList(String API_KEY,int currentPage,int previousPage) {
        if (onTheAirTvList == null || currentPage != previousPage) {
            onTheAirTvList = apiRepository.loadOnTheAirTv(API_KEY,currentPage);
        }
        return onTheAirTvList;
    }

    public LiveData<ShowList> getAiringTodayTvList(String API_KEY,int currentPage,int previousPage) {
        if (airingTodayTvList == null || currentPage != previousPage) {
            airingTodayTvList = apiRepository.loadAiringTodayTv(API_KEY,currentPage);
        }
        return airingTodayTvList;
    }

}
