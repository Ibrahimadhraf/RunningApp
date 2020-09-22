package com.ibrahim.runningapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.ibrahim.runningapp.R
import com.ibrahim.runningapp.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragments :Fragment(R.layout.run_fragment) {
    private val viewModel:MainViewModel by viewModels()
}