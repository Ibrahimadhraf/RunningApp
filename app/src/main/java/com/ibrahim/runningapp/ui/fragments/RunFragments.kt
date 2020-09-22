package com.ibrahim.runningapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.utils.Constance.REQUEST_LOCATION_PERMISSION
import com.ibrahim.runningapp.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.run_fragment.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import com.ibrahim.runningapp.utils.TrackingUtility as TrackingUtility1

@AndroidEntryPoint
class RunFragments :Fragment(R.layout.run_fragment),EasyPermissions.PermissionCallbacks {
    private val viewModel:MainViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragments_to_trackingFragment)
        }
    }
   private fun requestPermission(){
        if(TrackingUtility1.hasLocationPermission(requireContext())){
            return
        }
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
                EasyPermissions.requestPermissions(
                    this ,
                    "You need to Accept Location permission  to use this app",
                    REQUEST_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION ,
                    Manifest.permission.ACCESS_COARSE_LOCATION,

                )
        }else{
            EasyPermissions.requestPermissions(
                this ,
                "You need to Accept Location permission  to use this app",
                REQUEST_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION ,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this ,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermission()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode ,permissions ,grantResults ,this)
    }
}