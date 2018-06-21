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
import com.android.abhishek.megamovies.viewModel.TvDetailApiVM;
import com.android.abhishek.megamovies.viewModel.TvDetailDbVM;
import com.android.abhishek.megamovies.viewModel.TvViewModelFactory;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TvDetailAct extends AppCompatActivity {
    //  Xml View
    @BindView(R.id.toolBarAtTv) android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.posterImageAtTv) ImageView posterImageIv;
    @BindView(R.id.logoIvAtTv) ImageView tvLogoIv;
    @BindView(R.id.movieNameAtTv) TextView tvNameTv;
    @BindView(R.id.productionNameAtTv) TextView productionNameTv;
    @BindView(R.id.lengthTvAtTv) TextView lengthTv;
    @BindView(R.id.ratingTvAtTv) TextView ratingTv;
    @BindView(R.id.totalVoteTvAtTv) TextView totalVoteTv;
    @BindView(R.id.firstDateAtTv) TextView firstEpisodeDateTv;
    @BindView(R.id.lastDateAtTv) TextView lastEpisodeDateTv;
    @BindView(R.id.overviewAtTv) TextView overviewTv;
    @BindView(R.id.readAllReviewAtTv) TextView readAll;
    @BindView(R.id.ratingCountAtTv) TextView ratingCount;
    @BindView(R.id.ratingStrAtTv) TextView ratingStrTv;
    @BindView(R.id.tvFavBtn) LikeButton likeButton;
    @BindView(R.id.totalRatingBarAtTv) RatingBar totalRating;
    @BindView(R.id.trailerRvAtTv) RecyclerView trailerRv;
    @BindView(R.id.creatorRvAtTv) RecyclerView creatorRv;
    @BindView(R.id.reviewRvAtTv) RecyclerView reviewRv;
    @BindView(R.id.noTSEAtTvCreator) RelativeLayout creatorError;
    @BindView(R.id.noTSEAtTvReview) RelativeLayout reviewError;
    @BindView(R.id.noTSEAtTvTrailer) RelativeLayout trailerError;

    //  Constant String
    @BindString(R.string.apiKey) String API_KEY;
    @BindString(R.string.imageBaseUrl) String IMAGE_BASE_URL;
    @BindString(R.string.infoUnavailable) String DATA_NOT_AVAILABLE;
    @BindString(R.string.videoAppBaseUrl) String VIDEO_APP_BASE_URL;
    @BindString(R.string.videoWebBaseUrl) String VIDEO_WEB_BASE_URL;
    @BindString(R.string.appendQueryTv) String APPEND_QUERY;

    //  Temp Variable
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
    private Toast toast;

    private String tvId;
    private TvDetail tvDetail;
    private boolean isExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

        ButterKnife.bind(this);

        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
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
                intent.putExtra(getResources().getString(R.string.intentPassingTwo),tvDetail.getName());
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
        TvDetailApiVM tvDetailApiVM = ViewModelProviders.of(this).get(TvDetailApiVM.class);
        tvDetailApiVM.getTvDetailFromApi(tvId,API_KEY,APPEND_QUERY).observe(this, new Observer<TvDetail>() {
            @Override
            public void onChanged(@Nullable TvDetail tD) {
                tvDetail = tD;
                if(tD != null){
                    videos = tD.getVideos()!=null?tD.getVideos().getVideosResults():null;
                    reviews = tD.getReview()!=null?tD.getReview().getMovieReviewResults():null;
                    tvCreator = tD.getTvCreatedByResults();
                    productionName = "by "+(tD.getProductionCompanies()!=null&&tD.getProductionCompanies().size()>0?tD.getProductionCompanies().get(0).getName():DATA_NOT_AVAILABLE);
                    length = String.valueOf(tD.getRunTime().get(0))!=null&&tD.getRunTime().size()>0?String.valueOf(tD.getRunTime().get(0))+" min":DATA_NOT_AVAILABLE;
                    setVariable();
                    setView();
                }else{
                    closeOnError(getResources().getString(R.string.somethingWrong));
                }
            }
        });
    }

    private void loadFromDb(){
        final ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
            @Override
            public void run() {
                productionName = showDatabase.showDao().getProductionCompany(tvId)==null?"":showDatabase.showDao().getProductionCompany(tvId).getName();
                videos = showDatabase.showDao().getVideos(tvId);
                tvCreator = showDatabase.showDao().getTvCreator(tvId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setVariable();
                        setView();
                    }
                });
            }
        });
    }

    private void setVariable(){
        posterImageUrl = IMAGE_BASE_URL + tvDetail.getBackdropPath();
        tvLogo = IMAGE_BASE_URL + tvDetail.getPosterPath();
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
                    .placeholder(R.drawable.loading_place_holder)
                    .error(R.drawable.error_place_holder)
                    .into(posterImageIv);
            Picasso.get()
                    .load(tvLogo)
                    .placeholder(R.drawable.loading_place_holder)
                    .error(R.drawable.error_place_holder)
                    .into(tvLogoIv);
        }else{
            Picasso.get()
                    .load(posterImageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.loading_place_holder)
                    .error(R.drawable.error_place_holder)
                    .into(posterImageIv);
            Picasso.get()
                    .load(tvLogo)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.loading_place_holder)
                    .error(R.drawable.error_place_holder)
                    .into(tvLogoIv);
        }

        tvNameTv.setText(tvName);
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
        likeButton.setEnabled(true);
    }

    private void addToDb(){
        final ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
            @Override
            public void run() {
                if(tvDetail != null){
                    showDatabase.showDao().addTvDetail(tvDetail);
                }

                if(productionName != null && tvId != null){
                    showDatabase.showDao().addProductionCompany(new ProductionCompany(productionName,tvId));
                }

                if(tvCreator != null){
                    for(int i=0;i<tvCreator.size();i++){
                        if(validateCreator(tvCreator.get(i))){
                            showDatabase.showDao().addTvCreator(new TvCreatedByResults(tvCreator.get(i).getId(),tvCreator.get(i).getName(),tvCreator.get(i).getProfilePath(),tvId,tvCreator.get(i).getKey()));
                        }
                    }
                }

                if(videos != null){
                    for(int i=0;i<videos.size();i++){
                        if(validateVideos(videos.get(i))){
                            showDatabase.showDao().addVideos(new VideosResults(tvId,videos.get(i).getVideoKey()));
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        likeButton.setLiked(true);
                    }
                });
            }
        });
    }

    private void removeFromDb(){
        final ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
            @Override
            public void run() {
                if(tvDetail != null){
                    showDatabase.showDao().removeTvDetail(tvDetail);
                }

                if(productionName != null && tvId != null){
                    showDatabase.showDao().removeProductionCompany(new ProductionCompany(productionName,tvId));
                }

                if(tvCreator != null){
                    for(int i=0;i<tvCreator.size();i++){
                        if(validateCreator(tvCreator.get(i))){
                            showDatabase.showDao().removeTvCreator(new TvCreatedByResults(tvCreator.get(i).getId(),tvCreator.get(i).getName(),tvCreator.get(i).getProfilePath(),tvId,tvCreator.get(i).getKey()));
                        }
                    }
                }

                if(videos != null){
                    for(int i=0;i<videos.size();i++){
                        if(validateVideos(videos.get(i))){
                            showDatabase.showDao().removeVideos(new VideosResults(tvId,videos.get(i).getVideoKey()));
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        likeButton.setLiked(false);
                    }
                });
            }
        });
    }

    private void isExistInDb(){
        ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        TvViewModelFactory vmf = new TvViewModelFactory(showDatabase,tvId);
        final TvDetailDbVM tvDetailVM = ViewModelProviders.of(this,vmf).get(TvDetailDbVM.class);
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
                }else if(!networkStatus() && !isExist && tvDetail == null){
                    closeOnError(getResources().getString(R.string.netProblem));
                }
            }
        });
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
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private boolean validateCreator(TvCreatedByResults tvCreatedByResult){
        return tvCreatedByResult.getName() != null && tvCreatedByResult.getProfilePath() != null && tvCreatedByResult.getId() != null && tvId != null;
    }

    private boolean validateVideos(VideosResults videos){
        return tvId != null && videos.getVideoKey() != null;
    }

    private void closeOnError(String message){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(TvDetailAct.this,message,Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}
