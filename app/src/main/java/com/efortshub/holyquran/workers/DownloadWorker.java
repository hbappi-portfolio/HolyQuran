package com.efortshub.holyquran.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.efortshub.holyquran.R;
import com.efortshub.holyquran.utils.HbUtils;

/**
 * Created by H. Bappi on  9:36 AM  10/15/21.
 * Contact email:
 * contact@efortshub.com
 * bappi@efortshub.com
 * contact.efortshub@gmail.com
 * github: https://github.com/hbappi
 * Copyright (c) 2021 eFortsHub . All rights reserved.
 **/
public class DownloadWorker extends Worker {
    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);


    }

    @NonNull
    @Override
    public Result doWork() {

       // setForegroundAsync(createForegroundInfo("Starting Download"));


        download();


        return Result.success();
    }

    private void download() {
        for (int i =0; i<100; i++){
            try {
                setForegroundAsync(createForegroundInfo("Downloading...", i, 100));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ForegroundInfo createForegroundInfo(String name, int progress, int total) {
        Context context = getApplicationContext();
        PendingIntent cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannel();
        }

        int  t = HbUtils.getSavedTheme(context);
        Resources.Theme theme = new ContextThemeWrapper(context, t).getTheme();
        TypedValue primaryVariantValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimaryVariant, primaryVariantValue, true);
        int colorPrimaryVariant = primaryVariantValue.data;

        TypedValue primaryValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, primaryValue, true);
        int colorPrimary = primaryValue.data;

        TypedValue selectableValue = new TypedValue();
        theme.resolveAttribute(android.R.attr.listSelector, selectableValue, true);
        int selectableBg = selectableValue.resourceId;


        // set big remote view
        RemoteViews remoteViewsBigContent = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remote_notif_big_download);

        remoteViewsBigContent.setInt(R.id.ll_root, "setBackgroundColor", colorPrimaryVariant);
        remoteViewsBigContent.setInt(R.id.btn_details, "setBackgroundResource", selectableBg);
        remoteViewsBigContent.setInt(R.id.btn_cancel, "setBackgroundResource", selectableBg);
        remoteViewsBigContent.setTextColor(R.id.tv_app_name, colorPrimary);
        remoteViewsBigContent.setTextColor(R.id.btn_details, colorPrimary);
        remoteViewsBigContent.setTextColor(R.id.btn_cancel, colorPrimary);
        remoteViewsBigContent.setTextColor(R.id.tv_download_title, colorPrimary);
        remoteViewsBigContent.setTextColor(R.id.tv_download_progress, colorPrimary);
        remoteViewsBigContent.setTextViewText(R.id.tv_download_progress, progress+"/"+total);


        //set normal remote view
        RemoteViews remoteViewNormal = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remote_notif_big_download);

        remoteViewNormal.setInt(R.id.ll_root, "setBackgroundColor", colorPrimaryVariant);
        remoteViewNormal.setInt(R.id.btn_details, "setBackgroundResource", selectableBg);
        remoteViewNormal.setInt(R.id.btn_cancel, "setBackgroundResource", selectableBg);
        remoteViewNormal.setTextColor(R.id.tv_app_name, colorPrimary);
        remoteViewNormal.setTextColor(R.id.btn_details, colorPrimary);
        remoteViewNormal.setTextColor(R.id.btn_cancel, colorPrimary);
        remoteViewNormal.setTextColor(R.id.tv_download_title, colorPrimary);
        remoteViewNormal.setTextColor(R.id.tv_download_progress, colorPrimary);
        remoteViewNormal.setTextViewText(R.id.tv_download_progress, progress+"/"+total);



        remoteViewsBigContent.setOnClickPendingIntent(R.id.btn_cancel, cancelIntent);



        Notification notification = new NotificationCompat.Builder(context, "idid")
                .setCustomContentView(remoteViewNormal)
                .setCustomBigContentView(remoteViewsBigContent)
                .setTicker("ticker title")
                .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                .setOngoing(true)
                .build();



        return new ForegroundInfo(11, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationManager notification = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        CharSequence name = "name";
        String description = "desc";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("idid", name, importance);
        channel.setDescription(description);
        notification.createNotificationChannel(channel);



    }
}
