package com.themanol.weather.common_di

import com.themanol.weather.data.datasource.WeatherDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
object ServiceModule {

    @Provides
    fun provideRepositoriesDataSource(retrofit: Retrofit): WeatherDataSource =
        retrofit.create(WeatherDataSource::class.java)
}
