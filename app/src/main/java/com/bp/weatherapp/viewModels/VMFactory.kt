package com.navertest.sampleMap.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bp.weatherapp.repositories.WeatherRepository
import com.bp.weatherapp.viewModels.WeatherViewModel
import java.lang.IllegalArgumentException

class VMFactory(private val weatherRepository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            WeatherViewModel(weatherRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}