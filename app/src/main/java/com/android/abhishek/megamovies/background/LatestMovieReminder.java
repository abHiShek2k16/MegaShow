package com.android.abhishek.megamovies.background;

import android.support.annotation.NonNull;

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.network.ApiInterface;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.util.NotificationUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestMovieReminder extends JobService{

    private ApiInterface apiInterface = BuildUrl.getRetrofit().create(ApiInterface.class);
    private int CURRENT_PAGE = 1;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        apiInterface.getPopularMovies(getResources().getString(R.string.apiKey),CURRENT_PAGE).enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(@NonNull Call<ShowList> call, @NonNull Response<ShowList> response) {
                ShowList popularMovieList = response.body();
                String logoPath = popularMovieList.getResults().get(0).getPosterPath();
                String id = popularMovieList.getResults().get(0).getId();
                if(logoPath != null && id != null){
                    NotificationUtils.movieNotify(LatestMovieReminder.this,id,getResources().getString(R.string.imageBaseUrl)+logoPath);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShowList> call, @NonNull Throwable t) {

            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

}
