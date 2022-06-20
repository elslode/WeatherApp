package com.elslode.weather.presentation.mainFragment.adapterRv

import android.app.Application
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elslode.weather.R
import com.elslode.weather.data.sharedPref.HelperPreferences.getTemperature
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.databinding.ItemWeatherBinding
import com.elslode.weather.domain.entityModel.WeatherEntity
import com.elslode.weather.utils.DataExtensions.changeData
import com.squareup.picasso.Picasso
import javax.inject.Inject

class WeatherAdapter @Inject constructor(
    val application: Application,
    private val preferences: SharedPreferences
) : ListAdapter<WeatherEntity, WeatherViewHolder>(WeatherDiffCallback) {

    var onWeatherItemClickListener: ((WeatherEntity, pos: Int) -> Unit)? = null

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
                binding.avgTemp.text =
                    when (preferences.getTemperature()) {
                        PrefKeys.c -> application.getString(R.string.temp_c).format(this.avgtempC)
                        PrefKeys.f -> application.getString(R.string.temp_f).format(this.avgtempF)
                        else -> application.getString(R.string.temp_c).format(this.avgtempC)
                    }

                binding.tvDayOfWeek.text = this.date?.changeData()
                binding.tvDescWeather.text = this.hourly?.last()?.weather_ru?.last()?.value
                    ?: this.hourly?.last()?.weatherDesc?.last()?.value
                Picasso.get().load(this.hourly?.last()?.weatherIconUrl?.last()?.value)
                    .into(binding.ivWeatherDay)
            }
        }
        binding.root.setOnClickListener {
            onWeatherItemClickListener?.invoke(weatherItem, position)
        }
    }
}
