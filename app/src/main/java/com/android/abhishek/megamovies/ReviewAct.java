package com.android.abhishek.megamovies;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.abhishek.megamovies.adapter.ReviewAdapter;
import com.android.abhishek.megamovies.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAct extends AppCompatActivity {

    @BindView(R.id.reviewRvAtReview) RecyclerView recyclerView;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        final Drawable upArrow = getResources().getDrawable(R.drawable.baseline_arrow_back_white_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Intent intent = getIntent();
        if(intent == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        Review review = getIntent().getParcelableExtra(getResources().getString(R.string.intentPassingOne));
        if(review == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }
        String movieName = getIntent().getStringExtra(getResources().getString(R.string.intentPassingTwo));
        if(movieName != null){
            getSupportActionBar().setTitle(movieName);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ReviewAdapter reviewAdapter = new ReviewAdapter(review.getMovieReviewResults());
        recyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void closeOnError(String message){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(ReviewAct.this,message,Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}
