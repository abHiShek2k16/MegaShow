package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.repository.ApiRepository;

public class TvListVM extends ViewModel {

    private MutableLiveData<ShowList> popularTvList;
    private MutableLiveData<ShowList> topRatedTvList;
    private MutableLiveData<ShowList> onTheAirTvList;
    private MutableLiveData<ShowList> airingTodayTvList;

    private MutableLiveData<Integer> currentPage = new MutableLiveData<>();
    private ApiRepository apiRepository = new ApiRepository();

    public LiveData<ShowList> getPopularTvList(String API_KEY, int CURRENT_PAGE) {
        if (popularTvList == null) {
            popularTvList = apiRepository.loadPopularTv(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            popularTvList = apiRepository.loadPopularTv(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return popularTvList;
    }

    public LiveData<ShowList> getTopRatedTvList(String API_KEY,int CURRENT_PAGE) {
        if (topRatedTvList == null) {
            topRatedTvList = apiRepository.loadTopRatedTv(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            topRatedTvList = apiRepository.loadTopRatedTv(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return topRatedTvList;
    }

    public LiveData<ShowList> getOnTheAirTvList(String API_KEY,int CURRENT_PAGE) {
        if (onTheAirTvList == null) {
            onTheAirTvList = apiRepository.loadOnTheAirTv(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            onTheAirTvList = apiRepository.loadOnTheAirTv(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return onTheAirTvList;
    }

    public LiveData<ShowList> getAiringTodayTvList(String API_KEY,int CURRENT_PAGE) {
        if (airingTodayTvList == null) {
            airingTodayTvList = apiRepository.loadAiringTodayTv(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            airingTodayTvList = apiRepository.loadAiringTodayTv(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return airingTodayTvList;
    }

}
