package com.android.abhishek.megamovies.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.TvDetailAct;
import com.android.abhishek.megamovies.adapter.ListAdapter;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.List;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvFragment extends Fragment {

    //  network may gone while changing sort
    //  setChecked menu item

    @BindView(R.id.tvFragmentPb) ProgressBar progressBar;
    @BindView(R.id.tvFragmentRv) RecyclerView recyclerView;
    @BindView(R.id.tvFragmentNextBtn) Button next;
    @BindView(R.id.tvFragmentPreviousBtn) Button previous;

    @BindString(R.string.apiKey) String API_KEY;
    @BindString(R.string.popularQ) String POPULAR;
    @BindString(R.string.topRatedQ) String TOP_RATED;
    @BindString(R.string.onTheAirQ) String ON_THE_AIR;
    @BindString(R.string.airingTodayQ) String AIRING_TODAY;

    private String SORT_BY;

    private int NO_OF_IMAGE = 2;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE = 1;

    private List tvList;

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
        ButterKnife.bind(this, view);
        checkOrientation();
        loadPreferences();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadTv();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), TvDetailAct.class);
                        intent.putExtra(getResources().getString(R.string.intentPassingOne),tvList.getResults().get(position).getId());
                        startActivity(intent);
                    }
                })
        );

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CURRENT_PAGE < TOTAL_PAGE) {
                    CURRENT_PAGE++;
                    loadTv();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CURRENT_PAGE > 1) {
                    CURRENT_PAGE--;
                    loadTv();
                }
            }
        });
    }

    private void loadTv(){
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = BuildUrl.getRetrofit(getActivity()).create(ApiInterface.class);
        retrofit2.Call<List> tvListCall;
        if(SORT_BY.equals(POPULAR)){
            tvListCall = apiInterface.getPopularTv(API_KEY,CURRENT_PAGE);
        }else if(SORT_BY.equals(TOP_RATED)){
            tvListCall = apiInterface.getTopRatedTv(API_KEY,CURRENT_PAGE);
        }else if(SORT_BY.equals(ON_THE_AIR)){
            tvListCall = apiInterface.getOnTheAir(API_KEY,CURRENT_PAGE);
        }else {
            tvListCall = apiInterface.getAiringToday(API_KEY,CURRENT_PAGE);
        }

        tvListCall.enqueue(new Callback<List>() {
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                tvList = response.body();
                setTvList();
            }

            @Override
            public void onFailure(Call<List> call, Throwable t) {
                closeOnError(getResources().getString(R.string.somethingWrong));
            }
        });
    }

    private void setTvList(){
        progressBar.setVisibility(View.GONE);
        TOTAL_PAGE = tvList.getTotalPages();

        if(tvList.getResults() == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }else if(tvList.getResults().size() == 0){
            closeOnError(getResources().getString(R.string.nothingToShow));
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), NO_OF_IMAGE));
        ListAdapter movieListAdapter = new ListAdapter(tvList.getResults());
        recyclerView.setAdapter(movieListAdapter);

        if (TOTAL_PAGE > CURRENT_PAGE) {
            next.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.INVISIBLE);
        }

        if (CURRENT_PAGE == 1) {
            previous.setVisibility(View.INVISIBLE);
        } else {
            previous.setVisibility(View.VISIBLE);
        }
    }

    public void checkOrientation(){
        int configuration = this.getResources().getConfiguration().orientation;
        if(configuration == Configuration.ORIENTATION_PORTRAIT){
            NO_OF_IMAGE = 2;
        }else{
            NO_OF_IMAGE = 3;
        }
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SORT_BY = sharedPreferences.getString(getResources().getString(R.string.tvSortPref),getResources().getString(R.string.popularQ));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_by_tv,menu);

       /* if(SORT_BY.equals(POPULAR)){
            menu.getItem(R.id.popularTv).setChecked(true);
        }else if(SORT_BY.equals(TOP_RATED)){
            menu.getItem(R.id.mostRatedTv).setChecked(true);
        }else if(SORT_BY.equals(ON_THE_AIR)){
            menu.getItem(R.id.onTheAirTv).setChecked(true);
        }else {
            menu.getItem(R.id.airingTodayTv).setChecked(true);
        }   */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popularTv :
                if(SORT_BY.equalsIgnoreCase(POPULAR)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = POPULAR;
                loadTv();
                return true;

            case R.id.mostRatedTv :
                if(SORT_BY.equalsIgnoreCase(TOP_RATED)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = TOP_RATED;
                loadTv();
                return true;

            case R.id.onTheAirTv :
                if(SORT_BY.equalsIgnoreCase(ON_THE_AIR)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = ON_THE_AIR;
                loadTv();
                return true;

            case R.id.airingTodayTv :
                if(SORT_BY.equalsIgnoreCase(AIRING_TODAY)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = AIRING_TODAY;
                loadTv();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeOnError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
