package com.themanol.weather.data.datasource

import com.squareup.moshi.Moshi
import com.themanol.weather.data.model.WeatherEntity
import com.themanol.weather.data.model.WeatherEntityJsonAdapter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File
import javax.inject.Inject
import javax.inject.Named

private const val FILE_NAME = "weather.json"

class LocalWeatherDataSource
@Inject constructor(@Named("filesDir") private val filesDir: File) {

    fun getWeather(): Single<WeatherEntity> {
        return Single.fromCallable {
            val moshi = Moshi.Builder().build()
            val adapter = WeatherEntityJsonAdapter(moshi)
            adapter.fromJson(
                getFileOrCreate(
                    filesDir,
                    FILE_NAME
                ).bufferedReader().use {
                    it.readText()
                }
            ) ?: throw Throwable("Error parsing json file")
        }
    }

    fun persistWeather(weatherEntity: WeatherEntity): Completable {
        val moshi = Moshi.Builder().build()
        val adapter = WeatherEntityJsonAdapter(moshi)
        return Completable.fromAction {
            getFileOrCreate(filesDir, FILE_NAME).bufferedWriter().use {
                it.write(adapter.toJson(weatherEntity))
            }
        }
    }
}

private fun getFileOrCreate(dir: File, path: String): File {
    val file = dir.resolve(path)
    if (!file.exists()) {
        file.createNewFile()
    }
    return file
}
