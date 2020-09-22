package com.ibrahim.runningapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.dp.RunDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var runDao: RunDao
    val TAG="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
          setSupportActionBar(toolbar)
        bottomNavigationView.setupWithNavController(nav_host.findNavController())
        nav_host.findNavController()
            .addOnDestinationChangedListener { _, destination, _->
                when (destination.id){
                    R.id.runFragments ,R.id.statisticFragment ,R.id.settingsFragment ->
                        bottomNavigationView.visibility= View.VISIBLE
                    else -> bottomNavigationView.visibility= View.GONE
                }
            }
    }


}