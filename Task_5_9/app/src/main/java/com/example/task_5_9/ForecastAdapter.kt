package com.example.task_5_9

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task_5_9.databinding.ItemForecastBinding

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private val forecastItems = mutableListOf<ForecastItem>()

    fun updateForecast(newList: List<ForecastItem>) {
        forecastItems.clear()
        forecastItems.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastItems[position])
    }

    override fun getItemCount(): Int = forecastItems.size

    class ForecastViewHolder(private val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastItem) {
            binding.tvDate.text = item.dtTxt
            binding.tvForecastTemp.text = "${item.main.temp.toInt()}°C"
        }
    }
}
