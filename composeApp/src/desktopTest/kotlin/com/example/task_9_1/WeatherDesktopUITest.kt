package com.example.task_9_1

import androidx.compose.ui.test.*
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class WeatherDesktopUITest {

    @Test
    fun testIdleStateShowsWelcomeMessage() = runComposeUiTest {
        val api = MockWeatherApi()
        val repository = WeatherRepository(api)
        val viewModel = WeatherViewModel(repository)
        
        setContent {
            App(viewModel)
        }

        onNodeWithText("Введите название города или выберите из списка выше").assertExists()
    }

    @Test
    fun testSelectingCityTriggersLoadingAndSuccess() = runComposeUiTest {
        val api = MockWeatherApi()
        val repository = WeatherRepository(api)
        val viewModel = WeatherViewModel(repository)
        
        setContent {
            App(viewModel)
        }

        // Click on "Москва" button. Use a more specific matcher to avoid ambiguity with the search field.
        onNode(hasText("Москва") and hasClickAction()).performClick()
        
        // Wait for results
        waitForIdle()
        
        // Verify result card is shown. 
        // We look for the temperature which is unique to the Success state in MockWeatherApi.
        onNodeWithText("20.0°C", substring = true).assertExists()
    }

    @Test
    fun testErrorStateShowsRetryButton() = runComposeUiTest {
        val api = MockWeatherApi(shouldFail = true)
        val repository = WeatherRepository(api)
        val viewModel = WeatherViewModel(repository)
        
        setContent {
            App(viewModel)
        }

        // Trigger a search
        viewModel.fetchWeather("London")
        
        // Wait for the UI to update. On desktop, we sometimes need to wait explicitly for 
        // background state changes to propagate if waitForIdle isn't sufficient.
        // We use a matcher to check for the error text.
        val errorMatcher = hasText("Ошибка:", substring = true)
        
        // Attempt to wait for the node to appear
        waitUntil(timeoutMillis = 5000) {
            onAllNodes(errorMatcher).fetchSemanticsNodes().isNotEmpty()
        }

        onNode(errorMatcher).assertExists()
        onNodeWithText("Повторить").assertExists()
    }
}
