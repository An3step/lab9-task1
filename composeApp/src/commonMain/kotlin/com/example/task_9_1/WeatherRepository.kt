package com.example.task_9_1

import com.example.task_9_1.api.WeatherApi
import com.example.task_9_1.models.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherRepository(private val api: WeatherApi) {
    private val _cachedWeather = MutableStateFlow<Map<String, WeatherResponse>>(emptyMap())
    val cachedWeather: StateFlow<Map<String, WeatherResponse>> = _cachedWeather

    suspend fun getWeather(city: String): WeatherResponse {
        if (city.isBlank()) {
            throw IllegalArgumentException("City name cannot be blank")
        }
        return try {
            val weather = api.getWeather(city)
            val currentMap = _cachedWeather.value.toMutableMap()
            currentMap[city] = weather
            _cachedWeather.value = currentMap
            weather
        } catch (e: Exception) {
            _cachedWeather.value[city] ?: throw e
        }
    }
}
