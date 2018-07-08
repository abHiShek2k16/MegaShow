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
import android.widget.TextView;

import com.android.abhishek.megamovies.CastProfileAct;
import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.model.EndPoint;
import com.android.abhishek.megamovies.model.TvCreatedByResults;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatorAdapter extends RecyclerView.Adapter<CreatorAdapter.CreatorCustomAdapter>{

    private final List<TvCreatedByResults> tvCreatorResults;
    private final Activity activity;

    public CreatorAdapter(List<TvCreatedByResults> tvCreatorResults,Activity activity) {
        this.tvCreatorResults = tvCreatorResults;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CreatorCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.creater_list_item,parent,false);
        return new CreatorCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreatorCustomAdapter holder,int position) {

        TvCreatedByResults createdByResults = tvCreatorResults.get(position);
        if(createdByResults == null){
            return;
        }
        final String IMAGE_BASE_URL = EndPoint.IMAGE_BASE_URL;
        Picasso.get()
                .load(IMAGE_BASE_URL +createdByResults.getProfilePath())
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .into(holder.creatorIv);
        String name = createdByResults.getName().isEmpty()?"":createdByResults.getName();
        holder.nameTv.setText(name);

        holder.creatorIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CastProfileAct.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra(activity.getResources().getString(R.string.intentPassingOne),tvCreatorResults.get(holder.getAdapterPosition()).getId());
                boolean curve = (holder.getAdapterPosition() % 2 == 0);
                intent.setData(Uri.parse(IMAGE_BASE_URL+tvCreatorResults.get(holder.getAdapterPosition()).getProfilePath()));
                intent.putExtra(CastProfileAct.EXTRA_CURVE, curve);
                if(Build.VERSION.SDK_INT>=21){
                    activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, holder.creatorIv, holder.creatorIv.getTransitionName()).toBundle());
                }else{
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvCreatorResults.size();
    }

    public class CreatorCustomAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.creatorIv) ImageView creatorIv;
        @BindView(R.id.TvOneCreator) TextView nameTv;

        CreatorCustomAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
