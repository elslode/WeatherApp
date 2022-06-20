package com.elslode.weather.presentation.detailFragment.adapterHourlyRv

import androidx.recyclerview.widget.DiffUtil
import com.elslode.weather.domain.entityModel.Hourly

object HourlyDiffCallback: DiffUtil.ItemCallback<Hourly>()  {

    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }
}