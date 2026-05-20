package com.example.task_9_1

import com.example.task_9_1.api.WeatherApi
import com.example.task_9_1.models.Main
import com.example.task_9_1.models.Weather
import com.example.task_9_1.models.WeatherResponse
import com.example.task_9_1.models.Wind
import kotlinx.coroutines.delay

class MockWeatherApi(var shouldFail: Boolean = false) : WeatherApi {
    override suspend fun getWeather(city: String): WeatherResponse {
        delay(10) // Small delay to make Loading state observable in tests
        if (shouldFail) throw Exception("Network error")
        return WeatherResponse(
            name = city,
            main = Main(20.0, 18.0, 50, 1013),
            weather = listOf(Weather("Clear", "01d")),
            wind = Wind(5.0)
        )
    }
}
