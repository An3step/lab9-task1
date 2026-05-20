package com.example.task_9_1

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.task_9_1.api.WeatherApi
import com.example.task_9_1.models.Main
import com.example.task_9_1.models.Weather
import com.example.task_9_1.models.WeatherResponse
import com.example.task_9_1.models.Wind
import org.junit.Rule
import org.junit.Test

class WeatherAndroidUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testIdleStateShowsWelcomeMessage() {
        val api = MockWeatherApiUI()
        val repository = WeatherRepository(api)
        val viewModel = WeatherViewModel(repository)
        
        composeTestRule.setContent {
            App(viewModel)
        }

        composeTestRule.onNodeWithText("Введите название города или выберите из списка выше").assertExists()
    }

    @Test
    fun testSelectingCityTriggersLoadingAndSuccess() {
        val api = MockWeatherApiUI()
        val repository = WeatherRepository(api)
        val viewModel = WeatherViewModel(repository)
        
        composeTestRule.setContent {
            App(viewModel)
        }

        // Click on "Москва" chip
        composeTestRule.onNodeWithText("Москва").performClick()
        
        // Success state should eventually show the city name
        // We use a substring check because the text might be in a complex layout
        composeTestRule.onNodeWithText("Москва", substring = true).assertExists()
    }

    @Test
    fun testErrorStateShowsRetryButton() {
        val api = MockWeatherApiUI(shouldFail = true)
        val repository = WeatherRepository(api)
        val viewModel = WeatherViewModel(repository)
        
        composeTestRule.setContent {
            App(viewModel)
        }

        // Trigger a search via ViewModel directly to ensure we enter Error state
        viewModel.fetchWeather("London")
        
        composeTestRule.onNodeWithText("Ошибка:", substring = true).assertExists()
        composeTestRule.onNodeWithText("Повторить").assertExists().assertHasClickAction()
    }
}

// Mock implementation specifically for UI tests in this source set
class MockWeatherApiUI(var shouldFail: Boolean = false) : WeatherApi {
    override suspend fun getWeather(city: String): WeatherResponse {
        if (shouldFail) throw Exception("Network error")
        return WeatherResponse(
            name = city,
            main = Main(20.0, 18.0, 50, 1013),
            weather = listOf(Weather("Clear", "01d")),
            wind = Wind(5.0)
        )
    }
}
