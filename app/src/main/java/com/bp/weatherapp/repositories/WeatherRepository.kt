package com.bp.weatherapp.repositories

import android.util.Log
import com.google.gson.Gson
import com.bp.weatherapp.models.Location
import com.bp.weatherapp.models.Weather
import com.bp.weatherapp.models.responses.WeatherResponse
import com.bp.weatherapp.network.RetrofitManager
import com.bp.weatherapp.network.WeatherApis
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.collections.ArrayList

/**
 * poiList를 가져오는 통신을 처리.
 * */
class WeatherRepository private constructor() {

    fun getLocations(query: String): Observable<Array<Location>> {

        return Observable.just(RetrofitManager.getInstnace())
            .flatMap {
                return@flatMap Observable.just(it.create(WeatherApis::class.java))
            }
            .observeOn(Schedulers.io())
            .flatMap { weatherApis ->
                return@flatMap Observable.create<Array<Location>> { emitter ->
                    weatherApis.getLocations(query)
                        .enqueue(object : Callback<Array<Location>> {
                            override fun onFailure(call: Call<Array<Location>>, t: Throwable) {
                                //api 실패.
                                emitter.onError(t)
                                emitter.onComplete()
                            }

                            override fun onResponse(
                                call: Call<Array<Location>>,
                                response: Response<Array<Location>>
                            ) {
                                //api성공.
                                Log.d("test","api getLocations success : " + response.body()?.size)
                                emitter.onNext(response.body())
                                emitter.onComplete()
                            }
                        })
                }
            }
    }


    fun getWeather(location: Location): Observable<Pair<Location, Pair<Weather, Weather>>> {
        Log.d("test","api getWeather start : " + location.title)

        return Observable.just(RetrofitManager.getInstnace())
            .flatMap {
                return@flatMap Observable.just(it.create(WeatherApis::class.java))
            }
            .observeOn(Schedulers.io())
            .flatMap { weatherApis ->
                return@flatMap Observable.create<Pair<Location, Pair<Weather, Weather>>> { emitter ->
                    weatherApis.getWeather(location.woeid.toString())
                        .enqueue(object : Callback<WeatherResponse> {
                            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                                //api 실패.
                                emitter.onError(t)
                                emitter.onComplete()
                            }

                            override fun onResponse(
                                call: Call<WeatherResponse>,
                                response: Response<WeatherResponse>
                            ) {
                                //api성공.


                                try {

                                    response.body()?.let { wr ->
                                        Log.d("test","api getWeather end : " + location.title)
                                        emitter.onNext(
                                            Pair(
                                                location,
                                                Pair(
                                                    wr.consolidated_weather?.get(0)!!,
                                                    wr.consolidated_weather?.get(1)!!
                                                )
                                            )
                                        )
                                        emitter.onComplete()
                                    }
                                } catch (exception: Exception) {
                                    emitter.onError(exception)
                                    emitter.onComplete()
                                }

                            }
                        })
                }
            }
    }



    companion object {
        @Volatile
        private var instance: WeatherRepository? = null

        fun getInstance(): WeatherRepository {
            if (instance == null) {
                instance = synchronized(this) {
                    instance ?: WeatherRepository().also { instance = it }
                }
            }
            return instance!!
        }
    }

}