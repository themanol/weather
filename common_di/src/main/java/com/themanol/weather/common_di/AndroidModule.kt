package com.themanol.weather.common_di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object AndroidModule {

    @Provides
    @Named("filesDir")
    fun providesFilesDir(@ApplicationContext context: Context): File =
        context.filesDir
}
