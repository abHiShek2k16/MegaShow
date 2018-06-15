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

import com.android.abhishek.megamovies.MovieDetailAct;
import com.android.abhishek.megamovies.R;
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

public class MovieFragment extends Fragment {

    //  network may gone while changing sort
    //  setChecked menu item

    @BindView(R.id.movieFragmentRv) RecyclerView recyclerView;
    @BindView(R.id.movieFragmentPb) ProgressBar progressBar;
    @BindView(R.id.movieFragmentPreviousBtn) Button previousBtn;
    @BindView(R.id.movieFragmentNextBtn) Button nextBtn;

    @BindString(R.string.apiKey) String API_KEY;
    @BindString(R.string.popularQ) String POPULAR;
    @BindString(R.string.topRatedQ) String TOP_RATED;
    @BindString(R.string.upcomingQ) String UPCOMING;
    @BindString(R.string.nowPlayingQ) String NOW_PLAYING;
    private String SORT_BY;

    private int NO_OF_IMAGE = 2;
    private int CURRENT_PAGE = 1;
    private int TOTAL_PAGE = 1;

    private List movieList;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);
        checkOrientation();
        loadPreferences();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadMovies();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), MovieDetailAct.class);
                        intent.putExtra(getResources().getString(R.string.intentPassingOne),movieList.getResults().get(position).getId());
                        startActivity(intent);
                    }
                })
        );

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CURRENT_PAGE < TOTAL_PAGE) {
                    CURRENT_PAGE++;
                    loadMovies();
                }
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CURRENT_PAGE > 1) {
                    CURRENT_PAGE--;
                    loadMovies();
                }
            }
        });

    }

    public void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = BuildUrl.getRetrofit(getActivity()).create(ApiInterface.class);
        retrofit2.Call<List> movieListCall;

        if (SORT_BY.equals(POPULAR)) {
            movieListCall = apiInterface.getPopularMovies(API_KEY,CURRENT_PAGE);
        } else if(SORT_BY.equals(TOP_RATED)){
            movieListCall = apiInterface.getTopRatedMovies(API_KEY,CURRENT_PAGE);
        }else if(SORT_BY.equals(UPCOMING)){
            movieListCall = apiInterface.getUpcomingMovies(API_KEY,CURRENT_PAGE);
        }else {
            movieListCall = apiInterface.getNowPlayingMovies(API_KEY,CURRENT_PAGE);
        }

        movieListCall.enqueue(new Callback<List>() {
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                movieList = response.body();
                setMovieList();
            }

            @Override
            public void onFailure(Call<List> call, Throwable t) {
                closeOnError(getResources().getString(R.string.somethingWrong));
            }
        });
    }

    public void setMovieList() {

        progressBar.setVisibility(View.GONE);
        TOTAL_PAGE = movieList.getTotalPages();
        if(movieList.getResults() == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }else if(movieList.getResults().size()==0){
            closeOnError(getResources().getString(R.string.nothingToShow));
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), NO_OF_IMAGE));
        ListAdapter movieListAdapter = new ListAdapter(movieList.getResults());
        recyclerView.setAdapter(movieListAdapter);

        if (TOTAL_PAGE > CURRENT_PAGE) {
            nextBtn.setVisibility(View.VISIBLE);
        } else {
            nextBtn.setVisibility(View.INVISIBLE);
        }

        if (CURRENT_PAGE == 1) {
            previousBtn.setVisibility(View.INVISIBLE);
        } else {
            previousBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_by_movie,menu);
        /*
        if (SORT_BY.equals(POPULAR)) {
            menu.getItem(R.id.popularMv).setChecked(true);
        } else if(SORT_BY.equals(TOP_RATED)){
            menu.getItem(R.id.mostRatedMv).setChecked(true);
        }else if(SORT_BY.equals(UPCOMING)){
            menu.getItem(R.id.upcomingMv).setChecked(true);
        }else {
            menu.getItem(R.id.nowPlayMv).setChecked(true);
        }  */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popularMv :
                if(SORT_BY.equalsIgnoreCase(POPULAR)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = POPULAR;
                loadMovies();
                return true;

            case R.id.mostRatedMv :
                if(SORT_BY.equalsIgnoreCase(TOP_RATED)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = TOP_RATED;
                loadMovies();
                return true;

            case R.id.upcomingMv :
                if(SORT_BY.equalsIgnoreCase(UPCOMING)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = UPCOMING;
                loadMovies();
                return true;

            case R.id.nowPlayMv :
                if(SORT_BY.equalsIgnoreCase(NOW_PLAYING)){
                    return true;
                }
                item.setChecked(true);
                SORT_BY = NOW_PLAYING;
                loadMovies();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        SORT_BY = sharedPreferences.getString(getResources().getString(R.string.movieSortPref),getResources().getString(R.string.popularQ));
    }

    public void closeOnError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
