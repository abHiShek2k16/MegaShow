package com.android.abhishek.megamovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.model.EndPoint;
import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieCastsAdapter extends RecyclerView.Adapter<MovieCastsAdapter.CastsCustomAdapter>{

    private final List<MovieCastsResult> movieCastsResults;

    public MovieCastsAdapter(List<MovieCastsResult> movieCastsResults) {
        this.movieCastsResults = movieCastsResults;
    }

    @NonNull
    @Override
    public CastsCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_list_item,parent,false);
        return new CastsCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastsCustomAdapter holder, int position) {
        MovieCastsResult movieCastsResult = movieCastsResults.get(position);
        if(movieCastsResult == null){
            return;
        }
        String IMAGE_BASE_URL = EndPoint.IMAGE_BASE_URL;
        Picasso.get()
                .load(IMAGE_BASE_URL +movieCastsResult.getProfilePath())
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .into(holder.castIv);
        String name = movieCastsResult.getName()==null?"":movieCastsResult.getName();
        String character = movieCastsResult.getCharacter()==null?"":movieCastsResult.getCharacter();
        holder.nameTv.setText(name);
        holder.rollTv.setText(character);
    }

    @Override
    public int getItemCount() {
        return movieCastsResults.size();
    }

    public class CastsCustomAdapter extends RecyclerView.ViewHolder{
        @BindView(R.id.castIv)
        ImageView castIv;
        @BindView(R.id.TvOneCast)
        TextView rollTv;
        @BindView(R.id.TvTwoCast)
        TextView nameTv;
        CastsCustomAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
