package com.example.marty.martysweatherapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marty.martysweatherapp.MainActivity
import com.example.marty.martysweatherapp.R
import com.example.marty.martysweatherapp.data.AppDatabase
import com.example.marty.martysweatherapp.data.City
import com.example.marty.martysweatherapp.touch.TouchAdapter
import kotlinx.android.synthetic.main.city_card.view.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.util.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>, TouchAdapter {

    var cities = mutableListOf<City>()
    val context: Context

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val txtCityName = itemView.txtCityName
    }

    constructor(context: Context, items: List<City>) : super() {
        this.context = context
        this.cities.addAll(items)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate (
                R.layout.city_card, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]

        holder.txtCityName.text = city.cityName

        holder.itemView.setOnClickListener {
            (context as MainActivity).toWeatherView(city.cityName)
        }

    }

    fun addCity(city: City) {
        cities.add(0, city)

        notifyItemInserted(0)
    }

    private fun deleteCity(adapterPosition: Int) {

        Thread {
            AppDatabase.getInstance(context).cityDao().deleteCity(
                    cities[adapterPosition]
            )

            cities.removeAt(adapterPosition)

            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    override fun onDismissed(position: Int) {
        deleteCity(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(cities, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}