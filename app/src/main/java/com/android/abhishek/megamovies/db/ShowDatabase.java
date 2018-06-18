package com.android.abhishek.megamovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.android.abhishek.megamovies.model.MovieCastsResult;
import com.android.abhishek.megamovies.model.MovieDetail;
import com.android.abhishek.megamovies.model.ProductionCompany;
import com.android.abhishek.megamovies.model.ReviewResults;
import com.android.abhishek.megamovies.model.TvCreatedByResults;
import com.android.abhishek.megamovies.model.TvDetail;
import com.android.abhishek.megamovies.model.VideosResults;

@Database(entities = {MovieDetail.class, TvDetail.class, MovieCastsResult.class, TvCreatedByResults.class, ProductionCompany.class, VideosResults.class},version = 1,exportSchema = false)
public abstract class ShowDatabase extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "SHOW_DATABASE";    //  Non translatable Db Name
    private static ShowDatabase showDatabase;

    public static ShowDatabase getShowDatabase(Context context){
        if(showDatabase == null){
            synchronized (LOCK){
                showDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        ShowDatabase.class,
                        ShowDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return showDatabase;
    }

    public abstract ShowDao showDao();
}
