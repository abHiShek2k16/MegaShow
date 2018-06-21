package com.android.abhishek.megamovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.abhishek.megamovies.R;
import com.android.abhishek.megamovies.model.ReviewResults;
import com.cooltechworks.views.WhatsAppTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewCustomAdapter>{

    private final List<ReviewResults> reviewResults;
    private boolean flag = false;       //  for the expand and collapse state of review

    public ReviewAdapter(List<ReviewResults> reviewResults) {
        this.reviewResults = reviewResults;
    }

    @NonNull
    @Override
    public ReviewCustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item,parent,false);
        return new ReviewCustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewCustomAdapter holder, int position) {
        ReviewResults reviewResults = this.reviewResults.get(position);
        if(reviewResults == null){
            return;
        }
        String name = reviewResults.getAuthor()==null?"":reviewResults.getAuthor();
        if(!name.isEmpty()){
            name = name.substring(0,1).toUpperCase() + name.substring(1);
            holder.authorNameTv.setText(name);
            holder.circleTv.setText(name.substring(0, 1));
        }
        final String review = reviewResults.getContent();
        //  Cutting the review if it's length more than 100 char
        if(!review.isEmpty()){
            char[] array = review.toCharArray();
            if(array.length<=100){
                holder.reviewTv.setText(review);
            }else{
                String reviewStr = review.substring(0,100)+" ...";
                holder.reviewTv.setText(reviewStr);
            }
        }

        holder.reviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                char[] array = review.toCharArray();
                if(array.length>100){
                    if(flag){
                        String reviewStr = review.substring(0,100)+" ...";
                        holder.reviewTv.setText(reviewStr);
                        flag = false;
                    }else {
                        holder.reviewTv.setText(review);
                        flag = true;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewResults.size();
    }

    public class ReviewCustomAdapter extends RecyclerView.ViewHolder{
        @BindView(R.id.circleTextTv)
        TextView circleTv;
        @BindView(R.id.authorNameTv)
        TextView authorNameTv;
        @BindView(R.id.reviewTv)
        WhatsAppTextView reviewTv;
        ReviewCustomAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
