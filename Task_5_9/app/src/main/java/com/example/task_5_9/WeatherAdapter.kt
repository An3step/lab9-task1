package com.example.task_5_9

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task_5_9.databinding.ItemCityBinding

class WeatherAdapter(
    private val onCityClick: (String) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val cities = mutableListOf<WeatherResponse>()

    fun updateCities(newCities: List<WeatherResponse>) {
        cities.clear()
        cities.addAll(newCities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int = cities.size

    inner class WeatherViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: WeatherResponse) {
            binding.tvCityName.text = weather.name
            binding.tvTemperature.text = "${weather.main.temp.toInt()}°C"
            binding.root.setOnClickListener { onCityClick(weather.name) }
        }
    }
}
