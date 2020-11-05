package com.themanol.weather.data.repository

import com.themanol.weather.data.datasource.LocalWeatherDataSource
import com.themanol.weather.data.datasource.WeatherDataSource
import com.themanol.weather.domain.model.WeatherModel
import com.themanol.weather.domain.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherRepositoryImpl
@Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val localWeatherDataSource: LocalWeatherDataSource
) : WeatherRepository {

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        forceLoad: Boolean
    ): Single<WeatherModel> {
        return if (forceLoad) {
            getWeatherFromRemoteAndPersist(latitude, longitude)
        } else {
            localWeatherDataSource.getWeather()
                .map {
                    it.toDomain()
                }
                .onErrorResumeNext {
                    getWeatherFromRemoteAndPersist(latitude, longitude)
                }
        }
    }

    private fun getWeatherFromRemoteAndPersist(latitude: Double, longitude: Double) =
        weatherDataSource.getWeather(latitude, longitude).flatMap { entity ->
            localWeatherDataSource.persistWeather(entity).toSingle {
                entity
            }.onErrorResumeNext {
                Single.just(entity)
            }
        }.map { weather ->
            weather.toDomain()
        }
}
