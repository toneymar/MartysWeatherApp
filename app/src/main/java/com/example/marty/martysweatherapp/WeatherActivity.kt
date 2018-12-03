package com.example.marty.martysweatherapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.marty.martysweatherapp.data.WeatherResult
import com.example.marty.martysweatherapp.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherActivity : AppCompatActivity() {

    private val HOST_URL = "https://api.openweathermap.org"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val weatherAPI = retrofit.create(WeatherAPI::class.java)

        val weatherRatesCall = weatherAPI.getWeather(intent.getStringExtra("CITY_KEY"), getString(R.string.metric), getString(R.string.api_key))

        weatherRatesCall.enqueue(object: Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                txtTemp.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                Glide.with(this@WeatherActivity)
                        .load(
                                ("https://openweathermap.org/img/w/" +
                                        response.body()?.weather?.get(0)?.icon
                                        + ".png"))
                        .into(img)


                val weatherData = response.body()

                txtTitle.text = "Weather for ${intent.getStringExtra("CITY_KEY")}"
                txtTemp.text = "${weatherData?.main?.temp?.toInt()}°C"
                txtDescription.text = "Current conditions:\n${weatherData?.weather?.get(0)?.description}"
                txtHiLo.text = "Hi: ${weatherData?.main?.temp_max}°C    Lo: ${weatherData?.main?.temp_min}°C"
                txtPressure.text = "Pressure: ${weatherData?.main?.pressure} hPa"
                txtClouds.text = "Number of Clouds: ${weatherData?.clouds?.all}"
                txtWind.text = "Wind Speed: ${weatherData?.wind?.speed} km/h"
            }

        })
    }
}
