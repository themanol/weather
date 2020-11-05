package com.themanol.weather.common_di

import com.themanol.common_di.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory.create
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

private const val CALL_TIMEOUT = 30L
private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesHTTPClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
            readTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        @BaseURL baseUrl: String,
        httpClient: dagger.Lazy<OkHttpClient>,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .delegatingCallFactory(httpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(create())
            .build()
    }

    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory {
        return MoshiConverterFactory.create()
    }

    @BaseURL
    @Provides
    fun providesBaseUrl(): String = BASE_URL
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseURL
