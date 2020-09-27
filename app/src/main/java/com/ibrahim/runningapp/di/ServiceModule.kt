package com.ibrahim.runningapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.ui.MainActivity
import com.ibrahim.runningapp.utils.Constance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @ServiceScoped
    @Provides
    fun providesFusedLocationProviderClient(
        @ApplicationContext app: Context
    ) =FusedLocationProviderClient(app)
    @ServiceScoped
    @Provides
fun providesMainActivityPendingIntent(
    @ApplicationContext app: Context
)= PendingIntent.getActivity(
        app,
        0,
        Intent(app, MainActivity::class.java).also {
            it.action = Constance.ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT

    )!!
    @ServiceScoped
    @Provides
fun  providesBaseNotificationBuilder(@ApplicationContext app: Context ,
pendingIntent:PendingIntent)=
        NotificationCompat.Builder(app, Constance.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)!!

}