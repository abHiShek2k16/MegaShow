package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.network.ApiRepository;

public class MovieDetailApiVM extends ViewModel{

    private MutableLiveData<MovieDetail> movieDetailFromApi;
    private ApiRepository apiRepository = new ApiRepository();

    public LiveData<MovieDetail> getMovieDetailFromApi(String movieId,String ApiKey,String AppendQuery){
        if(movieDetailFromApi == null){
            movieDetailFromApi = apiRepository.loadMovieDetailFromApi(movieId,ApiKey,AppendQuery);
        }
        return movieDetailFromApi;
    }

}

