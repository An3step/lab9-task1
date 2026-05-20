package com.example.task_9_1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.task_9_1.models.WeatherResponse

@Composable
actual fun PlatformSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    // SearchBar-like UI for iOS
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Search") },
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
actual fun PlatformCitySelector(
    cities: List<String>,
    onCitySelected: (String) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }
    
    // Simple SegmentedControl implementation using TabRow
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.LightGray.copy(alpha = 0.2f),
        indicator = {},
        divider = {}
    ) {
        cities.forEachIndexed { index, city ->
            Tab(
                selected = selectedIndex == index,
                onClick = { 
                    selectedIndex = index
                    onCitySelected(city)
                },
                text = { Text(city, style = MaterialTheme.typography.bodySmall) }
            )
        }
    }
}

@Composable
actual fun WeatherCard(weather: WeatherResponse) {
    // iOS: flat cards without shadows
    Surface(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(weather.name, style = MaterialTheme.typography.headlineMedium)
            Text("${weather.main.temp}°C", style = MaterialTheme.typography.displayMedium)
            Text(weather.weather.firstOrNull()?.description ?: "", style = MaterialTheme.typography.bodyLarge)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
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
