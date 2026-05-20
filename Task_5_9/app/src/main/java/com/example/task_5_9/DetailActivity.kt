package com.example.task_5_9

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task_5_9.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val forecastAdapter = ForecastAdapter()
    private val service = WeatherApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cityName = intent.getStringExtra("CITY_NAME") ?: return

        binding.rvForecast.layoutManager = LinearLayoutManager(this)
        binding.rvForecast.adapter = forecastAdapter

        fetchWeatherData(cityName)
        fetchForecastData(cityName)
    }

    private fun fetchWeatherData(city: String) {
        service.getCurrentWeather(city, WeatherApiService.API_KEY).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    weather?.let {
                        binding.tvDetailCityName.text = it.name
                        binding.tvDetailTemp.text = "${it.main.temp.toInt()}°C"
                        binding.tvDescription.text = it.weather.firstOrNull()?.description ?: ""
                        binding.tvFeelsLike.text = "Ощущается как: ${it.main.feelsLike.toInt()}°C"
                        binding.tvHumidity.text = "Влажность: ${it.main.humidity}%"
                        binding.tvPressure.text = "Давление: ${it.main.pressure} hPa"
                        binding.tvWindSpeed.text = "Ветер: ${it.wind.speed} м/с"
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Ошибка сети", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchForecastData(city: String) {
        service.getForecast(city, WeatherApiService.API_KEY).enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // OpenWeatherMap возвращает прогноз каждые 3 часа.
                        // 24 элемента = 3 дня (24 * 3 = 72 часа).
                        forecastAdapter.updateForecast(it.list.take(24))
                    }
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {}
        })
    }
}
