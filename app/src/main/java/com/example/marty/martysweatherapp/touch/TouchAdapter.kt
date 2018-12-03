package com.example.marty.martysweatherapp.touch

interface TouchAdapter {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
