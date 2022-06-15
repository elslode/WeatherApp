package com.elslode.weather.presentation.mainFragment.adapterRv

import androidx.recyclerview.widget.DiffUtil
import com.elslode.weather.domain.entityModel.WeatherEntity

object WeatherDiffCallback: DiffUtil.ItemCallback<WeatherEntity>()  {

    override fun areItemsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity): Boolean {
        return oldItem == newItem
    }
}