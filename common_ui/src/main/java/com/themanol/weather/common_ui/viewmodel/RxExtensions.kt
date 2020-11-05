package com.themanol.weather.common_ui.viewmodel

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> Single<T>.subscribe(
    disposables: CompositeDisposable,
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit = {}
) {
    disposables.add(subscribe(onSuccess, onError))
}

fun <T> Single<T>.prepareForUI(): Single<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
