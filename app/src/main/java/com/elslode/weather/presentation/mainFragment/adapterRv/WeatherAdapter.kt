package com.elslode.weather.presentation.mainFragment.adapterRv

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elslode.weather.R
import com.elslode.weather.databinding.ItemWeatherBinding
import com.elslode.weather.domain.entityModel.WeatherEntity
import com.elslode.weather.utils.DataExtensions.changeData
import com.squareup.picasso.Picasso
import javax.inject.Inject

class WeatherAdapter @Inject constructor(
    val application: Application
) : ListAdapter<WeatherEntity, WeatherViewHolder>(WeatherDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherItem = getItem(position)
        val binding = holder.binding
        with(holder.binding) {
            with(weatherItem) {
                binding.avgTemp.text = application.getString(R.string.temp).format(this.avgtempC)
                binding.tvDayOfWeek.text = this.date?.changeData()
                binding.tvDescWeather.text = this.hourly?.last()?.weather_ru?.last()?.value ?: this.hourly?.last()?.weatherDesc?.last()?.value
                Picasso.get().load(this.hourly?.last()?.weatherIconUrl?.last()?.value).into(binding.ivWeatherDay)
            }
        }
    }
}
