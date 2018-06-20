package com.android.abhishek.megamovies.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.TvDetailAct;
import com.android.abhishek.megamovies.adapter.ListAdapter;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.ShowList;
import com.android.abhishek.megamovies.viewModel.TvListVM;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TvFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    //  Xml View
    @BindView(R.id.fragmentRvAtTv) RecyclerView recyclerView;
    @BindView(R.id.fragmentPbAtTv) ProgressBar progressBar;
    @BindView(R.id.errorLayoutAtTv) RelativeLayout errorLayout;
    @BindView(R.id.showPageNoAtTv) TextView pageText;
    @BindView(R.id.nextAtTv) Button next;
    @BindView(R.id.previousAtTv) Button previous;
    private ImageView menu;

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
    private ShowList tvListObj;
    private ShowList tempShowList = new ShowList();
    private Toast toast;

    public TvFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);
        ButterKnife.bind(this,view);
        loadPreferences();

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(currentPageKey)){
                CURRENT_PAGE = savedInstanceState.getInt(currentPageKey);
            }
            if(savedInstanceState.containsKey(tvSortKey)){
                TV_SORT_BY = savedInstanceState.getString(tvSortKey);
            }
        }

        menu = getActivity().findViewById(R.id.menuBtn);
        registerForContextMenu(menu);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(tvSortKey,TV_SORT_BY);
        outState.putInt(currentPageKey,CURRENT_PAGE);
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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toast != null){
                    toast.cancel();
                }
                toast = Toast.makeText(getActivity(),getResources().getString(R.string.pressLong),Toast.LENGTH_SHORT);
                toast.show();
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
        if(!networkStatus()){
            showError();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        if(TV_SORT_BY.equals(POPULAR)){
            TvListVM tvViewModel = ViewModelProviders.of(this).get(TvListVM.class);
            tvViewModel.getPopularTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    if(tempShowList.equals(showList)){
                        return;
                    }else{
                        tempShowList = showList;
                        setTvList(showList);
                    }
                }
            });
        }else if(TV_SORT_BY.equals(TOP_RATED)){
            TvListVM tvViewModel = ViewModelProviders.of(this).get(TvListVM.class);
            tvViewModel.getTopRatedTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    if(tempShowList.equals(showList)){
                        return;
                    }else{
                        tempShowList = showList;
                        setTvList(showList);
                    }
                }
            });
        }else if(TV_SORT_BY.equals(ON_THE_AIR)){
            TvListVM tvViewModel = ViewModelProviders.of(this).get(TvListVM.class);
            tvViewModel.getOnTheAirTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    if(tempShowList.equals(showList)){
                        return;
                    }else{
                        tempShowList = showList;
                        setTvList(showList);
                    }
                }
            });
        }else {
            TvListVM tvViewModel = ViewModelProviders.of(this).get(TvListVM.class);
            tvViewModel.getAiringTodayTvList(API_KEY,CURRENT_PAGE).observe(this, new Observer<ShowList>() {
                @Override
                public void onChanged(@Nullable ShowList showList) {
                    if(tempShowList.equals(showList)){
                        return;
                    }else{
                        tempShowList = showList;
                        setTvList(showList);
                    }
                }
            });
        }
    }

    private void setTvList(ShowList showList){
        if(showList == null || showList.getResults().size()==0){
            showError();
        }
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        tvListObj = showList;
        TOTAL_PAGE = showList.getTotalPages();
        doPagination();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), NO_OF_IMAGE));
        ListAdapter movieListAdapter = new ListAdapter(showList.getResults());
        recyclerView.setAdapter(movieListAdapter);
    }

    private void showError(){
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        pageText.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        previous.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private boolean networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected())){
            return false;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.tv_sort_by,menu);

        MenuItem popular = menu.findItem(R.id.popularTv);
        MenuItem topRated = menu.findItem(R.id.mostRatedTv);
        MenuItem onTheAir = menu.findItem(R.id.onTheAirTv);
        MenuItem airingToday = menu.findItem(R.id.airingTodayTv);

        if(TV_SORT_BY.equalsIgnoreCase(POPULAR )){
           popular.setChecked(true);
        }else if(TV_SORT_BY.equalsIgnoreCase(AIRING_TODAY)){
            airingToday.setChecked(true);
        }else if(TV_SORT_BY.equalsIgnoreCase(ON_THE_AIR)){
            onTheAir.setChecked(true);
        }else{
            topRated.setChecked(true);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
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

        return super.onContextItemSelected(item);
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
        loadTv();
    }
    @OnClick(R.id.nextAtTv)
    void loadNext(){
        CURRENT_PAGE++;
        loadTv();
    }
    @OnClick(R.id.previousAtTv)
    void loadPrv(){
        CURRENT_PAGE--;
        loadTv();
    }

    private void doPagination(){
        if(CURRENT_PAGE == 1){
            previous.setVisibility(View.GONE);
        }else if(CURRENT_PAGE >1){
            previous.setVisibility(View.VISIBLE);
        }

        if(CURRENT_PAGE >= TOTAL_PAGE){
            next.setVisibility(View.GONE);
        }else if(CURRENT_PAGE < TOTAL_PAGE){
            next.setVisibility(View.VISIBLE);
        }
        pageText.setVisibility(View.VISIBLE);
        pageText.setText("Page "+CURRENT_PAGE+" of "+TOTAL_PAGE);
    }
}
