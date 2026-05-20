package com.example.task_9_1

import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WeatherIntegrationTest {
    private lateinit var repository: WeatherRepository
    private lateinit var api: MockWeatherApi

    @BeforeTest
    fun setup() {
        api = MockWeatherApi()
        repository = WeatherRepository(api)
    }

    @Test
    fun testRepositoryReturnsDataFromApiAndCachesIt() = runTest {
        val city = "London"
        val result = repository.getWeather(city)
        
        assertEquals(city, result.name)
        assertTrue(repository.cachedWeather.value.containsKey(city))
        assertEquals(result, repository.cachedWeather.value[city])
    }

    @Test
    fun testRepositoryReturnsCachedDataWhenApiFails() = runTest {
        val city = "London"
        // First successful call to populate cache
        repository.getWeather(city)
        
        // Make API fail
        api.shouldFail = true
        
        val result = repository.getWeather(city)
        assertEquals(city, result.name) // Should return from cache
    }

    @Test
    fun testRepositoryThrowsExceptionWhenNoCacheAndApiFails() = runTest {
        api.shouldFail = true
        val result = runCatching { repository.getWeather("Unknown") }
        assertTrue(result.isFailure)
    }

    @Test
    fun testRepositoryThrowsExceptionForBlankCityName() = runTest {
        val result = runCatching { repository.getWeather("   ") }
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun testRepositoryStoresMultipleCitiesInCache() = runTest {
        repository.getWeather("Moscow")
        repository.getWeather("London")
        
        val cache = repository.cachedWeather.value
        assertEquals(2, cache.size)
        assertTrue(cache.containsKey("Moscow"))
        assertTrue(cache.containsKey("London"))
    }
}
