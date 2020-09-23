package com.ibrahim.runningapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.ui.MainActivity
import com.ibrahim.runningapp.utils.Constance
import com.ibrahim.runningapp.utils.Constance.ACTION_OR_RESUME_SERVICE
import com.ibrahim.runningapp.utils.Constance.ACTION_PAUSE_SERVICE
import com.ibrahim.runningapp.utils.Constance.ACTION_STOP_SERVICE
import com.ibrahim.runningapp.utils.Constance.NOTIFICATION_ID
import timber.log.Timber

class TrackingService:LifecycleService() {
    private var isFirstRun=true
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_OR_RESUME_SERVICE->{
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun=false
                    }else{
                        Timber.d("Resuming Service ")
                    }

                }
                ACTION_PAUSE_SERVICE->{
                    Timber.d("Pause Service")
                }
                ACTION_STOP_SERVICE->{
                    Timber.d("Stop Service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun startForegroundService(){
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder=
            NotificationCompat.Builder(this , Constance.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())
        startForeground(NOTIFICATION_ID  ,notificationBuilder.build())
    }
    private fun getMainActivityPendingIntent()= PendingIntent.getActivity(
        this ,
        0,
        Intent(this , MainActivity::class.java).also {
            it.action= Constance.ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT

    )
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel= NotificationChannel(   Constance.NOTIFICATION_CHANNEL_ID ,
            Constance.NOTIFICATION_CHANNEL_Name  ,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}