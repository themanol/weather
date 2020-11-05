package com.themanol.weather.common_di

import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Suppress("NOTHING_TO_INLINE")
inline fun Retrofit.Builder.delegatingCallFactory(
    delegate: dagger.Lazy<OkHttpClient>
): Retrofit.Builder =
    callFactory {
        delegate.get().newCall(it)
    }
