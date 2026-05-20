package com.example.task_5_9

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task_5_9.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = WeatherAdapter { cityName ->
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("CITY_NAME", cityName)
        startActivity(intent)
    }

    private val cities = mutableListOf("Minsk", "Syracuse")
    private val weatherList = mutableListOf<WeatherResponse>()
    private val service = WeatherApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewCities.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCities.adapter = adapter

        binding.btnAddCity.setOnClickListener {
            showAddCityDialog()
        }

        fetchAllWeather()
    }

    private fun fetchAllWeather() {
        weatherList.clear()
        cities.forEach { city ->
            fetchWeatherForCity(city)
        }
    }

    private fun fetchWeatherForCity(city: String) {
        Log.d("WeatherApp", "Fetching weather for city: $city")
        service.getCurrentWeather(city, WeatherApiService.API_KEY).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.d("WeatherApp", "onResponse for $city: code=${response.code()}, isSuccessful=${response.isSuccessful}")
                if (response.isSuccessful) {
                    response.body()?.let { newItem ->
                        Log.d("WeatherApp", "Received weather for ${newItem.name}")
                        // Избегаем дубликатов в списке по имени города
                        val index = weatherList.indexOfFirst { it.name.equals(newItem.name, ignoreCase = true) }
                        if (index != -1) {
                            weatherList[index] = newItem
                        } else {
                            weatherList.add(newItem)
                        }
                        adapter.updateCities(weatherList.toList())
                    } ?: Log.w("WeatherApp", "Response body is null for $city")
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Log.e("WeatherApp", "Error response for $city: $errorMsg")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherApp", "onFailure for $city", t)
                Toast.makeText(this@MainActivity, "Ошибка загрузки для $city", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAddCityDialog() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Добавить город")
            .setView(editText)
            .setPositiveButton("Добавить") { _, _ ->
                val newCity = editText.text.toString().trim()
                if (newCity.isNotEmpty()) {
                    if (!cities.any { it.equals(newCity, ignoreCase = true) }) {
                        cities.add(newCity)
                    }
                    fetchWeatherForCity(newCity)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
