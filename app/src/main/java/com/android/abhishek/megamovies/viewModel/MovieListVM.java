package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.network.ApiRepository;

public class MovieListVM extends ViewModel {

    private MutableLiveData<ShowList> popularMovieList;
    private MutableLiveData<ShowList> topRatedMovieList;
    private MutableLiveData<ShowList> upcomingMovieList;
    private MutableLiveData<ShowList> nowPlayingMovieList;

    private ApiRepository apiRepository = new ApiRepository();

    public LiveData<ShowList> getPopularMoviesList(String API_KEY, int currentPage,int previousPage) {
        if (popularMovieList == null || currentPage != previousPage) {
            popularMovieList = apiRepository.loadPopularMovies(API_KEY,currentPage);
        }
        return popularMovieList;
    }

    public LiveData<ShowList> getTopRatedMoviesList(String API_KEY,int currentPage,int previousPage) {
        if (topRatedMovieList == null || currentPage != previousPage) {
            topRatedMovieList = apiRepository.loadTopRatedMovies(API_KEY,currentPage);
        }
        return topRatedMovieList;
    }

    public LiveData<ShowList> getUpcomingMoviesList(String API_KEY,int currentPage,int previousPage) {
        if (upcomingMovieList == null || currentPage != previousPage) {
            upcomingMovieList = apiRepository.loadUpcomingMovies(API_KEY, currentPage);
        }
        return upcomingMovieList;
    }

    public LiveData<ShowList> getNowPlayingMoviesList(String API_KEY,int currentPage,int previousPage) {
        if (nowPlayingMovieList == null || currentPage != previousPage) {
            nowPlayingMovieList = apiRepository.loadNowPlayingMovies(API_KEY,currentPage);
        }
        return nowPlayingMovieList;
    }

}
