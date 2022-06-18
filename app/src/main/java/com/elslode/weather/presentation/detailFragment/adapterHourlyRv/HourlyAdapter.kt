package com.elslode.weather.presentation.detailFragment.adapterHourlyRv

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.elslode.weather.R
import com.elslode.weather.data.sharedPref.PrefHelper
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.databinding.ItemHourBinding
import com.elslode.weather.domain.entityModel.Hourly
import com.squareup.picasso.Picasso
import javax.inject.Inject

class HourlyAdapter @Inject constructor(
    val application: Application,
    val prefHelper: PrefHelper
) : ListAdapter<Hourly, HourlyViewHolder>(HourlyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = ItemHourBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hourlyItem = getItem(position)
        val binding = holder.binding
        with(holder.binding) {
            with(hourlyItem) {
                Picasso.get().load(weatherIconUrl?.last()?.value)
                    .into(binding.ivWeatherHourly)
                binding.tvTempHourly.text = when (prefHelper.getTemperature()) {
                    PrefKeys.c -> application.getString(R.string.temp_c).format(tempC)
                    PrefKeys.f -> application.getString(R.string.temp_f).format(tempF)
                    else -> tempC
                }
                binding.tvTimeHourly.text = time
            }
        }
    }
}
