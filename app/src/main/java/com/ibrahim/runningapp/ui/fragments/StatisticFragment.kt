package com.ibrahim.runningapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ibrahim.runningapp.R

import com.ibrahim.runningapp.ui.viewModel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticFragment :Fragment(R.layout.statistic_fragment) {
    private val viewModel: StatisticsViewModel by viewModels()
}