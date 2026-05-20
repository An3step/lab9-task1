package com.example.task_9_1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.task_9_1.api.WeatherApiImpl
import com.example.task_9_1.api.createHttpClient
import com.example.task_9_1.models.WeatherResponse

@Composable
fun App(viewModel: WeatherViewModel = remember { 
    WeatherViewModel(WeatherRepository(WeatherApiImpl(createHttpClient()))) 
}) {
    val uiState by viewModel.uiState.collectAsState()
    val cities by viewModel.cities.collectAsState()
    
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var cityName by remember { mutableStateOf("") }
                
                PlatformSearchField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    onSearch = { 
                        if (cityName.isNotBlank()) {
                            viewModel.fetchWeather(cityName)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PlatformCitySelector(
                    cities = cities,
                    onCitySelected = { 
                        cityName = it
                        viewModel.fetchWeather(it) 
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                when (val state = uiState) {
                    is WeatherUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is WeatherUiState.Success -> {
                        AdaptiveWeatherLayout(state.weather)
                    }
                    is WeatherUiState.Error -> {
                        Text("Ошибка: ${state.message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.fetchWeather(cityName) }) {
                            Text("Повторить")
                        }
                    }
                    is WeatherUiState.Idle -> {
                        Text("Введите название города или выберите из списка выше", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
expect fun PlatformSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
)

@Composable
expect fun PlatformCitySelector(
    cities: List<String>,
    onCitySelected: (String) -> Unit
)

@Composable
expect fun WeatherCard(weather: WeatherResponse)

@Composable
expect fun AdaptiveWeatherLayout(weather: WeatherResponse)
