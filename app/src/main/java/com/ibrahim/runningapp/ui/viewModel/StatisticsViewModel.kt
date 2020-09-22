package com.ibrahim.runningapp.ui.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.ibrahim.runningapp.reposotories.MainRepository
import javax.inject.Inject

class StatisticsViewModel @ViewModelInject constructor(
    mainRepository: MainRepository
):ViewModel(){
}