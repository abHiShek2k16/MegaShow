package com.android.abhishek.megamovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.model.EndPoint;
import com.android.abhishek.megamovies.model.ListResults;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MovieListCustomAdapter>{

    private final List<ListResults> list;
    private final String IMAGE_BASE_URL = EndPoint.IMAGE_BASE_URL;

    public ListAdapter(List<ListResults> list){
        this.list = list;
    }

    @NonNull
    @Override
    public MovieListCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MovieListCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListCustomAdapter holder, int position) {
        ListResults movieListDetail = list.get(position);
        Picasso.get()
                .load(IMAGE_BASE_URL+movieListDetail.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MovieListCustomAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.posterImageMovieList)
        ImageView posterImage;
        protected MovieListCustomAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
