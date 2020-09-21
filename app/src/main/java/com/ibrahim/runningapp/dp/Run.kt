package com.ibrahim.runningapp.dp

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var img: Bitmap? = null,
    var timeStamp: Long = 0L,
    var averageSpeedInKMH: Float = 0f,
    var distanceInMetres: Int = 0,
    var timeInMilliSeconds: Long = 0L,
    var caloriesBurned: Int = 0,

    ) {
    @PrimaryKey(autoGenerate = true)
    var  id:Int?=null
}