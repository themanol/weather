package com.themanol.weather.data.datasource

import com.themanol.weather.data.model.WeatherEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherDataSource {
    @GET("weather?units=metric&appid=5ad7218f2e11df834b0eaf3a33a39d2a")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Single<WeatherEntity>
}
