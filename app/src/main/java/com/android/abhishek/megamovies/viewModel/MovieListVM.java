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

    private MutableLiveData<Integer> currentPage = new MutableLiveData<>();
    private ApiRepository apiRepository = new ApiRepository();

    public LiveData<ShowList> getPopularMoviesList(String API_KEY, int CURRENT_PAGE) {
        if (popularMovieList == null) {
            popularMovieList = apiRepository.loadPopularMovies(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            popularMovieList = apiRepository.loadPopularMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return popularMovieList;
    }

    public LiveData<ShowList> getTopRatedMoviesList(String API_KEY,int CURRENT_PAGE) {
        if (topRatedMovieList == null) {
            topRatedMovieList = apiRepository.loadTopRatedMovies(API_KEY,CURRENT_PAGE);

        }else if(!currentPage.equals(CURRENT_PAGE)){
            topRatedMovieList = apiRepository.loadTopRatedMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return topRatedMovieList;
    }

    public LiveData<ShowList> getUpcomingMoviesList(String API_KEY,int CURRENT_PAGE) {
        if (upcomingMovieList == null) {
            upcomingMovieList = apiRepository.loadUpcomingMovies(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            upcomingMovieList = apiRepository.loadUpcomingMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return upcomingMovieList;
    }

    public LiveData<ShowList> getNowPlayingMoviesList(String API_KEY,int CURRENT_PAGE) {
        if (nowPlayingMovieList == null) {
            nowPlayingMovieList = apiRepository.loadNowPlayingMovies(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            nowPlayingMovieList = apiRepository.loadNowPlayingMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return nowPlayingMovieList;
    }

}
