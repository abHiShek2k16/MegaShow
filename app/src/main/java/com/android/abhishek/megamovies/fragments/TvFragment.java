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

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.TvDetailAct;
import com.android.abhishek.megamovies.adapter.ListAdapter;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.viewModel.TvVM;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TvFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    //  Xml View
    @BindView(R.id.fragmentRvAtTv) RecyclerView recyclerView;
    @BindView(R.id.fragmentPbAtTv) ProgressBar progressBar;
    @BindView(R.id.errorLayoutAtTv) RelativeLayout errorLayout;

    //  Api Key
    @BindString(R.string.apiKey) String API_KEY;

    //  Sort Features
    @BindString(R.string.popularQ) String POPULAR;
    @BindString(R.string.topRatedQ) String TOP_RATED;
    @BindString(R.string.onTheAirQ) String ON_THE_AIR;
    @BindString(R.string.airingTodayQ) String AIRING_TODAY;

    //  Temporary Variable
    @BindString(R.string.popularQ) String TV_SORT_BY;
    @BindString(R.string.tvSortPref) String tvSortKey;
    @BindString(R.string.currentPage) String currentPageKey;

    private int NO_OF_IMAGE = 2;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE = 1;
    private boolean isScrolling = false;
    private ShowList tvListObj;

    public TvFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        loadPreferences();
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(currentPageKey)){
                CURRENT_PAGE = savedInstanceState.getInt(currentPageKey);
            }
            if(savedInstanceState.containsKey(tvSortKey)){
                TV_SORT_BY = savedInstanceState.getString(tvSortKey);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(tvSortKey,TV_SORT_BY);
        outState.putInt(currentPageKey,CURRENT_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkOrientation();
        loadTv();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),TvDetailAct.class);
                intent.putExtra(getResources().getString(R.string.intentPassingOne),tvListObj.getResults().get(position).getId());
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
                            loadTv();
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
                            loadTv();
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
        TV_SORT_BY = sharedPreferences.getString(tvSortKey,POPULAR);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadTv(){
        progressBar.setVisibility(View.VISIBLE);
        if(TV_SORT_BY.equals(POPULAR)){
            TvVM tvViewModel = ViewModelProviders.of(this).get(TvVM.class);
            tvViewModel.getPopularTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setTvList(showList);
                }
            });
        }else if(TV_SORT_BY.equals(TOP_RATED)){
            TvVM tvViewModel = ViewModelProviders.of(this).get(TvVM.class);
            tvViewModel.getTopRatedTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setTvList(showList);
                }
            });
        }else if(TV_SORT_BY.equals(ON_THE_AIR)){
            TvVM tvViewModel = ViewModelProviders.of(this).get(TvVM.class);
            tvViewModel.getOnTheAirTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setTvList(showList);
                }
            });
        }else {
            TvVM tvViewModel = ViewModelProviders.of(this).get(TvVM.class);
            tvViewModel.getAiringTodayTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    setTvList(showList);
                }
            });
        }
    }

    private void setTvList(ShowList showList){
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if(showList == null || showList.getResults().size()==0){
            showError();
        }
        tvListObj = showList;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tv_sort_by,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popularTv :
                item.setChecked(true);
                TV_SORT_BY = POPULAR;
                loadTv();
                return true;
            case R.id.mostRatedTv :
                item.setChecked(true);
                TV_SORT_BY = TOP_RATED;
                loadTv();
                return true;
            case R.id.onTheAirTv :
                item.setChecked(true);
                TV_SORT_BY = ON_THE_AIR;
                loadTv();
                return true;
            case R.id.airingTodayTv :
                item.setChecked(true);
                TV_SORT_BY = AIRING_TODAY;
                loadTv();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(tvSortKey)){
            TV_SORT_BY = sharedPreferences.getString(tvSortKey,POPULAR);
            loadTv();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @OnClick(R.id.retryAtTv)
    void refresh(){
        errorLayout.setVisibility(View.GONE);
        loadTv();
    }
}
