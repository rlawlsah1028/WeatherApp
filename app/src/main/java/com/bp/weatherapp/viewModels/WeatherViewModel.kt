package com.bp.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bp.weatherapp.models.Location
import com.bp.weatherapp.models.Weather
import com.bp.weatherapp.repositories.WeatherRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * weather 검색에 대한 로직 처리를 하는 뷰모델.
 * */
class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {


    /**
     * location 정보 가져오기.
     * */
    fun getLocations(): Observable<Array<Location>> {
        return this.repository.getLocations("se")
    }



    fun getAllData(query: String) :Observable<Array<Pair<Location, Pair<Weather, Weather>>>>  {
        Log.d("test","api start")
        return this.repository.getLocations(query)
            .flatMap {locations ->

                return@flatMap Observable.fromIterable(locations.toList())
                    .flatMap { location -> this.repository.getWeather(location) }
                    .subscribeOn(Schedulers.io())
                    .toList()
                    .observeOn(Schedulers.io())
                    .flatMapObservable {result ->
                        Log.d("test","api end : " + result.size)
                        return@flatMapObservable Observable.just(result.toTypedArray())
                    }
            }

    }


}