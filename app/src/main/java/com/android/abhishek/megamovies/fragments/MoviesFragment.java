package com.android.abhishek.megamovies.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.abhishek.megamovies.MovieDetailAct;
import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.adapter.ListAdapter;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.viewModel.MoviesVM;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoviesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    //  XML Views
    @BindView(R.id.fragmentRvAtMv) RecyclerView recyclerView;
    @BindView(R.id.fragmentPbAtMv) ProgressBar progressBar;
    @BindView(R.id.errorLayoutAtMv) RelativeLayout errorLayout;

    //  Api Key
    @BindString(R.string.apiKey) String API_KEY;

    //  Sort Features
    @BindString(R.string.popularQ) String POPULAR;
    @BindString(R.string.topRatedQ) String TOP_RATED;
    @BindString(R.string.upcomingQ) String UPCOMING;
    @BindString(R.string.nowPlayingQ) String NOW_PLAYING;

    // Temporary Variable To Store Sort Pref
    @BindString(R.string.popularQ) String MOVIE_SORT_BY;

    //  Key To Restore From Shared Pref
    @BindString(R.string.movieSortPref) String movieSortKey;
    @BindString(R.string.currentPage) String currentPageKey;

    //  Temporary Variables
    private int NO_OF_IMAGE = 2;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE = 1;
    private boolean isScrolling = false;
    private ShowList movieListObj;

    public MoviesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        loadPreferences();
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(currentPageKey)){
                CURRENT_PAGE = savedInstanceState.getInt(currentPageKey);
            }
            if(savedInstanceState.containsKey(movieSortKey)){
                MOVIE_SORT_BY = savedInstanceState.getString(movieSortKey);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(movieSortKey,MOVIE_SORT_BY);
        outState.putInt(currentPageKey,CURRENT_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkOrientation();
        loadMovies();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),MovieDetailAct.class);
                intent.putExtra(getResources().getString(R.string.intentPassingOne),movieListObj.getResults().get(position).getId());
                startActivity(intent);
            }
        }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0 && isScrolling){
                    final int visibleThreshold = NO_OF_IMAGE;
                    GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
                    int lastItem  = layoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = layoutManager.getItemCount();
                    if(currentTotalCount <= lastItem + visibleThreshold){
                       if(CURRENT_PAGE < TOTAL_PAGE){
                           CURRENT_PAGE++;
                           loadMovies();
                       }
                    }
                }else if(dy<0 && isScrolling){
                    final int visibleThreshold = NO_OF_IMAGE;
                    GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
                    int lastItem  = layoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = layoutManager.getItemCount();
                    if(currentTotalCount <= lastItem + visibleThreshold){
                        if(CURRENT_PAGE > TOTAL_PAGE){
                            CURRENT_PAGE--;
                            loadMovies();
                        }
                    }
                }
            }
        });
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        MOVIE_SORT_BY = sharedPreferences.getString(movieSortKey,POPULAR);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadMovies(){
        //  to check network status
        progressBar.setVisibility(View.VISIBLE);
        if (MOVIE_SORT_BY.equals(POPULAR)) {
            MoviesVM moviesViewModel = ViewModelProviders.of(this).get(MoviesVM.class);
            moviesViewModel.getPopularMoviesList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setMovieList(showList);
                }
            });
        } else if(MOVIE_SORT_BY.equals(TOP_RATED)){
            MoviesVM moviesViewModel = ViewModelProviders.of(this).get(MoviesVM.class);
            moviesViewModel.getTopRatedMoviesList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setMovieList(showList);
                }
            });
        }else if(MOVIE_SORT_BY.equals(UPCOMING)){
            MoviesVM moviesViewModel = ViewModelProviders.of(this).get(MoviesVM.class);
            moviesViewModel.getUpcomingMoviesList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setMovieList(showList);
                }
            });
        }else {
            MoviesVM moviesViewModel = ViewModelProviders.of(this).get(MoviesVM.class);
            moviesViewModel.getNowPlayingMoviesList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setMovieList(showList);
                }
            });
        }
    }

    private void setMovieList(ShowList showList){
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if(showList == null || showList.getResults().size()==0){
            showError();
        }
        movieListObj = showList;
        TOTAL_PAGE = showList.getTotalPages();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), NO_OF_IMAGE));
        ListAdapter movieListAdapter = new ListAdapter(showList.getResults());
        recyclerView.setAdapter(movieListAdapter);
    }

    private void showError(){
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(movieSortKey)){
            MOVIE_SORT_BY = sharedPreferences.getString(movieSortKey,POPULAR);
            loadMovies();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.movie_sort_by,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popularMv :
                item.setChecked(true);
                MOVIE_SORT_BY = POPULAR;
                loadMovies();
                return true;
            case R.id.mostRatedMv :
                item.setChecked(true);
                MOVIE_SORT_BY = TOP_RATED;
                loadMovies();
                return true;
            case R.id.upcomingMv :
                item.setChecked(true);
                MOVIE_SORT_BY = UPCOMING;
                loadMovies();
                return true;
            case R.id.nowPlayMv :
                item.setChecked(true);
                MOVIE_SORT_BY = NOW_PLAYING;
                loadMovies();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @OnClick(R.id.retryAtMv)
    void refresh(){
        errorLayout.setVisibility(View.GONE);
        loadMovies();
    }

}
