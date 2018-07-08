package com.android.abhishek.megamovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.abhishek.megamovies.fragments.FavFragment;
import com.android.abhishek.megamovies.fragments.MoviesFragment;
import com.android.abhishek.megamovies.fragments.TvFragment;
import com.android.abhishek.megamovies.util.LatestMovieUtilities;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //  XML View
    @BindView(R.id.toolBarAtMainAct) Toolbar toolbar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;
    @BindView(R.id.toolBarText) TextView title;

    //  Api key
    @BindString(R.string.apiKey) String API_KEY;
    @BindString(R.string.navigationOption) String currentFragmentKey;
    @BindString(R.string.movieStr) String movie;
    @BindString(R.string.tvStr) String tvSeries;
    @BindString(R.string.favStr) String fav;

    private Toast toast;
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setNavigationView();
        checkApiKey();

        LatestMovieUtilities.scheduledMovieFetchingReminder(this);

        if(savedInstanceState != null){
            currentFragment = savedInstanceState.getString(currentFragmentKey);
            title.setText(currentFragment);
            return;
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout,new MoviesFragment()).commit();
            currentFragment = movie;
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(currentFragmentKey,currentFragment);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navMv :
                loadMovieFragment();
                currentFragment = movie;
                break;
            case R.id.navTv :
                loadTvFragment();
                currentFragment = tvSeries;
                break;
            case R.id.navFav :
                loadFavFragment();
                currentFragment = fav;
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
            if(toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(ListAct.this,getResources().getString(R.string.apiKeyProblem),Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void loadMovieFragment(){
        title.setText(movie);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new MoviesFragment()).commit();
    }

    private void loadTvFragment(){
        title.setText(tvSeries);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new TvFragment()).commit();
    }

    private void loadFavFragment(){
        title.setText(fav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new FavFragment()).commit();
    }

    private void loadSettingAct(){
        startActivity(new Intent(ListAct.this,SettingActivity.class));
    }

    private void rateApp(){
       //   Implement while uploading app to play store
    }

    private void shareApp(){
        //   Implement while uploading app to play store
    }
}
