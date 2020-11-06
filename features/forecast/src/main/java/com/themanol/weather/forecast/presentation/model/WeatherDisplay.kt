package com.themanol.weather.forecast.presentation.model

import com.themanol.weather.common_ui.view.utils.ResourcesManager
import com.themanol.weather.domain.model.WeatherModel
import com.themanol.weather.forecast.R
import java.text.SimpleDateFormat
import java.util.*

private const val ICON_URL = "https://openweathermap.org/img/wn/{icon}@2x.png"
private const val ICON_KEY = "{icon}"
private const val ONE_KM_IN_METERS = 1000
private val DATE_FORMAT = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)

data class WeatherDisplay(
    val city: String,
    val temperature: String,
    val feelsLike: String,
    val description: String,
    val humidity: String,
    val cloudiness: String,
    val visibility: String,
    val dateOfData: String,
    val pressure: String,
    val wind: String,
    val windDirection: Int,
    val icon: String
)

fun WeatherModel.toDisplay(resourcesManager: ResourcesManager) =
    WeatherDisplay(
        city = city,
        temperature = resourcesManager.getString(
            R.string.temperature,
            conditionsDetails.temperature.toInt()
        ),
        feelsLike = resourcesManager.getString(
            R.string.feels_like,
            conditionsDetails.feelsLikeTemperature.toInt()
        ),
        description = conditionsDescription,
        humidity = resourcesManager.getString(R.string.humidity, conditionsDetails.humidity),
        cloudiness = resourcesManager.getString(R.string.cloudiness, clouds),
        wind = resourcesManager.getString(R.string.wind, String.format("%.2f", wind.speed)),
        windDirection = wind.direction,
        pressure = resourcesManager.getString(R.string.pressure, conditionsDetails.pressure),
        dateOfData = resourcesManager.getString(
            R.string.data_time,
            DATE_FORMAT.format(calculationDate)
        ),
        icon = ICON_URL.replace(ICON_KEY, conditionsIcon),
        visibility = resourcesManager.getString(R.string.visibility, visibility / ONE_KM_IN_METERS)
    )
