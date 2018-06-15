package com.android.abhishek.megamovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.abhishek.megamovies.adapter.CreatorAdapter;
import com.android.abhishek.megamovies.adapter.ReviewAdapter;
import com.android.abhishek.megamovies.adapter.TrailerAdapter;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.ReviewResults;
import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvDetailAct extends AppCompatActivity {

    private String tvId;
    private TvDetail tvDetail;

    @BindString(R.string.apiKey)
    String API_KEY;
    @BindString(R.string.imageBaseUrl)
    String IMAGE_BASE_URL;
    @BindString(R.string.emptyString)
    String EMPTY;
    @BindString(R.string.infoUnavailable)
    String DATA_NOT_AVAILABLE;
    @BindString(R.string.videoAppBaseUrl)
    String VIDEO_APP_BASE_URL;
    @BindString(R.string.videoWebBaseUrl)
    String VIDEO_WEB_BASE_URL;
    @BindString(R.string.appendQueryTv)
    String APPEND_QUERY;

    @BindView(R.id.posterImageAtTv)
    ImageView posterImageIv;
    @BindView(R.id.movieNameAtTv)
    TextView movieNameTv;
    @BindView(R.id.productionNameAtTv)
    TextView productionNameTv;
    @BindView(R.id.tvFavBtn)
    LikeButton likeButton;
    @BindView(R.id.logoIvAtTv)
    ImageView movieLogoIv;
    @BindView(R.id.lengthTvAtTv)
    TextView lengthTv;
    @BindView(R.id.ratingTvAtTv)
    TextView ratingTv;
    @BindView(R.id.totalVoteTvAtTv)
    TextView totalVoteTv;
    @BindView(R.id.firstDateAtTv)
    TextView firstEpisodeDateTv;
    @BindView(R.id.lastDateAtTv)
    TextView lastEpisodeDateTv;
    @BindView(R.id.overviewAtTv)
    TextView overviewTv;
    @BindView(R.id.trailerRvAtTv)
    RecyclerView trailerRv;
    @BindView(R.id.creatorRvAtTv)
    RecyclerView creatorRv;
    @BindView(R.id.reviewRvAtTv)
    RecyclerView reviewRv;
    @BindView(R.id.readAllReviewAtTv)
    TextView readAll;
    @BindView(R.id.toolBarAtTv)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.totalRatingBarAtTv)
    RatingBar totalRating;
    @BindView(R.id.ratingCountAtTv)
    TextView ratingCount;
    @BindView(R.id.ratingStrAtTv)
    TextView ratingStrTv;
    @BindView(R.id.noTSEAtTvCreator)
    RelativeLayout creatorError;
    @BindView(R.id.noTSEAtTvReview)
    RelativeLayout reviewError;
    @BindView(R.id.noTSEAtTvTrailer)
    RelativeLayout trailerError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

        ButterKnife.bind(this);

        final Drawable upArrow = getResources().getDrawable(R.drawable.baseline_arrow_back_white_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Intent intent = getIntent();
        if(intent == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        tvId = intent.getStringExtra(getResources().getString(R.string.intentPassingOne));
        if(tvId==null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        loadTvDetail();

        trailerRv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, trailerRv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String key = tvDetail.getVideos().getVideosResults().get(position).getVideoKey();
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VIDEO_APP_BASE_URL+key));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(VIDEO_WEB_BASE_URL+key));
                        try {
                            startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            startActivity(webIntent);
                        }
                    }
                })
        );

        creatorRv.addOnItemTouchListener(new RecyclerItemClickListener(this, creatorRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = tvDetail.getTvCreatedByResults().get(position).getId();
                Intent intent = new Intent(TvDetailAct.this,CastProfileAct.class);
                intent.putExtra(getResources().getString(R.string.intentPassingOne),id);
                startActivity(intent);
            }
        }));

        readAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TvDetailAct.this,ReviewAct.class);
                intent.putExtra(getResources().getString(R.string.intentPassingOne),tvDetail.getReview());
                startActivity(intent);
            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadTvDetail(){
        ApiInterface apiInterface = BuildUrl.getRetrofit(this).create(ApiInterface.class);
        retrofit2.Call<TvDetail> tvDetailCall = apiInterface.getTvDetail(tvId,API_KEY,APPEND_QUERY);
        tvDetailCall.enqueue(new Callback<TvDetail>() {
            @Override
            public void onResponse(Call<TvDetail> call, Response<TvDetail> response) {
                tvDetail = response.body();
                setTvDetail();
            }

            @Override
            public void onFailure(Call<TvDetail> call, Throwable t) {
                closeOnError(getResources().getString(R.string.netproblem));
            }
        });
    }

    private void setTvDetail(){
        if(tvDetail == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        String posterImageUrl = tvDetail.getBackdropPath();
        String movieLogo = tvDetail.getPosterPath();

        String productionName = EMPTY;
        ArrayList<ProductionCompany> movieProductionCompanies = tvDetail.getProductionCompanies();
        for(int i=0;i<movieProductionCompanies.size();i++){
            if(movieProductionCompanies.get(i).getLogoPath()!=null && movieProductionCompanies.get(i).getName()!=null){
                productionName = "by "+tvDetail.getProductionCompanies().get(i).getName();
                break;
            }
        }

        ArrayList<Integer> runTime = tvDetail.getRunTime();
        String length = "";
        for(int i=0;i<runTime.size();i++){
            if(i==runTime.size()-1){
                length += String.valueOf(runTime.get(i))+" min";
            }else{
                length += String.valueOf(runTime.get(i))+" min,";
            }
        }

        String movieName = tvDetail.getName()==null?DATA_NOT_AVAILABLE:tvDetail.getName();
        String rating = tvDetail.getVoteAvg()==null?DATA_NOT_AVAILABLE:tvDetail.getVoteAvg();
        if(rating.length()>3){
            rating = rating.substring(0,3);
        }
        String totalVote = tvDetail.getVoteCount()==null?DATA_NOT_AVAILABLE:tvDetail.getVoteCount();
        String firstEpisode = tvDetail.getFirstAirDate()==null?DATA_NOT_AVAILABLE:tvDetail.getFirstAirDate();
        String lastEpisode = tvDetail.getLastAirDate()==null?DATA_NOT_AVAILABLE:tvDetail.getLastAirDate();
        String overView = tvDetail.getOverview()==null?DATA_NOT_AVAILABLE:tvDetail.getOverview();

        Picasso.get()
                .load(IMAGE_BASE_URL+posterImageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(posterImageIv);
        Picasso.get()
                .load(IMAGE_BASE_URL+movieLogo)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(movieLogoIv);

        movieNameTv.setText(movieName);
        productionNameTv.setText(productionName);
        lengthTv.setText(length);
        ratingTv.setText(rating+" / 10");
        totalVoteTv.setText(totalVote);
        firstEpisodeDateTv.setText(changeFormatOfDate(firstEpisode));
        lastEpisodeDateTv.setText(changeFormatOfDate(lastEpisode));
        overviewTv.setText(overView);

        ratingCount.setText(totalVote);
        ratingStrTv.setText(rating);
        totalRating.setRating(Float.parseFloat(rating));

        trailerRv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        TrailerAdapter trailerAdapter = new TrailerAdapter(tvDetail.getVideos().getVideosResults());
        if(tvDetail.getVideos().getVideosResults().size() == 0){
            trailerError.setVisibility(View.VISIBLE);
        }
        trailerRv.setAdapter(trailerAdapter);

        creatorRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        CreatorAdapter creatorAdapter = new CreatorAdapter(tvDetail.getTvCreatedByResults());
        if(tvDetail.getTvCreatedByResults().size() == 0){
            creatorError.setVisibility(View.VISIBLE);
        }
        creatorRv.setAdapter(creatorAdapter);

        reviewRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ReviewAdapter reviewAdapter;
        ArrayList<ReviewResults> movieReviewResults = tvDetail.getReview().getMovieReviewResults();
        if(movieReviewResults.size()>3) {
            ArrayList<ReviewResults> movieReviewResultsArrayList = new ArrayList<>();
            movieReviewResultsArrayList.add(movieReviewResults.get(0));
            movieReviewResultsArrayList.add(movieReviewResults.get(1));
            movieReviewResultsArrayList.add(movieReviewResults.get(2));
            readAll.setVisibility(View.VISIBLE);
            reviewAdapter = new ReviewAdapter(movieReviewResultsArrayList);
        }else if(movieReviewResults.size() == 0){
            reviewError.setVisibility(View.VISIBLE);
            return;
        }else {
            reviewAdapter = new ReviewAdapter(movieReviewResults);
        }
        reviewRv.setAdapter(reviewAdapter);
    }

    private String changeFormatOfDate(String releaseDate){
        try{
            String day = releaseDate.substring(8,10);
            int month;
            try{
                month = Integer.parseInt(releaseDate.substring(5,7));
            }catch (ClassCastException e){
                month = 0;
            }
            String year = releaseDate.substring(0,4);
            String changedFormatDate = day;
            switch (month){
                case 1:
                    changedFormatDate += getResources().getString(R.string.jan);
                    break;
                case 2:
                    changedFormatDate += getResources().getString(R.string.Feb);
                    break;
                case 3:
                    changedFormatDate += getResources().getString(R.string.Mar);
                    break;
                case 4:
                    changedFormatDate += getResources().getString(R.string.April);
                    break;
                case 5:
                    changedFormatDate += getResources().getString(R.string.May);
                    break;
                case 6:
                    changedFormatDate += getResources().getString(R.string.June);
                    break;
                case 7:
                    changedFormatDate += getResources().getString(R.string.july);
                    break;
                case 8:
                    changedFormatDate += getResources().getString(R.string.Aug);
                    break;
                case 9:
                    changedFormatDate += getResources().getString(R.string.Sept);
                    break;
                case 10:
                    changedFormatDate += getResources().getString(R.string.Oct);
                    break;
                case 11:
                    changedFormatDate += getResources().getString(R.string.Nov);
                    break;
                case 12:
                    changedFormatDate += getResources().getString(R.string.Dec);
                    break;
            }

            changedFormatDate += " "+year;
            return changedFormatDate;
        }catch (Exception e){
            return releaseDate;
        }
    }

    private void closeOnError(String message){
        Toast.makeText(TvDetailAct.this,message,Toast.LENGTH_SHORT).show();
        finish();
    }
}
