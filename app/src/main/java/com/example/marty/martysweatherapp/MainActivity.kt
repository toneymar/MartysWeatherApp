package com.example.marty.martysweatherapp

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.ListAdapter
import android.widget.Toast
import com.example.marty.martysweatherapp.adapter.RecyclerAdapter
import com.example.marty.martysweatherapp.data.AppDatabase
import com.example.marty.martysweatherapp.data.City
import com.example.marty.martysweatherapp.touch.TouchCallback
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_parent.*
import kotlinx.android.synthetic.main.city_card.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_add.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AddDialog.ItemHandler {

    private lateinit var itemAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            showAddDialog()
        }

        initRecyclerView()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.open_navDrawer, R.string.close_navDrawer)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if(isFirstStart()) {
            MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.fab)
                    .setPrimaryText(getString(R.string.tut_title))
                    .setSecondaryText("""-> Click here to add a city to your list.
-> Once created, tap on it to show the weather for that city!
-> Swipe right to remove a city from the list.""")
                    .show()
            saveStart()
        }
    }

    private val KEY_FIRST = "KEY_FIRST"

    fun isFirstStart() : Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        return sp.getBoolean(KEY_FIRST, true)
    }

    fun saveStart() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sp.edit()
        editor.putBoolean(KEY_FIRST, false)
        editor.apply()
    }

    private fun initRecyclerView() {
        Thread {
            val cities = AppDatabase.getInstance(this).cityDao().findAllCities()

            itemAdapter = RecyclerAdapter(this@MainActivity, cities)

            runOnUiThread {
                cityRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
                cityRecycler.adapter = itemAdapter
                val callback = TouchCallback(itemAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(cityRecycler)
            }
        }.start()
    }

    private fun showAddDialog() {
        AddDialog().show(supportFragmentManager, null)
    }

    fun toWeatherView(city : String) {
        val intent = Intent(this@MainActivity, WeatherActivity::class.java)
        intent.putExtra("CITY_KEY", city)
        startActivity(intent)
    }

    override fun itemCreated(item: City) {
        Thread {
            val id = AppDatabase.getInstance (
                    this@MainActivity).cityDao().insertCity(item)

            item.cityId = id

            runOnUiThread {
                itemAdapter.addCity(item)
            }
        }.start()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnAddCity -> {
                showAddDialog()
            }
            R.id.btnAbout -> {
                Toast.makeText(this@MainActivity, getString(R.string.dev_credits), Toast.LENGTH_LONG).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
