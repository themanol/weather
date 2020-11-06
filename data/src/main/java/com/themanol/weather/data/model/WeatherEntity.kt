package com.themanol.weather.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.themanol.weather.domain.model.WeatherConditionsModel
import com.themanol.weather.domain.model.WeatherModel
import com.themanol.weather.domain.model.WindModel
import java.util.*

@JsonClass(generateAdapter = true)
data class WeatherEntity(
    @Json(name = "coord") val coord: CoordEntity,
    @Json(name = "weather") val weather: List<WeatherDetailsEntity>,
    @Json(name = "base") val base: String,
    @Json(name = "main") val conditions: ConditionsEntity,
    @Json(name = "wind") val wind: WindEntity,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "clouds") val clouds: CloudsEntity,
    @Json(name = "rain") val rain: RainEntity?,
    @Json(name = "snow") val snow: SnowEntity?,
    @Json(name = "dt") val calculationDate: Long,
    @Json(name = "sys") val sunset: SunsetEntity,
    @Json(name = "timezone") val timeZone: Int,
    @Json(name = "id") val cityId: Int,
    @Json(name = "name") val cityName: String,
    @Json(name = "cod") val cod: Int
) {
    fun toDomain() =
        WeatherModel(
            city = cityName,
            cityId = cityId,
            latitude = coord.lat,
            longitude = coord.lon,
            clouds = clouds.all,
            rain = rain?.oneHour,
            snow = snow?.oneHour,
            visibility = visibility,
            conditionsTitle = weather.first().main,
            conditionsDescription = weather.first().description,
            conditionsIcon = weather.first().icon,
            conditionsId = weather.first().id,
            conditionsDetails = WeatherConditionsModel(
                temperature = conditions.temp,
                minTemperature = conditions.tempMin,
                maxTemperature = conditions.tempMax,
                feelsLikeTemperature = conditions.feelsLike,
                pressure = conditions.pressure,
                humidity = conditions.humidity
            ),
            calculationDate = Date(calculationDate * 1000),
            wind = WindModel(
                speed = wind.speed,
                direction = wind.degree,
                gust = wind.gust
            )
        )
}

@JsonClass(generateAdapter = true)
data class CoordEntity(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double
)

@JsonClass(generateAdapter = true)
data class WeatherDetailsEntity(
    @Json(name = "id") val id: Int,
    @Json(name = "main") val main: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String,
)

@JsonClass(generateAdapter = true)
data class ConditionsEntity(
    @Json(name = "temp") val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "sea_level") val seaLevel: Int?,
    @Json(name = "grnd_level") val groundLevel: Int?,
    @Json(name = "humidity") val humidity: Int
)

@JsonClass(generateAdapter = true)
data class WindEntity(
    @Json(name = "speed") val speed: Double,
    @Json(name = "deg") val degree: Int,
    @Json(name = "gust") val gust: Double?
)

@JsonClass(generateAdapter = true)
data class CloudsEntity(
    @Json(name = "all") val all: Int
)

@JsonClass(generateAdapter = true)
data class RainEntity(
    @Json(name = "1h") val oneHour: Int,
    @Json(name = "3h") val threeHour: Int?
)

@JsonClass(generateAdapter = true)
data class SnowEntity(
    @Json(name = "1h") val oneHour: Int,
    @Json(name = "3h") val threeHour: Int?
)

@JsonClass(generateAdapter = true)
data class SunsetEntity(
    @Json(name = "type") val type: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "message") val message: String?,
    @Json(name = "country") val country: String,
    @Json(name = "sunrise") val sunrise: Long,
    @Json(name = "sunset") val sunset: Long
)
