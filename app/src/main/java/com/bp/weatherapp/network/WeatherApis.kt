package com.bp.weatherapp.network

import com.bp.weatherapp.models.Location
import com.bp.weatherapp.models.responses.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApis {
    @GET("api/location/search")
    fun getLocations(
        @Query("query") query: String
    ) : Call<Array<Location>>


    @GET("api/location/{woeid}")
    fun getWeather(
        @Path("woeid") woeid: String
    ) : Call<WeatherResponse>
}