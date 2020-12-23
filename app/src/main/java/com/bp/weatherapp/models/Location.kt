package com.bp.weatherapp.models


/**
 * 지역검색 결과
 * */
class Location(
    var title: String,
    var location_type: String,
    var woeid: Int,
    var latt_long: String
) {

}