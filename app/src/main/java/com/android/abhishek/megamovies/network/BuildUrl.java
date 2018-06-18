package com.android.abhishek.megamovies.network;

import android.content.Context;

import com.android.abhishek.megamovies.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuildUrl {

    private static Retrofit retrofit = null;
    private static String BASE_URL = "https://api.themoviedb.org/3/";   //  Non translatable Base Url

    public static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

}
