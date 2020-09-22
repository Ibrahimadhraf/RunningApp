package com.ibrahim.runningapp.reposotories

import com.ibrahim.runningapp.dp.Run
import com.ibrahim.runningapp.dp.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {
    suspend fun insertRun(run:Run)=runDao.insertRun(run)
    suspend fun deleteRun(run:Run)=runDao.deleteRun(run)
    fun getAllRunsSortedByDate()=runDao.getAllRunSortedByDate()
    fun getAllRunsSortedByDistance()=runDao.getTotalDistance()
    fun getAllRunsSortedByTimeInMilliSeconds()=runDao.getAllRunSortedByTimeInMilliSeconds()
    fun getAllRunsSortedByAverageSpeed()=runDao.getAllRunSortedByAverageSpeed()
    fun getAllRunsSortedByACaloriesBurned()=runDao.getAllRunSortedByCaloriesBurned()
    fun getTotalAverageSpeed()=runDao.getTotalAverageSpeed()
    fun getTotalDistance()=runDao.getTotalDistance()
    fun getTotalCaloriesBurned()=runDao.getTotalCaloriesBurned()
    fun getTotalTimeInMilliSeconds()=runDao.getTotalTimeInInMilliSeconds()
}