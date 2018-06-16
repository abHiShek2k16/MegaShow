package com.android.abhishek.megamovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
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

import com.android.abhishek.megamovies.adapter.MovieCastsAdapter;
import com.android.abhishek.megamovies.adapter.ReviewAdapter;
import com.android.abhishek.megamovies.adapter.TrailerAdapter;
import com.android.abhishek.megamovies.db.ShowDatabase;
import com.android.abhishek.megamovies.listener.RecyclerItemClickListener;
import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.ReviewResults;
import com.android.abhishek.megamovies.model.VideosResults;
import com.android.abhishek.megamovies.network.BuildUrl;
import com.android.abhishek.megamovies.utils.ApiInterface;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailAct extends AppCompatActivity {
    //  xml view
    @BindView(R.id.posterImageAtMovieDetail) ImageView posterImageIv;
    @BindView(R.id.movieNameAtMovieDetail) TextView movieNameTv;
    @BindView(R.id.productionNameAtMovie) TextView productionNameTv;
    @BindView(R.id.movieFavBtn) LikeButton likeButton;
    @BindView(R.id.logoIv) ImageView movieLogoIv;
    @BindView(R.id.lengthTvAtMovieDetail) TextView lengthTv;
    @BindView(R.id.ratingTvAtMovieDetail) TextView ratingTv;
    @BindView(R.id.totalVoteTvAtMovieDetail) TextView totalVoteTv;
    @BindView(R.id.dateTvAtMovieDetail) TextView releaseDateTv;
    @BindView(R.id.overviewAtMovieDetail) TextView overviewTv;
    @BindView(R.id.trailerRv) RecyclerView trailerRv;
    @BindView(R.id.castRvAtMovieDetail) RecyclerView castRv;
    @BindView(R.id.reviewRvAtMovieDetail) RecyclerView reviewRv;
    @BindView(R.id.readAllReviewAtMovieDetail) TextView readAll;
    @BindView(R.id.toolBarATMovieDetail) android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.totalRatingBarAtMv) RatingBar totalRating;
    @BindView(R.id.ratingCountAtMv) TextView ratingCount;
    @BindView(R.id.ratingStrAtMv) TextView ratingStrTv;
    @BindView(R.id.noTSEAtMvCasts) RelativeLayout castsError;
    @BindView(R.id.noTSEAtMvReview) RelativeLayout reviewError;
    @BindView(R.id.noTSEAtMvTrailer) RelativeLayout trailerError;

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
    private String movieLogo = "";
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

    private MovieDetail movieDetail;
    private ShowDatabase showDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

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

        movieId = intent.getStringExtra(getResources().getString(R.string.intentPassingOne));
        if(movieId == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());

        if(isExistInDb() && networkStatus()){
            loadFromApi();
            likeButton.setLiked(true);
        }else if(isExistInDb() && !networkStatus()){
            loadFromDb();
            likeButton.setLiked(true);
        }else if(!isExistInDb() && networkStatus()){
            loadFromApi();
            likeButton.setLiked(false);
        }else {
            closeOnError(getResources().getString(R.string.netproblem));
        }

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

        castRv.addOnItemTouchListener(new RecyclerItemClickListener(this, castRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = movieCasts.get(position).getId();
                Intent intent = new Intent(MovieDetailAct.this,CastProfileAct.class);
                intent.putExtra(getResources().getString(R.string.intentPassingOne),id);
                startActivity(intent);
            }
        }));

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
        ShowDatabase showDatabase = ShowDatabase.getShowDatabase(getApplicationContext());
        try{
            movieDetail = showDatabase.showDao().getMovieDetail(movieId);
        }catch (Exception e){}
        try{
            productionName = "by "+showDatabase.showDao().getProductionCompany(movieId).getName()==null?"":showDatabase.showDao().getProductionCompany(movieId).getName();
        }catch (Exception e){}
        try{
            movieCasts = showDatabase.showDao().getMovieCast(movieId);
        }catch (Exception e){}
        try{
            videos = showDatabase.showDao().getVideos(movieId);
        }catch (Exception e){}
        setVariableFromDb();
        setView();
    }

    private void setVariableFromDb(){
        posterImageUrl = IMAGE_BASE_URL+movieDetail.getBackdropPath();
        movieLogo = IMAGE_BASE_URL+movieDetail.getPosterPath();
        length = movieDetail.getRuntime()==null?DATA_NOT_AVAILABLE:movieDetail.getRuntime();
        movieName = movieDetail.getTitle()==null?DATA_NOT_AVAILABLE:movieDetail.getTitle();
        rating = movieDetail.getVoteAvg()==null?DATA_NOT_AVAILABLE:movieDetail.getVoteAvg();
        totalVote = movieDetail.getVoteCount()==null?DATA_NOT_AVAILABLE:movieDetail.getVoteCount();
        releaseDate = movieDetail.getReleaseDate()==null?DATA_NOT_AVAILABLE:movieDetail.getReleaseDate();
        overView = movieDetail.getOverview()==null?DATA_NOT_AVAILABLE:movieDetail.getOverview();
    }

    private void loadFromApi(){
        ApiInterface apiInterface = BuildUrl.getRetrofit(this).create(ApiInterface.class);
        retrofit2.Call<MovieDetail> movieDetailCall = apiInterface.getMovieDetail(movieId,API_KEY,APPEND_QUERY);
        movieDetailCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                movieDetail = response.body();
                if(movieDetail == null){
                    closeOnError(getResources().getString(R.string.somethingWrong));
                }
                setVariableFromApi();
                setView();
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                closeOnError(getResources().getString(R.string.netproblem));
            }
        });
    }

    private void setVariableFromApi(){
        posterImageUrl = IMAGE_BASE_URL+movieDetail.getBackdropPath();
        movieLogo = IMAGE_BASE_URL+movieDetail.getPosterPath();
        productionName = "by "+(movieDetail.getProductionCompanies().get(0).getName()==null?DATA_NOT_AVAILABLE:movieDetail.getProductionCompanies().get(0).getName());
        try{
            length = movieDetail.getRuntime().isEmpty() ? DATA_NOT_AVAILABLE : String.valueOf(Integer.parseInt(movieDetail.getRuntime())/60)+"h "+String.valueOf(Integer.parseInt(movieDetail.getRuntime())%60)+"min";
        }catch (Exception e){
            length = DATA_NOT_AVAILABLE;
        }
        movieName = movieDetail.getTitle()==null?DATA_NOT_AVAILABLE:movieDetail.getTitle();
        rating = movieDetail.getVoteAvg()==null?DATA_NOT_AVAILABLE:movieDetail.getVoteAvg();
        if(rating.length()>3){
            rating = rating.substring(0,3);
        }
        totalVote = movieDetail.getVoteCount()==null?DATA_NOT_AVAILABLE:movieDetail.getVoteCount();
        releaseDate = movieDetail.getReleaseDate()==null?DATA_NOT_AVAILABLE:movieDetail.getReleaseDate();
        overView = movieDetail.getOverview()==null?DATA_NOT_AVAILABLE:movieDetail.getOverview();
        videos = movieDetail.getMovieVideos().getVideosResults();
        movieCasts = movieDetail.getMovieCasts().getMovieCastsResults();
        reviews = movieDetail.getMovieReview().getMovieReviewResults();
    }

    private void setView(){
        if(networkStatus()){
            Picasso.get()
                    .load(posterImageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(posterImageIv);
            Picasso.get()
                    .load(movieLogo)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(movieLogoIv);
        }else{
            Picasso.get()
                    .load(posterImageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(posterImageIv);
            Picasso.get()
                    .load(movieLogo)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(movieLogoIv);
        }

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
            MovieCastsAdapter castsAdapter = new MovieCastsAdapter(movieCasts);
            castRv.setAdapter(castsAdapter);
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
            showDatabase.showDao().addMovieDetail(movieDetail);
            for(int i=0;i<movieCasts.size();i++){
                showDatabase.showDao().addMovieCast(new MovieCastsResult(movieCasts.get(i).getCharacter(),movieCasts.get(i).getName(),movieCasts.get(i).getProfilePath(),movieCasts.get(i).getId(),movieId,movieCasts.get(i).getKey()));
            }
            showDatabase.showDao().addProductionCompany(new ProductionCompany(movieDetail.getProductionCompanies().get(0).getName(),movieId,movieDetail.getProductionCompanies().get(0).getId()));
            for(int i=0;i<videos.size();i++){
                showDatabase.showDao().addVideos(new VideosResults(movieId,videos.get(i).getVideoKey()));
            }
            likeButton.setLiked(true);
        }catch (Exception e){}
    }

    private void removeFromDb(){
        try {
            showDatabase.showDao().removeMovieDetail(movieDetail);
            for (int i = 0; i < movieCasts.size(); i++) {
                showDatabase.showDao().removeMovieCast(new MovieCastsResult(movieCasts.get(i).getCharacter(), movieCasts.get(i).getName(), movieCasts.get(i).getProfilePath(), movieCasts.get(i).getId(), movieId,movieCasts.get(i).getKey()));
            }
            showDatabase.showDao().removeProductionCompany(new ProductionCompany(movieDetail.getProductionCompanies().get(0).getName(), movieId, movieDetail.getProductionCompanies().get(0).getId()));
            for (int i = 0; i < videos.size(); i++) {
                showDatabase.showDao().removeVideos(new VideosResults(movieId, videos.get(i).getVideoKey()));
            }
            likeButton.setLiked(false);
        }catch (Exception e){}
    }

    private boolean isExistInDb(){
        if(showDatabase.showDao().getMovieDetail(movieId) == null){
            return false;
        }
        return true;
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
        Toast.makeText(MovieDetailAct.this,message,Toast.LENGTH_SHORT).show();
        finish();
    }

}

