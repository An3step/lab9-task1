package com.example.task_9_1

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.task_9_1.models.WeatherResponse

@Composable
actual fun PlatformSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Поиск города") },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
actual fun PlatformCitySelector(
    cities: List<String>,
    onCitySelected: (String) -> Unit
) {
    // Android can use a Row of filter chips or just a simple list
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        cities.forEach { city ->
            FilterChip(
                selected = false,
                onClick = { onCitySelected(city) },
                label = { Text(city) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
actual fun WeatherCard(weather: WeatherResponse) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(weather.name, style = MaterialTheme.typography.headlineMedium)
            Text("${weather.main.temp}°C", style = MaterialTheme.typography.displayMedium)
            Text(weather.weather.firstOrNull()?.description ?: "", style = MaterialTheme.typography.bodyLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Влажность: ${weather.main.humidity}%")
                Text("Ветер: ${weather.wind.speed} м/с")
            }
        }
    }
}

@Composable
actual fun AdaptiveWeatherLayout(weather: WeatherResponse) {
    WeatherCard(weather)
}
