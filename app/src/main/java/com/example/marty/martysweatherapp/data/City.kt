package com.example.marty.martysweatherapp.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "city")
data class City(
        @PrimaryKey(autoGenerate = true) var cityId: Long?,
        @ColumnInfo(name = "cityname") var cityName: String
) : Serializable