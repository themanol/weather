package com.themanol.weather.forecast.view.fragment

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.themanol.weather.forecast.databinding.FragmentForecastBinding
import com.themanol.weather.forecast.presentation.viewmodel.Error
import com.themanol.weather.forecast.presentation.viewmodel.ForecastViewModel
import com.themanol.weather.forecast.presentation.viewmodel.Loading
import com.themanol.weather.forecast.presentation.viewmodel.Success
import com.themanol.weather.forecast.presentation.worker.FORECAST_WORKER_TAG
import com.themanol.weather.forecast.presentation.worker.ForecastWorker
import com.themanol.weather.common_ui.view.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

private const val PERMISSION_REQUEST_CODE = 10
private const val WORKER_TIME = 2L

@AndroidEntryPoint
class ForecastFragment : BaseFragment<FragmentForecastBinding>() {
    private val forecastViewModel: ForecastViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        getLocationWithPermissionCheck()
    }

    private fun getLocationWithPermissionCheck() {
        context?.let { _context ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(_context)
            if (checkSelfPermission(_context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                activity?.let {
                    requestPermissions(
                        arrayOf(ACCESS_COARSE_LOCATION),
                        PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(): Task<Location> {
        return fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    forecastViewModel.start(it.latitude, it.longitude)
                }
            }
    }

    private fun initObservers() {
        forecastViewModel.state.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    Loading -> {
                        // TODO: Show loading screen
                    }
                    Error -> {
                        // TODO: Show error screen
                    }
                    is Success -> {
                        initWorker()
                        binding.forecastDescription.text = it.weatherDisplay.description
                    }
                }
            }
        )
    }

    private fun initWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val myWorkRequest =
            PeriodicWorkRequestBuilder<ForecastWorker>(WORKER_TIME, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(WORKER_TIME, TimeUnit.HOURS)
                .build()
        context?.let {
            WorkManager
                .getInstance(it)
                .enqueueUniquePeriodicWork(
                    FORECAST_WORKER_TAG,
                    ExistingPeriodicWorkPolicy.KEEP,
                    myWorkRequest
                )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.firstOrNull() == PERMISSION_GRANTED) {
                getLocation()
            } else {
                // TODO: Show request permissions screen
                Log.d("Weather", "No permission")
            }
        }
    }
}
