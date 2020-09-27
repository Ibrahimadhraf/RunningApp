package com.ibrahim.runningapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.services.TrackingService
import com.ibrahim.runningapp.services.polyline
import com.ibrahim.runningapp.ui.viewModel.MainViewModel
import com.ibrahim.runningapp.utils.Constance.ACTION_OR_RESUME_SERVICE
import com.ibrahim.runningapp.utils.Constance.ACTION_PAUSE_SERVICE
import com.ibrahim.runningapp.utils.Constance.MAP_ZOOM
import com.ibrahim.runningapp.utils.Constance.POLY_LINE_COLOR
import com.ibrahim.runningapp.utils.Constance.POLY_LINE_WIDTH
import com.ibrahim.runningapp.utils.TrackingUtility
import kotlinx.android.synthetic.main.tracking_fragment.*
import timber.log.Timber


class TrackingFragment :Fragment(R.layout.tracking_fragment) {
    private val viewModel:MainViewModel by viewModels()
    private var isTracking=false
     val myPosition=MutableLiveData<LatLng>()
    private var pathPoints= mutableListOf<polyline>()
    val position = LatLng(-33.920455, 18.466941)
    lateinit var locationManager: LocationManager
    private var map:GoogleMap?=null
    private var curTimeInMillis=0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentLocation()

        btnToggleRun.setOnClickListener {
            toggleRun()
        }

        mapView?.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map=it
            addAllPollyLines()
           setMapLocation(it)
        }

        subscribeToObserver()
    }
private fun sendCommandToService(action: String)=
    Intent(requireContext(), TrackingService::class.java).also {
        it.action=action
        requireContext().startService(it)
    }
    private fun subscribeToObserver(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyLine()
            moveCameraToUser()
        })
        TrackingService.timeRunInMillis.observe(viewLifecycleOwner , Observer {
            curTimeInMillis=it
            val formattedTime=TrackingUtility.getFormattedStopWatchTime(curTimeInMillis ,true)
            Timber.d(formattedTime)
            tvTimer.text=formattedTime
        })
    }
    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_OR_RESUME_SERVICE)
        }
    }
    private fun updateTracking(isTracking: Boolean){
        this.isTracking=isTracking
        if(!isTracking){
            btnToggleRun.text="Start"
            btnFinishRun.visibility=View.VISIBLE
        }else{
            btnToggleRun.text="Stop"
            btnFinishRun.visibility=View.GONE
        }
    }
    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty()&&pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }
    private fun addAllPollyLines(){
        for (polyline in pathPoints){
            val polylineOptions=PolylineOptions()
                .color(POLY_LINE_COLOR)
                .width(POLY_LINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }
private fun addLatestPolyLine(){
    if(pathPoints.isNotEmpty()&&pathPoints.last().size>1){
        val preLastLatLang=pathPoints.last()[pathPoints.last().size - 2]
        val lastLatLng=pathPoints.last().last()
        val polylineOptions=PolylineOptions()
            .color(POLY_LINE_COLOR)
            .width(POLY_LINE_WIDTH)
            .add(preLastLatLang)
            .add(lastLatLng)
        map?.addPolyline(polylineOptions)
    }
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
    @SuppressLint("MissingPermission")
    private fun setMapLocation(map: GoogleMap) {

        with(map) {
           map.isMyLocationEnabled=true

            val myLocation: Unit = map.setOnMyLocationClickListener(GoogleMap.OnMyLocationClickListener {
                val myPosition = LatLng(it.latitude, it.longitude)
                moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15f))
                addMarker(MarkerOptions().position(myPosition))
            })

            myPosition.observe(viewLifecycleOwner , Observer {
                moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                addMarker(MarkerOptions().position(it!!))
                mapType = GoogleMap.MAP_TYPE_NORMAL
            })


        }
    }
   @SuppressLint("MissingPermission")
   private fun getCurrentLocation(){
       locationManager = (requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?)!!
       locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)

   }
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
        val p=   LatLng(location.latitude, location.longitude)
            myPosition.postValue(p)
            Timber.d("my location is:${location.longitude} ,${location.latitude}")

        }

    }
}