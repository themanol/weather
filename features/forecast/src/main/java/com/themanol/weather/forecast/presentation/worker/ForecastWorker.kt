package com.themanol.weather.forecast.presentation.worker

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import com.themanol.weather.domain.repository.WeatherRepository

const val FORECAST_WORKER_TAG = "FORECAST_WORKER"

class ForecastWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WeatherRepository
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        if (ActivityCompat.checkSelfPermission(applicationContext, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(applicationContext)
            val location = Tasks.await(fusedLocationClient.lastLocation)
            return try {
                repository.getWeather(
                    location.latitude,
                    location.longitude,
                    true
                ).blockingGet()
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
        return Result.failure()
    }
}
