package com.themanol.weather.forecast.view.fragment

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.themanol.weather.common_ui.view.fragment.BaseFragment
import com.themanol.weather.forecast.databinding.FragmentForecastBinding
import com.themanol.weather.forecast.presentation.viewmodel.Error
import com.themanol.weather.forecast.presentation.viewmodel.ForecastViewModel
import com.themanol.weather.forecast.presentation.viewmodel.Loading
import com.themanol.weather.forecast.presentation.viewmodel.Success
import com.themanol.weather.forecast.presentation.worker.FORECAST_WORKER_TAG
import com.themanol.weather.forecast.presentation.worker.ForecastWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

private const val PERMISSION_REQUEST_CODE = 10
private const val PERMISSION_BACKGROUND_REQUEST_CODE = 11
private const val SETTINGS_REQUEST_CODE = 20
private const val WORKER_TIME = 2L
private const val PACKAGE = "package"

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
        initListeners()
        getLocationWithPermissionCheck()
    }

    private fun getLocationWithPermissionCheck() {
        context?.let { _context ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(_context)
            if (checkSelfPermission(_context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val currentLocation = location ?: Location(LocationManager.PASSIVE_PROVIDER)
                forecastViewModel.start(currentLocation.latitude, currentLocation.longitude)
            }
    }

    private fun initListeners() {
        binding.retryButton.setOnClickListener {
            getLocationWithPermissionCheck()
        }
        binding.permissionsButton.setOnClickListener {
            openPermissionSettings()
        }
    }

    private fun initObservers() {
        forecastViewModel.state.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    Loading -> {
                        binding.progressCircular.isVisible = true
                        binding.permissionsGroup.isVisible = false
                        binding.successGroup.isVisible = false
                        binding.errorGroup.isVisible = false
                    }
                    Error -> {
                        binding.progressCircular.isVisible = false
                        binding.permissionsGroup.isVisible = false
                        binding.successGroup.isVisible = false
                        binding.errorGroup.isVisible = true
                    }
                    is Success -> {
                        binding.progressCircular.isVisible = false
                        binding.permissionsGroup.isVisible = false
                        binding.errorGroup.isVisible = false
                        binding.successGroup.isVisible = true
                        binding.forecastDescription.text = it.weatherDisplay.description
                        binding.forecastTemperature.text = it.weatherDisplay.temperature
                        binding.forecastCity.text = it.weatherDisplay.city
                        binding.forecastDateOfData.text = it.weatherDisplay.dateOfData
                        binding.forecastHumidity.text = it.weatherDisplay.humidity
                        binding.forecastCloudiness.text = it.weatherDisplay.cloudiness
                        binding.forecastFeelLike.text = it.weatherDisplay.feelsLike
                        binding.forecastPressure.text = it.weatherDisplay.pressure
                        binding.forecastVisibility.text = it.weatherDisplay.visibility
                        binding.forecastWind.text = it.weatherDisplay.wind
                        Glide.with(binding.forecastIcon)
                            .load(it.weatherDisplay.icon)
                            .into(binding.forecastIcon)
                        binding.forecastWindIcon.rotation =
                            it.weatherDisplay.windDirection.toFloat()
                        initWorkerWithPermissionCheck()
                    }
                }
            }
        )
    }

    private fun initWorkerWithPermissionCheck() {
        context?.let { _context ->
            if (Build.VERSION.SDK_INT >= 29 && checkSelfPermission(
                    _context,
                    ACCESS_BACKGROUND_LOCATION
                ) != PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(ACCESS_BACKGROUND_LOCATION),
                    PERMISSION_BACKGROUND_REQUEST_CODE
                )
            } else {
                initWorker()
            }
        }
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
                binding.progressCircular.isVisible = false
                binding.permissionsGroup.isVisible = true
                binding.successGroup.isVisible = false
                binding.errorGroup.isVisible = false
            }
        }
    }

    private fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts(PACKAGE, context?.packageName, null)
        intent.data = uri
        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQUEST_CODE) {
            getLocationWithPermissionCheck()
        }
    }
}
