package com.example.marty.martysweatherapp.data

import android.arch.persistence.room.*

@Dao
interface CityDAO {

    @Query("SELECT * FROM city")
    fun findAllCities(): List<City>

    @Insert
    fun insertCity(item: City) : Long

    @Delete
    fun deleteCity(item: City)

    @Update
    fun updateCity(item: City)
}