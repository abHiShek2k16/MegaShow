package com.android.abhishek.megamovies.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailAVM extends ViewModel{

    private MutableLiveData<MovieDetail> movieDetailFromApi;

    public LiveData<MovieDetail> getMovieDetailFromApi(String movieId,String ApiKey,String AppendQuery){
        if(movieDetailFromApi == null){
            movieDetailFromApi = new MutableLiveData<>();
            loadMovieDetailFromApi(movieId,ApiKey,AppendQuery);
        }
        return movieDetailFromApi;
    }
    private void loadMovieDetailFromApi(String movieId,String ApiKey,String AppendQuery){
        ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<MovieDetail> movieDetailCall = apiInterface.getMovieDetail(movieId,ApiKey,AppendQuery);
        movieDetailCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                movieDetailFromApi.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {

            }
        });
    }
}

