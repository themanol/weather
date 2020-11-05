package com.themanol.weather.forecast.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.themanol.weather.common_ui.viewmodel.BaseViewModel
import com.themanol.weather.common_ui.viewmodel.prepareForUI
import com.themanol.weather.common_ui.viewmodel.subscribe
import com.themanol.weather.domain.repository.WeatherRepository
import com.themanol.weather.forecast.presentation.model.WeatherDisplay

class ForecastViewModel
@ViewModelInject constructor(
    private val weatherRepository: WeatherRepository
) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun start(latitude: Double, longitude: Double) {
        getWeatherForecast(latitude, longitude)
    }

    private fun getWeatherForecast(latitude: Double, longitude: Double) {
        weatherRepository.getWeather(latitude, longitude)
            .prepareForUI()
            .doOnSubscribe {
                _state.postValue(
                    Loading
                )
            }.subscribe(
                disposables = disposables,
                onSuccess = { weatherModel ->
                    _state.postValue(
                        Success(
                            WeatherDisplay(weatherModel.conditionsDescription)
                        )
                    )
                },
                onError = {
                    _state.postValue(Error)
                }
            )
    }
}

sealed class State
object Loading : State()
object Error : State()
data class Success(
    val weatherDisplay: WeatherDisplay
) : State()
