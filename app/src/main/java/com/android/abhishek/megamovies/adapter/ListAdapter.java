package com.android.abhishek.megamovies.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.abhishek.megamovies.MovieDetailAct;
import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.TvDetailAct;
import com.android.abhishek.megamovies.model.EndPoint;
import com.android.abhishek.megamovies.model.ListResults;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MovieListCustomAdapter>{

    private final List<ListResults> list;
    private final Activity host;
    private final String sortBy;

    public ListAdapter(List<ListResults> list,Activity host,String sortBy){
        this.list = list;
        this.host = host;
        this.sortBy = sortBy;
    }

    @NonNull
    @Override
    public MovieListCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MovieListCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieListCustomAdapter holder,int position) {

        ListResults movieListDetail = list.get(position);
        final String IMAGE_BASE_URL = EndPoint.IMAGE_BASE_URL;
        Picasso.get()
                .load(IMAGE_BASE_URL +movieListDetail.getPosterPath())
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .into(holder.posterImage);

        holder.posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(sortBy.equalsIgnoreCase("Movie")){
                    intent = new Intent(host, MovieDetailAct.class);
                }else{
                    intent = new Intent(host, TvDetailAct.class);
                }
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra(host.getResources().getString(R.string.intentPassingOne),list.get(holder.getAdapterPosition()).getId());
                boolean curve = (holder.getAdapterPosition() % 2 == 0);
                intent.putExtra(MovieDetailAct.EXTRA_CURVE, curve);
                intent.setData(Uri.parse(IMAGE_BASE_URL+list.get(holder.getAdapterPosition()).getPosterPath()));
                if(Build.VERSION.SDK_INT>=21){
                    host.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(host, holder.posterImage, holder.posterImage.getTransitionName()).toBundle());
                }else{
                    host.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MovieListCustomAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.posterImageMovieList)
        ImageView posterImage;
        MovieListCustomAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
