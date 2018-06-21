package com.android.abhishek.megamovies.network;

import com.android.abhishek.megamovies.model.EndPoint;
import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.PersonProfile;
import com.android.abhishek.megamovies.model.TvDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface ApiInterface {

    @GET(EndPoint.UPCOMING_URL)
    Call<ShowList> getUpcomingMovies(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.TOP_RATED_URL)
    Call<ShowList> getTopRatedMovies(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.POPULAR_URL)
    Call<ShowList> getPopularMovies(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.NOW_PLAYING_URL)
    Call<ShowList> getNowPlayingMovies(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.MOVIE_DETAIL_URl)
    Call<MovieDetail> getMovieDetail(@Path(EndPoint.ID_QUERY) String id, @Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.APPEND_TO_RESPONSE) String append);

    @GET(EndPoint.POPULAR_TV_URL)
    Call<ShowList> getPopularTv(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.TOP_RATED_TV_URL)
    Call<ShowList> getTopRatedTv(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.ON_THE_AIR_URL)
    Call<ShowList> getOnTheAir(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.AIRING_TODAY)
    Call<ShowList> getAiringToday(@Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.PAGE_QUERY) int page);

    @GET(EndPoint.TV_DETAIL_URL)
    Call<TvDetail> getTvDetail(@Path(EndPoint.ID_QUERY) String id, @Query(EndPoint.API_QUERY) String apiKey, @Query(EndPoint.APPEND_TO_RESPONSE) String append);

    @GET(EndPoint.PROFILE_URL)
    Call<PersonProfile> getProfile(@Path(EndPoint.ID_QUERY) String id, @Query(EndPoint.API_QUERY) String apiKey);
}
