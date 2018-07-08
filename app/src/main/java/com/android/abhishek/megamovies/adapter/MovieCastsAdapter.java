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
import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieCastsAdapter extends RecyclerView.Adapter<MovieCastsAdapter.CastsCustomAdapter>{

    private final List<MovieCastsResult> movieCastsResults;
    private final Activity activity;

    public MovieCastsAdapter(List<MovieCastsResult> movieCastsResults, Activity activity) {
        this.movieCastsResults = movieCastsResults;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CastsCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_list_item,parent,false);
        return new CastsCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CastsCustomAdapter holder, int position) {

        final MovieCastsResult movieCastsResult = movieCastsResults.get(position);
        if(movieCastsResult == null){
            return;
        }
        final String IMAGE_BASE_URL = EndPoint.IMAGE_BASE_URL;
        Picasso.get()
                .load(IMAGE_BASE_URL +movieCastsResult.getProfilePath())
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .into(holder.castIv);
        String name = movieCastsResult.getName()==null?"":movieCastsResult.getName();
        String character = movieCastsResult.getCharacter()==null?"":movieCastsResult.getCharacter();
        holder.nameTv.setText(name);
        holder.rollTv.setText(character);

        holder.castIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CastProfileAct.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra(activity.getResources().getString(R.string.intentPassingOne),movieCastsResult.getId());
                boolean curve = (holder.getAdapterPosition() % 2 == 0);
                intent.setData(Uri.parse(IMAGE_BASE_URL+movieCastsResult.getProfilePath()));
                intent.putExtra(CastProfileAct.EXTRA_CURVE, curve);
                if(Build.VERSION.SDK_INT>=21){
                    activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, holder.castIv, holder.castIv.getTransitionName()).toBundle());
                }else{
                    activity.startActivity(intent);
                }
            }
        });
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
