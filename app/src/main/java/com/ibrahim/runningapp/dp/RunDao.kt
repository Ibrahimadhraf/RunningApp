package com.ibrahim.runningapp.dp

import android.icu.number.IntegerWidth
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)
    @Delete
    suspend fun deleteRun(run: Run)
     @Query("SELECT *FROM RUNNING_TABLE ORDER BY timeStamp DESC")
    fun getAllRunSortedByDate():LiveData<List<Run>>
    @Query("SELECT *FROM RUNNING_TABLE ORDER BY timeInMilliSeconds DESC")
    fun getAllRunSortedByTimeInMilliSeconds():LiveData<List<Run>>
    @Query("SELECT *FROM RUNNING_TABLE ORDER BY caloriesBurned DESC")
    fun getAllRunSortedByCaloriesBurned():LiveData<List<Run>>
    @Query("SELECT *FROM RUNNING_TABLE ORDER BY averageSpeedInKMH DESC")
    fun getAllRunSortedByAverageSpeed():LiveData<List<Run>>
    @Query("SELECT *FROM RUNNING_TABLE ORDER BY distanceInMetres DESC")
    fun getAllRunSortedByDistance():LiveData<List<Run>>
    @Query("SELECT SUM(timeInMilliSeconds) FROM running_table")
    fun getTotalTimeInInMilliSeconds():LiveData<Long>
    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    fun getTotalCaloriesBurned():LiveData<Int>
    @Query("SELECT SUM(distanceInMetres) FROM running_table")
    fun getTotalDistance():LiveData<Int>
    @Query("SELECT AVG(averageSpeedInKMH) FROM running_table")
    fun getTotalAverageSpeed():LiveData<Float>
}