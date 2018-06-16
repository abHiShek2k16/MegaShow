package com.android.abhishek.megamovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.abhishek.megamovies.adapter.ListAdapter;
import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.model.ListResults;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SharedPreferences.OnSharedPreferenceChangeListener{
    //  xml view
    @BindView(R.id.toolBarAtMainAct) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;
    @BindView(R.id.fragmentRv) RecyclerView recyclerView;
    @BindView(R.id.fragmentPb) ProgressBar progressBar;
    @BindView(R.id.errorText) TextView errorText;
    @BindView(R.id.errorLayout) RelativeLayout errorLayout;
    @BindView(R.id.listLayout) RelativeLayout listLayout;

    //  Api key
    @BindString(R.string.apiKey) String API_KEY;

    //  Sort feature
    @BindString(R.string.popularQ) String POPULAR;
    @BindString(R.string.topRatedQ) String TOP_RATED;
    @BindString(R.string.upcomingQ) String UPCOMING;
    @BindString(R.string.nowPlayingQ) String NOW_PLAYING;
    @BindString(R.string.onTheAirQ) String ON_THE_AIR;
    @BindString(R.string.airingTodayQ) String AIRING_TODAY;
    @BindString(R.string.movieQ) String MOVIE;
    @BindString(R.string.tvQ) String TV;

    //  Indicate the temporary sort preference
    @BindString(R.string.popularQ) String MOVIE_SORT_BY;
    @BindString(R.string.popularQ) String TV_SORT_BY;
    @BindString(R.string.movieQ) String FAV_SORT_BY;

    //  key to restore from savedInstanceState and SettingPreference
    @BindString(R.string.movieSortPref) String movieSortKey;
    @BindString(R.string.tvSortPref) String tvSortKey;
    @BindString(R.string.favSortPref) String favSortKey;
    @BindString(R.string.currentPage) String currentPageKey;
    @BindString(R.string.currentFlag) String currentFlagKey;

    //  Temporary variable
    private int NO_OF_IMAGE = 2;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE = 1;
    private int flag = 0;       /*  flag = 0    represent Movies
                                    flag = 1    represent Tv Series
                                    flag = 2    represent Fav Collection  */

    private List<ListResults> showArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setNavigationView();
        checkApiKey();
        networkStatus();
        checkOrientation();
        loadPreferences();
        loadList();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(flag==0){
                    Intent intent = new Intent(ListAct.this,MovieDetailAct.class);
                    intent.putExtra(getResources().getString(R.string.intentPassingOne),showArrayList.get(position).getId());
                    startActivity(intent);
                }else if(flag==1){
                    Intent intent = new Intent(ListAct.this,TvDetailAct.class);
                    intent.putExtra(getResources().getString(R.string.intentPassingOne),showArrayList.get(position).getId());
                    startActivity(intent);
                }else if(flag==2){
                    if(FAV_SORT_BY.equals(MOVIE)){
                        Intent intent = new Intent(ListAct.this,MovieDetailAct.class);
                        intent.putExtra(getResources().getString(R.string.intentPassingOne),showArrayList.get(position).getId());
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ListAct.this,TvDetailAct.class);
                        intent.putExtra(getResources().getString(R.string.intentPassingOne),showArrayList.get(position).getId());
                        startActivity(intent);
                    }
                }
            }
        }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(movieSortKey, MOVIE_SORT_BY);
        outState.putString(tvSortKey, TV_SORT_BY);
        outState.putString(favSortKey,FAV_SORT_BY);
        outState.putInt(currentPageKey,CURRENT_PAGE);
        outState.putInt(currentFlagKey,flag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey(movieSortKey)){
            MOVIE_SORT_BY = savedInstanceState.getString(movieSortKey);
        }
        if(savedInstanceState.containsKey(tvSortKey)){
            TV_SORT_BY = savedInstanceState.getString(tvSortKey);
        }
        if(savedInstanceState.containsKey(favSortKey)){
            FAV_SORT_BY = savedInstanceState.getString(favSortKey);
        }
        if(savedInstanceState.containsKey(currentPageKey)){
            CURRENT_PAGE = savedInstanceState.getInt(currentPageKey);
        }
        if(savedInstanceState.containsKey(currentFlagKey)){
            flag = savedInstanceState.getInt(currentFlagKey);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(movieSortKey)){
            MOVIE_SORT_BY = sharedPreferences.getString(movieSortKey,POPULAR);
            if(flag==0){
                networkStatus();
                loadMovies();
            }
        }
        if(s.equals(tvSortKey)){
            TV_SORT_BY = sharedPreferences.getString(tvSortKey,POPULAR);
            if(flag==1){
                networkStatus();
                loadTv();
            }
        }
        if(s.equals(favSortKey)){
            FAV_SORT_BY = sharedPreferences.getString(favSortKey,MOVIE);
            if (flag==2){
                loadFav();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navMv :
                if(flag!=0){
                    CURRENT_PAGE = 1;
                    networkStatus();
                    loadMovies();
                }
                flag = 0;
                break;
            case R.id.navTv :
                if(flag!=1){
                    CURRENT_PAGE = 1;
                    networkStatus();
                    loadTv();
                }
                flag = 1;
                break;
            case R.id.navFav :
                if(flag!=2){
                    loadFav();
                }
                flag = 2;
                break;
            case R.id.navSetting :
                loadSetting();
                break;
            case R.id.navExit :
                finish();
            case R.id.navShare :
                shareApp();
                break;
            case R.id.navRate :
                rateApp();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationView(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkApiKey(){
        if(API_KEY == null || API_KEY.isEmpty()){
            showError(getResources().getString(R.string.apiKeyProblem));
        }
    }

    private void networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected())){
            showError(getResources().getString(R.string.connectionError));
        }
    }

    public void checkOrientation(){
        int configuration = this.getResources().getConfiguration().orientation;
        if(configuration == Configuration.ORIENTATION_PORTRAIT){
            NO_OF_IMAGE = 2;
        }else{
            NO_OF_IMAGE = 4;
        }
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MOVIE_SORT_BY = sharedPreferences.getString(movieSortKey,POPULAR);
        TV_SORT_BY = sharedPreferences.getString(tvSortKey,POPULAR);
        FAV_SORT_BY = sharedPreferences.getString(favSortKey,MOVIE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadList(){
        switch (flag){
            case 0:
                loadMovies();
                break;
            case 1:
                loadTv();
                break;
            case 2:
                loadFav();
        }
    }

    public void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = BuildUrl.getRetrofit(this).create(ApiInterface.class);
        retrofit2.Call<ShowList> movieListCall;

        if (MOVIE_SORT_BY.equals(POPULAR)) {
            movieListCall = apiInterface.getPopularMovies(API_KEY,CURRENT_PAGE);
        } else if(MOVIE_SORT_BY.equals(TOP_RATED)){
            movieListCall = apiInterface.getTopRatedMovies(API_KEY,CURRENT_PAGE);
        }else if(MOVIE_SORT_BY.equals(UPCOMING)){
            movieListCall = apiInterface.getUpcomingMovies(API_KEY,CURRENT_PAGE);
        }else {
            movieListCall = apiInterface.getNowPlayingMovies(API_KEY,CURRENT_PAGE);
        }

        movieListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                showArrayList = response.body().getResults();
                TOTAL_PAGE = response.body().getTotalPages();
                setList();
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {
                showError(t.getCause().toString());
            }
        });
    }

    private void loadTv(){
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = BuildUrl.getRetrofit(this).create(ApiInterface.class);
        retrofit2.Call<ShowList> tvListCall;
        if(TV_SORT_BY.equals(POPULAR)){
            tvListCall = apiInterface.getPopularTv(API_KEY,CURRENT_PAGE);
        }else if(TV_SORT_BY.equals(TOP_RATED)){
            tvListCall = apiInterface.getTopRatedTv(API_KEY,CURRENT_PAGE);
        }else if(TV_SORT_BY.equals(ON_THE_AIR)){
            tvListCall = apiInterface.getOnTheAir(API_KEY,CURRENT_PAGE);
        }else {
            tvListCall = apiInterface.getAiringToday(API_KEY,CURRENT_PAGE);
        }

        tvListCall.enqueue(new Callback<ShowList>() {
            @Override
            public void onResponse(Call<ShowList> call, Response<ShowList> response) {
                showArrayList = response.body().getResults();
                TOTAL_PAGE = response.body().getTotalPages();
                setList();
            }

            @Override
            public void onFailure(Call<ShowList> call, Throwable t) {
                showError(t.getCause().toString());
            }
        });
    }

    private void loadFav(){
        progressBar.setVisibility(View.VISIBLE);
        ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        List<ListResults> listResultsArrayList = new ArrayList<>();
        if(FAV_SORT_BY.equals(MOVIE)){
            List<MovieDetail> moveList = showDatabase.showDao().getListOfMv();
            for(int i=0;i<moveList.size();i++){
                listResultsArrayList.add(new ListResults(moveList.get(i).getMovieId(),moveList.get(i).getPosterPath()));
            }
        }else{
            List<TvDetail> tvList = showDatabase.showDao().getListOfTv();
            for(int i=0;i<tvList.size();i++){
                listResultsArrayList.add(new ListResults(tvList.get(i).getMovieId(),tvList.get(i).getPosterPath()));
            }
        }
        showArrayList = listResultsArrayList;
        TOTAL_PAGE = 1;
        setList();
    }

    public void setList() {
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
        if(showArrayList == null || showArrayList.size()==0){
            showError(getResources().getString(R.string.nothingToShow));
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, NO_OF_IMAGE));
        ListAdapter movieListAdapter = new ListAdapter(showArrayList);
        recyclerView.setAdapter(movieListAdapter);
    }

    private void loadSetting(){
        startActivity(new Intent(ListAct.this,SettingActivity.class));
    }

    private void rateApp(){

    }

    private void shareApp(){

    }

    private void showError(String message){
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(message);
        listLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.retryBtn)
    void refresh(){
        errorLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
        startActivity(new Intent(getIntent()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
