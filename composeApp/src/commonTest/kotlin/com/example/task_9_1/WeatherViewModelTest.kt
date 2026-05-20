package com.example.task_9_1

import com.example.task_9_1.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: WeatherRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialStateIsIdle() = runTest {
        val api = MockWeatherApi()
        repository = WeatherRepository(api)
        viewModel = WeatherViewModel(repository)
        assertEquals(WeatherUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun testLoadingStateWhenFetchingWeather() = runTest {
        val api = MockWeatherApi()
        repository = WeatherRepository(api)
        viewModel = WeatherViewModel(repository)
        
        viewModel.fetchWeather("Moscow")
        runCurrent() // Allow the coroutine to start and reach the Loading state
        assertEquals(WeatherUiState.Loading, viewModel.uiState.value)
        
        advanceUntilIdle() // Complete the weather fetching
        assertTrue(viewModel.uiState.value is WeatherUiState.Success)
    }

    @Test
    fun testErrorStateWhenApiFails() = runTest {
        val api = MockWeatherApi(shouldFail = true)
        repository = WeatherRepository(api)
        viewModel = WeatherViewModel(repository)
        
        viewModel.fetchWeather("Unknown")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is WeatherUiState.Error)
    }

    @Test
    fun testCityNamesInitialValues() = runTest {
        val api = MockWeatherApi()
        repository = WeatherRepository(api)
        viewModel = WeatherViewModel(repository)
        assertEquals(3, viewModel.cities.value.size)
        assertTrue(viewModel.cities.value.contains("Москва"))
    }
}
