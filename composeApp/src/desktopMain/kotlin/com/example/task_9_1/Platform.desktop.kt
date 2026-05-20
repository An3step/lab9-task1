package com.example.task_9_1

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.task_9_1.models.WeatherResponse

@Composable
actual fun PlatformSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    // Windows: Minimalist with clear borders
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Enter City") },
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Black, RectangleShape),
        shape = RectangleShape,
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    )
}

@Composable
actual fun PlatformCitySelector(
    cities: List<String>,
    onCitySelected: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        cities.forEach { city ->
            OutlinedButton(
                onClick = { onCitySelected(city) },
                modifier = Modifier.padding(end = 8.dp),
                shape = RectangleShape
            ) {
                Text(city)
            }
        }
    }
}

@Composable
actual fun WeatherCard(weather: WeatherResponse) {
    // Desktop: minimalist, clear borders
    Surface(
        modifier = Modifier.fillMaxWidth().padding(8.dp).border(1.dp, Color.Gray),
        shape = RectangleShape
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(weather.name, style = MaterialTheme.typography.headlineLarge)
            Text("${weather.main.temp}°C", style = MaterialTheme.typography.displayLarge)
            Text(weather.weather.firstOrNull()?.description ?: "", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text("Humidity: ${weather.main.humidity}%")
                Text("Wind: ${weather.wind.speed} m/s")
                Text("Pressure: ${weather.main.pressure} hPa")
            }
        }
    }
}

@Composable
actual fun AdaptiveWeatherLayout(weather: WeatherResponse) {
    WeatherCard(weather)
}
