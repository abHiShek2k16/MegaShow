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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.abhishek.megamovies.MovieDetailAct;
import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.TvDetailAct;
import com.android.abhishek.megamovies.adapter.ListAdapter;
import com.android.abhishek.megamovies.viewModel.FavVM;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.ListResults;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.TvDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    //  Xml View
    @BindView(R.id.fragmentRvAtFav) RecyclerView recyclerView;
    @BindView(R.id.fragmentPbAtFav) ProgressBar progressBar;
    @BindView(R.id.errorLayoutAtFav) RelativeLayout errorLayout;
    private ImageView menu;

    //  Sort Feature
    @BindString(R.string.movieQ) String MOVIE;
    @BindString(R.string.tvQ) String TV;

    //  Temporary Variable
    @BindString(R.string.favSortPref) String favSortKey;
    @BindString(R.string.movieQ) String FAV_SORT_BY;

    private int NO_OF_IMAGE = 2;
    private List<ListResults> resultList;
    private Toast toast;

    public FavFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        ButterKnife.bind(this,view);
        loadPreferences();

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(favSortKey)){
                FAV_SORT_BY = savedInstanceState.getString(favSortKey);
            }

        }
        menu = getActivity().findViewById(R.id.menuBtn);
        registerForContextMenu(menu);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(favSortKey,FAV_SORT_BY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkOrientation();
        loadFav();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(FAV_SORT_BY.equals(MOVIE)){
                    Intent intent = new Intent(getActivity(),MovieDetailAct.class);
                    intent.putExtra(getResources().getString(R.string.intentPassingOne),resultList.get(position).getId());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(),TvDetailAct.class);
                    intent.putExtra(getResources().getString(R.string.intentPassingOne),resultList.get(position).getId());
                    startActivity(intent);
                }
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
        FAV_SORT_BY = sharedPreferences.getString(favSortKey,MOVIE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadFav(){
        progressBar.setVisibility(View.VISIBLE);
        if(FAV_SORT_BY.equals(MOVIE)){
            FavVM viewModel = ViewModelProviders.of(this).get(FavVM.class);
            viewModel.getMovieDetailList().observe(this, new Observer<List<MovieDetail>>() {
                @Override
                public void onChanged(@Nullable List<MovieDetail> movieDetails) {
                    List<ListResults> listResultsArrayList = new ArrayList<>();
                    for(int i=0;i<movieDetails.size();i++){
                        listResultsArrayList.add(new ListResults(movieDetails.get(i).getMovieId(),movieDetails.get(i).getPosterPath()));
                    }
                    resultList = listResultsArrayList;
                    setResultList();
                }
            });
        }else{
            FavVM viewModel = ViewModelProviders.of(this).get(FavVM.class);
            viewModel.getTvDetailList().observe(this, new Observer<List<TvDetail>>() {
                @Override
                public void onChanged(@Nullable List<TvDetail> tvDetails) {
                    List<ListResults> listResultsArrayList = new ArrayList<>();
                    for(int i=0;i<tvDetails.size();i++){
                        listResultsArrayList.add(new ListResults(tvDetails.get(i).getMovieId(),tvDetails.get(i).getPosterPath()));
                    }
                    resultList = listResultsArrayList;
                    setResultList();
                }
            });
        }
    }

    private void setResultList(){
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if(resultList == null || resultList.size() == 0){
            showError();
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), NO_OF_IMAGE));
        ListAdapter movieListAdapter = new ListAdapter(resultList);
        recyclerView.setAdapter(movieListAdapter);
    }

    private void showError(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.fav_sort_by,menu);

        MenuItem mv = menu.findItem(R.id.movie);
        MenuItem tv = menu.findItem(R.id.tv);
        if(FAV_SORT_BY.equalsIgnoreCase(MOVIE)){
            mv.setChecked(true);
        }else{
            tv.setChecked(true);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.movie :
                item.setChecked(true);
                FAV_SORT_BY = MOVIE;
                loadFav();
                return true;
            case R.id.tv :
                item.setChecked(true);
                FAV_SORT_BY = TV;
                loadFav();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(favSortKey)){
            FAV_SORT_BY = sharedPreferences.getString(favSortKey,MOVIE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

}
