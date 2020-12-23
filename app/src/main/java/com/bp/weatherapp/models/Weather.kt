package com.bp.weatherapp.models

import com.bp.weatherapp.R
import java.text.Format

/**
 * 각 로케이션 날씨.
 * */
class Weather(
    var id: Long,
    var weather_state_name: String,
    var weather_state_abbr: String,
    var wind_direction_compass: String,
    var created: String,
    var applicable_date: String,
    var min_temp: Float,
    var max_temp: Float,
    var the_temp: Float,
    var wind_speed: Float,
    var wind_direction: Float,
    var air_pressure: Float,
    var humidity: Float,
    var visibility: Float,
    var predictability: Float
) {

    fun getTheTemp(): String {
        return String.format("%.1f", this.the_temp) + "°C"
    }

    fun getHum(): String {
        return this.humidity.toInt().toString() + "%"
    }


    fun getStateAsResource(): Int {
        return when (weather_state_name) {
            "Clear" -> R.drawable.clear
            "Light Rain" -> R.drawable.light_rain
            "Light Cloud" -> R.drawable.light_cloud
            "Snow" -> R.drawable.snow
            "Showers" -> R.drawable.showers
            "Heavy Rain" -> R.drawable.heavy_rain
            "Heavy Cloud" -> R.drawable.heavy_cloud
            "Thunder" -> R.drawable.thunder
            else -> 0
        }
    }
}