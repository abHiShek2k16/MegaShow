package com.android.abhishek.megamovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.abhishek.megamovies.fragments.ErrorFragment;
import com.android.abhishek.megamovies.fragments.MovieFragment;
import com.android.abhishek.megamovies.fragments.SettingFragment;
import com.android.abhishek.megamovies.fragments.TvFragment;
import com.android.abhishek.megamovies.model.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SharedPreferences.OnSharedPreferenceChangeListener{

    @BindView(R.id.toolBarAtMainAct) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;

    @BindString(R.string.apiKey) String API_KEY;

    private int flag = 0;

    private boolean shakeBool;
    private boolean notifyBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setNavigationView();

        if(API_KEY == null){
            showError();
        }

        loadPreferences();
        if(!networkStatus()){
            getSupportFragmentManager().beginTransaction().add(R.id.mainActFragmentLayout,new ErrorFragment()).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.mainActFragmentLayout,new MovieFragment()).commit();
        }
    }

    private void setNavigationView(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
        int id = item.getItemId();
        switch (id){
            case R.id.navMv :
                if(flag!=0){
                    if(networkStatus()){
                        setMovieFragment();
                    }else{
                        showError();
                    }
                }
                flag = 0;
                break;
            case R.id.navTv :
                if(flag!=1){
                    if(networkStatus()){
                        setTvFragment();
                    }else{
                        showError();
                    }
                }
                flag = 1;
                break;
            case R.id.navFav :
                if(flag!=2){
                    setFavFragment();
                }
                flag = 2;
                break;
            case R.id.navSetting :
                if(flag!=3){
                    setSettingFragment();
                }
                flag = 3;
                break;
            case R.id.navExit :
                finish();
            case R.id.navShare :
                if(flag!=4){
                    shareApp();
                }
                flag = 4;
                break;
            case R.id.navRate :
                if(flag!=5){
                    rateApp();
                }
                flag = 5;
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected())){
            return false;
        }
        return true;
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //  favSortBy = sharedPreferences.getString(getResources().getString(R.string.favSortPref),getResources().getString(R.string.moviePref));
        shakeBool = sharedPreferences.getBoolean(getResources().getString(R.string.shakePref),true);
        notifyBool = sharedPreferences.getBoolean(getResources().getString(R.string.notificationPref),true);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
       /* if(s.equals(getResources().getString(R.string.movieSortPref))){
            mvSortBy = sharedPreferences.getString(getResources().getString(R.string.movieSortPref),getResources().getString(R.string.popularQ));
        }
        if(s.equals(getResources().getString(R.string.tvSortPref))){
            tvSortBy = sharedPreferences.getString(getResources().getString(R.string.tvSortPref),getResources().getString(R.string.popularQ));
        }
        if(s.equals(getResources().getString(R.string.favSortPref))){
            favSortBy = sharedPreferences.getString(getResources().getString(R.string.favSortPref),getResources().getString(R.string.moviePref));
        }*/
        if(s.equals(getResources().getString(R.string.shakePref))){
            shakeBool = sharedPreferences.getBoolean(getResources().getString(R.string.shakePref),true);
        }
        if(s.equals(getResources().getString(R.string.notificationPref))){
            notifyBool = sharedPreferences.getBoolean(getResources().getString(R.string.notificationPref),true);
        }
    }

    private void setMovieFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActFragmentLayout,new MovieFragment()).commit();
    }

    private void setTvFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActFragmentLayout,new TvFragment()).commit();
    }

    private void setFavFragment(){

    }

    private void setSettingFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActFragmentLayout,new SettingFragment()).commit();
    }

    private void rateApp(){

    }

    private void shareApp(){

    }

    private void showError(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActFragmentLayout,new ErrorFragment()).commit();
    }

    private void refresh(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
