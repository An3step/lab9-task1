package com.example.task_9_1.api

import com.example.task_9_1.models.WeatherResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface WeatherApi {
    suspend fun getWeather(city: String): WeatherResponse
}

class WeatherApiImpl(private val client: HttpClient) : WeatherApi {
    private val apiKey = "a776d7d36b4b9f268e0f8d5adea2f3b8"
    private val baseUrl = "https://api.openweathermap.org/data/2.5"

    override suspend fun getWeather(city: String): WeatherResponse {
        return client.get("$baseUrl/weather") {
            parameter("q", city)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "ru")
        }.body()
    }
}

fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}
