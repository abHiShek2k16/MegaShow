package com.android.abhishek.megamovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.Nullable;
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
import com.android.abhishek.megamovies.db.DbExecutor;
import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.ReviewResults;
import com.android.abhishek.megamovies.model.TvCreatedByResults;
import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.model.VideosResults;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;
import com.android.abhishek.megamovies.viewModel.TvDetailAVM;
import com.android.abhishek.megamovies.viewModel.TvDetailDVM;
import com.android.abhishek.megamovies.viewModel.TvVMF;
import com.android.abhishek.megamovies.viewModel.VMF;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvDetailAct extends AppCompatActivity {
    @BindString(R.string.apiKey) String API_KEY;
    @BindString(R.string.imageBaseUrl) String IMAGE_BASE_URL;
    @BindString(R.string.emptyString) String EMPTY;
    @BindString(R.string.infoUnavailable) String DATA_NOT_AVAILABLE;
    @BindString(R.string.videoAppBaseUrl) String VIDEO_APP_BASE_URL;
    @BindString(R.string.videoWebBaseUrl) String VIDEO_WEB_BASE_URL;
    @BindString(R.string.appendQueryTv) String APPEND_QUERY;

    @BindView(R.id.posterImageAtTv) ImageView posterImageIv;
    @BindView(R.id.movieNameAtTv) TextView tvNameTv;
    @BindView(R.id.productionNameAtTv) TextView productionNameTv;
    @BindView(R.id.tvFavBtn) LikeButton likeButton;
    @BindView(R.id.logoIvAtTv) ImageView tvLogoIv;
    @BindView(R.id.lengthTvAtTv) TextView lengthTv;
    @BindView(R.id.ratingTvAtTv) TextView ratingTv;
    @BindView(R.id.totalVoteTvAtTv) TextView totalVoteTv;
    @BindView(R.id.firstDateAtTv) TextView firstEpisodeDateTv;
    @BindView(R.id.lastDateAtTv) TextView lastEpisodeDateTv;
    @BindView(R.id.overviewAtTv) TextView overviewTv;
    @BindView(R.id.trailerRvAtTv) RecyclerView trailerRv;
    @BindView(R.id.creatorRvAtTv) RecyclerView creatorRv;
    @BindView(R.id.reviewRvAtTv) RecyclerView reviewRv;
    @BindView(R.id.readAllReviewAtTv) TextView readAll;
    @BindView(R.id.toolBarAtTv) android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.totalRatingBarAtTv) RatingBar totalRating;
    @BindView(R.id.ratingCountAtTv) TextView ratingCount;
    @BindView(R.id.ratingStrAtTv) TextView ratingStrTv;
    @BindView(R.id.noTSEAtTvCreator) RelativeLayout creatorError;
    @BindView(R.id.noTSEAtTvReview) RelativeLayout reviewError;
    @BindView(R.id.noTSEAtTvTrailer) RelativeLayout trailerError;

    private String posterImageUrl = "";
    private String tvLogo = "";
    private String productionName;
    private String length;
    private String tvName;
    private String rating;
    private String totalVote;
    private String firstEpisode;
    private String lastEpisode;
    private String overView;
    private List<VideosResults> videos;
    private List<ReviewResults> reviews;
    private List<TvCreatedByResults> tvCreator;

    private String tvId;
    private TvDetail tvDetail;
    private boolean isExist = false;

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

        isExistInDb();

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
                addToDb();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                removeFromDb();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadFromApi(){
        TvDetailAVM tvDetailAVM = ViewModelProviders.of(this).get(TvDetailAVM.class);
        tvDetailAVM.getTvDetailFromApi(tvId,API_KEY,APPEND_QUERY).observe(this, new Observer<TvDetail>() {
            @Override
            public void onChanged(@Nullable TvDetail tD) {
                tvDetail = tD;
                videos = tD.getVideos().getVideosResults();
                reviews = tD.getReview().getMovieReviewResults();
                tvCreator = tD.getTvCreatedByResults();
                productionName = "by "+tD.getProductionCompanies().get(0).getName();
                tvCreator = tvDetail.getTvCreatedByResults();
                videos = tvDetail.getVideos().getVideosResults();
                reviews = tvDetail.getReview().getMovieReviewResults();
                setVariable();
                setView();
            }
        });
    }

    private void loadFromDb(){
        ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        VMF vmf = new VMF(showDatabase,tvId);
        final TvDetailDVM tvDetailVM = ViewModelProviders.of(this,vmf).get(TvDetailDVM.class);
        tvDetailVM.getProductionName().observe(this, new Observer<ProductionCompany>() {
            @Override
            public void onChanged(@Nullable ProductionCompany productionCompany) {
                tvDetailVM.getProductionName().removeObserver(this);
                if(productionCompany != null){
                    productionName = productionCompany.getName();
                }
            }
        });
        tvDetailVM.getTvCreator().observe(this, new Observer<List<TvCreatedByResults>>() {
            @Override
            public void onChanged(@Nullable List<TvCreatedByResults> tvCreatedByResult) {
                tvDetailVM.getTvCreator().removeObserver(this);
                tvCreator = tvCreatedByResult;
            }
        });
        tvDetailVM.getVideos().observe(this, new Observer<List<VideosResults>>() {
            @Override
            public void onChanged(@Nullable List<VideosResults> videosResults) {
                tvDetailVM.getVideos().removeObserver(this);
                videos = videosResults;
                setVariable();
                setView();
            }
        });
    }

    private void setVariable(){
        posterImageUrl = IMAGE_BASE_URL + tvDetail.getBackdropPath();
        tvLogo = IMAGE_BASE_URL + tvDetail.getPosterPath();
        //  length = String.valueOf(tvDetail.getRunTime().get(0));
        tvName = tvDetail.getName()==null?DATA_NOT_AVAILABLE:tvDetail.getName();
        rating = tvDetail.getVoteAvg()==null?DATA_NOT_AVAILABLE:tvDetail.getVoteAvg();
        if(rating.length()>3){
            rating = rating.substring(0,3);
        }
        totalVote = tvDetail.getVoteCount()==null?DATA_NOT_AVAILABLE:tvDetail.getVoteCount();
        firstEpisode = tvDetail.getFirstAirDate()==null?DATA_NOT_AVAILABLE:tvDetail.getFirstAirDate();
        lastEpisode = tvDetail.getLastAirDate()==null?DATA_NOT_AVAILABLE:tvDetail.getLastAirDate();
        overView = tvDetail.getOverview()==null?DATA_NOT_AVAILABLE:tvDetail.getOverview();
    }

    private void setView(){
        if(networkStatus()){
            Picasso.get()
                    .load(posterImageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(posterImageIv);
            Picasso.get()
                    .load(tvLogo)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(tvLogoIv);
        }else{
            Picasso.get()
                    .load(posterImageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(posterImageIv);
            Picasso.get()
                    .load(tvLogo)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(tvLogoIv);
        }

        tvNameTv.setText(tvName);
        productionNameTv.setText(productionName);
      //  lengthTv.setText(length+" min");
        ratingTv.setText(rating+" / 10");
        totalVoteTv.setText(totalVote);
        firstEpisodeDateTv.setText(changeFormatOfDate(firstEpisode));
        lastEpisodeDateTv.setText(changeFormatOfDate(lastEpisode));
        overviewTv.setText(overView);
        ratingCount.setText(totalVote);
        ratingStrTv.setText(rating);
        totalRating.setRating(Float.parseFloat(rating));

        trailerRv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        if(videos == null || videos.size() == 0){
            trailerError.setVisibility(View.VISIBLE);
        }else{
            TrailerAdapter trailerAdapter = new TrailerAdapter(videos);
            trailerRv.setAdapter(trailerAdapter);
        }

        creatorRv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        if(tvCreator == null || tvCreator.size() == 0){
            creatorError.setVisibility(View.VISIBLE);
        }else{
            CreatorAdapter creatorAdapter = new CreatorAdapter(tvCreator);
            creatorRv.setAdapter(creatorAdapter);
        }

        reviewRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ReviewAdapter reviewAdapter;
        if(reviews == null || reviews.size()==0){
            reviewError.setVisibility(View.VISIBLE);
            return;
        }else if(reviews.size()>3) {
            reviewError.setVisibility(View.GONE);
            List<ReviewResults> movieReviewResultsArrayList = reviews.subList(0,3);
            readAll.setVisibility(View.VISIBLE);
            reviewAdapter = new ReviewAdapter(movieReviewResultsArrayList);
            reviewRv.setAdapter(reviewAdapter);
        }else{
            reviewError.setVisibility(View.GONE);
            reviewAdapter = new ReviewAdapter(reviews);
            reviewRv.setAdapter(reviewAdapter);
        }
    }

    private void addToDb(){
        try{
            final ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
            DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
                @Override
                public void run() {
                    showDatabase.showDao().addTvDetail(tvDetail);
                    for(int i=0;i<tvCreator.size();i++){
                        showDatabase.showDao().addTvCreator(new TvCreatedByResults(tvCreator.get(i).getId(),tvCreator.get(i).getName(),tvCreator.get(i).getProfilePath(),tvId,tvCreator.get(i).getKey()));
                    }
                    try{
                        showDatabase.showDao().addProductionCompany(new ProductionCompany(productionName,tvId));
                    }catch (Exception e){}
                    for(int i=0;i<videos.size();i++){
                        showDatabase.showDao().addVideos(new VideosResults(tvId,videos.get(i).getVideoKey()));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            likeButton.setLiked(true);
                        }
                    });
                }
            });
        }catch (Exception e){}
    }

    private void removeFromDb(){
        try{
            final ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
            DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
                @Override
                public void run() {
                    showDatabase.showDao().removeTvDetail(tvDetail);
                    for(int i=0;i<tvCreator.size();i++){
                        showDatabase.showDao().removeTvCreator(new TvCreatedByResults(tvCreator.get(i).getId(),tvCreator.get(i).getName(),tvCreator.get(i).getProfilePath(),tvId,tvCreator.get(i).getKey()));
                    }
                    try{
                        showDatabase.showDao().removeProductionCompany(new ProductionCompany(productionName,tvId));
                    }catch (Exception e){}
                    for(int i=0;i<videos.size();i++){
                        showDatabase.showDao().removeVideos(new VideosResults(tvId,videos.get(i).getVideoKey()));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            likeButton.setLiked(false);
                        }
                    });
                }
            });
        }catch (Exception e){}
    }

    private boolean isExistInDb(){
        ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        TvVMF vmf = new TvVMF(showDatabase,tvId);
        final TvDetailDVM tvDetailVM = ViewModelProviders.of(this,vmf).get(TvDetailDVM.class);
        tvDetailVM.getTvDetail().observe(this, new Observer<TvDetail>() {
            @Override
            public void onChanged(@Nullable TvDetail tD) {
                if(tD == null){
                    likeButton.setLiked(false);
                    isExist = false;
                }else{
                    tvDetail = tD;
                    likeButton.setLiked(true);
                    isExist = true;
                }
                if(networkStatus()){
                    loadFromApi();
                }else if(!networkStatus() && isExist){
                    loadFromDb();
                }else if(!networkStatus() && !isExist){
                    closeOnError(getResources().getString(R.string.netproblem));
                }
            }
        });
        return isExist;
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

    private boolean networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected())){
            return false;
        }
        return true;
    }

    private void closeOnError(String message){
        Toast.makeText(TvDetailAct.this,message,Toast.LENGTH_SHORT).show();
        finish();
    }
}
