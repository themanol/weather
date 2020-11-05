package com.themanol.weather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themanol.weather.forecast.view.activity.ForecastActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(
            Intent(
                this,
                ForecastActivity::class.java
            )
        )
        finish()
    }
}
