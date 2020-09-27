package com.ibrahim.runningapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.utils.Constance
import com.ibrahim.runningapp.utils.Constance.ACTION_OR_RESUME_SERVICE
import com.ibrahim.runningapp.utils.Constance.ACTION_PAUSE_SERVICE
import com.ibrahim.runningapp.utils.Constance.ACTION_STOP_SERVICE
import com.ibrahim.runningapp.utils.Constance.FASTEST_UPDATE_INTERVAL
import com.ibrahim.runningapp.utils.Constance.LOCATION_UPDATE_INTERVAL
import com.ibrahim.runningapp.utils.Constance.NOTIFICATION_ID
import com.ibrahim.runningapp.utils.Constance.TIMER_UPDATE_INTERVAL
import com.ibrahim.runningapp.utils.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias polyline = MutableList<LatLng>
typealias polylines = MutableList<polyline>
@AndroidEntryPoint
class TrackingService : LifecycleService() {
    private var isFirstRun = true
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val timeRunInSeconds = MutableLiveData<Long>()
    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var curNotificationBuilder: NotificationCompat.Builder
    companion object {
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<polylines>()

    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        curNotificationBuilder=baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming Service ")
                        startTimer()
                    }

                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pause Service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop Service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnable = false
    private var labTime = 0L
    private var timeRun=0L
    private var timeStarted=0L
    private var lastSecondTimeStamp=0L
    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnable=false
    }

    private fun startTimer(){
        addEmptyPollyLine()
        isTracking.postValue(true)
        timeStarted=System.currentTimeMillis()
        isTimerEnable=true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                //different between now and time started
                labTime=System.currentTimeMillis()-timeStarted
                timeRunInMillis.postValue(timeRun+labTime )
                if (timeRunInMillis.value!!>=lastSecondTimeStamp+1000){
                    timeRunInSeconds.postValue(timeRunInSeconds.value!!+1)
                    lastSecondTimeStamp+=1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
           timeRun+=labTime
        }
    }
private fun updateNotificationTrackingState(isTracking: Boolean){
    val notificationCompatText=if(isTracking)"Pause" else "Resume"
   val pendingIntent= if(isTracking){
       val pauseIntent=Intent(this ,TrackingService::class.java).apply {
           action= ACTION_PAUSE_SERVICE
       }
        PendingIntent.getService(this ,1,pauseIntent ,FLAG_UPDATE_CURRENT)
    }else{
        val resumeIntent=Intent(this ,TrackingService::class.java).apply {
            action= ACTION_OR_RESUME_SERVICE
        }
        PendingIntent.getService(this ,1,resumeIntent ,FLAG_UPDATE_CURRENT)
    }
    val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
        isAccessible=true
        set(curNotificationBuilder ,ArrayList<NotificationCompat.Action>())
    }

    curNotificationBuilder=baseNotificationBuilder
        .addAction(R.drawable.ic_pause ,notificationCompatText ,pendingIntent)
        notificationManager.notify(NOTIFICATION_ID ,curNotificationBuilder.build())
}

    @SuppressLint("MissingPermission")
    private fun updateTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermission(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_UPDATE_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }

        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations?.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Timber.d("New Location:${location.latitude},${location.longitude}")

                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val position = LatLng(it.latitude, it.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPollyLine() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
        timeRunInSeconds.observe(this , Observer {
            val notification=curNotificationBuilder
                .setContentText(TrackingUtility.getFormattedStopWatchTime(it*1000L))
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constance.NOTIFICATION_CHANNEL_ID,
            Constance.NOTIFICATION_CHANNEL_Name,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}