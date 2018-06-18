package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.ListResults;
import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesVM extends ViewModel {

    private MutableLiveData<ShowList> popularMovieList;
    private MutableLiveData<ShowList> topRatedMovieList;
    private MutableLiveData<ShowList> upcomingMovieList;
    private MutableLiveData<ShowList> nowPlayingMovieList;

    private MutableLiveData<Integer> currentPage = new MutableLiveData<>();

    public MutableLiveData<Integer> getCurrentPage() {
        if(currentPage == null){
            currentPage = new MutableLiveData<>();
            currentPage.setValue(1);
        }
        return currentPage;
    }

    public LiveData<ShowList> getPopularMoviesList(String API_KEY, int CURRENT_PAGE) {
        if (popularMovieList == null) {
            popularMovieList = new MutableLiveData<ShowList>();
            loadPopularMovies(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            loadPopularMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return popularMovieList;
    }
    private void loadPopularMovies(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getPopularMovies(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                popularMovieList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }

    public LiveData<ShowList> getTopRatedMoviesList(String API_KEY,int CURRENT_PAGE) {
        if (topRatedMovieList == null) {
            topRatedMovieList = new MutableLiveData<ShowList>();
            loadTopRatedMovies(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            loadTopRatedMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return topRatedMovieList;
    }
    private void loadTopRatedMovies(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getTopRatedMovies(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                topRatedMovieList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }

    public LiveData<ShowList> getUpcomingMoviesList(String API_KEY,int CURRENT_PAGE) {
        if (upcomingMovieList == null) {
            upcomingMovieList = new MutableLiveData<ShowList>();
            loadUpcomingMovies(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            loadUpcomingMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return upcomingMovieList;
    }
    private void loadUpcomingMovies(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getUpcomingMovies(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                upcomingMovieList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }

    public LiveData<ShowList> getNowPlayingMoviesList(String API_KEY,int CURRENT_PAGE) {
        if (nowPlayingMovieList == null) {
            nowPlayingMovieList = new MutableLiveData<ShowList>();
            loadNowPlayingMovies(API_KEY,CURRENT_PAGE);
        }else if(!currentPage.equals(CURRENT_PAGE)){
            loadNowPlayingMovies(API_KEY,CURRENT_PAGE);
        }
        currentPage.setValue(CURRENT_PAGE);
        return nowPlayingMovieList;
    }
    private void loadNowPlayingMovies(String API_KEY,int CURRENT_PAGE){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall = apiInterface.getNowPlayingMovies(API_KEY,CURRENT_PAGE);
        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                nowPlayingMovieList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {

            }
        });
    }
}
