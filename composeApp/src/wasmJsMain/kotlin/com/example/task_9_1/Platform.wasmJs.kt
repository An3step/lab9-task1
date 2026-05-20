package com.example.task_9_1

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.task_9_1.models.WeatherResponse
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
actual fun PlatformSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Поиск города (Web)") },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
actual fun PlatformCitySelector(
    cities: List<String>,
    onCitySelected: (String) -> Unit
) {
    FlowRow(modifier = Modifier.fillMaxWidth()) {
        cities.forEach { city ->
            ElevatedButton(
                onClick = { onCitySelected(city) },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(city)
            }
        }
    }
}

@Composable
actual fun WeatherCard(weather: WeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(weather.name, style = MaterialTheme.typography.headlineSmall)
            Text("${weather.main.temp}°C", style = MaterialTheme.typography.displaySmall)
            Text(weather.weather.firstOrNull()?.description ?: "", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Влажность: ${weather.main.humidity}%")
            Text("Ветер: ${weather.wind.speed} м/с")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun AdaptiveWeatherLayout(weather: WeatherResponse) {
    val windowInfo = LocalWindowInfo.current
    val containerWidth = windowInfo.containerSize.width
    
    val columns = when {
        containerWidth < 600 -> 1
        containerWidth < 1200 -> 2
        else -> 3
    }

    if (columns == 1) {
        WeatherCard(weather)
    } else {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                WeatherCard(weather)
            }
            if (columns >= 2) {
                Box(modifier = Modifier.weight(1f)) {
                    DetailCard("Дополнительно", "Давление: ${weather.main.pressure} hPa")
                }
            }
            if (columns >= 3) {
                Box(modifier = Modifier.weight(1f)) {
                    DetailCard("Ветер", "Скорость: ${weather.wind.speed} м/с")
                }
            }
        }
    }
}

@Composable
fun DetailCard(title: String, info: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(info)
        }
    }
}
