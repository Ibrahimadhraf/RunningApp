package com.ibrahim.runningapp.ui.fragments
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleService
import com.google.android.gms.maps.GoogleMap
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.services.TrackingService
import com.ibrahim.runningapp.ui.MainActivity
import com.ibrahim.runningapp.ui.viewModel.MainViewModel
import com.ibrahim.runningapp.utils.Constance
import com.ibrahim.runningapp.utils.Constance.ACTION_SHOW_TRACKING_FRAGMENT
import com.ibrahim.runningapp.utils.Constance.NOTIFICATION_CHANNEL_ID
import com.ibrahim.runningapp.utils.Constance.NOTIFICATION_CHANNEL_Name
import com.ibrahim.runningapp.utils.Constance.NOTIFICATION_ID
import kotlinx.android.synthetic.main.tracking_fragment.*

class TrackingFragment :Fragment(R.layout.tracking_fragment) {
    private val viewModel:MainViewModel by viewModels()
    private var map:GoogleMap?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnToggleRun.setOnClickListener {
            sendCommandToService(Constance.ACTION_OR_RESUME_SERVICE)
        }
        mapView?.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map=it
        }
    }
private fun sendCommandToService(action:String)=
    Intent(requireContext() ,TrackingService::class.java).also {
        it.action=action
        requireContext().startService(it)
    }


    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

}