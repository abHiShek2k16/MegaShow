package com.android.abhishek.megamovies.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.android.abhishek.megamovies.MovieDetailAct;
import com.android.abhishek.megamovies.R;

import java.net.URL;

public class NotificationUtils {

    private static final int MOVIE_NOTIFICATION_ID = 1138;
    private static final int MOVIE_PENDING_INTENT_ID = 3417;
    private static final String MOVIE_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    public static void movieNotify(final Context context,final String id,final String logoPath) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    MOVIE_NOTIFICATION_CHANNEL_ID,
                    context.getResources().getString(R.string.notificationChannelName),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        //  Uses AsyncTask because downloading the image to convert in bitmap
        AsyncTask notifyBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                @SuppressLint("IconColors") NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MOVIE_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(R.drawable.app_logo)
                        .setContentTitle(context.getResources().getString(R.string.notificationTitle))
                        .setContentText(context.getResources().getString(R.string.notificationDesc))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getPosterImage(logoPath)))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context,id,logoPath))
                        .setAutoCancel(true);

                notificationManager.notify(MOVIE_NOTIFICATION_ID, notificationBuilder.build());

                return null;
            }
        };
        notifyBackgroundTask.execute();

    }

    private static PendingIntent contentIntent(Context context,String id,String logoPath) {
        Intent startActivityIntent = new Intent(context, MovieDetailAct.class);
        startActivityIntent.setAction(Intent.ACTION_VIEW);
        startActivityIntent.putExtra(MovieDetailAct.EXTRA_CURVE, false);
        startActivityIntent.putExtra(context.getResources().getString(R.string.intentPassingOne),id);
        startActivityIntent.setData(Uri.parse(logoPath));
        return PendingIntent.getActivity(context, MOVIE_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Bitmap getPosterImage(String logoPath){
        try{
            return BitmapFactory.decodeStream(new URL(logoPath).openConnection().getInputStream());
        }catch (Exception e){
            return null;
        }
    }
}
