package com.android.abhishek.megamovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.abhishek.megamovies.fragments.FavFragment;
import com.android.abhishek.megamovies.fragments.MoviesFragment;
import com.android.abhishek.megamovies.fragments.TvFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //  XML View
    @BindView(R.id.toolBarAtMainAct) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;
    @BindView(R.id.noInternetBack) TextView errorText;

    //  Api key
    @BindString(R.string.apiKey) String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setNavigationView();
        checkApiKey();
        networkStatus();

        if(savedInstanceState != null){
            return;
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout,new MoviesFragment()).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(API_KEY,API_KEY);        //      to be changed
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
                loadMovieFragment();
                break;
            case R.id.navTv :
                loadTvFragment();
                break;
            case R.id.navFav :
                loadFavFragment();
                break;
            case R.id.navSetting :
                loadSettingAct();
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
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getResources().getString(R.string.apiKeyProblem));
        }
    }

    private void loadMovieFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new MoviesFragment()).commit();
    }

    private void loadTvFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new TvFragment()).commit();
    }

    private void loadFavFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new FavFragment()).commit();
    }

    private void loadSettingAct(){
        startActivity(new Intent(ListAct.this,SettingActivity.class));
    }

    private void rateApp(){
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    private void shareApp(){

    }

    private void networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected())){
            showError();
        }
    }

    private void showError(){
        errorText.setText(getResources().getString(R.string.netproblem));
        errorText.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.noInternetBack)
    void refresh(){
        errorText.setVisibility(View.GONE);
        networkStatus();
    }

}
