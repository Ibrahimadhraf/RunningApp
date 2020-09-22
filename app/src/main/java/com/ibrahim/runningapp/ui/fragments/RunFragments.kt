package com.ibrahim.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.run_fragment.*
import kotlinx.android.synthetic.main.setup_fragment.*

@AndroidEntryPoint
class RunFragments :Fragment(R.layout.run_fragment) {
    private val viewModel:MainViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragments_to_trackingFragment)
        }
    }
}