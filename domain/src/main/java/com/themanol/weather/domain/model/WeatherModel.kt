package com.themanol.weather.domain.model

import java.util.*

data class WeatherModel(
    val city: String,
    val cityId: Int,
    val latitude: Double,
    val longitude: Double,
    val conditionsId: Int,
    val conditionsTitle: String,
    val conditionsDescription: String,
    val conditionsIcon: String,
    val conditionsDetails: WeatherConditionsModel,
    val visibility: Int,
    val wind: WindModel,
    val clouds: Int,
    val rain: Int?,
    val snow: Int?,
    val calculationDate: Date
)

data class WeatherConditionsModel(
    val temperature: Double,
    val feelsLikeTemperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double,
    val pressure: Int,
    val humidity: Int
)

data class WindModel(
    val speed: Double,
    val direction: Int,
    val gust: Double?
)
