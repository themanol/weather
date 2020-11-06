package com.themanol.weather.data.repository

import com.themanol.weather.data.datasource.LocalWeatherDataSource
import com.themanol.weather.data.datasource.WeatherDataSource
import com.themanol.weather.data.model.CloudsEntity
import com.themanol.weather.data.model.ConditionsEntity
import com.themanol.weather.data.model.CoordEntity
import com.themanol.weather.data.model.SunsetEntity
import com.themanol.weather.data.model.WeatherDetailsEntity
import com.themanol.weather.data.model.WeatherEntity
import com.themanol.weather.data.model.WindEntity
import com.themanol.weather.domain.model.WeatherConditionsModel
import com.themanol.weather.domain.model.WeatherModel
import com.themanol.weather.domain.model.WindModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import java.util.Date

class WeatherRepositoryImplTest {
    private val dataSource: WeatherDataSource = mockk()
    private val localDataSource: LocalWeatherDataSource = mockk()
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(dataSource, localDataSource)
    }

    @Test
    fun `when get weather and no local information verify data source is called and result completes`() {
        // Given
        val entity = WeatherEntity(
            visibility = 10,
            wind = WindEntity(1.0, 12, null),
            calculationDate = 1604668073,
            snow = null,
            rain = null,
            clouds = CloudsEntity(20),
            base = "base",
            cityId = 1,
            cityName = "city",
            cod = 123,
            conditions = ConditionsEntity(
                temp = 20.0,
                humidity = 50,
                feelsLike = 21.0,
                pressure = 1000,
                groundLevel = null,
                seaLevel = null,
                tempMax = 30.0,
                tempMin = 10.0
            ),
            coord = CoordEntity(10.0, 10.0),
            sunset = SunsetEntity(
                type = 10,
                sunset = 10101002L,
                id = 1,
                country = "country",
                message = null,
                sunrise = 234234234L
            ),
            timeZone = 1,
            weather = listOf(
                WeatherDetailsEntity(
                    id = 1,
                    icon = "icon",
                    description = "description",
                    main = "main"
                )
            )
        )
        val model = WeatherModel(
            cityId = 1,
            rain = null,
            snow = null,
            calculationDate = Date(1604668073L * 1000),
            wind = WindModel(
                speed = 1.0,
                direction = 12,
                gust = null
            ),
            visibility = 10,
            city = "city",
            conditionsDetails = WeatherConditionsModel(
                pressure = 1000,
                humidity = 50,
                temperature = 20.0,
                feelsLikeTemperature = 21.0,
                maxTemperature = 30.0,
                minTemperature = 10.0
            ),
            conditionsId = 1,
            conditionsIcon = "icon",
            conditionsDescription = "description",
            conditionsTitle = "main",
            clouds = 20,
            longitude = 10.0,
            latitude = 10.0
        )
        every { localDataSource.getWeather() } returns Single.error(Exception())
        every { localDataSource.persistWeather(any()) } returns Completable.complete()
        every { dataSource.getWeather(any(), any()) } returns Single.just(entity)

        // When
        val test = repository.getWeather(10.0, 10.0, false).test()

        // Then
        test.assertComplete()
        verify(exactly = 1) { localDataSource.getWeather() }
        verify(exactly = 1) { dataSource.getWeather(10.0, 10.0) }
        verify(exactly = 1) { localDataSource.persistWeather(entity) }
        test.assertResult(
            model
        )
    }

    @Test
    fun `when get weather and no local information and persist fails verify data source is called and result completes`() {
        // Given
        val entity = WeatherEntity(
            visibility = 10,
            wind = WindEntity(1.0, 12, null),
            calculationDate = 1604668073,
            snow = null,
            rain = null,
            clouds = CloudsEntity(20),
            base = "base",
            cityId = 1,
            cityName = "city",
            cod = 123,
            conditions = ConditionsEntity(
                temp = 20.0,
                humidity = 50,
                feelsLike = 21.0,
                pressure = 1000,
                groundLevel = null,
                seaLevel = null,
                tempMax = 30.0,
                tempMin = 10.0
            ),
            coord = CoordEntity(10.0, 10.0),
            sunset = SunsetEntity(
                type = 10,
                sunset = 10101002L,
                id = 1,
                country = "country",
                message = null,
                sunrise = 234234234L
            ),
            timeZone = 1,
            weather = listOf(
                WeatherDetailsEntity(
                    id = 1,
                    icon = "icon",
                    description = "description",
                    main = "main"
                )
            )
        )
        val model = WeatherModel(
            cityId = 1,
            rain = null,
            snow = null,
            calculationDate = Date(1604668073L * 1000),
            wind = WindModel(
                speed = 1.0,
                direction = 12,
                gust = null
            ),
            visibility = 10,
            city = "city",
            conditionsDetails = WeatherConditionsModel(
                pressure = 1000,
                humidity = 50,
                temperature = 20.0,
                feelsLikeTemperature = 21.0,
                maxTemperature = 30.0,
                minTemperature = 10.0
            ),
            conditionsId = 1,
            conditionsIcon = "icon",
            conditionsDescription = "description",
            conditionsTitle = "main",
            clouds = 20,
            longitude = 10.0,
            latitude = 10.0
        )
        every { localDataSource.getWeather() } returns Single.error(Exception())
        every { localDataSource.persistWeather(any()) } returns Completable.error(Exception())
        every { dataSource.getWeather(any(), any()) } returns Single.just(entity)

        // When
        val test = repository.getWeather(10.0, 10.0, false).test()

        // Then
        test.assertComplete()
        verify(exactly = 1) { localDataSource.getWeather() }
        verify(exactly = 1) { dataSource.getWeather(10.0, 10.0) }
        verify(exactly = 1) { localDataSource.persistWeather(entity) }
        test.assertResult(
            model
        )
    }

    @Test
    fun `when get weather and local information exists verify data source is called and result completes`() {
        // Given
        val entity = WeatherEntity(
            visibility = 10,
            wind = WindEntity(1.0, 12, null),
            calculationDate = 1604668073,
            snow = null,
            rain = null,
            clouds = CloudsEntity(20),
            base = "base",
            cityId = 1,
            cityName = "city",
            cod = 123,
            conditions = ConditionsEntity(
                temp = 20.0,
                humidity = 50,
                feelsLike = 21.0,
                pressure = 1000,
                groundLevel = null,
                seaLevel = null,
                tempMax = 30.0,
                tempMin = 10.0
            ),
            coord = CoordEntity(10.0, 10.0),
            sunset = SunsetEntity(
                type = 10,
                sunset = 10101002L,
                id = 1,
                country = "country",
                message = null,
                sunrise = 234234234L
            ),
            timeZone = 1,
            weather = listOf(
                WeatherDetailsEntity(
                    id = 1,
                    icon = "icon",
                    description = "description",
                    main = "main"
                )
            )
        )
        val model = WeatherModel(
            cityId = 1,
            rain = null,
            snow = null,
            calculationDate = Date(1604668073L * 1000),
            wind = WindModel(
                speed = 1.0,
                direction = 12,
                gust = null
            ),
            visibility = 10,
            city = "city",
            conditionsDetails = WeatherConditionsModel(
                pressure = 1000,
                humidity = 50,
                temperature = 20.0,
                feelsLikeTemperature = 21.0,
                maxTemperature = 30.0,
                minTemperature = 10.0
            ),
            conditionsId = 1,
            conditionsIcon = "icon",
            conditionsDescription = "description",
            conditionsTitle = "main",
            clouds = 20,
            longitude = 10.0,
            latitude = 10.0
        )
        every { localDataSource.getWeather() } returns Single.just(entity)

        // When
        val test = repository.getWeather(10.0, 10.0, false).test()

        // Then
        test.assertComplete()
        verify(exactly = 1) { localDataSource.getWeather() }
        verify(exactly = 0) { dataSource.getWeather(10.0, 10.0) }
        test.assertResult(
            model
        )
    }

    @Test
    fun `when get weather and local information exists but force load verify data source is called and result completes`() {
        // Given
        val entity = WeatherEntity(
            visibility = 10,
            wind = WindEntity(1.0, 12, null),
            calculationDate = 1604668073,
            snow = null,
            rain = null,
            clouds = CloudsEntity(20),
            base = "base",
            cityId = 1,
            cityName = "city",
            cod = 123,
            conditions = ConditionsEntity(
                temp = 20.0,
                humidity = 50,
                feelsLike = 21.0,
                pressure = 1000,
                groundLevel = null,
                seaLevel = null,
                tempMax = 30.0,
                tempMin = 10.0
            ),
            coord = CoordEntity(10.0, 10.0),
            sunset = SunsetEntity(
                type = 10,
                sunset = 10101002L,
                id = 1,
                country = "country",
                message = null,
                sunrise = 234234234L
            ),
            timeZone = 1,
            weather = listOf(
                WeatherDetailsEntity(
                    id = 1,
                    icon = "icon",
                    description = "description",
                    main = "main"
                )
            )
        )
        val model = WeatherModel(
            cityId = 1,
            rain = null,
            snow = null,
            calculationDate = Date(1604668073L * 1000),
            wind = WindModel(
                speed = 1.0,
                direction = 12,
                gust = null
            ),
            visibility = 10,
            city = "city",
            conditionsDetails = WeatherConditionsModel(
                pressure = 1000,
                humidity = 50,
                temperature = 20.0,
                feelsLikeTemperature = 21.0,
                maxTemperature = 30.0,
                minTemperature = 10.0
            ),
            conditionsId = 1,
            conditionsIcon = "icon",
            conditionsDescription = "description",
            conditionsTitle = "main",
            clouds = 20,
            longitude = 10.0,
            latitude = 10.0
        )
        every { localDataSource.persistWeather(any()) } returns Completable.complete()
        every { dataSource.getWeather(any(), any()) } returns Single.just(entity)

        // When
        val test = repository.getWeather(10.0, 10.0, true).test()

        // Then
        test.assertComplete()
        verify(exactly = 0) { localDataSource.getWeather() }
        verify(exactly = 1) { dataSource.getWeather(10.0, 10.0) }
        verify(exactly = 1) { localDataSource.persistWeather(entity) }
        test.assertResult(
            model
        )
    }
}
