package com.android.abhishek.megamovies;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.abhishek.megamovies.adapter.MovieCastsAdapter;
import com.android.abhishek.megamovies.adapter.ReviewAdapter;
import com.android.abhishek.megamovies.adapter.TrailerAdapter;
import com.android.abhishek.megamovies.db.DbExecutor;
import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.ReviewResults;
import com.android.abhishek.megamovies.model.VideosResults;
import com.android.abhishek.megamovies.viewModel.MovieDetailDbVM;
import com.android.abhishek.megamovies.viewModel.MovieDetailApiVM;
import com.android.abhishek.megamovies.viewModel.MovieModelFactory;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailAct extends AppCompatActivity {
    //  xml view
    @BindView(R.id.toolBarATMovieDetail) android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.posterImageAtMovieDetail) ImageView posterImageIv;
    @BindView(R.id.logoIv) ImageView movieLogoIv;
    @BindView(R.id.shareBtnAtMovieDetail) ImageView share;
    @BindView(R.id.movieNameAtMovieDetail) TextView movieNameTv;
    @BindView(R.id.productionNameAtMovie) TextView productionNameTv;
    @BindView(R.id.lengthTvAtMovieDetail) TextView lengthTv;
    @BindView(R.id.ratingTvAtMovieDetail) TextView ratingTv;
    @BindView(R.id.totalVoteTvAtMovieDetail) TextView totalVoteTv;
    @BindView(R.id.dateTvAtMovieDetail) TextView releaseDateTv;
    @BindView(R.id.overviewAtMovieDetail) TextView overviewTv;
    @BindView(R.id.readAllReviewAtMovieDetail) TextView readAll;
    @BindView(R.id.ratingCountAtMv) TextView ratingCount;
    @BindView(R.id.ratingStrAtMv) TextView ratingStrTv;
    @BindView(R.id.movieFavBtn) LikeButton likeButton;
    @BindView(R.id.totalRatingBarAtMv) RatingBar totalRating;
    @BindView(R.id.trailerRv) RecyclerView trailerRv;
    @BindView(R.id.castRvAtMovieDetail) RecyclerView castRv;
    @BindView(R.id.reviewRvAtMovieDetail) RecyclerView reviewRv;
    @BindView(R.id.noTSEAtMvCasts) RelativeLayout castsError;
    @BindView(R.id.noTSEAtMvReview) RelativeLayout reviewError;
    @BindView(R.id.noTSEAtMvTrailer) RelativeLayout trailerError;
    @BindView(R.id.detailCardAtMv) CardView cardView;

    //  constant string
    @BindString(R.string.apiKey) String API_KEY;
    @BindString(R.string.imageBaseUrl) String IMAGE_BASE_URL;
    @BindString(R.string.infoUnavailable) String DATA_NOT_AVAILABLE;
    @BindString(R.string.videoAppBaseUrl) String VIDEO_APP_BASE_URL;
    @BindString(R.string.videoWebBaseUrl) String VIDEO_WEB_BASE_URL;
    @BindString(R.string.appendQueryMv) String APPEND_QUERY;

    //  temporary variable
    private String movieId;
    private String posterImageUrl = "";
    private String productionName;
    private String length;
    private String movieName;
    private String rating;
    private String totalVote;
    private String releaseDate;
    private String overView;
    private List<VideosResults> videos;
    private List<ReviewResults> reviews;
    private List<MovieCastsResult> movieCasts;
    private Toast toast;

    private MovieDetail movieDetail = new MovieDetail();
    private boolean isExist = false;
    public static final float LARGE_SCALE = 1.1f;
    private boolean symmetric = true;
    private boolean small = true;

    public static final String EXTRA_CURVE = "EXTRA_CURVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        share.setEnabled(false);

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

        movieId = intent.getStringExtra(getResources().getString(R.string.intentPassingOne));
        if(movieId == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        boolean curve = getIntent().getBooleanExtra(EXTRA_CURVE, false);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(curve ? R.transition.curve : R.transition.move));
        }

        Picasso.get()
                .load(getIntent().getData())
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .into(movieLogoIv);

        isExistInDb();

        trailerRv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, trailerRv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String key = videos.get(position).getVideoKey();
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VIDEO_APP_BASE_URL+key));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(VIDEO_WEB_BASE_URL+key));
                        try {
                            startActivity(appIntent);
                        } catch (Exception e) {
                            startActivity(webIntent);
                        }
                    }
                })
        );

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

        readAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailAct.this,ReviewAct.class);
                intent.putExtra(getResources().getString(R.string.intentPassingOne),movieDetail.getMovieReview());
                intent.putExtra(getResources().getString(R.string.intentPassingTwo),movieDetail.getTitle());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadFromDb(){
        final ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
            @Override
            public void run() {
               productionName = showDatabase.showDao().getProductionCompany(movieId)==null?"":showDatabase.showDao().getProductionCompany(movieId).getName();
               videos = showDatabase.showDao().getVideos(movieId);
               movieCasts = showDatabase.showDao().getMovieCast(movieId);

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

    private void loadFromApi(){
        MovieDetailApiVM movieDetailVM = ViewModelProviders.of(this).get(MovieDetailApiVM.class);
        movieDetailVM.getMovieDetailFromApi(movieId,API_KEY,APPEND_QUERY).observe(this, new Observer<MovieDetail>() {
            @Override
            public void onChanged(@Nullable MovieDetail mD) {
                movieDetail = mD;
                if(movieDetail != null){
                    videos = movieDetail.getMovieVideos()!=null?movieDetail.getMovieVideos().getVideosResults():null;
                    movieCasts = movieDetail.getMovieCasts()!=null?movieDetail.getMovieCasts().getMovieCastsResults():null;
                    reviews = movieDetail.getMovieReview()!=null?movieDetail.getMovieReview().getMovieReviewResults():null;
                    productionName = "by "+(movieDetail.getProductionCompanies()!=null&&movieDetail.getProductionCompanies().size()!=0?movieDetail.getProductionCompanies().get(0).getName():DATA_NOT_AVAILABLE);
                    setVariable();
                    setView();
                }else{
                    closeOnError(getResources().getString(R.string.somethingWrong));
                }
            }
        });
    }

    private void setVariable() {
        posterImageUrl = IMAGE_BASE_URL + movieDetail.getBackdropPath();
        try {
            length = movieDetail.getRuntime().isEmpty() ? DATA_NOT_AVAILABLE : String.valueOf(Integer.parseInt(movieDetail.getRuntime()) / 60) + "h " + String.valueOf(Integer.parseInt(movieDetail.getRuntime()) % 60) + "min";
        } catch (Exception e) {
            length = DATA_NOT_AVAILABLE;
        }
        movieName = movieDetail.getTitle() == null ? DATA_NOT_AVAILABLE : movieDetail.getTitle();
        rating = movieDetail.getVoteAvg() == null ? DATA_NOT_AVAILABLE : movieDetail.getVoteAvg();
        if (rating.length() > 3) {
            rating = rating.substring(0, 3);
        }
        totalVote = movieDetail.getVoteCount() == null ? DATA_NOT_AVAILABLE : movieDetail.getVoteCount();
        releaseDate = movieDetail.getReleaseDate() == null ? DATA_NOT_AVAILABLE : movieDetail.getReleaseDate();
        overView = movieDetail.getOverview() == null ? DATA_NOT_AVAILABLE : movieDetail.getOverview();
    }

    private void setView(){
        Picasso.get()
                .load(posterImageUrl)
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .into(posterImageIv);

        movieNameTv.setText(movieName);
        productionNameTv.setText(productionName);
        lengthTv.setText(length);
        ratingTv.setText(rating+" / 10");
        totalVoteTv.setText(totalVote);
        releaseDateTv.setText(changeFormatOfDate(releaseDate));
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

        castRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        if(movieCasts == null || movieCasts.size() == 0){
            castsError.setVisibility(View.VISIBLE);
        }else{
            MovieCastsAdapter castsAdapter = new MovieCastsAdapter(movieCasts,this);
            castRv.setAdapter(castsAdapter);
        }

        reviewRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ReviewAdapter reviewAdapter;
        //  checking if item more than 3 only show 3 review and rest on another activity
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
        share.setEnabled(true);
    }

    private void addToDb(){
        final ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        DbExecutor.getDbExecutor().getBackgroundIo().execute(new Runnable() {
            @Override
            public void run() {
                if(movieDetail != null){
                    showDatabase.showDao().addMovieDetail(movieDetail);
                }
                if(productionName != null && movieId != null){
                    showDatabase.showDao().addProductionCompany(new ProductionCompany(productionName,movieId));
                }
                if(movieCasts != null){
                    for(int i=0;i<movieCasts.size();i++) {
                        if(validateCast(movieCasts.get(i))){
                            showDatabase.showDao().addMovieCast(new MovieCastsResult(movieCasts.get(i).getCharacter(),movieCasts.get(i).getName(),movieCasts.get(i).getProfilePath(),movieCasts.get(i).getId(),movieId,movieCasts.get(i).getKey()));
                        }
                    }
                }
                if(videos != null){
                    for(int i=0;i<videos.size();i++){
                        if(validateVideos(videos.get(i))){
                            try{
                                showDatabase.showDao().addVideos(new VideosResults(movieId,videos.get(i).getVideoKey()));
                            }catch (Exception e){
                               e.printStackTrace();
                            }
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
                if(movieDetail != null){
                    showDatabase.showDao().removeMovieDetail(movieDetail);
                }
                if(productionName != null && movieId != null){
                    showDatabase.showDao().removeProductionCompany(new ProductionCompany(productionName,movieId));
                }
                if(movieCasts != null){
                    for(int i=0;i<movieCasts.size();i++) {
                        if(validateCast(movieCasts.get(i))){
                            showDatabase.showDao().removeMovieCast(new MovieCastsResult(movieCasts.get(i).getCharacter(),movieCasts.get(i).getName(),movieCasts.get(i).getProfilePath(),movieCasts.get(i).getId(),movieId,movieCasts.get(i).getKey()));
                        }
                    }
                }
                if(videos != null){
                    for(int i=0;i<videos.size();i++){
                        if(validateVideos(videos.get(i))){
                            showDatabase.showDao().removeVideos(new VideosResults(movieId,videos.get(i).getVideoKey()));
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
        MovieModelFactory movieDetailMovieModelFactory = new MovieModelFactory(showDatabase,movieId);
        final MovieDetailDbVM movieDetailVM = ViewModelProviders.of(this, movieDetailMovieModelFactory).get(MovieDetailDbVM.class);
        movieDetailVM.getMovieDetail().observe(this, new Observer<MovieDetail>() {
            @Override
            public void onChanged(@Nullable MovieDetail mD) {
                if(mD == null){
                    likeButton.setLiked(false);
                    isExist = false;
                }else{
                    movieDetail = mD;
                    likeButton.setLiked(true);
                    isExist = true;
                }
                if(networkStatus()){
                    loadFromApi();
                }else if(!networkStatus() && isExist){
                    loadFromDb();
                }else if(!networkStatus() && !isExist){
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

    private boolean validateCast(MovieCastsResult movieCasts){
        return movieCasts.getCharacter() != null && movieCasts.getName() != null && movieCasts.getProfilePath() != null && movieCasts.getId() != null && movieId != null;
    }

    private boolean validateVideos(VideosResults videos){
        return movieId != null && videos.getVideoKey() != null;
    }

    private void closeOnError(String message){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(MovieDetailAct.this,message,Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    @OnClick(R.id.detailCardAtMv)
    public void zoomDetail() {
        Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardView, View.SCALE_X, (small ? LARGE_SCALE : 1f));
        scaleX.setInterpolator(interpolator);
        scaleX.setDuration(symmetric ? 600L : 200L);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardView, View.SCALE_Y, (small ? LARGE_SCALE : 1f));
        scaleY.setInterpolator(interpolator);
        scaleY.setDuration(600L);
        scaleX.start();
        scaleY.start();

        small = !small;
        if (small) {
            symmetric = !symmetric;
        }
    }

    @OnClick(R.id.shareBtnAtMovieDetail)
    void shareMovie(){
        if(movieDetail != null){
            String toBeShare = movieDetail.getTitle();
            if(movieDetail.getOverview() != null){
                toBeShare += "\n\n*Description*\n"+movieDetail.getOverview();
            }
            if(videos != null){
                toBeShare += "\n\n*Videos*\n";
                for(int i=0;i<videos.size();i++){
                    toBeShare += Uri.parse(VIDEO_WEB_BASE_URL+videos.get(i).getVideoKey())+"\n";
                }
            }
            Intent sharingIntent = new Intent();
            sharingIntent.setAction(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, toBeShare);
            startActivity(sharingIntent);
        }
    }
}

