package com.ibrahim.runningapp.ui.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
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
import kotlinx.android.synthetic.main.tracking_fragment.*

class TrackingFragment :Fragment(R.layout.tracking_fragment) {
    private val viewModel:MainViewModel by viewModels()
    private var isTracking=false
    private var pathPoints= mutableListOf<polyline>()
    private var map:GoogleMap?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnToggleRun.setOnClickListener {
            toggleRun()
        }

        mapView?.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map=it
            addAllPollyLines()
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

}