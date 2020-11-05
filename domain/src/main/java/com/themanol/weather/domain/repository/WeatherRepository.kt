package com.themanol.weather.domain.repository

import com.themanol.weather.domain.model.WeatherModel
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {

    fun getWeather(
        latitude: Double,
        longitude: Double,
        forceLoad: Boolean = false
    ): Single<WeatherModel>
}
