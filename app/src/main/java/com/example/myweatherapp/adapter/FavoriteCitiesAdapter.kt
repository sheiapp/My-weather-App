package com.example.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.local.room.WeatherEntity
import com.example.myweatherapp.databinding.FavCityItemBinding
import kotlinx.android.synthetic.main.fav_city_item.view.*
import kotlinx.android.synthetic.main.forecast_item.view.condition

/**
 * Created by Shaheer cs on 05/03/2022.
 */
class FavoriteCitiesAdapter(private val requestManager: RequestManager,private val clickEventData:(String?) ->Unit) :
    ListAdapter<WeatherEntity, FavoriteCitiesItemViewHolder>(FavoriteCitiesDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteCitiesItemViewHolder {
        val itemView =
            FavCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteCitiesItemViewHolder(itemView, requestManager)
    }

    override fun onBindViewHolder(holder: FavoriteCitiesItemViewHolder, position: Int) {
        holder.onBindView(getItem(position))
        holder.itemView.setOnClickListener {
            clickEventData(getItem(position).cityName)
        }
    }
}

class FavoriteCitiesItemViewHolder(
    itemView: FavCityItemBinding,
    private val requestManager: RequestManager
) :
    RecyclerView.ViewHolder(itemView.root) {
    fun onBindView(dataItem: WeatherEntity) {
        itemView.city_name.text = dataItem.cityName
        itemView.temperature.text = dataItem.tempInCelsius
        itemView.condition.text = dataItem.conditionText
        setupImage(dataItem.conditionIcon)
    }

    private fun setupImage(imageUrl: String?) {
        requestManager
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(itemView.img)
    }
}

class FavoriteCitiesDiffUtil : DiffUtil.ItemCallback<WeatherEntity>() {
    override fun areItemsTheSame(
        oldItem: WeatherEntity,
        newItem: WeatherEntity
    ) =
        newItem.columnId == oldItem.columnId

    override fun areContentsTheSame(
        oldItem: WeatherEntity,
        newItem: WeatherEntity
    ) =
        newItem == oldItem
}