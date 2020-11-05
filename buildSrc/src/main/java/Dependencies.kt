object ApplicationId {
    const val id = "com.themanol.weather"
}

object Android {
    const val compileSdkVersion = 29
    const val buildToolsVersion = "30.0.1"
    const val minSdkVersion = 23
    const val targetSdkVersion = 29
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Modules {
    const val app = ":app"
    const val common_ui = ":common_ui"
    const val common_di = ":common_di"
    const val common_test = ":common_test"
    const val domain = ":domain"
    const val data = ":data"
    const val forecast = ":features:forecast"
}

object Versions {
    const val kotlin = "1.4.10"
    const val gradle = "4.1.0"
    const val ktlint = "0.37.1"
    const val material = "1.2.1"
    const val location = "17.1.0"
    const val navigation = "2.3.0"
    const val fragment = "1.2.5"
    const val lifecycle_version = "2.2.0"
    const val constraint_layout = "2.0.1"

    const val core_ktx = "1.3.0"
    const val appcompat = "1.1.0"
    const val hilt = "2.28-alpha"
    const val hilt_worker = "1.0.0-alpha01"
    const val hilt_android_x = "1.0.0-alpha02"
    const val rx_java = "3.0.6"
    const val rx_android = "3.0.0"
    const val retrofit = "2.9.0"
    const val moshi = "1.9.3"
    const val logging_interceptor = "4.9.0"
    const val glide = "4.11.0"
    const val worker_manager = "2.4.0"

    const val junit = "4.13"
    const val mockk = "1.10.0"
    const val andoidx_core_testing = "2.1.0"
}

object Plugins {
    const val kotlin_gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val ktlint = "org.jlleitschuh.gradle.ktlint"
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val androidx_core = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val navigation =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}

object Libs {
    const val androidx_appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val location = "com.google.android.gms:play-services-location:${Versions.location}"
    const val constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    const val androidx_core = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val androidx_fragments = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val navigation_fragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"
    const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle_version}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hilt_viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hilt_android_x}"
    const val hilt_androidx_compiler = "androidx.hilt:hilt-compiler:${Versions.hilt_android_x}"
    const val hilt_worker = "androidx.hilt:hilt-work:${Versions.hilt_worker}"
    const val rx_java = "io.reactivex.rxjava3:rxjava:${Versions.rx_java}"
    const val rx_android = "io.reactivex.rxjava3:rxandroid:${Versions.rx_android}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val rx_java_adapter = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"
    const val moshi_converter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val moshi_annotations = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.logging_interceptor}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_annotations = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val worker_manager = "androidx.work:work-runtime-ktx:${Versions.worker_manager}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val life_cycle = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle_version}"
    const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.andoidx_core_testing}"
}
