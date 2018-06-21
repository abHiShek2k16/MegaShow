package com.android.abhishek.megamovies.network;

import com.android.abhishek.megamovies.model.EndPoint;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class BuildUrl {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = EndPoint.BASE_URL;   //  Non translatable Base Url

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
