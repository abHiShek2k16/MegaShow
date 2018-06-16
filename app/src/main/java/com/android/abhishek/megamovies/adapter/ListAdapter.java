package com.android.abhishek.megamovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.model.ListResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MovieListCustomAdapter>{

    private List<ListResults> arrayList;
    private String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public ListAdapter(List<ListResults> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MovieListCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MovieListCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListCustomAdapter holder, int position) {
        ListResults movieListDetail = arrayList.get(position);
        Picasso.get()
                .load(IMAGE_BASE_URL+movieListDetail.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MovieListCustomAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.posterImageMovieList)
        ImageView posterImage;
        public MovieListCustomAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
