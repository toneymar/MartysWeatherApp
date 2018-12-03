package com.example.marty.martysweatherapp.network

import com.example.marty.martysweatherapp.data.WeatherResult
import retrofit2.Call
import retrofit2.http.*

interface WeatherAPI {
    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") city : String,
                   @Query("units") units : String,
                   @Query("appid") apiKey : String): Call<WeatherResult>
}