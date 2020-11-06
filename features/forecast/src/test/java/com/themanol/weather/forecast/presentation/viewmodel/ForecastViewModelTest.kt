package com.themanol.weather.forecast.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.themanol.weather.common_ui.view.utils.ResourcesManager
import com.themanol.weather.domain.model.WeatherConditionsModel
import com.themanol.weather.domain.model.WeatherModel
import com.themanol.weather.domain.model.WindModel
import com.themanol.weather.domain.repository.WeatherRepository
import com.themanol.weather.testutils.lifecycle.observeOnce
import com.themanol.weather.testutils.rules.RxSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

class ForecastViewModelTest {
    @get:Rule
    val schedulerRule = RxSchedulerRule()

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val repository: WeatherRepository = mockk()
    private val resourcesManager: ResourcesManager = mockk()

    private lateinit var viewModel: ForecastViewModel

    @Before
    fun setup() {
        viewModel = ForecastViewModel(repository, resourcesManager)
    }

    @Test
    fun `when start retrieve forecast data and success`() {
        // Given
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
        val subject: SingleSubject<WeatherModel> = SingleSubject.create()
        every { repository.getWeather(any(), any()) } returns subject
        every { resourcesManager.getString(any()) } returns "Any"
        every { resourcesManager.getString(any(), any()) } returns "Any Any"

        // When
        viewModel.start(10.0, 10.0)

        // Then
        verify { repository.getWeather(10.0, 10.0) }
        viewModel.state.observeOnce {
            assert(it is Loading)
        }
        subject.onSuccess(model)
        viewModel.state.observeOnce {
            assert(it is Success)
            assert((it as Success).weatherDisplay.city == "city")
            assert(it.weatherDisplay.windDirection == 12)
        }
    }

    @Test
    fun `when start retrieve forecast data and error`() {
        // Given
        val subject: SingleSubject<WeatherModel> = SingleSubject.create()
        every { repository.getWeather(any(), any()) } returns subject

        // When
        viewModel.start(10.0, 10.0)

        // Then
        verify { repository.getWeather(10.0, 10.0) }
        viewModel.state.observeOnce {
            assert(it is Loading)
        }
        subject.onError(Exception())
        viewModel.state.observeOnce {
            assert(it is Error)
        }
    }
}
