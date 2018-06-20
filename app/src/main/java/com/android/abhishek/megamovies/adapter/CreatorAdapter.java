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
import com.android.abhishek.megamovies.model.TvCreatedByResults;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatorAdapter extends RecyclerView.Adapter<CreatorAdapter.CreatorCustomAdapter>{

    private final List<TvCreatedByResults> tvCreatorResults;
    private final String IMAGE_BASE_URL = EndPoint.IMAGE_BASE_URL;

    public CreatorAdapter(List<TvCreatedByResults> tvCreatorResults) {
        this.tvCreatorResults = tvCreatorResults;
    }

    @NonNull
    @Override
    public CreatorCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.creater_list_item,parent,false);
        return new CreatorCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatorCustomAdapter holder, int position) {
        TvCreatedByResults createdByResults = tvCreatorResults.get(position);
        if(createdByResults == null){
            return;
        }
        Picasso.get()
                .load(IMAGE_BASE_URL+createdByResults.getProfilePath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.creatorIv);
        String name = createdByResults.getName().isEmpty()?"":createdByResults.getName();
        holder.nameTv.setText(name);
    }

    @Override
    public int getItemCount() {
        return tvCreatorResults.size();
    }

    public class CreatorCustomAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.creatorIv) ImageView creatorIv;
        @BindView(R.id.TvOneCreator) TextView nameTv;

        protected CreatorCustomAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
