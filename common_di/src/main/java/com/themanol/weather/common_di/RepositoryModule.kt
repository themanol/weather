package com.themanol.weather.common_di

import com.themanol.weather.data.repository.WeatherRepositoryImpl
import com.themanol.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWeatherRepository(
        weatherRepository: WeatherRepositoryImpl
    ): WeatherRepository
}
