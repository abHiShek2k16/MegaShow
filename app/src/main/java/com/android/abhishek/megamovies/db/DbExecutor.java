package com.android.abhishek.megamovies.db;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DbExecutor {

    private static final Object LOCK = new Object();
    private static DbExecutor dbExecutor;
    private final Executor backgroundIo;

    private DbExecutor(Executor backgroundIo) {
        this.backgroundIo = backgroundIo;
    }

    public static DbExecutor getDbExecutor(){
        if(dbExecutor == null){
            synchronized (LOCK){
                dbExecutor = new DbExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return dbExecutor;
    }

    public Executor getBackgroundIo() {
        return backgroundIo;
    }
}
